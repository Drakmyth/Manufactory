/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.power;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.power.IPowerBlock.Type;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.util.Constants;

public class PowerNetworkManager extends SavedData {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String DATA_NAME = Reference.MOD_ID + "_PowerNetworkData";

    private Map<BlockPos, String> blockCache;
    private Map<String, PowerNetwork> networks;

    public PowerNetworkManager() {
        blockCache = new HashMap<>();
        networks = new HashMap<>();
    }

    public static PowerNetworkManager get(ServerLevel world) {
        DimensionDataStorage storage = world.getDataStorage();
        return storage.computeIfAbsent(PowerNetworkManager::load, PowerNetworkManager::new, DATA_NAME);
    }

    public void tick(Level world) {
        LOGGER.trace(LogMarkers.POWERNETWORK, "Ticking power networks...");
        boolean setDirty = networks.values().stream().reduce(false, (isDirty, network) -> {
            network.tick(world);
            return isDirty || network.isDirty();
        }, (a, b) -> a || b);

        if (setDirty) setDirty();
        LOGGER.trace(LogMarkers.POWERNETWORK, "All Power Networks have ticked successfully!");
    }

    public String[] getNetworkIds() {
        return networks.keySet().toArray(new String[0]);
    }

    public void deleteNetwork(String networkId) {
        networks.remove(networkId);
        setDirty();
        LOGGER.debug(LogMarkers.POWERNETWORK, "Power Network %s deleted", networkId);
    }

    public int getBlockCount(String networkId) {
        return networks.get(networkId).getBlocks().size();
    }

    public int getSourceCount(String networkId) {
        return networks.get(networkId).getSources().size();
    }

    public int getSinkCount(String networkId) {
        return networks.get(networkId).getSinks().size();
    }

    public float consumePower(float requested, BlockPos pos) {
        String networkId = blockCache.get(pos);
        if (networkId == null) {
            LOGGER.warn(LogMarkers.POWERNETWORK, "Machine at %s requested power, but is not part of a power network.", pos);
            return 0;
        }

        PowerNetwork network = networks.get(networkId);
        if (network == null) {
            LOGGER.warn(LogMarkers.POWERNETWORK, "Power requested from network %s, but network doesn't exist.", networkId);
        }

        float consumed = network.consumePower(requested, pos);
        if (network.isDirty()) setDirty();
        return consumed;
    }

    public void trackBlock(BlockPos pos, Direction[] dirs, Type type) {
        LOGGER.debug(LogMarkers.POWERNETWORK, "Request received to track block (%d, %d, %d) as %s", pos.getX(), pos.getY(), pos.getZ(), type);
        PowerNetworkNode node = new PowerNetworkNode(pos, dirs);
        List<String> existingNetworks = getSurroundingNetworkIds(node);

        if (existingNetworks.isEmpty()) {
            LOGGER.debug(LogMarkers.POWERNETWORK, "No adjacent connecting network identified. Creating new network...");
            PowerNetwork network = new PowerNetwork();
            networks.put(network.getId(), network);
            addNodeToNetwork(node, network.getId(), type);
            LOGGER.debug(LogMarkers.POWERNETWORK, "Block (%d, %d, %d) added to newly created network %s", pos.getX(), pos.getY(), pos.getZ(), network.getId());
        } else if (existingNetworks.size() == 1) {
            addNodeToNetwork(node, existingNetworks.get(0), type);
            LOGGER.debug(LogMarkers.POWERNETWORK, "Block (%d, %d, %d) added to adjacent connecting network %s", pos.getX(), pos.getY(), pos.getZ(), existingNetworks.get(0));
        } else {
            LOGGER.debug(LogMarkers.POWERNETWORK, "Multiple adjacent connecting networks identified. Merging...");
            PowerNetwork firstNetwork = networks.get(existingNetworks.remove(0));
            List<PowerNetwork> otherNetworks = existingNetworks.stream().map(networkId -> networks.get(networkId)).collect(Collectors.toList());

            for (PowerNetwork mergeNetwork : otherNetworks) {
                firstNetwork.merge(mergeNetwork);
                mergeNetwork.getBlocks().forEach(block -> blockCache.put(block, firstNetwork.getId()));
                deleteNetwork(mergeNetwork.getId());
            }

            addNodeToNetwork(node, firstNetwork.getId(), type);
            LOGGER.debug(LogMarkers.POWERNETWORK, "Block (%d, %d, %d) added to adjacent connecting network %s", pos.getX(), pos.getY(), pos.getZ(), existingNetworks.get(0));
        }

        setDirty();
    }

    public void untrackBlock(BlockPos pos) {
        LOGGER.debug(LogMarkers.POWERNETWORK, "Request received to untrack block (%d, %d, %d)", pos.getX(), pos.getY(), pos.getZ());
        PowerNetwork currentNetwork = networks.get(blockCache.get(pos));
        if(currentNetwork == null) {
            LOGGER.warn(LogMarkers.POWERNETWORK, "Tried to untrack block (%d, %d, %d), but block wasn't being tracked", pos.getX(), pos.getY(), pos.getZ());
            return;
        }

        PowerNetworkNode node = currentNetwork.getNode(pos);
        List<BlockPos> networkedNeighbors = getSurroundingNetworkedBlocks(node);

        if (networkedNeighbors.isEmpty()) {
            LOGGER.debug(LogMarkers.POWERNETWORK, "Untracking last node in network %s...", currentNetwork.getId());
            deleteNetwork(currentNetwork.getId());
            blockCache.remove(pos);
        } else if (networkedNeighbors.size() == 1) {
            currentNetwork.removeBlock(pos);
            blockCache.remove(pos);
        } else {
            LOGGER.debug(LogMarkers.POWERNETWORK, "Untracking (%d, %d, %d) requires a network split. Splitting...", pos.getX(), pos.getY(), pos.getZ());
            Map<BlockPos, Direction[]> allNodes = currentNetwork.getNodes();
            allNodes.remove(pos);
            List<List<PowerNetworkNode>> branches = Arrays.stream(node.getDirections())
                .map(dir -> {
                    BlockPos start = pos.relative(dir);
                    if (!allNodes.containsKey(start)) return null;
                    List<PowerNetworkNode> branchNodes = PowerNetworkWalker.walk(allNodes, start);
                    branchNodes.forEach(bn -> allNodes.remove(bn.getPos()));
                    return branchNodes;
                })
                .filter(list -> list != null)
                .collect(Collectors.toList());

            branches.remove(0); // The first branch will keep the old networkId
            for (List<PowerNetworkNode> branch : branches) {
                PowerNetwork newNetwork = currentNetwork.split(branch);
                String newNetworkId = newNetwork.getId();
                networks.put(newNetworkId, newNetwork);
                branch.forEach(n -> blockCache.put(n.getPos(), newNetworkId));
                LOGGER.debug(LogMarkers.POWERNETWORK, "New network %s split from existing network %s", newNetworkId, currentNetwork.getId());
            }
            currentNetwork.removeBlock(pos);
            blockCache.remove(pos);
        }

        setDirty();
    }

    private void addNodeToNetwork(PowerNetworkNode node, String networkId, Type type) {
        networks.get(networkId).addNode(node, type);
        blockCache.put(node.getPos(), networkId);
    }

    private List<BlockPos> getSurroundingNetworkedBlocks(PowerNetworkNode node) {
        return Stream.of(node.getDirections())
            .map(dir -> node.getPos().relative(dir))
            .filter(block -> blockCache.containsKey(block))
            .collect(Collectors.toList());
    }

    private List<String> getSurroundingNetworkIds(PowerNetworkNode node) {
        return Stream.of(node.getDirections())
            .map(dir -> blockCache.get(node.getPos().relative(dir)))
            .distinct()
            .filter(networkId -> networkId != null)
            .collect(Collectors.toList());
    }

    public static PowerNetworkManager load(CompoundTag nbt) {
        PowerNetworkManager pnm = new PowerNetworkManager();
        LOGGER.debug(LogMarkers.POWERNETWORK, "Loading Power Networks from NBT...");
        pnm.blockCache = new HashMap<>();
        pnm.networks = new HashMap<>();
        ListTag networkNBTs = nbt.getList("powerNetworks", Constants.NBT.TAG_COMPOUND);
        networkNBTs.stream().forEach(compound -> {
            PowerNetwork network = PowerNetwork.fromNBT((CompoundTag)compound);
            pnm.networks.put(network.getId(), network);
            network.getBlocks().stream().forEach(block -> {
                pnm.blockCache.put(block, network.getId());
            });
        });
        LOGGER.debug(LogMarkers.POWERNETWORK, "All Power Networks loaded!");
        return pnm;
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        LOGGER.trace(LogMarkers.POWERNETWORK, "Writing Power Networks to NBT...");
        ListTag powerNetworksNBT = new ListTag();
        networks.values().stream().forEach(network -> {
            CompoundTag networkNBT = network.write(new CompoundTag());
            powerNetworksNBT.add(networkNBT);
        });
        compound.put("powerNetworks", powerNetworksNBT);
        LOGGER.trace(LogMarkers.POWERNETWORK, "All Power Networks written!");
        return compound;
    }
}

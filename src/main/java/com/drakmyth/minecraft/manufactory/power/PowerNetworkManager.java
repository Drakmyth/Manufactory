/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.power;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.power.IPowerBlock.Type;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class PowerNetworkManager extends WorldSavedData {
    private static final String DATA_NAME = Reference.MOD_ID + "_PowerNetworkData";

    private Map<BlockPos, String> blockCache;
    private Map<String, PowerNetwork> networks;

    public PowerNetworkManager() {
        super(DATA_NAME);

        blockCache = new HashMap<>();
        networks = new HashMap<>();
    }

    public static PowerNetworkManager get(ServerWorld world) {
        DimensionSavedDataManager storage = world.getSavedData();
        return storage.getOrCreate(PowerNetworkManager::new, DATA_NAME);
    }

    public void tick(World world) {
        boolean markDirty = networks.values().stream().reduce(false, (isDirty, network) -> {
            network.tick(world);
            return isDirty || network.isDirty();
        }, (a, b) -> a || b);

        if (markDirty) markDirty();
    }

    public String[] getNetworkIds() {
        return networks.keySet().toArray(new String[0]);
    }

    public void deleteNetwork(String networkId) {
        networks.remove(networkId);
        markDirty();
    }

    public int getBlockCount(String networkId) {
        return networks.get(networkId).getBlocks().size();
    }

    public int getSourceCount(String networkId) {
        return networks.get(networkId).getSources().size();
    }

    public float consumePower(float requested, BlockPos pos) {
        PowerNetwork network = networks.get(blockCache.get(pos));
        float consumed = network.consumePower(requested);
        if (network.isDirty()) markDirty();
        return consumed;
    }

    public void trackBlock(BlockPos pos, Type type) {
        List<String> existingNetworks = getSurroundingNetworkIds(pos);

        if (existingNetworks.isEmpty()) {
            PowerNetwork network = new PowerNetwork();
            networks.put(network.getId(), network);
            addBlockToNetwork(pos, network.getId(), type);
        } else if (existingNetworks.size() == 1) {
            addBlockToNetwork(pos, existingNetworks.get(0), type);
        } else {
            // TODO: Merge existing networks, add block to merged network
        }

        markDirty();
    }

    public void untrackBlock(BlockPos pos) {
        List<BlockPos> networkedNeighbors = getSurroundingNetworkedBlocks(pos);
        PowerNetwork currentNetwork = networks.get(blockCache.get(pos));
        boolean splitNetwork = false;

        if (networkedNeighbors.isEmpty()) {
            networks.remove(currentNetwork.getId());
            blockCache.remove(pos);
        } else if (networkedNeighbors.size() == 1) {
            currentNetwork.removeBlock(pos);
            blockCache.remove(pos);
        } else {
            // TODO: If two or more connections, check if block is cut vertice
            // splitNetwork = true;
        }

        if (splitNetwork) {
            // TODO: If block was cut vertice, split network
        }

        markDirty();
    }

    private void addBlockToNetwork(BlockPos pos, String networkId, Type type) {
        networks.get(networkId).addBlock(pos, type);
        blockCache.put(pos, networkId);
    }

    private List<BlockPos> getSurroundingNetworkedBlocks(BlockPos pos) {
        BlockPos[] surroundingBlocks = new BlockPos[] { pos.north(), pos.east(), pos.south(), pos.west(), pos.up(), pos.down() };
        return Stream.of(surroundingBlocks)
            .filter(block -> blockCache.containsKey(block))
            .collect(Collectors.toList());
    }

    private List<String> getSurroundingNetworkIds(BlockPos pos) {
        BlockPos[] surroundingBlocks = new BlockPos[] { pos.north(), pos.east(), pos.south(), pos.west(), pos.up(), pos.down() };
        return Stream.of(surroundingBlocks)
            .map(block -> blockCache.get(block))
            .distinct()
            .filter(networkId -> networkId != null)
            .collect(Collectors.toList());
    }

    @Override
    public void read(CompoundNBT nbt) {
        blockCache = new HashMap<>();
        networks = new HashMap<>();
        ListNBT networkNBTs = nbt.getList("powerNetworks", Constants.NBT.TAG_COMPOUND);
        networkNBTs.stream().forEach(compound -> {
            PowerNetwork network = PowerNetwork.fromNBT((CompoundNBT)compound);
            networks.put(network.getId(), network);
            network.getBlocks().stream().forEach(block -> {
                blockCache.put(block, network.getId());
            });
        });
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT powerNetworksNBT = new ListNBT();
        networks.values().stream().forEach(network -> {
            CompoundNBT networkNBT = network.write(new CompoundNBT());
            powerNetworksNBT.add(networkNBT);
        });
        compound.put("powerNetworks", powerNetworksNBT);
        return compound;
    }
}

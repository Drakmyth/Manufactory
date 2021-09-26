/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.power;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.power.IPowerBlock.Type;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Constants;

public class PowerNetwork {
    private static final Logger LOGGER = LogManager.getLogger();

    private String networkId;
    private Map<BlockPos, Direction[]> nodes;
    private Queue<BlockPos> spreading_window;
    private List<BlockPos> sources;
    private List<BlockPos> sinks;
    private float totalPower;
    private float remainingPower;
    private boolean isDirty;

    public PowerNetwork() {
        this(UUID.randomUUID().toString(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    private PowerNetwork(String networkId, List<PowerNetworkNode> nodes, List<BlockPos> sources, List<BlockPos> sinks) {
        this.networkId = networkId;
        this.nodes = nodes.stream().collect(Collectors.toMap(node -> node.getPos(), node -> node.getDirections()));
        this.spreading_window = new ArrayDeque<>();
        this.sources = new ArrayList<>(sources);
        this.sinks = new ArrayList<>(sinks);
        totalPower = 0;
        remainingPower = 0;
        isDirty = true;
    }

    public String getId() {
        return networkId;
    }

    public boolean isDirty() {
        return isDirty;
    }

    private void markDirty() {
        isDirty = true;
    }

    public void addNode(PowerNetworkNode node) {
        addNode(node, Type.NONE);
    }

    public void merge(PowerNetwork network) {
        nodes.putAll(network.getNodes());
        sources.addAll(network.getSources());
        sinks.addAll(network.getSinks());
        markDirty();
        LOGGER.debug(LogMarkers.POWERNETWORK, "Network %s merged into network %s", network.getId(), networkId);
    }

    public PowerNetwork split(List<PowerNetworkNode> splitNodes) {
        PowerNetwork network = new PowerNetwork();
        for (PowerNetworkNode node : splitNodes) {
            Type nodeType = Type.NONE;
            if (sources.contains(node.getPos())) {
                nodeType = Type.SOURCE;
            } else if (sinks.contains(node.getPos())) {
                nodeType = Type.SINK;
            }
            network.addNode(node, nodeType);
            removeBlock(node.getPos());
        }
        markDirty();
        return network;
    }

    public void addNode(PowerNetworkNode node, Type type) {
        BlockPos pos = node.getPos();
        LOGGER.debug(LogMarkers.POWERNETWORK, "Adding node type %s at (%d, %d, %d) to Power Network %s...", type, pos.getX(), pos.getY(), pos.getZ(), networkId);
        nodes.put(pos, node.getDirections());
        switch(type) {
            case SOURCE:
                sources.add(pos);
                break;
            case SINK:
                sinks.add(pos);
                break;
            case NONE:
                break;
        }
    }

    public PowerNetworkNode getNode(BlockPos pos) {
        return new PowerNetworkNode(pos, nodes.get(pos));
    }

    public void removeBlock(BlockPos pos) {
        nodes.remove(pos);
        sources.remove(pos);
        sinks.remove(pos);
        LOGGER.debug(LogMarkers.POWERNETWORK, "Removed (%d, %d, %d) from Power Network %s", pos.getX(), pos.getY(), pos.getZ(), networkId);
    }

    public Map<BlockPos, Direction[]> getNodes() {
        return new HashMap<>(nodes);
    }

    public List<BlockPos> getBlocks() {
        return new ArrayList<>(nodes.keySet());
    }

    public List<BlockPos> getSources() {
        return new ArrayList<>(sources);
    }

    public List<BlockPos> getSinks() {
        return new ArrayList<>(sinks);
    }

    public void tick(Level world) {
        LOGGER.trace(LogMarkers.POWERNETWORK, "Ticking network %s...", networkId);
        if (sinks.isEmpty()) {
            LOGGER.trace(LogMarkers.POWERNETWORK, "No sinks on network %s, skipping...", networkId);
            return;
        }
        totalPower = sources.stream().reduce(0f, (powerFromSources, source) -> {
            if (!world.isAreaLoaded(source, 1)) return powerFromSources;
            BlockState sourceBlockState = world.getBlockState(source);
            Block sourceBlock = sourceBlockState.getBlock();
            if (!(sourceBlock instanceof IPowerBlock)) return powerFromSources;
            return powerFromSources + ((IPowerBlock)sourceBlock).getAvailablePower(sourceBlockState, world, source);
        }, (a, b) -> a + b);
        remainingPower = totalPower;
        LOGGER.trace(LogMarkers.POWERNETWORK, "Network %s Received %f power from sources. Marking network dirty...", networkId, totalPower);
        markDirty();
    }

    public float consumePower(float requested, BlockPos pos) {
        LOGGER.trace(LogMarkers.POWERNETWORK, "Request to consume %f power received from (%d, %d, %d) by network %s", requested, pos.getX(), pos.getY(), pos.getZ(), networkId);
        if (requested <= 0) {
            LOGGER.warn(LogMarkers.POWERNETWORK, "Negative power requested from network %s by (%d, %d, %d). Rejecting request...", networkId, pos.getX(), pos.getY(), pos.getZ());
            return 0;
        }
        spreading_window.add(pos);
        while(spreading_window.size() > sinks.size()) {
            spreading_window.remove();
        }
        LOGGER.trace(LogMarkers.POWERNETWORK, "Network %s spreading window updated", networkId);
        long activeSinks = spreading_window.stream().distinct().count();
        LOGGER.trace(LogMarkers.POWERNETWORK, "Network %s has identified %d active sinks", networkId, activeSinks);
        float available = Math.min(requested, totalPower/activeSinks);
        available = Math.min(available, remainingPower);
        remainingPower -= available;
        markDirty();
        LOGGER.trace(LogMarkers.POWERNETWORK, "Network %s is returning %f power and has %f power remaining.", networkId, available, remainingPower);
        return available;
    }

    public static PowerNetwork fromNBT(CompoundTag nbt) {
        String networkId = nbt.getString("networkId");
        LOGGER.debug(LogMarkers.POWERNETWORK, "Creating Power Network %s from NBT...", networkId);
        ListTag nodeListTag = nbt.getList("nodes", Constants.NBT.TAG_COMPOUND);
        List<PowerNetworkNode> nodes = nodeListTag.stream().map(compound -> {
            CompoundTag nodeNBT = (CompoundTag)compound;
            int x = nodeNBT.getInt("x");
            int y = nodeNBT.getInt("y");
            int z = nodeNBT.getInt("z");
            BlockPos pos = new BlockPos(x, y, z);
            Direction[] directions = Arrays.stream(nodeNBT.getIntArray("directions"))
                .boxed()
                .map(index -> Direction.from3DDataValue(index))
                .toArray(Direction[]::new);
            LOGGER.debug(LogMarkers.POWERNETWORK, "Loaded node at (%d, %d, %d) with directions %s", x, y, z, Arrays.toString(directions));
            return new PowerNetworkNode(pos, directions);
        }).collect(Collectors.toList());

        ListTag sourceListTag = nbt.getList("sources", Constants.NBT.TAG_COMPOUND);
        List<BlockPos> sources = sourceListTag.stream().map(compound -> {
            CompoundTag sourcePosNBT = (CompoundTag)compound;
            int x = sourcePosNBT.getInt("x");
            int y = sourcePosNBT.getInt("y");
            int z = sourcePosNBT.getInt("z");
            LOGGER.debug(LogMarkers.POWERNETWORK, "Loaded node at (%d, %d, %d) as SOURCE", x, y, z);
            return new BlockPos(x, y, z);
        }).collect(Collectors.toList());

        ListTag sinkListTag = nbt.getList("sinks", Constants.NBT.TAG_COMPOUND);
        List<BlockPos> sinks = sinkListTag.stream().map(compound -> {
            CompoundTag sinkPosNBT = (CompoundTag)compound;
            int x = sinkPosNBT.getInt("x");
            int y = sinkPosNBT.getInt("y");
            int z = sinkPosNBT.getInt("z");
            LOGGER.debug(LogMarkers.POWERNETWORK, "Loaded node at (%d, %d, %d) as SINK", x, y, z);
            return new BlockPos(x, y, z);
        }).collect(Collectors.toList());

        LOGGER.debug(LogMarkers.POWERNETWORK, "Power Network %s successfully loaded from NBT!", networkId);
        return new PowerNetwork(networkId, nodes, sources, sinks);
    }

    public CompoundTag write(CompoundTag compound) {
        isDirty = false;
        LOGGER.trace(LogMarkers.POWERNETWORK, "Saving Power Network %s to NBT...", networkId);
        compound.putString("networkId", networkId);
        ListTag nodeListTag = new ListTag();
        nodes.entrySet().stream().forEach(node -> {
            BlockPos block = node.getKey();
            CompoundTag nodeNBT = new CompoundTag();
            nodeNBT.putInt("x", block.getX());
            nodeNBT.putInt("y", block.getY());
            nodeNBT.putInt("z", block.getZ());
            List<Integer> directions = Stream.of(node.getValue())
                .map(dir -> dir.get3DDataValue())
                .collect(Collectors.toList());
            nodeNBT.putIntArray("directions", directions);
            nodeListTag.add(nodeNBT);
        });
        compound.put("nodes", nodeListTag);
        LOGGER.trace(LogMarkers.POWERNETWORK, "Power Network %s finished serializing nodes", networkId);

        ListTag sourceListTag = new ListTag();
        sources.stream().forEach(source -> {
            CompoundTag sourcePosNBT = new CompoundTag();
            sourcePosNBT.putInt("x", source.getX());
            sourcePosNBT.putInt("y", source.getY());
            sourcePosNBT.putInt("z", source.getZ());
            sourceListTag.add(sourcePosNBT);
        });
        compound.put("sources", sourceListTag);
        LOGGER.trace(LogMarkers.POWERNETWORK, "Power Network %s finished serializing sources", networkId);

        ListTag sinkListTag = new ListTag();
        sinks.stream().forEach(sink -> {
            CompoundTag sinkPosNBT = new CompoundTag();
            sinkPosNBT.putInt("x", sink.getX());
            sinkPosNBT.putInt("y", sink.getY());
            sinkPosNBT.putInt("z", sink.getZ());
            sinkListTag.add(sinkPosNBT);
        });
        compound.put("sinks", sinkListTag);
        LOGGER.trace(LogMarkers.POWERNETWORK, "Power Network %s finished serializing sinks");
        return compound;
    }
}

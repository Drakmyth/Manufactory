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
import com.drakmyth.minecraft.manufactory.util.LogHelper;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class PowerNetwork {
    private static final Logger LOGGER = LogUtils.getLogger();

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
        LOGGER.debug(LogMarkers.POWERNETWORK, "Network {} merged into network {}", network.getId(), networkId);
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
        LOGGER.debug(LogMarkers.POWERNETWORK, "Adding node type {} at {} to Power Network {}...", type, LogHelper.blockPos(pos), networkId);
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
        LOGGER.debug(LogMarkers.POWERNETWORK, "Removed {} from Power Network {}", LogHelper.blockPos(pos), networkId);
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

    public void tick(Level level) {
        LOGGER.trace(LogMarkers.POWERNETWORK, "Ticking network {}...", networkId);
        if (sinks.isEmpty()) {
            LOGGER.trace(LogMarkers.POWERNETWORK, "No sinks on network {}, skipping...", networkId);
            return;
        }
        totalPower = sources.stream().reduce(0f, (powerFromSources, source) -> {
            if (!level.isAreaLoaded(source, 1)) return powerFromSources;
            BlockState sourceBlockState = level.getBlockState(source);
            Block sourceBlock = sourceBlockState.getBlock();
            if (!(sourceBlock instanceof IPowerBlock)) return powerFromSources;
            return powerFromSources + ((IPowerBlock)sourceBlock).getAvailablePower(sourceBlockState, level, source);
        }, (a, b) -> a + b);
        remainingPower = totalPower;
        LOGGER.trace(LogMarkers.POWERNETWORK, "Network {} Received {} power from sources. Marking network dirty...", networkId, totalPower);
        markDirty();
    }

    public float consumePower(float requested, BlockPos pos) {
        LOGGER.trace(LogMarkers.POWERNETWORK, "Request to consume {} power received from {} by network {}", requested, LogHelper.blockPos(pos), networkId);
        if (requested <= 0) {
            LOGGER.warn(LogMarkers.POWERNETWORK, "Negative power requested from network {} by {}. Rejecting request...", networkId, LogHelper.blockPos(pos));
            return 0;
        }
        spreading_window.add(pos);
        while(spreading_window.size() > sinks.size()) {
            spreading_window.remove();
        }
        LOGGER.trace(LogMarkers.POWERNETWORK, "Network {} spreading window updated", networkId);
        long activeSinks = spreading_window.stream().distinct().count();
        LOGGER.trace(LogMarkers.POWERNETWORK, "Network {} has identified {} active sinks", networkId, activeSinks);
        float available = Math.min(requested, totalPower/activeSinks);
        available = Math.min(available, remainingPower);
        remainingPower -= available;
        markDirty();
        LOGGER.trace(LogMarkers.POWERNETWORK, "Network {} is returning {} power and has {} power remaining.", networkId, available, remainingPower);
        return available;
    }

    public static PowerNetwork fromNBT(CompoundTag nbt) {
        String networkId = nbt.getString("networkId");
        LOGGER.debug(LogMarkers.POWERNETWORK, "Creating Power Network {} from NBT...", networkId);
        ListTag nodeListTag = nbt.getList("nodes", Tag.TAG_COMPOUND);
        List<PowerNetworkNode> nodes = nodeListTag.stream().map(compound -> {
            CompoundTag nodeTag = (CompoundTag)compound;
            int x = nodeTag.getInt("x");
            int y = nodeTag.getInt("y");
            int z = nodeTag.getInt("z");
            BlockPos pos = new BlockPos(x, y, z);
            Direction[] directions = Arrays.stream(nodeTag.getIntArray("directions"))
                .boxed()
                .map(index -> Direction.from3DDataValue(index))
                .toArray(Direction[]::new);
            LOGGER.debug(LogMarkers.POWERNETWORK, "Loaded node at ({}, {}, {}) with directions {}", x, y, z, Arrays.toString(directions));
            return new PowerNetworkNode(pos, directions);
        }).collect(Collectors.toList());

        ListTag sourceListTag = nbt.getList("sources", Tag.TAG_COMPOUND);
        List<BlockPos> sources = sourceListTag.stream().map(compound -> {
            CompoundTag sourcePosTag = (CompoundTag)compound;
            int x = sourcePosTag.getInt("x");
            int y = sourcePosTag.getInt("y");
            int z = sourcePosTag.getInt("z");
            LOGGER.debug(LogMarkers.POWERNETWORK, "Loaded node at ({}, {}, {}) as SOURCE", x, y, z);
            return new BlockPos(x, y, z);
        }).collect(Collectors.toList());

        ListTag sinkListTag = nbt.getList("sinks", Tag.TAG_COMPOUND);
        List<BlockPos> sinks = sinkListTag.stream().map(compound -> {
            CompoundTag sinkPosTag = (CompoundTag)compound;
            int x = sinkPosTag.getInt("x");
            int y = sinkPosTag.getInt("y");
            int z = sinkPosTag.getInt("z");
            LOGGER.debug(LogMarkers.POWERNETWORK, "Loaded node at ({}, {}, {}) as SINK", x, y, z);
            return new BlockPos(x, y, z);
        }).collect(Collectors.toList());

        LOGGER.debug(LogMarkers.POWERNETWORK, "Power Network {} successfully loaded from NBT!", networkId);
        return new PowerNetwork(networkId, nodes, sources, sinks);
    }

    public CompoundTag write(CompoundTag compound) {
        isDirty = false;
        LOGGER.trace(LogMarkers.POWERNETWORK, "Saving Power Network {} to NBT...", networkId);
        compound.putString("networkId", networkId);
        ListTag nodeListTag = new ListTag();
        nodes.entrySet().stream().forEach(node -> {
            BlockPos block = node.getKey();
            CompoundTag nodeTag = new CompoundTag();
            nodeTag.putInt("x", block.getX());
            nodeTag.putInt("y", block.getY());
            nodeTag.putInt("z", block.getZ());
            List<Integer> directions = Stream.of(node.getValue())
                .map(dir -> dir.get3DDataValue())
                .collect(Collectors.toList());
            nodeTag.putIntArray("directions", directions);
            nodeListTag.add(nodeTag);
        });
        compound.put("nodes", nodeListTag);
        LOGGER.trace(LogMarkers.POWERNETWORK, "Power Network {} finished serializing nodes", networkId);

        ListTag sourceListTag = new ListTag();
        sources.stream().forEach(source -> {
            CompoundTag sourcePosTag = new CompoundTag();
            sourcePosTag.putInt("x", source.getX());
            sourcePosTag.putInt("y", source.getY());
            sourcePosTag.putInt("z", source.getZ());
            sourceListTag.add(sourcePosTag);
        });
        compound.put("sources", sourceListTag);
        LOGGER.trace(LogMarkers.POWERNETWORK, "Power Network {} finished serializing sources", networkId);

        ListTag sinkListTag = new ListTag();
        sinks.stream().forEach(sink -> {
            CompoundTag sinkPosTag = new CompoundTag();
            sinkPosTag.putInt("x", sink.getX());
            sinkPosTag.putInt("y", sink.getY());
            sinkPosTag.putInt("z", sink.getZ());
            sinkListTag.add(sinkPosTag);
        });
        compound.put("sinks", sinkListTag);
        LOGGER.trace(LogMarkers.POWERNETWORK, "Power Network {} finished serializing sinks", networkId);
        return compound;
    }
}

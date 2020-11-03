/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.power;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.drakmyth.minecraft.manufactory.power.IPowerBlock.Type;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class PowerNetwork {
    private String networkId;
    private Map<BlockPos, Direction[]> nodes;
    private List<BlockPos> sources;
    private float power;
    private boolean isDirty;

    public PowerNetwork() {
        this(UUID.randomUUID().toString(), Collections.emptyList(), Collections.emptyList());
    }

    private PowerNetwork(String networkId, List<PowerNetworkNode> nodes, List<BlockPos> sources) {
        this.networkId = networkId;
        this.nodes = nodes.stream().collect(Collectors.toMap(node -> node.getPos(), node -> node.getDirections()));
        this.sources = new ArrayList<>(sources);
        power = 0;
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
        markDirty();
    }

    public PowerNetwork split(List<PowerNetworkNode> splitNodes) {
        PowerNetwork network = new PowerNetwork();
        for (PowerNetworkNode node : splitNodes) {
            boolean isNodeSource = sources.contains(node.getPos());
            network.addNode(node, isNodeSource ? Type.SOURCE : Type.NONE);
            removeBlock(node.getPos());
        }
        markDirty();
        return network;
    }

    public void addNode(PowerNetworkNode node, Type type) {
        nodes.put(node.getPos(), node.getDirections());
        if (type == Type.SOURCE) {
            sources.add(node.getPos());
        }
    }

    public PowerNetworkNode getNode(BlockPos pos) {
        return new PowerNetworkNode(pos, nodes.get(pos));
    }

    public void removeBlock(BlockPos pos) {
        nodes.remove(pos);
        sources.remove(pos);
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

    public void tick(World world) {
        power = sources.stream().reduce(0f, (powerFromSources, source) -> {
            if (!world.isAreaLoaded(source, 1)) return powerFromSources;
            Block sourceBlock = world.getBlockState(source).getBlock();
            if (!(sourceBlock instanceof IPowerBlock)) return powerFromSources;
            return powerFromSources + ((IPowerBlock)sourceBlock).getAvailablePower();
        }, (a, b) -> a + b);
        markDirty();
    }

    public float consumePower(float requested) {
        float available = Math.min(requested, power);
        power -= available;
        markDirty();
        return available;
    }

    public static PowerNetwork fromNBT(CompoundNBT nbt) {
        String networkId = nbt.getString("networkId");
        ListNBT nodeListNBT = nbt.getList("nodes", Constants.NBT.TAG_COMPOUND);
        List<PowerNetworkNode> nodes = nodeListNBT.stream().map(compound -> {
            CompoundNBT nodeNBT = (CompoundNBT)compound;
            int x = nodeNBT.getInt("x");
            int y = nodeNBT.getInt("y");
            int z = nodeNBT.getInt("z");
            BlockPos pos = new BlockPos(x, y, z);
            Direction[] directions = Arrays.stream(nodeNBT.getIntArray("directions"))
                .boxed()
                .map(index -> Direction.byIndex(index))
                .toArray(Direction[]::new);
            return new PowerNetworkNode(pos, directions);
        }).collect(Collectors.toList());

        ListNBT sourceListNBT = nbt.getList("sources", Constants.NBT.TAG_COMPOUND);
        List<BlockPos> sources = sourceListNBT.stream().map(compound -> {
            CompoundNBT sourcePosNBT = (CompoundNBT)compound;
            int x = sourcePosNBT.getInt("x");
            int y = sourcePosNBT.getInt("y");
            int z = sourcePosNBT.getInt("z");
            return new BlockPos(x, y, z);
        }).collect(Collectors.toList());

        return new PowerNetwork(networkId, nodes, sources);
    }

    public CompoundNBT write(CompoundNBT compound) {
        isDirty = false;
        compound.putString("networkId", networkId);
        ListNBT nodeListNBT = new ListNBT();
        nodes.entrySet().stream().forEach(node -> {
            BlockPos block = node.getKey();
            CompoundNBT nodeNBT = new CompoundNBT();
            nodeNBT.putInt("x", block.getX());
            nodeNBT.putInt("y", block.getY());
            nodeNBT.putInt("z", block.getZ());
            List<Integer> directions = Stream.of(node.getValue())
                .map(dir -> dir.getIndex())
                .collect(Collectors.toList());
            nodeNBT.putIntArray("directions", directions);
            nodeListNBT.add(nodeNBT);
        });
        compound.put("nodes", nodeListNBT);

        ListNBT sourceListNBT = new ListNBT();
        sources.stream().forEach(source -> {
            CompoundNBT sourcePosNBT = new CompoundNBT();
            sourcePosNBT.putInt("x", source.getX());
            sourcePosNBT.putInt("y", source.getY());
            sourcePosNBT.putInt("z", source.getZ());
            sourceListNBT.add(sourcePosNBT);
        });
        compound.put("sources", sourceListNBT);
        return compound;
    }
}

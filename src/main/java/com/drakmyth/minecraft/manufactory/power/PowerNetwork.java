/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.power;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.drakmyth.minecraft.manufactory.power.IPowerBlock.Type;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class PowerNetwork {
    private String networkId;
    private List<BlockPos> blocks;
    private List<BlockPos> sources;
    private float power;
    private boolean isDirty;

    public PowerNetwork() {
        this(UUID.randomUUID().toString(), Collections.emptyList(), Collections.emptyList());
    }

    private PowerNetwork(String networkId, List<BlockPos> blocks, List<BlockPos> sources) {
        this.networkId = networkId;
        this.blocks = new ArrayList<>(blocks);
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

    public void addBlock(BlockPos pos) {
        addBlock(pos, Type.NONE);
    }

    public void addBlock(BlockPos pos, Type type) {
        blocks.add(pos);
        if (type == Type.SOURCE) {
            sources.add(pos);
        }
    }

    public void removeBlock(BlockPos pos) {
        blocks.remove(pos);
        sources.remove(pos);
    }

    public List<BlockPos> getBlocks() {
        return new ArrayList<>(blocks);
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
        ListNBT blockListNBT = nbt.getList("blocks", Constants.NBT.TAG_COMPOUND);
        List<BlockPos> blocks = blockListNBT.stream().map(compound -> {
            CompoundNBT blockPosNBT = (CompoundNBT)compound;
            int x = blockPosNBT.getInt("x");
            int y = blockPosNBT.getInt("y");
            int z = blockPosNBT.getInt("z");
            return new BlockPos(x, y, z);
        }).collect(Collectors.toList());
        ListNBT sourceListNBT = nbt.getList("sources", Constants.NBT.TAG_COMPOUND);
        List<BlockPos> sources = sourceListNBT.stream().map(compound -> {
            CompoundNBT sourcePosNBT = (CompoundNBT)compound;
            int x = sourcePosNBT.getInt("x");
            int y = sourcePosNBT.getInt("y");
            int z = sourcePosNBT.getInt("z");
            return new BlockPos(x, y, z);
        }).collect(Collectors.toList());

        return new PowerNetwork(networkId, blocks, sources);
    }

    public CompoundNBT write(CompoundNBT compound) {
        isDirty = false;
        compound.putString("networkId", networkId);
        ListNBT blockListNBT = new ListNBT();
        blocks.stream().forEach(block -> {
            CompoundNBT blockPosNBT = new CompoundNBT();
            blockPosNBT.putInt("x", block.getX());
            blockPosNBT.putInt("y", block.getY());
            blockPosNBT.putInt("z", block.getZ());
            blockListNBT.add(blockPosNBT);
        });
        compound.put("blocks", blockListNBT);
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

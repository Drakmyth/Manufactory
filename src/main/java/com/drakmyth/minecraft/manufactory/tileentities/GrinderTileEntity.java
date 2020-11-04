/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.tileentities;

import java.util.Random;

import com.drakmyth.minecraft.manufactory.init.ModTileEntityTypes;
import com.drakmyth.minecraft.manufactory.network.IMachineProgressListener;
import com.drakmyth.minecraft.manufactory.network.MachineProgressPacket;
import com.drakmyth.minecraft.manufactory.network.ModPacketHandler;
import com.drakmyth.minecraft.manufactory.power.PowerNetworkManager;
import com.drakmyth.minecraft.manufactory.recipes.GrinderRecipe;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class GrinderTileEntity extends TileEntity implements ITickableTileEntity, IMachineProgressListener {

    private boolean firstTick;
    private ItemStackHandler grinderInventory;
    private ItemStackHandler grinderUpgradeInventory;
    private GrinderRecipe currentRecipe;
    private float lastPowerReceived;
    private float powerRequired;
    private float powerRemaining;
    private float maxPowerPerTick;

    public GrinderTileEntity() {
        super(ModTileEntityTypes.GRINDER.get());

        firstTick = true;
        grinderInventory = new ItemStackHandler(2);
        grinderUpgradeInventory = new ItemStackHandler(4);
    }

    public ItemStackHandler getInventory() {
        return grinderInventory;
    }

    public ItemStackHandler getUpgradeInventory() {
        return grinderUpgradeInventory;
    }

    public float getProgress() {
        if (powerRequired <= 0) return 0;
        return (powerRequired - powerRemaining) / powerRequired;
    }

    public float getPowerRate() {
        if (maxPowerPerTick <= 0) return 0;
        return lastPowerReceived / maxPowerPerTick;
    }

    // Client-Side Only
    @Override
    public void onProgressUpdate(float progress, float total) {
        powerRequired = total;
        powerRemaining = progress;
    }

    // Client-Side Only
    @Override
    public void onPowerRateUpdate(float amount, float expected) {
        // TODO: Consider using a rolling window to display ramp up/down
        lastPowerReceived = amount;
        maxPowerPerTick = expected;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("inventory", grinderInventory.serializeNBT());
        compound.put("upgradeInventory", grinderUpgradeInventory.serializeNBT());
        compound.putFloat("powerRequired", powerRequired);
        compound.putFloat("powerRemaining", powerRemaining);
        compound.putFloat("maxPowerPerTick", maxPowerPerTick);
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        grinderInventory.deserializeNBT(nbt.getCompound("inventory"));
        grinderUpgradeInventory.deserializeNBT(nbt.getCompound("upgradeInventory"));
        powerRequired = nbt.getFloat("powerRequired");
        powerRemaining = nbt.getFloat("powerRemaining");
        maxPowerPerTick = nbt.getFloat("maxPowerPerTick");
    }

    private boolean tryStartRecipe() {
        GrinderRecipe recipe = world.getRecipeManager().getRecipe(GrinderRecipe.recipeType, new RecipeWrapper(grinderInventory), world).orElse(null);
        if (recipe == null) return false;
        ItemStack maxResult = recipe.getMaxOutput();
        if (!grinderInventory.insertItem(1, maxResult, true).isEmpty()) return false;
        lastPowerReceived = 0;
        powerRequired = recipe.getPowerRequired();
        powerRemaining = recipe.getPowerRequired();
        maxPowerPerTick = recipe.getPowerRequired() / (float)recipe.getProcessTime();
        currentRecipe = recipe;
        return true;
    }

    private void updateClientGui() {
        MachineProgressPacket msg = new MachineProgressPacket(powerRemaining, powerRequired, lastPowerReceived, maxPowerPerTick, getPos());
        Chunk chunk = world.getChunkAt(getPos());
        ModPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), msg);
    }

    @Override
    public void tick() {
        if (world.isRemote) return;

        if (firstTick) {
            firstTick = false;
            if (!grinderInventory.getStackInSlot(0).isEmpty()) {
                currentRecipe = world.getRecipeManager().getRecipe(GrinderRecipe.recipeType, new RecipeWrapper(grinderInventory), world).orElse(null);
            }
        }

        if (currentRecipe == null) {
            boolean recipeStarted = tryStartRecipe();
            if (!recipeStarted) return;
        }

        if (!currentRecipe.getIngredient().test(grinderInventory.getStackInSlot(0))) {
            currentRecipe = null;
            lastPowerReceived = 0;
            powerRequired = 0;
            powerRemaining = 0;
            maxPowerPerTick = 0;
            updateClientGui();
            markDirty();
            return;
        }

        PowerNetworkManager pnm = PowerNetworkManager.get((ServerWorld)world);
        lastPowerReceived = pnm.consumePower(maxPowerPerTick, pos);
        powerRemaining -= lastPowerReceived; // TODO: Consider making PowerRateUpdate its own packet and only sending if different from last tick
        if (powerRemaining <= 0) {
            grinderInventory.extractItem(0, 1, false);
            ItemStack resultStack = currentRecipe.getRecipeOutput().copy();
            Random rand = world.getRandom();
            if (currentRecipe.hasExtraChance() && rand.nextFloat() <= currentRecipe.getExtraChance()) {
                resultStack.grow(currentRecipe.getRandomExtraAmount(rand));
            }
            grinderInventory.insertItem(1, resultStack, false);
            currentRecipe = null;
            lastPowerReceived = 0;
            powerRequired = 0;
            powerRemaining = 0;
            maxPowerPerTick = 0;
        }

        updateClientGui();
        markDirty();
    }
}

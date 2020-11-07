/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.tileentities;

import java.util.Random;

import com.drakmyth.minecraft.manufactory.init.ModTileEntityTypes;
import com.drakmyth.minecraft.manufactory.items.upgrades.IMillingBallUpgrade;
import com.drakmyth.minecraft.manufactory.items.upgrades.IMotorUpgrade;
import com.drakmyth.minecraft.manufactory.items.upgrades.IPowerProvider;
import com.drakmyth.minecraft.manufactory.network.IMachineProgressListener;
import com.drakmyth.minecraft.manufactory.network.MachineProgressPacket;
import com.drakmyth.minecraft.manufactory.network.ModPacketHandler;
import com.drakmyth.minecraft.manufactory.recipes.BallMillRecipe;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class BallMillTileEntity extends TileEntity implements ITickableTileEntity, IMachineProgressListener {

    private boolean firstTick;
    private ItemStackHandler ballMillInventory;
    private ItemStackHandler ballMillUpgradeInventory;
    private BallMillRecipe currentRecipe;
    private float lastPowerReceived;
    private float powerRequired;
    private float powerRemaining;
    private float maxPowerPerTick;

    public BallMillTileEntity() {
        super(ModTileEntityTypes.BALL_MILL.get());

        firstTick = true;
        ballMillInventory = new ItemStackHandler(2);
        ballMillUpgradeInventory = new ItemStackHandler(3);
    }

    public ItemStackHandler getInventory() {
        return ballMillInventory;
    }

    public ItemStackHandler getUpgradeInventory() {
        return ballMillUpgradeInventory;
    }

    public float getProgress() {
        if (powerRequired <= 0) return 0;
        return (powerRequired - powerRemaining) / powerRequired;
    }

    public float getPowerRate() {
        float motorSpeed = getMotorSpeed();
        if (maxPowerPerTick <= 0 || motorSpeed <= 0) return 0;
        return lastPowerReceived / (maxPowerPerTick * motorSpeed);
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
        compound.put("inventory", ballMillInventory.serializeNBT());
        compound.put("upgradeInventory", ballMillUpgradeInventory.serializeNBT());
        compound.putFloat("powerRequired", powerRequired);
        compound.putFloat("powerRemaining", powerRemaining);
        compound.putFloat("maxPowerPerTick", maxPowerPerTick);
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        ballMillInventory.deserializeNBT(nbt.getCompound("inventory"));
        ballMillUpgradeInventory.deserializeNBT(nbt.getCompound("upgradeInventory"));
        powerRequired = nbt.getFloat("powerRequired");
        powerRemaining = nbt.getFloat("powerRemaining");
        maxPowerPerTick = nbt.getFloat("maxPowerPerTick");
    }

    private int getTier() {
        Item millingBallItem = ballMillUpgradeInventory.getStackInSlot(0).getItem();
        if (!(millingBallItem instanceof IMillingBallUpgrade)) return -1;
        IMillingBallUpgrade millingBall = (IMillingBallUpgrade)millingBallItem;
        return millingBall.getTier().getHarvestLevel();
    }

    private float getProcessChance() {
        ItemStack millingBallStack = ballMillUpgradeInventory.getStackInSlot(0);
        if (!(millingBallStack.getItem() instanceof IMillingBallUpgrade)) return 0;
        IMillingBallUpgrade millingBall = (IMillingBallUpgrade)millingBallStack.getItem();
        return millingBall.getProcessChance(millingBallStack);
    }

    private float getEfficiencyModifier() {
        ItemStack millingBallStack = ballMillUpgradeInventory.getStackInSlot(0);
        if (!(millingBallStack.getItem() instanceof IMillingBallUpgrade)) return 0;
        IMillingBallUpgrade millingBall = (IMillingBallUpgrade)millingBallStack.getItem();
        return millingBall.getEfficiency(millingBallStack);
    }

    private boolean tryStartRecipe() {
        BallMillRecipe recipe = world.getRecipeManager().getRecipe(BallMillRecipe.recipeType, new RecipeWrapper(ballMillInventory), world).orElse(null);
        if (recipe == null) return false;
        if (getTier() < recipe.getTierRequired()) return false;
        ItemStack maxResult = recipe.getMaxOutput();
        if (!ballMillInventory.insertItem(1, maxResult, true).isEmpty()) return false;
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

    private float getMotorSpeed() {
        Item motor = ballMillUpgradeInventory.getStackInSlot(1).getItem();
        return motor instanceof IMotorUpgrade ? ((IMotorUpgrade)motor).getPowerCapMultiplier() : 0.0f;
    }

    private IPowerProvider getPowerProvider() {
        Item powerProvider = ballMillUpgradeInventory.getStackInSlot(2).getItem();
        IPowerProvider emptyPowerProvider = (requestedPower, world, pos) -> 0;
        return powerProvider instanceof IPowerProvider ? (IPowerProvider)powerProvider : emptyPowerProvider;
    }

    @Override
    public void tick() {
        if (world.isRemote) return;

        if (firstTick) {
            firstTick = false;
            if (!ballMillInventory.getStackInSlot(0).isEmpty()) {
                currentRecipe = world.getRecipeManager().getRecipe(BallMillRecipe.recipeType, new RecipeWrapper(ballMillInventory), world).orElse(null);
            }
        }

        if (currentRecipe == null) {
            boolean recipeStarted = tryStartRecipe();
            if (!recipeStarted) return;
        }

        if (!currentRecipe.getIngredient().test(ballMillInventory.getStackInSlot(0))) {
            currentRecipe = null;
            lastPowerReceived = 0;
            powerRequired = 0;
            powerRemaining = 0;
            maxPowerPerTick = 0;
            updateClientGui();
            markDirty();
            return;
        }

        if (getTier() < currentRecipe.getTierRequired()) return;

        IPowerProvider powerProvider = getPowerProvider();
        lastPowerReceived = powerProvider.consumePower(maxPowerPerTick * getMotorSpeed(), (ServerWorld)world, pos);
        powerRemaining -= lastPowerReceived; // TODO: Consider making PowerRateUpdate its own packet and only sending if different from last tick
        if (powerRemaining <= 0) {
            Random rand = world.getRandom();
            ItemStack resultStack;
            if (rand.nextFloat() > getProcessChance()) {
                resultStack = ItemStack.EMPTY;
            } else {
                ballMillInventory.extractItem(0, 1, false);
                resultStack = currentRecipe.getRecipeOutput().copy();
            }
            if (!resultStack.isEmpty() && getEfficiencyModifier() > 0) {
                if (currentRecipe.hasExtraChance() && rand.nextFloat() <= (currentRecipe.getExtraChance() * getEfficiencyModifier())) {
                    resultStack.grow(currentRecipe.getRandomExtraAmount(rand));
                }
            }
            ballMillInventory.insertItem(1, resultStack, false);
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

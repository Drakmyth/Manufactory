/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.blocks.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.init.ModBlockEntityTypes;
import com.drakmyth.minecraft.manufactory.items.upgrades.IMillingBallUpgrade;
import com.drakmyth.minecraft.manufactory.items.upgrades.IMotorUpgrade;
import com.drakmyth.minecraft.manufactory.items.upgrades.IPowerProvider;
import com.drakmyth.minecraft.manufactory.network.IMachineProgressListener;
import com.drakmyth.minecraft.manufactory.network.IOpenContainerWithUpgradesListener;
import com.drakmyth.minecraft.manufactory.network.IPowerRateListener;
import com.drakmyth.minecraft.manufactory.network.MachineProgressPacket;
import com.drakmyth.minecraft.manufactory.network.ModPacketHandler;
import com.drakmyth.minecraft.manufactory.network.PowerRatePacket;
import com.drakmyth.minecraft.manufactory.recipes.BallMillRecipe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class BallMillBlockEntity extends BlockEntity implements IMachineProgressListener, IPowerRateListener, IOpenContainerWithUpgradesListener {
    private static final Logger LOGGER = LogManager.getLogger();

    private boolean firstTick;
    private ItemStackHandler ballMillInventory;
    private ItemStackHandler ballMillUpgradeInventory;
    private BallMillRecipe currentRecipe;
    private float lastPowerReceived;
    private float powerRequired;
    private float powerRemaining;
    private float maxPowerPerTick;

    public BallMillBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.BALL_MILL.get(), pos, state);

        firstTick = true;
        ballMillInventory = new ItemStackHandler(2);
        ballMillUpgradeInventory = new ItemStackHandler(3);
        LOGGER.debug(LogMarkers.MACHINE, "Ball Mill tile entity initialized with {} inventory slots and {} upgrade inventory slots", ballMillInventory.getSlots(), ballMillUpgradeInventory.getSlots());
    }

    public ItemStackHandler getInventory() {
        return ballMillInventory;
    }

    public ItemStackHandler getUpgradeInventory() {
        return ballMillUpgradeInventory;
    }

    public ItemStack[] getInstalledUpgrades() {
        List<ItemStack> upgrades = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            upgrades.add(ballMillUpgradeInventory.getStackInSlot(i));
        }
        return upgrades.toArray(new ItemStack[]{});
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
        LOGGER.trace(LogMarkers.MACHINE, "Ball Mill at ({}, {}, {}) synced progress with powerRequired {} and powerRemaining {}", getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), powerRequired, powerRemaining);
    }

    // Client-Side Only
    @Override
    public void onPowerRateUpdate(float received, float expected) {
        // TODO: Consider using a rolling window to display ramp up/down
        lastPowerReceived = received;
        maxPowerPerTick = expected;
        LOGGER.trace(LogMarkers.MACHINE, "Ball Mill at ({}, {}, {}) synced power rate with lastPowerReceived {} and maxPowerPerTick {}", getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), lastPowerReceived, maxPowerPerTick);
    }

    // Client-Side Only
    @Override
    public void onContainerOpened(ItemStack[] upgrades) {
        for(int i = 0; i < upgrades.length; i++) {
            ballMillUpgradeInventory.setStackInSlot(i, upgrades[i]);
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        LOGGER.trace(LogMarkers.MACHINE, "Writing Ball Mill at ({}, {}, {}) to NBT...", getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ());
        compound.put("inventory", ballMillInventory.serializeNBT());
        compound.put("upgradeInventory", ballMillUpgradeInventory.serializeNBT());
        compound.putFloat("powerRequired", powerRequired);
        compound.putFloat("powerRemaining", powerRemaining);
        compound.putFloat("maxPowerPerTick", maxPowerPerTick);
        return compound;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        LOGGER.debug(LogMarkers.MACHINE, "Reading Ball Mill at ({}, {}, {}) from NBT...", getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ());
        ballMillInventory.deserializeNBT(nbt.getCompound("inventory"));
        ballMillUpgradeInventory.deserializeNBT(nbt.getCompound("upgradeInventory"));
        powerRequired = nbt.getFloat("powerRequired");
        powerRemaining = nbt.getFloat("powerRemaining");
        maxPowerPerTick = nbt.getFloat("maxPowerPerTick");
        LOGGER.debug(LogMarkers.MACHINE, "Ball Mill Loaded!");
    }

    private int getTier() {
        Item millingBallItem = ballMillUpgradeInventory.getStackInSlot(0).getItem();
        if (!(millingBallItem instanceof IMillingBallUpgrade)) return -1;
        IMillingBallUpgrade millingBall = (IMillingBallUpgrade)millingBallItem;
        return millingBall.getTier().getLevel();
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
        LOGGER.trace(LogMarkers.MACHINE, "Trying to start Ball Mill recipe...");
        BallMillRecipe recipe = level.getRecipeManager().getRecipeFor(BallMillRecipe.recipeType, new RecipeWrapper(ballMillInventory), level).orElse(null);
        if (recipe == null) {
            LOGGER.trace(LogMarkers.MACHINE, "No recipe matches input. Skipping...");
            return false;
        }
        if (getTier() < recipe.getTierRequired()) {
            LOGGER.trace(LogMarkers.MACHINE, "Tier {} not sufficient for matching recipe. Needed {}. Skipping...", getTier(), recipe.getTierRequired());
            return false;
        }
        ItemStack maxResult = recipe.getMaxOutput();
        if (!ballMillInventory.insertItem(1, maxResult, true).isEmpty()) {
            LOGGER.trace(LogMarkers.MACHINE, "Simulation shows this recipe may not have enough room in output to complete. Skipping...");
            return false;
        }
        lastPowerReceived = 0;
        powerRequired = recipe.getPowerRequired();
        powerRemaining = recipe.getPowerRequired();
        maxPowerPerTick = recipe.getPowerRequired() / (float)recipe.getProcessTime();
        currentRecipe = recipe;
        LOGGER.debug(LogMarkers.MACHINE, "Recipe started: {}", maxResult.getDisplayName());
        return true;
    }

    private void updateClientGui() {
        MachineProgressPacket machineProgress = new MachineProgressPacket(powerRemaining, powerRequired, getBlockPos());
        PowerRatePacket powerRate = new PowerRatePacket(lastPowerReceived, maxPowerPerTick, getBlockPos());
        LevelChunk chunk = level.getChunkAt(getBlockPos());
        LOGGER.trace(LogMarkers.NETWORK, "Sending MachineProgress packet to update gui at ({}, {}, {})...", getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ());
        ModPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), machineProgress);
        LOGGER.trace(LogMarkers.NETWORK, "Sending PowerRate packet to update gui at ({}, {}, {})...", getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ());
        ModPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), powerRate);
        LOGGER.trace(LogMarkers.NETWORK, "Packet sent");
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

    public void tick() {
        if (level.isClientSide) return;

        if (firstTick) {
            firstTick = false;
            if (!ballMillInventory.getStackInSlot(0).isEmpty()) {
                currentRecipe = level.getRecipeManager().getRecipeFor(BallMillRecipe.recipeType, new RecipeWrapper(ballMillInventory), level).orElse(null);
                LOGGER.debug(LogMarkers.MACHINE, "Ball Mill input at ({}, {}, {}) not empty on first tick, initialized current recipe", getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ());
            }
        }

        if (currentRecipe == null) {
            boolean recipeStarted = tryStartRecipe();
            if (!recipeStarted) return;
        }

        if (!currentRecipe.getIngredient().test(ballMillInventory.getStackInSlot(0))) {
            LOGGER.warn(LogMarkers.MACHINE, "The item in the input slot changed out from under us. Bail!");
            currentRecipe = null;
            lastPowerReceived = 0;
            powerRequired = 0;
            powerRemaining = 0;
            maxPowerPerTick = 0;
            updateClientGui();
            setChanged();
            return;
        }

        if (getTier() < currentRecipe.getTierRequired()) {
            LOGGER.debug(LogMarkers.MACHINE, "Tier {} not sufficient for current recipe. Needed {}.", getTier(), currentRecipe.getTierRequired());
            return;
        }

        IPowerProvider powerProvider = getPowerProvider();
        lastPowerReceived = powerProvider.consumePower(maxPowerPerTick * getMotorSpeed(), (ServerLevel)level, worldPosition);
        powerRemaining -= lastPowerReceived;
        if (powerRemaining <= 0) {
            LOGGER.debug(LogMarkers.MACHINE, "Ball Mill operation complete, processing results...");
            Random rand = level.getRandom();
            ItemStack resultStack;
            LOGGER.debug(LogMarkers.MACHINE, "Rolling to determine if process was successful...");
            if (rand.nextFloat() > getProcessChance()) {
                LOGGER.debug(LogMarkers.MACHINE, "Failed!");
                resultStack = ItemStack.EMPTY;
            } else {
                LOGGER.debug(LogMarkers.MACHINE, "Success!");
                ballMillInventory.extractItem(0, 1, false);
                resultStack = currentRecipe.getResultItem().copy();
            }

            if (!resultStack.isEmpty() && getEfficiencyModifier() > 0) {
                LOGGER.debug(LogMarkers.MACHINE, "Rolling to determine if extra results happen...");
                if (currentRecipe.hasExtraChance() && rand.nextFloat() <= (currentRecipe.getExtraChance() * getEfficiencyModifier())) {
                    LOGGER.debug(LogMarkers.MACHINE, "Success!");
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
        setChanged();
    }
}
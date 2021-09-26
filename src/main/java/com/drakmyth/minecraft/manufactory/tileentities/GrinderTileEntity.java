/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.tileentities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.init.ModTileEntityTypes;
import com.drakmyth.minecraft.manufactory.items.upgrades.IGrinderWheelUpgrade;
import com.drakmyth.minecraft.manufactory.items.upgrades.IMotorUpgrade;
import com.drakmyth.minecraft.manufactory.items.upgrades.IPowerProvider;
import com.drakmyth.minecraft.manufactory.network.IMachineProgressListener;
import com.drakmyth.minecraft.manufactory.network.IOpenContainerWithUpgradesListener;
import com.drakmyth.minecraft.manufactory.network.IPowerRateListener;
import com.drakmyth.minecraft.manufactory.network.MachineProgressPacket;
import com.drakmyth.minecraft.manufactory.network.ModPacketHandler;
import com.drakmyth.minecraft.manufactory.network.PowerRatePacket;
import com.drakmyth.minecraft.manufactory.recipes.GrinderRecipe;

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

public class GrinderTileEntity extends BlockEntity implements IMachineProgressListener, IPowerRateListener, IOpenContainerWithUpgradesListener {
    private static final Logger LOGGER = LogManager.getLogger();

    private boolean firstTick;
    private ItemStackHandler grinderInventory;
    private ItemStackHandler grinderUpgradeInventory;
    private GrinderRecipe currentRecipe;
    private float lastPowerReceived;
    private float powerRequired;
    private float powerRemaining;
    private float maxPowerPerTick;

    public GrinderTileEntity(BlockPos pos, BlockState state) {
        super(ModTileEntityTypes.GRINDER.get(), pos, state);

        firstTick = true;
        grinderInventory = new ItemStackHandler(2);
        grinderUpgradeInventory = new ItemStackHandler(4);
        LOGGER.debug(LogMarkers.MACHINE, "Grinder tile entity initialized with %d inventory slots and %d upgrade inventory slots", grinderInventory.getSlots(), grinderUpgradeInventory.getSlots());
    }

    public ItemStackHandler getInventory() {
        return grinderInventory;
    }

    public ItemStackHandler getUpgradeInventory() {
        return grinderUpgradeInventory;
    }

    public ItemStack[] getInstalledUpgrades() {
        List<ItemStack> upgrades = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            upgrades.add(grinderUpgradeInventory.getStackInSlot(i));
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
        LOGGER.trace(LogMarkers.MACHINE, "Grinder at (%d, %d, %d) synced progress with powerRequired %f and powerRemaining %f", getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), powerRequired, powerRemaining);
    }

    // Client-Side Only
    @Override
    public void onPowerRateUpdate(float received, float expected) {
        // TODO: Consider using a rolling window to display ramp up/down
        lastPowerReceived = received;
        maxPowerPerTick = expected;
        LOGGER.trace(LogMarkers.MACHINE, "Grinder at (%d, %d, %d) synced power rate with lastPowerReceived %f and maxPowerPerTick %f", getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), lastPowerReceived, maxPowerPerTick);
    }

    // Client-Side Only
    @Override
    public void onContainerOpened(ItemStack[] upgrades) {
        for(int i = 0; i < upgrades.length; i++) {
            grinderUpgradeInventory.setStackInSlot(i, upgrades[i]);
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        LOGGER.trace(LogMarkers.MACHINE, "Writing Grinder at (%d, %d, %d) to NBT...", getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ());
        compound.put("inventory", grinderInventory.serializeNBT());
        compound.put("upgradeInventory", grinderUpgradeInventory.serializeNBT());
        compound.putFloat("powerRequired", powerRequired);
        compound.putFloat("powerRemaining", powerRemaining);
        compound.putFloat("maxPowerPerTick", maxPowerPerTick);
        return compound;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        LOGGER.debug(LogMarkers.MACHINE, "Reading Grinder at (%d, %d, %d) from NBT...", getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ());
        grinderInventory.deserializeNBT(nbt.getCompound("inventory"));
        grinderUpgradeInventory.deserializeNBT(nbt.getCompound("upgradeInventory"));
        powerRequired = nbt.getFloat("powerRequired");
        powerRemaining = nbt.getFloat("powerRemaining");
        maxPowerPerTick = nbt.getFloat("maxPowerPerTick");
        LOGGER.debug(LogMarkers.MACHINE, "Grinder Loaded!");
    }

    private int getTier() {
        if (!hasBothWheels()) return -1;
        IGrinderWheelUpgrade wheel1 = (IGrinderWheelUpgrade)grinderUpgradeInventory.getStackInSlot(0).getItem();
        IGrinderWheelUpgrade wheel2 = (IGrinderWheelUpgrade)grinderUpgradeInventory.getStackInSlot(1).getItem();
        return Math.max(wheel1.getTier().getLevel(), wheel2.getTier().getLevel());
    }

    private float getEfficiencyModifier() {
        if (!hasBothWheels()) return 0;
        IGrinderWheelUpgrade wheel1 = (IGrinderWheelUpgrade)grinderUpgradeInventory.getStackInSlot(0).getItem();
        IGrinderWheelUpgrade wheel2 = (IGrinderWheelUpgrade)grinderUpgradeInventory.getStackInSlot(1).getItem();
        return wheel1.getTier().getLevel() <= wheel2.getTier().getLevel() ? wheel1.getEfficiency() : wheel2.getEfficiency();
    }

    private boolean hasBothWheels() {
        Item wheel1 = grinderUpgradeInventory.getStackInSlot(0).getItem();
        Item wheel2 = grinderUpgradeInventory.getStackInSlot(1).getItem();
        if (!(wheel1 instanceof IGrinderWheelUpgrade) || !(wheel2 instanceof IGrinderWheelUpgrade)) return false;
        return true;
    }

    private boolean tryStartRecipe() {
        LOGGER.trace(LogMarkers.MACHINE, "Trying to start Grinder recipe...");
        GrinderRecipe recipe = level.getRecipeManager().getRecipeFor(GrinderRecipe.recipeType, new RecipeWrapper(grinderInventory), level).orElse(null);
        if (recipe == null) {
            LOGGER.trace(LogMarkers.MACHINE, "No recipe matches input. Skipping...");
            return false;
        }
        if (!hasBothWheels()) {
            LOGGER.trace(LogMarkers.MACHINE, "Missing one or both grinder wheels. Skipping...");
            return false;
        }
        if (getTier() < recipe.getTierRequired()) {
            LOGGER.trace(LogMarkers.MACHINE, "Tier %d not sufficient for matching recipe. Needed %d. Skipping...", getTier(), recipe.getTierRequired());
            return false;
        }
        ItemStack maxResult = recipe.getMaxOutput();
        if (!grinderInventory.insertItem(1, maxResult, true).isEmpty()) {
            LOGGER.trace(LogMarkers.MACHINE, "Simulation shows this recipe may not have enough room in output to complete. Skipping...");
            return false;
        }
        lastPowerReceived = 0;
        powerRequired = recipe.getPowerRequired();
        powerRemaining = recipe.getPowerRequired();
        maxPowerPerTick = recipe.getPowerRequired() / (float)recipe.getProcessTime();
        currentRecipe = recipe;
        LOGGER.debug(LogMarkers.MACHINE, "Recipe started: %s", maxResult.getDisplayName());
        return true;
    }

    private void updateClientGui() {
        MachineProgressPacket machineProgress = new MachineProgressPacket(powerRemaining, powerRequired, getBlockPos());
        PowerRatePacket powerRate = new PowerRatePacket(lastPowerReceived, maxPowerPerTick, getBlockPos());
        LevelChunk chunk = level.getChunkAt(getBlockPos());
        LOGGER.trace(LogMarkers.NETWORK, "Sending MachineProgress packet to update gui at (%d, %d, %d)...", getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ());
        ModPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), machineProgress);
        LOGGER.trace(LogMarkers.NETWORK, "Sending PowerRate packet to update gui at (%d, %d, %d)...", getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ());
        ModPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), powerRate);
        LOGGER.trace(LogMarkers.NETWORK, "Packet sent");
    }

    private float getMotorSpeed() {
        Item motor = grinderUpgradeInventory.getStackInSlot(2).getItem();
        return motor instanceof IMotorUpgrade ? ((IMotorUpgrade)motor).getPowerCapMultiplier() : 0.0f;
    }

    private IPowerProvider getPowerProvider() {
        Item powerProvider = grinderUpgradeInventory.getStackInSlot(3).getItem();
        IPowerProvider emptyPowerProvider = (requestedPower, world, pos) -> 0;
        return powerProvider instanceof IPowerProvider ? (IPowerProvider)powerProvider : emptyPowerProvider;
    }

    public void tick() {
        if (level.isClientSide) return;

        if (firstTick) {
            firstTick = false;
            if (!grinderInventory.getStackInSlot(0).isEmpty()) {
                currentRecipe = level.getRecipeManager().getRecipeFor(GrinderRecipe.recipeType, new RecipeWrapper(grinderInventory), level).orElse(null);
                LOGGER.debug(LogMarkers.MACHINE, "Grinder input at (%d, %d, %d) not empty on first tick, initialized current recipe", getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ());
            }
        }

        if (currentRecipe == null) {
            boolean recipeStarted = tryStartRecipe();
            if (!recipeStarted) return;
        }

        if (!currentRecipe.getIngredient().test(grinderInventory.getStackInSlot(0))) {
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
            LOGGER.debug(LogMarkers.MACHINE, "Tier %d not sufficient for current recipe. Needed %d.", getTier(), currentRecipe.getTierRequired());
            return;
        }

        IPowerProvider powerProvider = getPowerProvider();
        lastPowerReceived = powerProvider.consumePower(maxPowerPerTick * getMotorSpeed(), (ServerLevel)level, worldPosition);
        powerRemaining -= lastPowerReceived;
        if (powerRemaining <= 0) {
            LOGGER.debug(LogMarkers.MACHINE, "Grinder operation complete, processing results...");
            grinderInventory.extractItem(0, 1, false);
            ItemStack resultStack = currentRecipe.getResultItem().copy();
            Random rand = level.getRandom();
            if (getEfficiencyModifier() > 0) {
                LOGGER.debug(LogMarkers.MACHINE, "Rolling to determine if extra results happen...");
                if (currentRecipe.hasExtraChance() && rand.nextFloat() <= (currentRecipe.getExtraChance() * getEfficiencyModifier())) {
                    LOGGER.debug(LogMarkers.MACHINE, "Success!");
                    resultStack.grow(currentRecipe.getRandomExtraAmount(rand));
                }
            }
            grinderInventory.insertItem(1, resultStack, false);
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

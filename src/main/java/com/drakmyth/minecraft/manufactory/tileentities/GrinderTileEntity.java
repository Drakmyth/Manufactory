/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.tileentities;

import java.util.Random;

import com.drakmyth.minecraft.manufactory.init.ModTileEntityTypes;
import com.drakmyth.minecraft.manufactory.items.upgrades.IGrinderWheelUpgrade;
import com.drakmyth.minecraft.manufactory.items.upgrades.IMotorUpgrade;
import com.drakmyth.minecraft.manufactory.items.upgrades.IPowerProvider;
import com.drakmyth.minecraft.manufactory.network.IMachineProgressListener;
import com.drakmyth.minecraft.manufactory.network.MachineProgressPacket;
import com.drakmyth.minecraft.manufactory.network.ModPacketHandler;
import com.drakmyth.minecraft.manufactory.recipes.GrinderRecipe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

public class GrinderTileEntity extends TileEntity implements ITickableTileEntity, IMachineProgressListener {
    private static final Logger LOGGER = LogManager.getLogger();

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
        LOGGER.debug("Grinder tile entity initialized with %d inventory slots and %d upgrade inventory slots", grinderInventory.getSlots(), grinderUpgradeInventory.getSlots());
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
        float motorSpeed = getMotorSpeed();
        if (maxPowerPerTick <= 0 || motorSpeed <= 0) return 0;
        return lastPowerReceived / (maxPowerPerTick * motorSpeed);
    }

    // Client-Side Only
    @Override
    public void onProgressUpdate(float progress, float total) {
        powerRequired = total;
        powerRemaining = progress;
        LOGGER.trace("Grinder at (%d, %d, %d) synced progress with powerRequired %f and powerRemaining %f", getPos().getX(), getPos().getY(), getPos().getZ(), powerRequired, powerRemaining);
    }

    // Client-Side Only
    @Override
    public void onPowerRateUpdate(float amount, float expected) {
        // TODO: Consider using a rolling window to display ramp up/down
        lastPowerReceived = amount;
        maxPowerPerTick = expected;
        LOGGER.trace("Grinder at (%d, %d, %d) synced power rate with lastPowerReceived %f and maxPowerPerTick %f", getPos().getX(), getPos().getY(), getPos().getZ(), lastPowerReceived, maxPowerPerTick);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        LOGGER.trace("Writing Grinder at (%d, %d, %d) to NBT...", getPos().getX(), getPos().getY(), getPos().getZ());
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
        LOGGER.debug("Reading Grinder at (%d, %d, %d) from NBT...", getPos().getX(), getPos().getY(), getPos().getZ());
        grinderInventory.deserializeNBT(nbt.getCompound("inventory"));
        grinderUpgradeInventory.deserializeNBT(nbt.getCompound("upgradeInventory"));
        powerRequired = nbt.getFloat("powerRequired");
        powerRemaining = nbt.getFloat("powerRemaining");
        maxPowerPerTick = nbt.getFloat("maxPowerPerTick");
        LOGGER.debug("Grinder Loaded!");
    }

    private int getTier() {
        if (!hasBothWheels()) return -1;
        IGrinderWheelUpgrade wheel1 = (IGrinderWheelUpgrade)grinderUpgradeInventory.getStackInSlot(0).getItem();
        IGrinderWheelUpgrade wheel2 = (IGrinderWheelUpgrade)grinderUpgradeInventory.getStackInSlot(1).getItem();
        return Math.max(wheel1.getTier().getHarvestLevel(), wheel2.getTier().getHarvestLevel());
    }

    private float getEfficiencyModifier() {
        if (!hasBothWheels()) return 0;
        IGrinderWheelUpgrade wheel1 = (IGrinderWheelUpgrade)grinderUpgradeInventory.getStackInSlot(0).getItem();
        IGrinderWheelUpgrade wheel2 = (IGrinderWheelUpgrade)grinderUpgradeInventory.getStackInSlot(1).getItem();
        return wheel1.getTier().getHarvestLevel() <= wheel2.getTier().getHarvestLevel() ? wheel1.getEfficiency() : wheel2.getEfficiency();
    }

    private boolean hasBothWheels() {
        Item wheel1 = grinderUpgradeInventory.getStackInSlot(0).getItem();
        Item wheel2 = grinderUpgradeInventory.getStackInSlot(1).getItem();
        if (!(wheel1 instanceof IGrinderWheelUpgrade) || !(wheel2 instanceof IGrinderWheelUpgrade)) return false;
        return true;
    }

    private boolean tryStartRecipe() {
        LOGGER.trace("Trying to start Grinder recipe...");
        GrinderRecipe recipe = world.getRecipeManager().getRecipe(GrinderRecipe.recipeType, new RecipeWrapper(grinderInventory), world).orElse(null);
        if (recipe == null) {
            LOGGER.trace("No recipe matches input. Skipping...");
            return false;
        }
        if (!hasBothWheels()) {
            LOGGER.trace("Missing one or both grinder wheels. Skipping...");
            return false;
        }
        if (getTier() < recipe.getTierRequired()) {
            LOGGER.trace("Tier %d not sufficient for matching recipe. Needed %d. Skipping...", getTier(), recipe.getTierRequired());
            return false;
        }
        ItemStack maxResult = recipe.getMaxOutput();
        if (!grinderInventory.insertItem(1, maxResult, true).isEmpty()) {
            LOGGER.trace("Simulation shows this recipe may not have enough room in output to complete. Skipping...");
            return false;
        }
        lastPowerReceived = 0;
        powerRequired = recipe.getPowerRequired();
        powerRemaining = recipe.getPowerRequired();
        maxPowerPerTick = recipe.getPowerRequired() / (float)recipe.getProcessTime();
        currentRecipe = recipe;
        LOGGER.debug("Recipe started: %s", maxResult.getDisplayName());
        return true;
    }

    private void updateClientGui() {
        LOGGER.trace("Sending MachineProgress packet to update gui at (%d, %d, %d)...", getPos().getX(), getPos().getY(), getPos().getZ());
        MachineProgressPacket msg = new MachineProgressPacket(powerRemaining, powerRequired, lastPowerReceived, maxPowerPerTick, getPos());
        Chunk chunk = world.getChunkAt(getPos());
        ModPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), msg);
        LOGGER.trace("Packet sent");
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

    @Override
    public void tick() {
        if (world.isRemote) return;

        if (firstTick) {
            firstTick = false;
            if (!grinderInventory.getStackInSlot(0).isEmpty()) {
                currentRecipe = world.getRecipeManager().getRecipe(GrinderRecipe.recipeType, new RecipeWrapper(grinderInventory), world).orElse(null);
                LOGGER.debug("Grinder input at (%d, %d, %d) not empty on first tick, initialized current recipe", getPos().getX(), getPos().getY(), getPos().getZ());
            }
        }

        if (currentRecipe == null) {
            boolean recipeStarted = tryStartRecipe();
            if (!recipeStarted) return;
        }

        if (!currentRecipe.getIngredient().test(grinderInventory.getStackInSlot(0))) {
            LOGGER.warn("The item in the input slot changed out from under us. Bail!");
            currentRecipe = null;
            lastPowerReceived = 0;
            powerRequired = 0;
            powerRemaining = 0;
            maxPowerPerTick = 0;
            updateClientGui();
            markDirty();
            return;
        }

        if (getTier() < currentRecipe.getTierRequired()) {
            LOGGER.debug("Tier %d not sufficient for current recipe. Needed %d.", getTier(), currentRecipe.getTierRequired());
            return;
        }

        IPowerProvider powerProvider = getPowerProvider();
        lastPowerReceived = powerProvider.consumePower(maxPowerPerTick * getMotorSpeed(), (ServerWorld)world, pos);
        powerRemaining -= lastPowerReceived; // TODO: Consider making PowerRateUpdate its own packet and only sending if different from last tick
        if (powerRemaining <= 0) {
            LOGGER.debug("Grinder operation complete, processing results...");
            grinderInventory.extractItem(0, 1, false);
            ItemStack resultStack = currentRecipe.getRecipeOutput().copy();
            Random rand = world.getRandom();
            if (getEfficiencyModifier() > 0) {
                LOGGER.debug("Rolling to determine if extra results happen...");
                if (currentRecipe.hasExtraChance() && rand.nextFloat() <= (currentRecipe.getExtraChance() * getEfficiencyModifier())) {
                    LOGGER.debug("Success!");
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
        markDirty();
    }
}

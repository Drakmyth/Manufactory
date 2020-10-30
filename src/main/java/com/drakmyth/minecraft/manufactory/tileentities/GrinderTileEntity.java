/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.tileentities;

import com.drakmyth.minecraft.manufactory.containers.GrinderContainer;
import com.drakmyth.minecraft.manufactory.init.ModTileEntityTypes;
import com.drakmyth.minecraft.manufactory.recipes.GrinderRecipe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class GrinderTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    private static final Logger LOGGER = LogManager.getLogger();

    private Inventory grinderInventory;
    private GrinderRecipe currentRecipe;
    private float powerRequired;
    private float powerRemaining; // 25 power, defined by recipe
    private float maxPowerPerTick; // powerRequired / processTime

    public GrinderTileEntity() {
        super(ModTileEntityTypes.GRINDER.get());

        grinderInventory = new Inventory(2);
    }

    public Inventory getInventory() {
        return grinderInventory;
    }

    public float getProgress() {
        return (powerRequired - powerRemaining) / powerRequired;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Grinder");
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
        return new GrinderContainer(windowId, playerInventory, player, getPos());
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
    }

    private boolean tryStartRecipe() {
        GrinderRecipe recipe = world.getRecipeManager().getRecipe(GrinderRecipe.recipeType, grinderInventory, world).orElse(null);
        if (recipe == null) return false;
        powerRequired = recipe.getPowerRequired();
        powerRemaining = recipe.getPowerRequired();
        maxPowerPerTick = recipe.getPowerRequired() / (float)recipe.getProcessTime();
        currentRecipe = recipe;
        LOGGER.debug(String.format("Starting recipe: %s, %f, %f", recipe.getId(), powerRemaining, maxPowerPerTick));
        return true;
    }

    @Override
    public void tick() {
        if (world.isRemote) return;
        if (currentRecipe == null) {
            boolean recipeStarted = tryStartRecipe();
            if (!recipeStarted) return;
        }

        if (!currentRecipe.getIngredient().test(grinderInventory.getStackInSlot(0))) {
            currentRecipe = null;
            powerRequired = 0;
            powerRemaining = 0;
            maxPowerPerTick = 0;
            return;
        }

        float power = maxPowerPerTick; // TODO: Get from power network
        powerRemaining -= power;
        if (powerRemaining <= 0) {
            grinderInventory.decrStackSize(0, 1);
            grinderInventory.setInventorySlotContents(1, currentRecipe.getResults().get(0).getA().copy());
            currentRecipe = null;
            powerRequired = 0;
            powerRemaining = 0;
            maxPowerPerTick = 0;
        }
    }
}

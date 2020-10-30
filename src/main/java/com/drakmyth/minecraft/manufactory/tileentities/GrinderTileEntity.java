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
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class GrinderTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    private static final Logger LOGGER = LogManager.getLogger();

    private boolean firstTick;
    private ItemStackHandler grinderInventory;
    private GrinderRecipe currentRecipe;
    private float powerRequired;
    private float powerRemaining; // 25 power, defined by recipe
    private float maxPowerPerTick; // powerRequired / processTime

    public GrinderTileEntity() {
        super(ModTileEntityTypes.GRINDER.get());

        firstTick = true;
        grinderInventory = createNewInventory();
    }

    public ItemStackHandler getInventory() {
        return grinderInventory;
    }

    public float getProgress() {
        if (powerRequired <= 0) return 0;
        return (powerRequired - powerRemaining) / powerRequired;
    }

    private ItemStackHandler createNewInventory() {
        return new ItemStackHandler(2);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Grinder");
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
        return new GrinderContainer(windowId, new InvWrapper(playerInventory), player, getPos());
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("inventory", grinderInventory.serializeNBT());
        compound.putFloat("powerRequired", powerRequired);
        compound.putFloat("powerRemaining", powerRemaining);
        compound.putFloat("maxPowerPerTick", maxPowerPerTick);
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        grinderInventory = createNewInventory();
        grinderInventory.deserializeNBT(nbt.getCompound("inventory"));
        powerRequired = nbt.getFloat("powerRequired");
        powerRemaining = nbt.getFloat("powerRemaining");
        maxPowerPerTick = nbt.getFloat("maxPowerPerTick");
    }

    private boolean tryStartRecipe() {
        GrinderRecipe recipe = world.getRecipeManager().getRecipe(GrinderRecipe.recipeType, new RecipeWrapper(grinderInventory), world).orElse(null);
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
            powerRequired = 0;
            powerRemaining = 0;
            maxPowerPerTick = 0;
            markDirty();
            return;
        }

        float power = maxPowerPerTick; // TODO: Get from power network
        powerRemaining -= power;
        if (powerRemaining <= 0) {
            grinderInventory.extractItem(0, 1, false);
            ItemStack outputStack = grinderInventory.getStackInSlot(1).copy();
            ItemStack recipeResultStack = currentRecipe.getResults().get(0).getA();
            // TODO: Account for additional results
            if (!outputStack.isItemEqual(recipeResultStack)) {
                outputStack = recipeResultStack.copy();
            }
            grinderInventory.insertItem(1, outputStack, false);
            currentRecipe = null;
            powerRequired = 0;
            powerRemaining = 0;
            maxPowerPerTick = 0;
        }

        markDirty();
    }
}

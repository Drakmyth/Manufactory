package com.drakmyth.minecraft.manufactory.blocks.entities;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.init.ModBlockEntityTypes;
import com.drakmyth.minecraft.manufactory.init.ModRecipeTypes;
import com.drakmyth.minecraft.manufactory.items.upgrades.IGrinderWheelUpgrade;
import com.drakmyth.minecraft.manufactory.items.upgrades.IMotorUpgrade;
import com.drakmyth.minecraft.manufactory.items.upgrades.IPowerProvider;
import com.drakmyth.minecraft.manufactory.network.IMachineProgressListener;
import com.drakmyth.minecraft.manufactory.network.IOpenMenuWithUpgradesListener;
import com.drakmyth.minecraft.manufactory.network.IPowerRateListener;
import com.drakmyth.minecraft.manufactory.network.MachineProgressPacket;
import com.drakmyth.minecraft.manufactory.network.ModPacketHandler;
import com.drakmyth.minecraft.manufactory.network.PowerRatePacket;
import com.drakmyth.minecraft.manufactory.recipes.GrinderRecipe;
import com.drakmyth.minecraft.manufactory.util.LogHelper;
import com.drakmyth.minecraft.manufactory.util.TierHelper;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class GrinderBlockEntity extends BlockEntity implements IMachineProgressListener, IPowerRateListener, IOpenMenuWithUpgradesListener {
    private static final Logger LOGGER = LogUtils.getLogger();

    private boolean firstTick;
    private ItemStackHandler grinderInventory;
    private ItemStackHandler grinderUpgradeInventory;
    private GrinderRecipe currentRecipe;
    private float lastPowerReceived;
    private float powerRequired;
    private float powerRemaining;
    private float maxPowerPerTick;

    public GrinderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.GRINDER.get(), pos, state);

        firstTick = true;
        grinderInventory = new ItemStackHandler(2);
        grinderUpgradeInventory = new ItemStackHandler(4);
        LOGGER.debug(LogMarkers.MACHINE, "Grinder block entity initialized with {} inventory slots and {} upgrade inventory slots", grinderInventory.getSlots(),
                grinderUpgradeInventory.getSlots());
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
        return upgrades.toArray(new ItemStack[] {});
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
        LOGGER.trace(LogMarkers.MACHINE, "Grinder at {} synced progress with powerRequired {} and powerRemaining {}", LogHelper.blockPos(getBlockPos()), powerRequired,
                powerRemaining);
    }

    // Client-Side Only
    @Override
    public void onPowerRateUpdate(float received, float expected) {
        // TODO: Consider using a rolling window to display ramp up/down
        lastPowerReceived = received;
        maxPowerPerTick = expected;
        LOGGER.trace(LogMarkers.MACHINE, "Grinder at {} synced power rate with lastPowerReceived {} and maxPowerPerTick {}", LogHelper.blockPos(getBlockPos()), lastPowerReceived,
                maxPowerPerTick);
    }

    // Client-Side Only
    @Override
    public void onContainerOpened(ItemStack[] upgrades) {
        for (int i = 0; i < upgrades.length; i++) {
            grinderUpgradeInventory.setStackInSlot(i, upgrades[i]);
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        LOGGER.trace(LogMarkers.MACHINE, "Writing Grinder at {} to NBT...", LogHelper.blockPos(getBlockPos()));
        compound.put("inventory", grinderInventory.serializeNBT());
        compound.put("upgradeInventory", grinderUpgradeInventory.serializeNBT());
        compound.putFloat("powerRequired", powerRequired);
        compound.putFloat("powerRemaining", powerRemaining);
        compound.putFloat("maxPowerPerTick", maxPowerPerTick);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        LOGGER.debug(LogMarkers.MACHINE, "Reading Grinder at {} from NBT...", LogHelper.blockPos(getBlockPos()));
        grinderInventory.deserializeNBT(tag.getCompound("inventory"));
        grinderUpgradeInventory.deserializeNBT(tag.getCompound("upgradeInventory"));
        powerRequired = tag.getFloat("powerRequired");
        powerRemaining = tag.getFloat("powerRemaining");
        maxPowerPerTick = tag.getFloat("maxPowerPerTick");
        LOGGER.debug(LogMarkers.MACHINE, "Grinder Loaded!");
    }

    @Nullable
    private Tier getTier() {
        if (!hasBothWheels()) return null;
        IGrinderWheelUpgrade wheel1 = (IGrinderWheelUpgrade)grinderUpgradeInventory.getStackInSlot(0).getItem();
        IGrinderWheelUpgrade wheel2 = (IGrinderWheelUpgrade)grinderUpgradeInventory.getStackInSlot(1).getItem();
        return TierHelper.max(wheel1.getTier(), wheel2.getTier());
    }

    private float getEfficiencyModifier() {
        if (!hasBothWheels()) return 0;
        IGrinderWheelUpgrade wheel1 = (IGrinderWheelUpgrade)grinderUpgradeInventory.getStackInSlot(0).getItem();
        IGrinderWheelUpgrade wheel2 = (IGrinderWheelUpgrade)grinderUpgradeInventory.getStackInSlot(1).getItem();
        return TierHelper.compare(wheel1.getTier(), wheel2.getTier()) < 0 ? wheel1.getEfficiency() : wheel2.getEfficiency();
    }

    private boolean hasBothWheels() {
        Item wheel1 = grinderUpgradeInventory.getStackInSlot(0).getItem();
        Item wheel2 = grinderUpgradeInventory.getStackInSlot(1).getItem();
        return (IGrinderWheelUpgrade.class.isInstance(wheel1) && IGrinderWheelUpgrade.class.isInstance(wheel2));
    }

    private boolean tryStartRecipe(Level level) {
        LOGGER.trace(LogMarkers.MACHINE, "Trying to start Grinder recipe...");
        GrinderRecipe recipe = level.getRecipeManager().getRecipeFor(ModRecipeTypes.GRINDER.get(), new RecipeWrapper(grinderInventory), level).orElse(null);
        if (recipe == null) {
            LOGGER.trace(LogMarkers.MACHINE, "No recipe matches input. Skipping...");
            return false;
        }
        if (!hasBothWheels()) {
            LOGGER.trace(LogMarkers.MACHINE, "Missing one or both grinder wheels. Skipping...");
            return false;
        }
        if (TierHelper.compare(getTier(), recipe.getTierRequired()) < 0) {
            LOGGER.trace(LogMarkers.MACHINE, "Tier {} not sufficient for matching recipe. Needed {}. Skipping...", getTier(), recipe.getTierRequired());
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
        LOGGER.debug(LogMarkers.MACHINE, "Recipe started: {}", maxResult);
        return true;
    }

    private void updateClientScreen(Level level) {
        MachineProgressPacket machineProgress = new MachineProgressPacket(powerRemaining, powerRequired, getBlockPos());
        PowerRatePacket powerRate = new PowerRatePacket(lastPowerReceived, maxPowerPerTick, getBlockPos());
        LevelChunk chunk = level.getChunkAt(getBlockPos());
        LOGGER.trace(LogMarkers.NETWORK, "Sending MachineProgress packet to update screen at {}...", LogHelper.blockPos(getBlockPos()));
        ModPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), machineProgress);
        LOGGER.trace(LogMarkers.NETWORK, "Sending PowerRate packet to update screen at {}...", LogHelper.blockPos(getBlockPos()));
        ModPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), powerRate);
        LOGGER.trace(LogMarkers.NETWORK, "Packet sent");
    }

    private float getMotorSpeed() {
        Item motorItem = grinderUpgradeInventory.getStackInSlot(2).getItem();
        return motorItem instanceof IMotorUpgrade motor ? motor.getPowerCapMultiplier() : 0.0f;
    }

    private IPowerProvider getPowerProvider() {
        Item powerProviderItem = grinderUpgradeInventory.getStackInSlot(3).getItem();
        IPowerProvider emptyPowerProvider = (requestedPower, level, pos) -> 0;
        return powerProviderItem instanceof IPowerProvider powerProvider ? powerProvider : emptyPowerProvider;
    }

    public void tick() {
        Level level = getLevel();
        if (level == null || level.isClientSide()) return;

        if (firstTick) {
            firstTick = false;
            if (!grinderInventory.getStackInSlot(0).isEmpty()) {
                currentRecipe = level.getRecipeManager().getRecipeFor(ModRecipeTypes.GRINDER.get(), new RecipeWrapper(grinderInventory), level).orElse(null);
                LOGGER.debug(LogMarkers.MACHINE, "Grinder input at {} not empty on first tick, initialized current recipe", LogHelper.blockPos(getBlockPos()));
            }
        }

        if (currentRecipe == null) {
            boolean recipeStarted = tryStartRecipe(level);
            if (!recipeStarted) return;
        }

        if (!currentRecipe.getIngredient().test(grinderInventory.getStackInSlot(0))) {
            LOGGER.warn(LogMarkers.MACHINE, "The item in the input slot changed out from under us. Bail!");
            currentRecipe = null;
            lastPowerReceived = 0;
            powerRequired = 0;
            powerRemaining = 0;
            maxPowerPerTick = 0;
            updateClientScreen(level);
            setChanged();
            return;
        }

        if (TierHelper.compare(getTier(), currentRecipe.getTierRequired()) < 0) {
            LOGGER.debug(LogMarkers.MACHINE, "Tier {} not sufficient for current recipe. Needed {}.", getTier(), currentRecipe.getTierRequired());
            return;
        }

        IPowerProvider powerProvider = getPowerProvider();
        lastPowerReceived = powerProvider.consumePower(maxPowerPerTick * getMotorSpeed(), (ServerLevel)level, getBlockPos());
        powerRemaining -= lastPowerReceived;
        if (powerRemaining <= 0) {
            LOGGER.debug(LogMarkers.MACHINE, "Grinder operation complete, processing results...");
            grinderInventory.extractItem(0, 1, false);
            ItemStack resultStack = currentRecipe.getResultItem().copy();
            RandomSource rand = level.getRandom();
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

        updateClientScreen(level);
        setChanged();
    }
}

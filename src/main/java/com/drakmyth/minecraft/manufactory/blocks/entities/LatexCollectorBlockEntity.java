package com.drakmyth.minecraft.manufactory.blocks.entities;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.blocks.LatexCollectorBlock;
import com.drakmyth.minecraft.manufactory.config.ConfigData;
import com.drakmyth.minecraft.manufactory.init.ModBlockEntityTypes;
import com.drakmyth.minecraft.manufactory.util.LogHelper;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.PacketDistributor;

public class LatexCollectorBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();

    private int totalTicks = 0;
    private int ticksRemaining = 0;

    public LatexCollectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.LATEX_COLLECTOR.get(), pos, state);
    }

    public boolean onTap(Level level, BlockPos pos, BlockState state) {
        if (isWaterlogged(state)) {
            LOGGER.debug(LogMarkers.INTERACTION, "Tapped, but waterlogged");
            return false;
        }
        if (!isEmpty(state)) {
            LOGGER.debug(LogMarkers.INTERACTION, "Tapped, but not empty");
            return false;
        }
        if (ticksRemaining > 0) {
            LOGGER.debug(LogMarkers.INTERACTION, "Tapped, but already filling");
            return false;
        }
        LOGGER.debug(LogMarkers.INTERACTION, "Tapped, starting countdown...");

        int configFillTimeSeconds = ConfigData.SERVER.LatexFillSeconds.get();
        totalTicks = 20 * configFillTimeSeconds;
        ticksRemaining = totalTicks;

        if (level.isClientSide()) return true;
        level.setBlockAndUpdate(getBlockPos(), state.setValue(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FILLING));
        updateClient(level, pos);
        setChanged();
        return true;
    }

    private void updateClient(Level level, BlockPos pos) {
        setChanged();
    }

    private boolean isWaterlogged(BlockState state) {
        return state.getValue(LatexCollectorBlock.WATERLOGGED);
    }

    private boolean isEmpty(BlockState state) {
        return state.getValue(LatexCollectorBlock.FILL_STATUS) == LatexCollectorBlock.FillStatus.EMPTY;
    }

    public int getTicksRemaining() {
        return ticksRemaining;
    }

    @Override
    protected void saveAdditional(ValueOutput compound) {
        super.saveAdditional(compound);
        LOGGER.trace(LogMarkers.MACHINE, "Writing Latex Collector at {} to NBT...", LogHelper.blockPos(getBlockPos()));
        compound.putInt("totalTicks", totalTicks);
        compound.putInt("ticksRemaining", ticksRemaining);
    }

    @Override
    protected void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
        LOGGER.debug(LogMarkers.MACHINE, "Reading Latex Collector at {} from NBT...", LogHelper.blockPos(getBlockPos()));
        totalTicks = tag.getIntOr("totalTicks", 0);
        ticksRemaining = tag.getIntOr("ticksRemaining", 0);
        LOGGER.debug(LogMarkers.MACHINE, "Latex Collector Loaded!");
    }

    private void reset() {
        totalTicks = 0;
        ticksRemaining = 0;
        setChanged();
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (isWaterlogged(state) && ticksRemaining > 0) {
            LOGGER.debug(LogMarkers.MACHINE, "Latex Collector flooded! Stop filling");
            level.setBlockAndUpdate(pos, state.setValue(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.EMPTY));
            reset();
        }

        if (ticksRemaining <= 0) return;
        if (level.isClientSide()) return;
        ticksRemaining--;
        updateClient(level, pos);
        if (ticksRemaining > 0) return;
        LOGGER.debug(LogMarkers.MACHINE, "Latex Collector fill complete! Transitioning to FULL state...");
        level.setBlockAndUpdate(pos, state.setValue(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FULL));
        reset();
    }
    public void onProgressUpdate(float progress, float total) {
        totalTicks = (int)total;
        ticksRemaining = (int)progress;
        LOGGER.trace(LogMarkers.MACHINE, "Latex Collector at {} synced progress with ticksRemaining {} and totalTicks {}", LogHelper.blockPos(getBlockPos()), ticksRemaining,
                totalTicks);
    }
}

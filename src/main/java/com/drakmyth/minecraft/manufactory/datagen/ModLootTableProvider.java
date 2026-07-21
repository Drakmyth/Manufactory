package com.drakmyth.minecraft.manufactory.datagen;

import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(ModBlockLoot::new, LootContextParamSets.BLOCK)), lookupProvider);
    }

    public static class ModBlockLoot extends BlockLootSubProvider {
        public ModBlockLoot(HolderLookup.Provider lookupProvider) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
        }

        @Override
        protected void generate() {
            dropSelf(ModBlocks.AMBER_BLOCK.get());
            dropSelf(ModBlocks.METALOSOL_BLOCK.get());
            dropSelf(ModBlocks.MECHANITE_BLOCK.get());
            dropSelf(ModBlocks.MECHANITE_PANEL.get());
            dropSelf(ModBlocks.MECHANITE_LAMP.get());
            dropSelf(ModBlocks.MECHANITE_LAMP_INVERTED.get());
            dropSelf(ModBlocks.POWER_CELL_RECEPTACLE.get());
            dropSelf(ModBlocks.GRINDER.get());
            dropSelf(ModBlocks.BALL_MILL.get());
            dropSelf(ModBlocks.LATEX_COLLECTOR.get());
            dropSelf(ModBlocks.POWER_CABLE.get());
            dropSelf(ModBlocks.SOLAR_PANEL.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ModBlocks.BLOCKS.getEntries().stream().map(holder -> (Block) holder.get())::iterator;
        }
    }
}

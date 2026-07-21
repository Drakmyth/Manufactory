package com.drakmyth.minecraft.manufactory.datagen;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.init.ModItems;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModItemProvider extends ModelProvider {
    private static final Set<Item> HANDHELD_ITEMS = Set.of(
            ModItems.WRENCH.get(),
            ModItems.TAPPING_KNIFE.get(),
            ModItems.ROCK_DRILL.get(),
            ModItems.SLURRIED_COAL_ORE_BUCKET.get(),
            ModItems.SLURRIED_DIAMOND_ORE_BUCKET.get(),
            ModItems.SLURRIED_EMERALD_ORE_BUCKET.get(),
            ModItems.SLURRIED_GOLD_ORE_BUCKET.get(),
            ModItems.SLURRIED_IRON_ORE_BUCKET.get(),
            ModItems.SLURRIED_COPPER_ORE_BUCKET.get(),
            ModItems.SLURRIED_LAPIS_ORE_BUCKET.get(),
            ModItems.SLURRIED_NETHER_QUARTZ_ORE_BUCKET.get(),
            ModItems.SLURRIED_REDSTONE_ORE_BUCKET.get(),
            ModItems.SLURRIED_ANCIENT_DEBRIS_BUCKET.get());

    public ModItemProvider(PackOutput output) {
        super(output, Reference.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        ModItems.ITEMS.getEntries().forEach(holder -> itemModels.generateFlatItem(
                holder.get(),
                HANDHELD_ITEMS.contains(holder.get()) ? ModelTemplates.FLAT_HANDHELD_ITEM : ModelTemplates.FLAT_ITEM));
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return ModItems.ITEMS.getEntries().stream();
    }
}

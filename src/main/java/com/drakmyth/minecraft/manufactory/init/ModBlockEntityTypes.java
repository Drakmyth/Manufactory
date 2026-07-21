package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.blocks.entities.BallMillBlockEntity;
import com.drakmyth.minecraft.manufactory.blocks.entities.GrinderBlockEntity;
import com.drakmyth.minecraft.manufactory.blocks.entities.LatexCollectorBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;

public final class ModBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Reference.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GrinderBlockEntity>> GRINDER =
            BLOCK_ENTITY_TYPES.register("grinder", () -> new BlockEntityType<>(GrinderBlockEntity::new, ModBlocks.GRINDER.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BallMillBlockEntity>> BALL_MILL =
            BLOCK_ENTITY_TYPES.register("ball_mill", () -> new BlockEntityType<>(BallMillBlockEntity::new, ModBlocks.BALL_MILL.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LatexCollectorBlockEntity>> LATEX_COLLECTOR =
            BLOCK_ENTITY_TYPES.register("latex_collector", () -> new BlockEntityType<>(LatexCollectorBlockEntity::new, ModBlocks.LATEX_COLLECTOR.get()));
}

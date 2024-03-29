package com.drakmyth.minecraft.manufactory.items;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.blocks.LatexCollectorBlock;
import com.drakmyth.minecraft.manufactory.blocks.entities.LatexCollectorBlockEntity;
import com.drakmyth.minecraft.manufactory.config.ConfigData;
import com.drakmyth.minecraft.manufactory.init.ModItems;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class TappingKnifeItem extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();

    public TappingKnifeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        LOGGER.debug(LogMarkers.INTERACTION, "Tapping knife used");
        Level level = context.getLevel();
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        BlockPos bePos = context.getClickedPos().relative(context.getClickedFace());
        BlockEntity be = level.getBlockEntity(bePos);
        if (be == null || !LatexCollectorBlockEntity.class.isInstance(be)) {
            LOGGER.debug(LogMarkers.MACHINE, "Latex Collector block entity not found");
            return InteractionResult.PASS;
        }
        Direction collectorFacing = level.getBlockState(bePos).getValue(LatexCollectorBlock.HORIZONTAL_FACING);
        if (collectorFacing.compareTo(context.getClickedFace().getOpposite()) != 0) {
            LOGGER.debug(LogMarkers.INTERACTION, "Latex Collector found, but not attached to clicked face");
            return InteractionResult.PASS;
        }
        LatexCollectorBlockEntity lcte = (LatexCollectorBlockEntity)be;
        LOGGER.debug(LogMarkers.INTERACTION, "Tapping...");
        boolean tapped = lcte.onTap(level, bePos, level.getBlockState(bePos));
        if (tapped) {
            tryGiveAmber(context.getPlayer(), context.getHand());
        }
        return InteractionResult.CONSUME;
    }

    private void tryGiveAmber(Player player, InteractionHand hand) {
        double configAmberSpawnChance = ConfigData.SERVER.AmberChance.get();
        LOGGER.debug(LogMarkers.INTERACTION, "Rolling for amber against chance {}...", configAmberSpawnChance);
        if (configAmberSpawnChance <= 0) return;
        if (player.getRandom().nextDouble() >= configAmberSpawnChance) return;
        LOGGER.debug(LogMarkers.INTERACTION, "Roll success!");

        int configAmberSpawnCount = ConfigData.SERVER.AmberTapSpawnCount.get();
        ItemStack holdingItem = player.getItemInHand(hand);
        ItemStack amberItemStack = new ItemStack(ModItems.AMBER.get(), configAmberSpawnCount);
        if (holdingItem.isEmpty()) {
            player.setItemInHand(hand, amberItemStack);
            LOGGER.debug(LogMarkers.INTERACTION, "{} amber put in player's hand", configAmberSpawnCount);
        } else if (!player.addItem(amberItemStack)) {
            player.drop(amberItemStack, false);
            LOGGER.debug(LogMarkers.INTERACTION, "Player inventory full. {} amber spawned into level", configAmberSpawnCount);
        }
    }
}

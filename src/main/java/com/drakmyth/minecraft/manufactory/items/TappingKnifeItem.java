package com.drakmyth.minecraft.manufactory.items;

import com.drakmyth.minecraft.manufactory.tileentities.LatexCollectorTileEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TappingKnifeItem extends Item {
    private static final Logger LOGGER = LogManager.getLogger();

    public TappingKnifeItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        LOGGER.debug("right click");
        LOGGER.info("right click");
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        LOGGER.debug("use first");
        LOGGER.info("use first");
        return super.onItemUseFirst(stack, context);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        LOGGER.debug("use finish");
        LOGGER.info("use finish");
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        LOGGER.debug("use");
        LOGGER.info("use");
        return super.onItemUse(context);
        // World world = context.getWorld();
        // if (world.isRemote) return ActionResultType.SUCCESS;
        // LOGGER.debug("onItemUse");
        // BlockPos tePos = context.getPos().offset(context.getFace());
        // TileEntity te = world.getTileEntity(tePos);
        // if (!LatexCollectorTileEntity.class.isInstance(te)) {
        //     LOGGER.debug("Latex Collector tile entity not found");
        //     return ActionResultType.PASS;
        // }
        // LOGGER.debug("Tapping...");
        // LatexCollectorTileEntity lcte = (LatexCollectorTileEntity)te;
        // lcte.onTap();
        // return ActionResultType.SUCCESS;
    }
}

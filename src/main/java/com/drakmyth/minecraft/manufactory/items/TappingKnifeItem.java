package com.drakmyth.minecraft.manufactory.items;

import com.drakmyth.minecraft.manufactory.tileentities.LatexCollectorTileEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TappingKnifeItem extends Item {
    private static final Logger LOGGER = LogManager.getLogger();

    public TappingKnifeItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        if (world.isRemote) return ActionResultType.SUCCESS;
        BlockPos tePos = context.getPos().offset(context.getFace());
        TileEntity te = world.getTileEntity(tePos);
        if (!LatexCollectorTileEntity.class.isInstance(te)) {
            LOGGER.debug("Latex Collector tile entity not found");
            return ActionResultType.PASS;
        }
        LOGGER.debug("Tapping...");
        LatexCollectorTileEntity lcte = (LatexCollectorTileEntity)te;
        lcte.onTap();
        return ActionResultType.SUCCESS;
    }
}

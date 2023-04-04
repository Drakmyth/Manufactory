package com.drakmyth.minecraft.manufactory.menus.providers;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraftforge.items.wrapper.InvWrapper;

public class BlockMenuProvider implements MenuProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private String displayName;
    private BlockPos pos;
    private IBlockMenuFactory factory;

    public BlockMenuProvider(String displayName, BlockPos pos, IBlockMenuFactory factory) {
        this.displayName = displayName;
        this.pos = pos;
        this.factory = factory;
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
        LOGGER.debug(LogMarkers.CONTAINER, "Creating {} menu...", displayName);
        return factory.create(windowId, new InvWrapper(playerInventory), player, pos);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal(displayName);
    }
}

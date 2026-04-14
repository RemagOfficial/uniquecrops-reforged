package com.remag.uniquecrops.blocks.crops;

import com.remag.uniquecrops.blocks.BaseCropsBlock;
import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.network.PacketOpenBook;
import com.remag.uniquecrops.network.UCPacketHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.context.BlockPlaceContext;

public class Eula extends BaseCropsBlock {

    private final boolean clickHarvest = true;

    public Eula() {

        super(UCItems.LEGALSTUFF, UCItems.EULA_SEED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {

        if (!ctx.getLevel().isClientSide) {
            if (ctx.getPlayer() instanceof ServerPlayer)
                UCPacketHandler.sendTo((ServerPlayer)ctx.getPlayer(), new PacketOpenBook(ctx.getPlayer().getId()));
        }
        return super.getStateForPlacement(ctx);
    }

    public boolean isClickHarvest() {

        return this.clickHarvest;
    }
}

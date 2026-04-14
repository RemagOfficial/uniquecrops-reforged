package com.remag.ucse.blocks.tiles;

import com.remag.ucse.init.UCTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileInvisibiliaGlass extends BaseTileUC {

    public TileInvisibiliaGlass(BlockPos pos, BlockState state) {

        super(UCTiles.INVISIBILIA_GLASS.get(), pos, state);
    }

}

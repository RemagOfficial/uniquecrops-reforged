package com.remag.ucse.blocks.tiles;

import com.remag.ucse.init.UCTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileInvisibilia extends BaseTileUC {

    public TileInvisibilia(BlockPos pos, BlockState state) {

        super(UCTiles.INVISIBILIA.get(), pos, state);
    }

}

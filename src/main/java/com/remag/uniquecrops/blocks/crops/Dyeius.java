package com.remag.uniquecrops.blocks.crops;

import com.remag.uniquecrops.blocks.BaseCropsBlock;
import com.remag.uniquecrops.blocks.tiles.TileDyeius;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.world.level.block.EntityBlock;
import com.remag.uniquecrops.core.DyeUtils;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.Containers;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class Dyeius extends BaseCropsBlock implements EntityBlock {

    public Dyeius() {

        super(() -> Items.BLUE_DYE, UCItems.DYEIUS_SEED);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

        return new TileDyeius(pos, state);
    }

    @Override
    public void harvestItems(Level world, BlockPos pos, BlockState state, int fortune) {

        Containers.dropItemStack(world, pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5, getDyeForTime(world));
        Containers.dropItemStack(world, pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5, new ItemStack(this.getSeed()));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {

        return RenderShape.INVISIBLE;
    }

    private ItemStack getDyeForTime(Level world) {

        long time = world.getDayTime() % 24000L;
        int meta = (int)(time / 1500);
        Item dye = DyeUtils.DYE_BY_COLOR.get(DyeColor.byId(meta)).asItem();

        return new ItemStack(dye);
    }
}

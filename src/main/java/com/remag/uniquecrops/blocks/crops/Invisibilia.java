package com.remag.uniquecrops.blocks.crops;

import com.remag.uniquecrops.blocks.BaseCropsBlock;
import com.remag.uniquecrops.blocks.tiles.TileInvisibilia;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;

public class Invisibilia extends BaseCropsBlock implements EntityBlock {

    public static final BooleanProperty VISIBLE = BooleanProperty.create("visible");

    public Invisibilia() {
        super(UCItems.INVISITWINE, UCItems.INVISIBILIA_SEED);
        registerDefaultState(this.stateDefinition.any().setValue(VISIBLE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VISIBLE);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileInvisibilia(pos, state);
    }

    @Override
    public BlockState getStateForAge(int age) {
        return this.defaultBlockState().setValue(this.getAgeProperty(), Integer.valueOf(age))
                .setValue(VISIBLE, Boolean.valueOf(false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {

        if (!(ctx instanceof EntityCollisionContext))
            return super.getShape(state, world, pos, ctx);

        Entity entity = ((EntityCollisionContext)ctx).getEntity();
        if (!(entity instanceof Player player))
            return super.getShape(state, world, pos, ctx);

        if (!player.isCreative()) {
            if (player.getInventory().armor.get(3).getItem() != UCItems.GLASSES_3D.get())
                return Shapes.empty();
        }
        return super.getShape(state, world, pos, ctx);
    }
}

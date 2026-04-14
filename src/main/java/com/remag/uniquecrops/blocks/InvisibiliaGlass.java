package com.remag.uniquecrops.blocks;

import com.remag.uniquecrops.blocks.tiles.TileInvisibiliaGlass;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
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
import org.jetbrains.annotations.Nullable;

public  class InvisibiliaGlass extends AbstractGlassBlock implements EntityBlock {

    public static final BooleanProperty VISIBLE = BooleanProperty.create("visible");

    // Needed because AbstractGlassBlock is abstract and doesn't have this built in.
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new TileInvisibiliaGlass(p_153215_, p_153216_);
    }

    public InvisibiliaGlass() {

        super(Properties.copy(Blocks.GLASS).isViewBlocking((state, reader, pos) -> false).isSuffocating((state, reader, pos) -> false));
        registerDefaultState(this.stateDefinition.any().setValue(VISIBLE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VISIBLE);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {

        if (!(ctx instanceof EntityCollisionContext))
            return super.getCollisionShape(state, world, pos, ctx);

        Entity entity = ((EntityCollisionContext)ctx).getEntity();
        if (!(entity instanceof Player player))
            return super.getCollisionShape(state, world, pos, ctx);

        if (!player.isCreative()) {
            if (player.getInventory().armor.get(3).getItem() != UCItems.GLASSES_3D.get())
                return Shapes.empty();
        }
        return super.getCollisionShape(state, world, pos, ctx);
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {

        return this.getCollisionShape(state, reader, pos, ctx);
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

package com.remag.uniquecrops.blocks.supercrops;

import com.remag.uniquecrops.blocks.BaseSuperCropsBlock;
import com.remag.uniquecrops.blocks.tiles.TileFascino;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;

public class Fascino extends BaseSuperCropsBlock implements EntityBlock {

    public Fascino() {

        super(Properties.of().noCollission().randomTicks().strength(5.0F, 1000.0F).sound(SoundType.CROP).lightLevel(l -> 1).mapColor(MapColor.GRASS));
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof TileFascino fe) {
            if (fe.getStage() != TileFascino.Stage.IDLE) return ItemInteractionResult.SUCCESS;

            if (stack.getItem() == UCItems.WILDWOOD_STAFF.get()) {
                if (fe.getStage() == TileFascino.Stage.IDLE) {
                    fe.checkEnchants(player, stack);
                }
                return ItemInteractionResult.SUCCESS;
            }
            if (!stack.isEmpty() && !player.isCrouching()) {
                player.setItemInHand(hand, ItemHandlerHelper.insertItem(fe.getInventory(), stack, false));
                fe.markBlockForUpdate();
                return ItemInteractionResult.SUCCESS;
            }
            if (stack.isEmpty() && player.isCrouching()) {
                for (int i = fe.getInventory().getSlots() - 1; i >= 0; i--) {
                    ItemStack tileStack = fe.getInventory().getStackInSlot(i);
                    if (!tileStack.isEmpty()) {
                        ItemHandlerHelper.giveItemToPlayer(player, tileStack);
                        ((ItemStackHandler)fe.getInventory()).setStackInSlot(i, ItemStack.EMPTY);
                        fe.setChanged();
                        break;
                    }
                }
                fe.markBlockForUpdate();
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {

        if (!state.is(newState.getBlock())) {
            BlockEntity tileentity = world.getBlockEntity(pos);
            if (tileentity instanceof TileFascino) {
                for (int i = 0; i < ((TileFascino)tileentity).getInventory().getSlots(); i++) {
                    ItemStack stack = ((TileFascino)tileentity).getInventory().getStackInSlot(i);
                    if (!stack.isEmpty())
                        Containers.dropItemStack(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
                }
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {

        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {

        if (!level.isClientSide()) {
            return (lvl, pos, st, te) -> {
                if (te instanceof TileFascino fascino) fascino.tickServer();
            };
        }
        return null;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

        return new TileFascino(pos, state);
    }
}

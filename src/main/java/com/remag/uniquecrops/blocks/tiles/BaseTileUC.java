package com.remag.uniquecrops.blocks.tiles;

import com.remag.uniquecrops.core.UCUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public abstract class BaseTileUC extends BlockEntity {

    public BaseTileUC(BlockEntityType<?> type, BlockPos pos, BlockState state) {

        super(type, pos, state);
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        writeCustomNBT(tag, provider);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        readCustomNBT(tag, provider);
        super.loadAdditional(tag, provider);
    }

    public void writeCustomNBT(CompoundTag tag, HolderLookup.Provider provider) {}

    public void readCustomNBT(CompoundTag tag, HolderLookup.Provider provider) {}

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {

        var tag = new CompoundTag();
        writeCustomNBT(tag, provider);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {

        return ClientboundBlockEntityDataPacket.create(this);
    }

//    @Override
//    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
//
//        super.onDataPacket(net, packet);
//        readCustomNBT(packet.getTag());
//    }

    public void markBlockForUpdate() {

        BlockState state = getLevel().getBlockState(getBlockPos());
        if (!getLevel().isClientSide())
            getLevel().sendBlockUpdated(getBlockPos(), state, state, Block.UPDATE_ALL);
    }

    public void markBlockForRenderUpdate() {

        getLevel().setBlocksDirty(worldPosition, getLevel().getBlockState(worldPosition), getLevel().getBlockState(worldPosition));
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (!level.isClientSide) {
            UCUtils.register(this);
        }
    }

    /* @Override
    public void invalidateCapabilities() {
        super.invalidateCapabilities();
        UCUtils.unregister(this);
    } */
}

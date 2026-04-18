package com.remag.uniquecrops.blocks.tiles;

import com.remag.uniquecrops.init.UCTiles;
import com.remag.uniquecrops.network.UCPacketDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class TileSunBlock extends BaseTileUC {

    public static final int MAX_POWER = 10;
    public int powerlevel;
    public boolean powered;

    public TileSunBlock(BlockPos pos, BlockState state) {

        super(UCTiles.SUNTILE.get(), pos, state);
    }

    public void tickServer() {

        if (level != null && level.getBestNeighborSignal(getBlockPos()) > 0) {
            this.powerlevel = Math.min(this.powerlevel + 1, MAX_POWER);
            this.powered = true;
        } else {
            this.powerlevel = Math.max(this.powerlevel - 1, 0);
            this.powered = false;
        }
        if (powerlevel != 0)
            UCPacketDispatcher.dispatchTEToNearbyPlayers(this);
    }

    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        // Use an infinite bounding box for rendering, as BlockEntity.INFINITE_EXTENT_AABB is no longer available.
        return new AABB(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    @Override
    public void writeCustomNBT(CompoundTag tag, HolderLookup.Provider provider) {

        tag.putInt("UC_powerlevel", powerlevel);
        tag.putBoolean("UC_powered", powered);
    }

    @Override
    public void readCustomNBT(CompoundTag tag, HolderLookup.Provider provider) {

        this.powerlevel = tag.getInt("UC_powerlevel");
        this.powered = tag.getBoolean("UC_powered");
    }
}

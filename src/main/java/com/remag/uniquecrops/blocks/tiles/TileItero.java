package com.remag.uniquecrops.blocks.tiles;

import com.remag.uniquecrops.blocks.supercrops.Itero;
import com.remag.uniquecrops.core.enums.EnumParticle;
import com.remag.uniquecrops.init.UCTiles;
import com.remag.uniquecrops.network.PacketUCEffect;
import com.remag.uniquecrops.network.UCPacketDispatcher;
import com.remag.uniquecrops.network.UCPacketHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;

public class TileItero extends BaseTileUC {

    public static final BlockPos[] PLATES =  {
            new BlockPos(-2, 0, -2), new BlockPos(2, 0, 2),
            new BlockPos(-2, 0, 2), new BlockPos(2, 0, -2)
    };

    boolean showDemo = false;
    int gameIndex = 0;
    int[] gameCombos;

    public TileItero(BlockPos pos, BlockState state) {

        super(UCTiles.ITERO.get(), pos, state);
    }

    public void tickServer() {

        if (!level.isClientSide && level.getGameTime() % 40 == 0 && showDemo) {
            if (gameCombos != null && gameCombos.length > 0) {
                if (gameIndex >= gameCombos.length) {
                    gameIndex = 0;
                    showDemo = false;
                    this.setChanged();
                    UCPacketDispatcher.dispatchTEToNearbyPlayers(this);
                    return;
                }
                BlockPos platePos = this.worldPosition.subtract(PLATES[gameCombos[gameIndex]]);
                BlockState plate = level.getBlockState(platePos);
                if (plate.getBlock() == Blocks.STONE_PRESSURE_PLATE) {
                    level.setBlock(platePos, plate.setValue(PressurePlateBlock.POWERED, true), 3);
                    level.scheduleTick(platePos, plate.getBlock(), 20);
                }
                gameIndex++;
            }
        }
    }

    public int getDemoPlatePos() {

        if (!this.showingDemo() || gameCombos == null || gameCombos.length == 0) return -1;

        return this.gameCombos[gameIndex];
    }

    public boolean createCombos(int age) {

        if (gameCombos != null)
            return false;

        int puzzleSteps = 3 + (age / 2);
        gameCombos = new int[puzzleSteps];
        for (int i = 0; i < puzzleSteps; i++) {
            gameCombos[i] = level.random.nextInt(PLATES.length);
        }
        return true;
    }

    public void matchCombo(BlockPos pos) {

        if (showDemo || gameCombos == null) return;

        BlockPos worldPos = this.worldPosition;
        BlockPos subPos = worldPos.subtract(pos);
        if (gameIndex >= gameCombos.length) {
            reset();
            return;
        }
        if (PLATES[gameCombos[gameIndex]].equals(subPos)) {
            if (++this.gameIndex >= this.gameCombos.length) {
                UCPacketHandler.sendToNearbyPlayers(level, worldPos, new PacketUCEffect(EnumParticle.END_ROD, worldPos.getX() - 0.5, worldPos.getY() + 0.6, worldPos.getZ() - 0.5, 4));
                advanceStage();
                reset();
                return;
            }
            UCPacketHandler.sendToNearbyPlayers(level, worldPos, new PacketUCEffect(EnumParticle.HEART, worldPos.getX(), worldPos.getY() + 0.3, worldPos.getZ(), 0));
        } else {
            UCPacketHandler.sendToNearbyPlayers(level, worldPos, new PacketUCEffect(EnumParticle.EXPLOSION, worldPos.getX(), worldPos.getY() + 0.3, worldPos.getZ(), 0));
            regressStage();
            reset();
        }
    }

    public void tryShowDemo() {

        if ((gameCombos != null && gameCombos.length > 0) && gameIndex == 0) {
            showDemo = true;
            UCPacketDispatcher.dispatchTEToNearbyPlayers(this);
        }
    }

    public void regressStage() {

        int age = getBlockState().getValue(Itero.AGE);
        level.setBlockAndUpdate(this.worldPosition, getBlockState().setValue(Itero.AGE, Math.max(--age, 0)));
    }

    public void advanceStage() {

        level.setBlock(this.worldPosition, getBlockState().setValue(Itero.AGE, getBlockState().getValue(Itero.AGE) + 1), 3);
    }

    public void reset() {

        gameCombos = null;
        gameIndex = 0;
    }

    public boolean showingDemo() {

        return showDemo;
    }

    @Override
    public void readCustomNBT(CompoundTag tag) {

        this.showDemo = tag.getBoolean("showDemo");
        this.gameIndex = tag.getInt("gameIndex");
        if (this.gameCombos != null)
            this.gameCombos = tag.getIntArray("gameCombos");
    }

    @Override
    public void writeCustomNBT(CompoundTag tag) {

        tag.putBoolean("showDemo", this.showDemo);
        tag.putInt("gameIndex", this.gameIndex);
        if (this.gameCombos != null)
            tag.putIntArray("gameCombos", this.gameCombos);
    }
}

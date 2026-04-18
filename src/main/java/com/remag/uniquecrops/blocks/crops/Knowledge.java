package com.remag.uniquecrops.blocks.crops;

import com.remag.uniquecrops.blocks.BaseCropsBlock;
import com.remag.uniquecrops.core.NBTUtils;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.Filterable;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class Knowledge extends BaseCropsBlock {

    private static final Pattern PAT = Pattern.compile("[aeiou]", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final String BOOKMARK = "UC:tagBookmark";

    public Knowledge() {

        super(UCItems.BOOK_DISCOUNT, UCItems.KNOWLEDGE_SEED);
        setBonemealable(false);
        setIgnoreGrowthRestrictions(true);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {

        if (this.isMaxAge(state) || world.isClientSide) return;

        if (this.canIgnoreGrowthRestrictions(world, pos)) {
            super.randomTick(state, world, pos, rand);
            return;
        }

        int growStages = consumeKnowledge(world, pos);
        if (growStages > 0)
            world.setBlock(pos, this.setValueAge(Math.min(getAge(state) + growStages, getMaxAge())), 2);
    }

    private int consumeKnowledge(Level world, BlockPos pos) {
        AtomicInteger result = new AtomicInteger();
        Iterable<BlockPos> getBox = BlockPos.betweenClosed(pos.offset(-4, -2, -4), pos.offset(4, 2, 4));

        for (BlockPos posit : getBox) {
            BlockState loopState = world.getBlockState(posit);
            if (loopState.getEnchantPowerBonus(world, posit) >= 1F) {
                BlockEntity be = world.getBlockEntity(posit.above());
                if (be instanceof ICapabilityProvider provider) {
                    IItemHandler cap = (IItemHandler) provider.getCapability(Capabilities.ItemHandler.BLOCK, Direction.DOWN);
                    if (cap != null) {
                        for (int i = 0; i < cap.getSlots(); i++) {
                            ItemStack book = cap.getStackInSlot(i);
                            if (!book.isEmpty() && book.getItem() == Items.WRITTEN_BOOK) {
                                WrittenBookContent tag;
                                tag = book.get(DataComponents.WRITTEN_BOOK_CONTENT);
                                if (!tag.pages().isEmpty()
                                        && !NBTUtils.getBoolean(book, BOOKMARK, false)) {

                                    List<Filterable<Component>> tagList = tag.pages();
                                    for (int j = 0; j < tagList.size(); j++) {
                                        String str = tagList.get(j).toString();
                                        Component text;
                                        try {
                                            text = Component.Serializer.fromJsonLenient(str, null);
                                        } catch (Exception e) {
                                            text = Component.literal(str);
                                        }

                                        String newString = eatSomeVowels(text.getString());
                                        Component newComponent = Component.literal(newString);
                                        tagList.set(j, new Filterable<>(newComponent, null));
                                        result.set(j + 1);
                                        i = cap.getSlots(); // skip remaining chest slots
                                    }

                                    // add all the modified pages with a loop
                                    for (Filterable<Component> page : tagList) {
                                        tag.pages().add(page);
                                    }
                                    NBTUtils.setBoolean(book, BOOKMARK, true);
                                }
                            }
                        }
                    }
                }
            }
        }

        return result.get();
    }

    private String eatSomeVowels(String str) {

        StringBuilder sb = new StringBuilder(str);
        if (str.length() >= 100 && str.length() <= 512) {
            sb.replace(0, str.length(), str.replaceAll(PAT.pattern(), " "));
            return sb.toString();
        }
        return str;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level worldIn, BlockPos pos, RandomSource rand) {

        if (isMaxAge(state)) {
            double x = pos.getX() + rand.nextFloat();
            double y = pos.getY() + 0.5D;
            double z = pos.getZ() + rand.nextFloat();
            worldIn.addParticle(ParticleTypes.ENCHANT, x, y, z, rand.nextGaussian(), rand.nextFloat(), rand.nextGaussian());
        }
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader world, BlockPos pos) {

        return isMaxAge(state) ? 3 : 0;
    }
}

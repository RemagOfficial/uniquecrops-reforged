package com.remag.uniquecrops.blocks.crops;

import com.remag.uniquecrops.blocks.BaseCropsBlock;
import com.remag.uniquecrops.core.UCUtils;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class Normal extends BaseCropsBlock {

    private final boolean clickHarvest = true;

    static final Item[] DROPS = new Item[] { Items.WHEAT, Items.BEETROOT, Items.CARROT, Items.POTATO };

    public Normal() {

        super(() -> Items.WHEAT, UCItems.NORMAL_SEED);
    }

    @Override
    public Item getCrop() {

        return UCUtils.selectRandom(RandomSource.create(), DROPS);
    }

    public boolean isClickHarvest() {

        return this.clickHarvest;
    }
}

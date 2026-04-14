package com.remag.uniquecrops.blocks.crops;

import com.remag.uniquecrops.blocks.BaseCropsBlock;
import com.remag.uniquecrops.init.UCItems;

public class Adventus extends BaseCropsBlock {

    public Adventus() {

        super(UCItems.GOODIE_BAG, UCItems.ADVENTUS_SEED);
        setClickHarvest(false);
        setIncludeSeed(false);
    }
}

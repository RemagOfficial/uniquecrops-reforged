package com.remag.uniquecrops.core.enums;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;

public enum EnumArmorMaterial {

    GLASSES_3D("3dglasses", ArmorMaterials.IRON),
    GLASSES_PIXELS("pixelglasses", GLASSES_3D),
    PONCHO("poncho", ArmorMaterials.LEATHER),
    SLIPPERS("slippers", ArmorMaterials.GOLD),
    CACTUS("cactus", ArmorMaterials.LEATHER),
    THUNDERPANTZ("thunderpantz", ArmorMaterials.LEATHER),
    BOOTS_LEAGUE("bootsleague", ArmorMaterials.LEATHER);

    private final String name;
    private final Holder<ArmorMaterial> material;

    EnumArmorMaterial(String name, Holder<ArmorMaterial> material) {

        this.name = name;
        this.material = material;
    }

    EnumArmorMaterial(String name, EnumArmorMaterial toCopy) {

        this.name = name;
        this.material = toCopy.material;
    }

    public String getName() {

        return name;
    }

    public Holder<ArmorMaterial> getMaterial() {

        return material;
    }
}

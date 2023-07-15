package net.TravisC.tutorialmod.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModTiers {
    public static final ForgeTier TIN = new ForgeTier(1, 164, 4.5f, 2f,
            7, BlockTags.NEEDS_STONE_TOOL, () -> Ingredient.of(ModItems.TIN_INGOT.get()));
    public static final ForgeTier COPPER = new ForgeTier(1, 92, 4.25f, 2f,
            17, BlockTags.NEEDS_STONE_TOOL, () -> Ingredient.of(Items.COPPER_INGOT));
}

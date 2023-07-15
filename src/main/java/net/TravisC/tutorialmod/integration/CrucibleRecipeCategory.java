package net.TravisC.tutorialmod.integration;

import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.TravisC.tutorialmod.TutorialMod;
import net.TravisC.tutorialmod.block.ModBlocks;
import net.TravisC.tutorialmod.recipe.CrucibleRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CrucibleRecipeCategory implements IRecipeCategory<CrucibleRecipe> {

    public final static ResourceLocation TEXTURE =
            new ResourceLocation(TutorialMod.MOD_ID, "textures/gui/crucible_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public CrucibleRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CRUCIBLE.get()));
    }
    @Override
    public RecipeType<CrucibleRecipe> getRecipeType() {
        return JEITutorialModPlugin.CRUCIBLE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Crucible");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CrucibleRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 68, 14).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 113, 28).addItemStack(recipe.getResultItem());


    }
}

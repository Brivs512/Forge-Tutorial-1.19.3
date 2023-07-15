package net.TravisC.tutorialmod.integration;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.TravisC.tutorialmod.TutorialMod;
import net.TravisC.tutorialmod.recipe.CrucibleRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;


@JeiPlugin
public class JEITutorialModPlugin implements IModPlugin {
    public static RecipeType<CrucibleRecipe> CRUCIBLE_TYPE =
            new RecipeType<>(CrucibleRecipe.UID, CrucibleRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(TutorialMod.MOD_ID, "jei_plugin");

    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new
                CrucibleRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<CrucibleRecipe> recipesInfusing = rm.getAllRecipesFor(CrucibleRecipe.Type.INSTANCE);
        registration.addRecipes(CRUCIBLE_TYPE, recipesInfusing);
    }
}

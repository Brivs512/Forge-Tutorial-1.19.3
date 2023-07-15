package net.TravisC.tutorialmod.recipe;

import net.TravisC.tutorialmod.TutorialMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipies {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TutorialMod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<CrucibleRecipe>> CRUCIBLE_SERIALIZER =
            SERIALIZERS.register("crucible", () -> CrucibleRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}

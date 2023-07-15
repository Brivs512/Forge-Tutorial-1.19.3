package net.TravisC.tutorialmod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.TravisC.tutorialmod.TutorialMod;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class CrucibleRecipe implements Recipe<SimpleContainer> {
    public final static ResourceLocation UID = new ResourceLocation(TutorialMod.MOD_ID, "crucible");
    private final ResourceLocation id;
    private final ItemStack outPut;
    private final NonNullList<Ingredient> recipeItems;

    public CrucibleRecipe(ResourceLocation id, ItemStack outPut, NonNullList<Ingredient> recipeItems) {
        this.id = id;
        this.outPut = outPut;
        this.recipeItems = recipeItems;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if (pLevel.isClientSide()){
            return false;
        }
        return recipeItems.get(0).test(pContainer.getItem(1));
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        return outPut;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return outPut.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
    public static class Type implements RecipeType<CrucibleRecipe> {
        private Type() { }
        public static final  Type INSTANCE = new Type();
        public static final  String ID = "crucible";
    }

    public static class Serializer implements RecipeSerializer<CrucibleRecipe> {
        public static final  Serializer INSTANCE = new Serializer();
        public static final  ResourceLocation ID =
                new ResourceLocation(TutorialMod.MOD_ID, "crucible");

        @Override
        public CrucibleRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }
            return new CrucibleRecipe(pRecipeId, output, inputs);
        }

        @Override
        public @Nullable CrucibleRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            return new CrucibleRecipe(id, output, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CrucibleRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItemStack(recipe.getResultItem(), false);

        }
    }


}

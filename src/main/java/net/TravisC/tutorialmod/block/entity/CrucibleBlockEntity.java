package net.TravisC.tutorialmod.block.entity;

import net.TravisC.tutorialmod.item.ModItems;
import net.TravisC.tutorialmod.recipe.CrucibleRecipe;
import net.TravisC.tutorialmod.screen.CrucibleMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CrucibleBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler ItemHandler = new ItemStackHandler( 4) {
        @Override
        protected  void  onContentsChanged(int slot) {
            setChanged();
        }
    };
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;

    private int progress = 0;
    private int maxProgress = 130;

    public CrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRUCIBLE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> CrucibleBlockEntity.this.progress;
                    case 1 -> CrucibleBlockEntity.this.maxProgress;
                    default -> 0;

                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> CrucibleBlockEntity.this.progress = value;
                    case 1 -> CrucibleBlockEntity.this.maxProgress = value;
                }


            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Crucible");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new CrucibleMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }
    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> ItemHandler);
    }
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", ItemHandler.serializeNBT());
        nbt.putInt("crucicle.progress", this.progress);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        ItemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("crucible.progress");
    }
    public void drops() {
        SimpleContainer inventory = new SimpleContainer(ItemHandler.getSlots());
        for (int i = 0; i < ItemHandler.getSlots(); i++) {
            inventory.setItem(i, ItemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CrucibleBlockEntity pEntity) {
        if(level.isClientSide()) {
            return;
        }

        if(hasRecipe(pEntity)) {
            pEntity.progress++;
            setChanged(level, pos, state);

            if(pEntity.progress >= pEntity.maxProgress) {
                craftItem(pEntity);
            }
        } else {
            pEntity.resetProgress();
            setChanged(level, pos, state);
        }
    }

    private static void craftItem(CrucibleBlockEntity pEntity) {
        Level level = pEntity.level;
        SimpleContainer inventory = new SimpleContainer(pEntity.ItemHandler.getSlots());
        for (int i = 0; i < pEntity.ItemHandler.getSlots(); i++) {
            inventory.setItem(i, pEntity.ItemHandler.getStackInSlot(i));
        }

        Optional<CrucibleRecipe> recipe = level.getRecipeManager().getRecipeFor
                (CrucibleRecipe.Type.INSTANCE, inventory, level);

        if(hasRecipe(pEntity)) {
            pEntity.ItemHandler.extractItem(0, 1, false);
            pEntity.ItemHandler.extractItem(1, 1, false);
            pEntity.ItemHandler.setStackInSlot(2, new ItemStack(recipe.get().getResultItem().getItem(),
                    pEntity.ItemHandler.getStackInSlot(2).getCount() + 1));

            pEntity.resetProgress();
        }
    }

    private void resetProgress() {
        this.progress = 0;
        
    }

    private static boolean hasRecipe(CrucibleBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.ItemHandler.getSlots());
        for (int i = 0; i < entity.ItemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.ItemHandler.getStackInSlot(i));
        }

        Optional<CrucibleRecipe> recipe = level.getRecipeManager().getRecipeFor
                (CrucibleRecipe.Type.INSTANCE, inventory, level);

        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) &&
                canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem());
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
        return inventory.getItem(2).getItem() == stack.getItem() || inventory.getItem(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(2).getMaxStackSize() > inventory.getItem(2).getCount();
    }
}
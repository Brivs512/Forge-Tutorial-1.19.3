package net.TravisC.tutorialmod.block.entity;

import net.TravisC.tutorialmod.TutorialMod;
import net.TravisC.tutorialmod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TutorialMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<CrucibleBlockEntity>> CRUCIBLE =
            BLOCK_ENTITIES.register("crucible", () ->
                    BlockEntityType.Builder.of(CrucibleBlockEntity:: new,
                            ModBlocks.CRUCIBLE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}

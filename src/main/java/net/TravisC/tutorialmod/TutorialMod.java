package net.TravisC.tutorialmod;

import com.mojang.logging.LogUtils;
import net.TravisC.tutorialmod.block.ModBlocks;
import net.TravisC.tutorialmod.item.ModCreativeModeTabs;
import net.TravisC.tutorialmod.item.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TutorialMod.MOD_ID)
public class TutorialMod {
    public static final String MOD_ID = "tutorialmod";
    private static final Logger LOGGER = LogUtils.getLogger();

    // Very Important Comment
    public TutorialMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void addCreative(CreativeModeTabEvent.BuildContents event) {
        if(event.getTab() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.BLACK_OPAL);
            event.accept(ModItems.RAW_BLACK_OPAL);
            event.accept(ModItems.TIN_INGOT);
        }

        if(event.getTab() == CreativeModeTabs. BUILDING_BLOCKS) {
            event.accept(ModBlocks.BLACK_OPAL_BLOCK);
        }
        if(event.getTab() == ModCreativeModeTabs.TUTORIAL_TAB) {

            //items here
            event.accept(ModItems.BLACK_OPAL);
            event.accept(ModItems.RAW_BLACK_OPAL);
            event.accept(ModItems.RAW_TIN);
            event.accept(ModItems.TIN_INGOT);

            //blocks here
            event.accept(ModBlocks.BLACK_OPAL_BLOCK);
            event.accept(ModBlocks.DEEPSLATE_BLACK_OPAL_ORE);
            event.accept(ModBlocks.BLACK_OPAL_ORE);
            event.accept(ModBlocks.TIN_ORE);



        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}

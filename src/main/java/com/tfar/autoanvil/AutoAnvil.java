package com.tfar.autoanvil;

import com.tfar.autoanvil.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AutoAnvil.MODID)
public class AutoAnvil {
  // Directly reference a log4j logger.

  public static final String MODID = "autoanvil";

  private static final Logger LOGGER = LogManager.getLogger();

  public static final Tag<Fluid> xptag = new FluidTags.Wrapper(new ResourceLocation("forge","xp"));

  public static final ResourceLocation FLUID_STILL = new ResourceLocation("minecraft:block/water_still");
  public static final ResourceLocation FLUID_FLOWING = new ResourceLocation("minecraft:block/water_flow");

  public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MODID);
  public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MODID);
  public static final DeferredRegister<Fluid> FLUIDS = new DeferredRegister<>(ForgeRegistries.FLUIDS, MODID);


  public static RegistryObject<FlowingFluid> xp = FLUIDS.register("xp", () ->
          new ForgeFlowingFluid.Source(AutoAnvil.xpfluid_properties)
  );
  public static RegistryObject<FlowingFluid> flowing_xp = FLUIDS.register("flowing_xp", () ->
          new ForgeFlowingFluid.Flowing(AutoAnvil.xpfluid_properties)
  );

  public static RegistryObject<FlowingFluidBlock> xpfluid_block = BLOCKS.register("xpfluid_block", () ->
          new FlowingFluidBlock(xp, Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops())
  );
  public static RegistryObject<Item> xp_bucket = ITEMS.register("xp_bucket", () ->
          new BucketItem(xp, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(ItemGroup.MISC))
  );

  public static final ForgeFlowingFluid.Properties xpfluid_properties =
          new ForgeFlowingFluid.Properties(xp, flowing_xp, FluidAttributes.builder(FLUID_STILL, FLUID_FLOWING).color(0xff00ff00))
                  .bucket(xp_bucket).block(xpfluid_block);

  public AutoAnvil() {
    // Register the setup method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    // Register the doClientStuff method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    BLOCKS.register(modEventBus);
    ITEMS.register(modEventBus);
    FLUIDS.register(modEventBus);
  }

  private void setup(final FMLCommonSetupEvent event) {
    PacketHandler.registerMessages(MODID);
  }

  private void doClientStuff(final FMLClientSetupEvent event) {
    ScreenManager.registerFactory(Objects.Containers.autoanvil,AutoAnvilScreen::new);
  }

  // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
  // Event bus for receiving Registry Events)
  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {
    @SubscribeEvent
    public static void block(final RegistryEvent.Register<Block> e) {
      e.getRegistry().register(new AutoAnvilBlock(Block.Properties.from(Blocks.ANVIL)).setRegistryName("autoanvil"));
    }

    @SubscribeEvent
    public static void item(final RegistryEvent.Register<Item> e) {
      e.getRegistry().register(new BlockItem(Objects.Blocks.autoanvil,new Item.Properties().group(ItemGroup.TOOLS)).setRegistryName("autoanvil"));
      e.getRegistry().register(new Item(new Item.Properties().group(ItemGroup.TOOLS)).setRegistryName("level_upgrade"));
      e.getRegistry().register(new Item(new Item.Properties().group(ItemGroup.TOOLS)).setRegistryName("efficiency_upgrade"));
    }

    @SubscribeEvent
    public static void container(final RegistryEvent.Register<ContainerType<?>> e) {
      e.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> new AutoAnvilContainer(windowId, inv.player.world, data.readBlockPos(), inv, inv.player)).setRegistryName("autoanvil"));
    }

    @SubscribeEvent
    public static void tile(final RegistryEvent.Register<TileEntityType<?>> e) {
      e.getRegistry().register(TileEntityType.Builder.create(AutoAnvilBlockEntity::new, Objects.Blocks.autoanvil).build(null).setRegistryName("autoanvil"));
    }
  }

  public static class Objects {

    @ObjectHolder(MODID)
    public static class Blocks {
      public static final Block autoanvil = null;
    }

    @ObjectHolder(MODID)
    public static class Items {
      public static final Item level_upgrade = null;
      public static final Item efficiency_upgrade = null;
    }

    @ObjectHolder(MODID)
    public static class Containers {
      public static final ContainerType<AutoAnvilContainer> autoanvil = null;
    }

    @ObjectHolder(MODID)
    public static class Tiles {
      public static final TileEntityType<?> autoanvil = null;
    }
  }
}

package tfar.autoanvil;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;
import tfar.autoanvil.client.AutoAnvilClient;
import tfar.autoanvil.client.AutoAnvilScreen;
import tfar.autoanvil.compat.AutoAnvilFluidHandler;
import tfar.autoanvil.compat.AutoAnvilItemHandler;
import tfar.autoanvil.datagen.ADatagen;
import tfar.autoanvil.fluids.FluidProperties;
import net.minecraft.client.gui.screens.MenuScreens;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import tfar.autoanvil.inventory.CFluidStack;
import tfar.autoanvil.mixin.FluidStackAccessor;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AutoAnvil.MOD_ID)
public class AutoAnvilNeoForge {

  public AutoAnvilNeoForge(IEventBus eventBus) {
    // Register the setup method for modloading
    eventBus.addListener(this::setup);
    // Register the doClientStuff method for modloading
    eventBus.addListener(this::doClientStuff);
    eventBus.addListener(this::register);
    eventBus.addListener(this::capabilities);
    eventBus.addListener(ADatagen::gather);
  }

    public static BaseFlowingFluid.Properties convertFluidProperties(FluidProperties properties) {
    BaseFlowingFluid.Properties props = new BaseFlowingFluid.Properties(() -> NeoforgeFluidTypes.lookup(properties.getFluidType().get()),
            properties.getStill(),properties.getFlowing()).bucket(properties.getBucket())
            .block(properties.getBlock()).slopeFindDistance(properties.getSlopeFindDistance())
            .levelDecreasePerBlock(properties.getLevelDecreasePerBlock()).explosionResistance(properties.getExplosionResistance())
            .tickRate(properties.getTickRate());
        return props;
    }

    void capabilities(RegisterCapabilitiesEvent event) {
    event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AutoAnvil.BlockEntityTypes.AUTO_ANVIL, AutoAnvilFluidHandler::new);
    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AutoAnvil.BlockEntityTypes.AUTO_ANVIL, AutoAnvilItemHandler::new);
  }

  void register(RegisterEvent event) {
    AutoAnvil.register();

    event.register(NeoForgeRegistries.Keys.FLUID_TYPES, helper -> helper.register(AutoAnvil.id("xp"), NeoforgeFluidTypes.XP));
  }

  private void setup(final FMLCommonSetupEvent event) {

  }

  private void doClientStuff(final FMLClientSetupEvent event) {
    AutoAnvilClient.setup();
  }
}

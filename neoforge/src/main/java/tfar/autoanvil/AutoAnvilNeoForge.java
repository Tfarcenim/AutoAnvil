package tfar.autoanvil;

import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;
import tfar.autoanvil.fluids.CFluidType;
import tfar.autoanvil.fluids.FluidProperties;
import tfar.autoanvil.network.PacketHandler;
import net.minecraft.client.gui.screens.MenuScreens;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static tfar.autoanvil.AutoAnvil.FLUID_FLOWING;
import static tfar.autoanvil.AutoAnvil.FLUID_STILL;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AutoAnvil.MOD_ID)
public class AutoAnvilNeoForge {

  public AutoAnvilNeoForge(IEventBus eventBus) {
    // Register the setup method for modloading
    eventBus.addListener(this::setup);
    // Register the doClientStuff method for modloading
    eventBus.addListener(this::doClientStuff);
    eventBus.addListener(this::register);
  }

    public static BaseFlowingFluid.Properties convertFluidProperties(FluidProperties properties) {
    BaseFlowingFluid.Properties props = new BaseFlowingFluid.Properties(() -> NeoforgeFluidTypes.lookup(properties.getFluidType().get()),
            properties.getStill(),properties.getFlowing()).bucket(properties.getBucket())
            .block(properties.getBlock()).slopeFindDistance(properties.getSlopeFindDistance())
            .levelDecreasePerBlock(properties.getLevelDecreasePerBlock()).explosionResistance(properties.getExplosionResistance())
            .tickRate(properties.getTickRate());
        return props;
    }


  void register(RegisterEvent event) {
    AutoAnvil.register();

    event.register(NeoForgeRegistries.Keys.FLUID_TYPES, helper -> helper.register(AutoAnvil.id("xp"), NeoforgeFluidTypes.XP));
  }

  private void setup(final FMLCommonSetupEvent event) {

  }

  private void doClientStuff(final FMLClientSetupEvent event) {
    MenuScreens.register(AutoAnvil.MenuTypes.AUTO_ANVIL,AutoAnvilScreen::new);
  }
}

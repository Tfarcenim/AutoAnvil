package tfar.autoanvil.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import tfar.autoanvil.AutoAnvil;
import tfar.autoanvil.fluids.CFluidType;
import tfar.autoanvil.fluids.ClientFluidType;

import java.util.HashMap;
import java.util.Map;

public class AutoAnvilClient {

    public static final Map<CFluidType, ClientFluidType> FLUID_TYPES = new HashMap<>();

    public static void handleFluidTypes() {
        FLUID_TYPES.put(AutoAnvil.FluidTypes.XP,
                new ClientFluidType(ResourceLocation.withDefaultNamespace("block/water_still"),
                        ResourceLocation.withDefaultNamespace("block/water_flow"),
                        ResourceLocation.withDefaultNamespace("block/water_overlay"),
                        ResourceLocation.withDefaultNamespace("textures/misc/underwater.png"),0xff00ff00));
    }

    public static void setup() {
        MenuScreens.register(AutoAnvil.MenuTypes.AUTO_ANVIL, AutoAnvilScreen::new);
        handleFluidTypes();
    }

}

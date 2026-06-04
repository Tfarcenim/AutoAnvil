package tfar.autoanvil;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import tfar.autoanvil.client.AutoAnvilClient;
import tfar.autoanvil.fluids.CFluidType;
import tfar.autoanvil.fluids.ClientFluidType;

import java.util.Map;

@Mod(value = AutoAnvil.MOD_ID,dist = Dist.CLIENT)
public class AutoAnvilClientNeoforge {


    public AutoAnvilClientNeoforge(IEventBus modBus) {
        modBus.addListener(this::registerClientExtensions);
    }

    void registerClientExtensions(RegisterClientExtensionsEvent event) {
        AutoAnvilClient.handleFluidTypes();
        for (Map.Entry<CFluidType, ClientFluidType> entry : AutoAnvilClient.FLUID_TYPES.entrySet()) {
            event.registerFluidType(new NeoforgeClientFluidType(entry.getValue()),NeoforgeFluidTypes.lookup(entry.getKey()));
        }
    }
}

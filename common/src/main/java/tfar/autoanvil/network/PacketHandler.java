package tfar.autoanvil.network;

import net.minecraft.resources.ResourceLocation;
import tfar.autoanvil.AutoAnvil;
import tfar.autoanvil.platform.Services;


import java.util.Locale;

public class PacketHandler {

    public static void registerPackets() {
        ///////server to client

    }

    public static ResourceLocation packet(Class<?> clazz) {
        return AutoAnvil.id(clazz.getName().toLowerCase(Locale.ROOT));
    }

}

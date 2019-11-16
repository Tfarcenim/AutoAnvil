package com.tfar.autoanvil.network;

import com.tfar.autoanvil.AutoAnvil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
  public static SimpleChannel INSTANCE;

  public static void registerMessages(String channelName) {
    int id = 0;

    INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(AutoAnvil.MODID, channelName), () -> "1.0", s -> true, s -> true);

    INSTANCE.registerMessage(id++, C2SMessageToggleSideConfig.class,
            C2SMessageToggleSideConfig::encode,
            C2SMessageToggleSideConfig::new,
            C2SMessageToggleSideConfig::handle);

  }
}

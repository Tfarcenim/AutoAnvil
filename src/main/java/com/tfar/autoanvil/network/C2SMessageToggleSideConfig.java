package com.tfar.autoanvil.network;

import com.tfar.autoanvil.AutoAnvilContainer;
import com.tfar.autoanvil.Util;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class C2SMessageToggleSideConfig {

  private int side;

  public C2SMessageToggleSideConfig(){}

  public C2SMessageToggleSideConfig(int side){this.side = side;}

  public C2SMessageToggleSideConfig(PacketBuffer buffer) {
    this.side = buffer.readInt();
  }


  public void handle(Supplier<NetworkEvent.Context> ctx) {
      PlayerEntity player = ctx.get().getSender();

      if (player == null) return;

      ctx.get().enqueueWork(  ()->  {
        Container container = player.openContainer;
        if (container instanceof AutoAnvilContainer){
          Util.cycleMode((AutoAnvilContainer) container,this.side);
        }
      });
      ctx.get().setPacketHandled(true);
    }

  public void encode(PacketBuffer buffer) {
    buffer.writeInt(this.side);
  }
}


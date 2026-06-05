package tfar.autoanvil.platform;

import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import tfar.autoanvil.AutoAnvilNeoForge;
import tfar.autoanvil.fluids.FluidProperties;
import tfar.autoanvil.network.C2SModPacket;
import tfar.autoanvil.network.S2CModPacket;
import tfar.autoanvil.platform.services.IPlatformHelper;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }


    public static PayloadRegistrar registrar;

    @Override
    public <MSG extends S2CModPacket> void registerClientPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf, MSG> streamCodec) {
        registrar.playToClient(type, streamCodec, (p, t) -> p.handleClient());
    }

    @Override
    public <MSG extends C2SModPacket> void registerServerPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf, MSG> streamCodec) {
        registrar.playToServer(type, streamCodec, (p, t) -> p.handleServer((ServerPlayer) t.player()));
    }

    @Override
    public void sendToClient(S2CModPacket msg, ServerPlayer player) {
        PacketHandlerNeoForge.sendToClient(msg, player);
    }

    @Override
    public void sendToServer(C2SModPacket msg) {
        PacketHandlerNeoForge.sendToServer(msg);
    }

    @Override
    public FlowingFluid createXPSourceFluid(FluidProperties properties) {
        return new BaseFlowingFluid.Source(AutoAnvilNeoForge.convertFluidProperties(properties));
    }

    @Override
    public FlowingFluid createXPFlowingFluid(FluidProperties properties) {
        return new BaseFlowingFluid.Flowing(AutoAnvilNeoForge.convertFluidProperties(properties));
    }

    @Override
    public boolean onAnvilChange(AnvilMenu container, ItemStack left, ItemStack right, Container outputSlot, String name, long baseCost, Player player) {
        return false;
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return stack.supportsEnchantment(enchantment);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return stack.isBookEnchantable(book);
    }
}
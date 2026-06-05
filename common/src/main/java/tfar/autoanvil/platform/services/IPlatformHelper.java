package tfar.autoanvil.platform.services;

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
import tfar.autoanvil.fluids.FluidProperties;
import tfar.autoanvil.network.C2SModPacket;
import tfar.autoanvil.network.S2CModPacket;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }


    <MSG extends S2CModPacket> void registerClientPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf, MSG> streamCodec);

    <MSG extends C2SModPacket> void registerServerPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf, MSG> streamCodec);

    void sendToClient(S2CModPacket msg, ServerPlayer player);

    void sendToServer(C2SModPacket msg);

    FlowingFluid createXPSourceFluid(FluidProperties properties);

    FlowingFluid createXPFlowingFluid(FluidProperties properties);

    boolean onAnvilChange(AnvilMenu container, ItemStack left, ItemStack right, Container outputSlot, String name, long baseCost, Player player);

    boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment);
    boolean isBookEnchantable(ItemStack stack, ItemStack book);
}
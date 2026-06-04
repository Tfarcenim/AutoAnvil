package tfar.autoanvil;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.Nullable;
import tfar.autoanvil.fluids.ClientFluidType;

public record NeoforgeClientFluidType(ClientFluidType clientFluidType) implements IClientFluidTypeExtensions {
    @Override
    public ResourceLocation getStillTexture() {
        return clientFluidType.stillTexture();
    }

    @Override
    public ResourceLocation getFlowingTexture() {
        return clientFluidType.flowingTexture();
    }

    @Override
    public @Nullable ResourceLocation getOverlayTexture() {
        return clientFluidType.overlayTexture();
    }

    @Override
    public @Nullable ResourceLocation getRenderOverlayTexture(Minecraft mc) {
        return clientFluidType.renderOverlayTexture(mc);
    }

    @Override
    public int getTintColor() {
        return clientFluidType.tintColor();
    }

    @Override
    public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return clientFluidType.getTintColor(state, getter, pos);
    }
}

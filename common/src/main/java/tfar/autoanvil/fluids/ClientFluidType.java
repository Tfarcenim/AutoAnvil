package tfar.autoanvil.fluids;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;

public class ClientFluidType {
    private final ResourceLocation stillTexture;
    private final ResourceLocation flowingTexture;
    private final ResourceLocation overlayTexture;
    private final ResourceLocation underwaterTexture;
    private final int tintColor;

    public ClientFluidType(ResourceLocation stillTexture, ResourceLocation flowingTexture,ResourceLocation overlayTexture,
                           ResourceLocation underwaterTexture,int tintColor) {
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.overlayTexture = overlayTexture;
        this.underwaterTexture = underwaterTexture;
        this.tintColor = tintColor;
    }

    public ResourceLocation stillTexture() {
        return stillTexture;
    }

    public ResourceLocation flowingTexture() {
        return flowingTexture;
    }

    public ResourceLocation overlayTexture() {
        return overlayTexture;
    }

    public ResourceLocation renderOverlayTexture(Minecraft minecraft) {
        return underwaterTexture;
    }

    public int tintColor() {
        return tintColor;
    }

    public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return tintColor;
    }
}

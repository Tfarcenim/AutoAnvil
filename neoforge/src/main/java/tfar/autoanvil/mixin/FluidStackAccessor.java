package tfar.autoanvil.mixin;

import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FluidStack.class)
public interface FluidStackAccessor {
    @Invoker("<init>")
    static FluidStack create(Fluid fluid, int amount, PatchedDataComponentMap components) {
        throw new IllegalStateException();
    }
}

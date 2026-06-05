package tfar.autoanvil.compat;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;
import tfar.autoanvil.AutoAnvilBlockEntity;
import tfar.autoanvil.inventory.CFluidStack;
import tfar.autoanvil.mixin.FluidStackAccessor;

public record AutoAnvilFluidHandler(AutoAnvilBlockEntity object, @Nullable Direction context) implements IFluidHandler {

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return getForgeFluidStack(object.fluidInventory.getStack());
    }

    protected FluidStack getForgeFluidStack(CFluidStack fluidStack) {
        return FluidStackAccessor.create(fluidStack.getFluid(), fluidStack.getAmount(), fluidStack.getComponents());
    }

    protected CFluidStack getCommonFluidStack(FluidStack stack) {
        return new CFluidStack(stack.getFluid(), stack.getAmount(), stack.getComponents());
    }

    @Override
    public int getTankCapacity(int tank) {
        return object.fluidInventory.getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return object.fluidInventory.isValid(getCommonFluidStack(stack));
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return object.fluidInventory.fill(getCommonFluidStack(resource), action.simulate());
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return getForgeFluidStack(object.fluidInventory.drain(getCommonFluidStack(resource), action.simulate()));
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return getForgeFluidStack(object.fluidInventory.drain(maxDrain, action.simulate()));
    }
}

package tfar.autoanvil.inventory;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import tfar.autoanvil.AutoAnvil;
import tfar.autoanvil.AutoAnvilBlockEntity;
import tfar.autoanvil.util.Util;

public class AutoAnvilFluidInventory {

    private final AutoAnvilBlockEntity autoAnvilBlockEntity;
    private CFluidStack stack = CFluidStack.EMPTY;

    private int capacity = Util.leveltoXPCost(40);

    public AutoAnvilFluidInventory(AutoAnvilBlockEntity autoAnvilBlockEntity) {
        this.autoAnvilBlockEntity = autoAnvilBlockEntity;
    }

    public CFluidStack getStack() {
        return stack;
    }

    public void setStack(CFluidStack stack) {
        this.stack = stack;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.put("fluid",stack.saveOptional(provider));
        nbt.putInt("capacity",capacity);
        return nbt;
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        stack = CFluidStack.parseOptional(provider, nbt.getCompound("fluid"));
        capacity = nbt.getInt("capacity");
        //onLoad();
    }

    public int getAmount() {
        return stack.getAmount();
    }

    public boolean isValid(CFluidStack stack) {
        return stack.is(AutoAnvil.XP);
    }


    public int fill(CFluidStack resource,boolean simulate) {
        if (resource.isEmpty() || !isValid(resource)) {
            return 0;
        }
        if (simulate) {
            if (stack.isEmpty()) {
                return Math.min(capacity, resource.getAmount());
            }
            if (!CFluidStack.isSameFluidSameComponents(stack, resource)) {
                return 0;
            }
            return Math.min(capacity - stack.getAmount(), resource.getAmount());
        }
        if (stack.isEmpty()) {
            stack = resource.copyWithAmount(Math.min(capacity, resource.getAmount()));
            onContentsChanged();
            return stack.getAmount();
        }
        if (!CFluidStack.isSameFluidSameComponents(stack, resource)) {
            return 0;
        }
        int filled = capacity - stack.getAmount();

        if (resource.getAmount() < filled) {
            stack.grow(resource.getAmount());
            filled = resource.getAmount();
        } else {
            stack.setAmount(capacity);
        }
        if (filled > 0)
            onContentsChanged();
        return filled;
    }

    public CFluidStack drain(CFluidStack resource, boolean simulate) {
        if (resource.isEmpty() || !CFluidStack.isSameFluidSameComponents(resource, stack)) {
            return CFluidStack.EMPTY;
        }
        return drain(resource.getAmount(), simulate);
    }

    public CFluidStack drain(int maxDrain, boolean simulate) {
        int drained = maxDrain;
        if (stack.getAmount() < drained) {
            drained = stack.getAmount();
        }
        CFluidStack cFluidStack = stack.copyWithAmount(drained);
        if (!simulate && drained > 0) {
            stack.shrink(drained);
            onContentsChanged();
        }
        return cFluidStack;
    }

    public void onContentsChanged() {
        if (autoAnvilBlockEntity != null) {
            autoAnvilBlockEntity.setChanged();
        }
    }
}

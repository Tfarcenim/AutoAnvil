package tfar.autoanvil.compat;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;
import tfar.autoanvil.AutoAnvilBlockEntity;

public record AutoAnvilItemHandler(AutoAnvilBlockEntity object, @Nullable Direction context) implements IItemHandler {

    @Override
    public int getSlots() {
        return object.anvilInventory.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return object.anvilInventory.get(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return object.anvilInventory.insertWithContext(slot, stack, simulate,context);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return object.anvilInventory.extractWithContext(slot, amount, simulate,context);
    }

    @Override
    public int getSlotLimit(int slot) {
        return object.anvilInventory.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return object.anvilInventory.isValid(slot, stack);
    }
}

package tfar.autoanvil.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AutoAnvilSlot extends Slot {

    private static final Container EMPTY = new SimpleContainer(0);
    private final AutoAnvilInventory inventory;

    public AutoAnvilSlot(AutoAnvilInventory inventory, int slot, int x, int y) {
        super(EMPTY, slot, x, y);
        this.inventory = inventory;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack.isEmpty())
            return false;
        return inventory.isValid(index, stack);
    }

    @Override
    public ItemStack getItem() {
        return this.getItemHandler().get(index);
    }

    // Override if your IItemHandler does not implement IItemHandlerModifiable
    @Override
    public void set(ItemStack stack) {
        this.getItemHandler().set(index, stack);
        this.setChanged();
    }

    @Override
    public void onQuickCraft(ItemStack oldStackIn, ItemStack newStackIn) {}

    @Override
    public int getMaxStackSize() {
        return getItemHandler().getSlotLimit(this.index);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return Math.min(stack.getMaxStackSize(), getItemHandler().getSlotLimit(this.index));
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return !this.getItemHandler().extract(index, 1, true).isEmpty();
    }

    @Override
    public ItemStack remove(int amount) {
        return this.getItemHandler().extract(index, amount, false);
    }

    public AutoAnvilInventory getItemHandler() {
        return inventory;
    }

}

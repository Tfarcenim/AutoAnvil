package tfar.autoanvil.compat;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleStackStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import tfar.autoanvil.inventory.AutoAnvilInventory;

public class AutoAnvilSingleSlotStorage extends SingleStackStorage {

    private final AutoAnvilInventory autoAnvilInventory;
    private final int slot;
    private final Direction direction;

    public AutoAnvilSingleSlotStorage(AutoAnvilInventory autoAnvilInventory, int slot, Direction direction) {
        this.autoAnvilInventory = autoAnvilInventory;
        this.slot = slot;
        this.direction = direction;
    }

    @Override
    protected boolean canInsert(ItemVariant itemVariant) {
        return autoAnvilInventory.canPlace(slot,itemVariant.toStack(),direction);
    }

    @Override
    protected boolean canExtract(ItemVariant itemVariant) {
        return autoAnvilInventory.canTake(slot,direction);
    }

    @Override
    protected ItemStack getStack() {
        return autoAnvilInventory.get(slot);
    }

    @Override
    protected void setStack(ItemStack stack) {
        autoAnvilInventory.set(slot, stack);
    }

    @Override
    protected int getCapacity(ItemVariant variant) {
        return autoAnvilInventory.getStackLimit(slot,variant.toStack());
    }
}

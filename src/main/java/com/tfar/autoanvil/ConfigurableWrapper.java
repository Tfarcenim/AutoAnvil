package com.tfar.autoanvil;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class ConfigurableWrapper implements IItemHandler {

  private AutoAnvilBlockEntity blockEntity;
  public final Direction side;
  public ConfigurableWrapper(Direction side, AutoAnvilBlockEntity autoAnvilBlockEntity) {
    this.blockEntity = autoAnvilBlockEntity;
    this.side = side;
  }

  @Nonnull
  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    return this.blockEntity.getSideConfig(side).allowsOutput() ?
            blockEntity.inv.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
  }

  /**
   * Retrieves the maximum stack size allowed to exist in the given slot.
   *
   * @param slot Slot to query.
   * @return The maximum stack size allowed in the slot.
   */
  @Override
  public int getSlotLimit(int slot) {
    return 64;
  }

  /**
   * <p>
   * This function re-implements the vanilla function {@link IInventory#isItemValidForSlot(int, ItemStack)}.
   * It should be used instead of simulated insertions in cases where the contents and state of the inventory are
   * irrelevant, mainly for the purpose of automation and logic (for instance, testing if a minecart can wait
   * to deposit its items into a full inventory, or if the items in the minecart can never be placed into the
   * inventory and should move on).
   * </p>
   * <ul>
   * <li>isItemValid is false when insertion of the item is never valid.</li>
   * <li>When isItemValid is true, no assumptions can be made and insertion must be simulated case-by-case.</li>
   * <li>The actual items in the inventory, its fullness, or any other state are <strong>not</strong> considered by isItemValid.</li>
   * </ul>
   *
   * @param slot  Slot to query for validity
   * @param stack Stack to test with for validity
   * @return true if the slot can insert the ItemStack, not considering the current state of the inventory.
   * false if the slot can never insert the ItemStack in any situation.
   */
  @Override
  public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
    return true;
  }

  /**
   * Returns the number of slots available
   *
   * @return The number of slots available
   **/
  @Override
  public int getSlots() {
    return 3;
  }

  /**
   * Returns the ItemStack in a given slot.
   * <p>
   * The result's stack size may be greater than the itemstack's max size.
   * <p>
   * If the result is empty, then the slot is empty.
   *
   * <p>
   * <strong>IMPORTANT:</strong> This ItemStack <em>MUST NOT</em> be modified. This method is not for
   * altering an inventory's contents. Any implementers who are able to detect
   * modification through this method should throw an exception.
   * </p>
   * <p>
   * <strong><em>SERIOUSLY: DO NOT MODIFY THE RETURNED ITEMSTACK</em></strong>
   * </p>
   *
   * @param slot Slot to query
   * @return ItemStack in given slot. Empty Itemstack if the slot is empty.
   **/
  @Nonnull
  @Override
  public ItemStack getStackInSlot(int slot) {
    return blockEntity.inv.getStackInSlot(slot);
  }

  @Nonnull
  @Override
  public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
    return this.blockEntity.getSideConfig(side).allowsInput(slot) ?
            blockEntity.inv.insertItem(slot, stack, simulate) : stack;
  }
}

package com.tfar.autoanvil.inventory;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class UpgradeSlot extends SlotItemHandler {

  public final Item item;

  public final int limit;
  public UpgradeSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Item item) {
    this(itemHandler, index, xPosition, yPosition,item,64);
  }

  public UpgradeSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Item item, int limit) {
    super(itemHandler, index, xPosition, yPosition);
    this.item = item;
    this.limit = limit;
  }

  @Override
  public boolean isItemValid(@Nonnull ItemStack stack) {
    return stack.getItem() == item;
  }

  @Override
  public int getSlotStackLimit() {
    return limit;
  }

  @Override
  public int getItemStackLimit(@Nonnull ItemStack stack) {
    return getSlotStackLimit();
  }
}

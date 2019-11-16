package com.tfar.autoanvil;

import com.tfar.autoanvil.inventory.OutputSlot;
import com.tfar.autoanvil.inventory.UpgradeSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class AutoAnvilContainer extends Container {

  protected final PlayerEntity player;
  protected final AutoAnvilBlockEntity blockEntity;
  public AutoAnvilContainer(int windowId, World world, BlockPos readBlockPos, PlayerInventory inv, PlayerEntity player) {
    super(AutoAnvil.Objects.Containers.autoanvil,windowId);
    this.player = player;

    this.blockEntity = (AutoAnvilBlockEntity)world.getTileEntity(readBlockPos);

    this.addSlot(new SlotItemHandlerUnconditioned(blockEntity.inv, 0, 27, 47));
    this.addSlot(new SlotItemHandlerUnconditioned(blockEntity.inv, 1, 76, 47));
    this.addSlot(new OutputSlot(blockEntity.inv, 2, 134, 47));
    this.addSlot(new UpgradeSlot(blockEntity.upgrades, 0, 177, 5,AutoAnvil.Objects.Items.level_upgrade){
      @Override
      public void onSlotChanged() {
        blockEntity.setCapacity();
      }
    });
    this.addSlot(new UpgradeSlot(blockEntity.upgrades, 1, 177, 23,AutoAnvil.Objects.Items.efficiency_upgrade));

    for(int i = 0; i < 3; ++i) {
      for(int j = 0; j < 9; ++j) {
        this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
      }
    }

    for(int k = 0; k < 9; ++k) {
      this.addSlot(new Slot(inv, k, 8 + k * 18, 142));
    }
  }

  /**
   * Determines whether supplied player can use this container
   *
   * @param playerIn
   */
  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return true;
  }

  /**
   * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
   * inventory and the other inventory(s).
   */@Nonnull
  @Override
  public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(index);
    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      if (index == 2) {
        if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
          return ItemStack.EMPTY;
        }

        slot.onSlotChange(itemstack1, itemstack);
      } else if (index != 0 && index != 1) {
        if (index < 39 && !this.mergeItemStack(itemstack1, 0, 2, false)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
        return ItemStack.EMPTY;
      }

      if (itemstack1.isEmpty()) {
        slot.putStack(ItemStack.EMPTY);
      } else {
        slot.onSlotChanged();
      }

      if (itemstack1.getCount() == itemstack.getCount()) {
        return ItemStack.EMPTY;
      }

      slot.onTake(playerIn, itemstack1);
    }

    return itemstack;
  }

}

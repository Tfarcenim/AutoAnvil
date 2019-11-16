package com.tfar.autoanvil;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

/**
 * borrowed from Actually Additions
 */
public class AutomationSensitiveItemStackHandler extends ItemStackHandler {

    public static final IAcceptor ACCEPT_TRUE = (a, b, c) -> true;
    public static final IRemover REMOVE_TRUE = (a, b) -> true;
    public static final IAcceptor ACCEPT_FALSE = (a, b, c) -> false;
    public static final IRemover REMOVE_FALSE = (a, b) -> false;

    private IAcceptor acceptor;
    private IRemover remover;

    public AutomationSensitiveItemStackHandler(int slots, IAcceptor acceptor, IRemover remover) {
      super(slots);
      this.acceptor = acceptor;
      this.remover = remover;
    }

  public AutomationSensitiveItemStackHandler(int slots) {
      this(slots, ACCEPT_TRUE, REMOVE_TRUE);
    }

    public NonNullList<ItemStack> getItems() {
      return this.stacks;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
      return this.insertItem(slot, stack, simulate, true);
    }

    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate, boolean fromAutomation) {
      if (!this.canAccept(slot, stack, fromAutomation)) return stack;
      return super.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
      return this.extractItem(slot, amount, simulate, true);
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate, boolean byAutomation) {
      if (!this.canRemove(slot, byAutomation)) return ItemStack.EMPTY;
      return super.extractItem(slot, amount, simulate);
    }

    public boolean canAccept(int slot, ItemStack stack, boolean automation) {
      return this.getAcceptor().canAccept(slot, stack, automation);
    }

    public boolean canRemove(int slot, boolean automation) {
      return this.getRemover().canRemove(slot, automation);
    }

    public IAcceptor getAcceptor() {
      return this.acceptor;
    }

    public IRemover getRemover() {
      return this.remover;
    }

    public interface IAcceptor {
      boolean canAccept(int slot, ItemStack stack, boolean automation);
    }

    public interface IRemover {
      boolean canRemove(int slot, boolean automation);
    }
  }


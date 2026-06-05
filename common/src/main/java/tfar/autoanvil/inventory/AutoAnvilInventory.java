package tfar.autoanvil.inventory;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import tfar.autoanvil.AutoAnvil;
import tfar.autoanvil.AutoAnvilBlockEntity;

public class AutoAnvilInventory {

    public static final int OUTPUT_SLOT = 0;
    public static final int INPUT_SLOT_XP_BOTTLE = 1;
    public static final int INPUT_SLOT_PRIMARY = 2;
    public static final int INPUT_SLOT_SECONDARY = 3;
    public static final int UPGRADE_SLOT_1 = 4;
    public static final int UPGRADE_SLOT_2 = 5;

    private final NonNullList<ItemStack> stacks = NonNullList.withSize(6, ItemStack.EMPTY);
    private final @Nullable AutoAnvilBlockEntity autoAnvilBlockEntity;


    public AutoAnvilInventory(@Nullable AutoAnvilBlockEntity autoAnvilBlockEntity) {
        this.autoAnvilBlockEntity = autoAnvilBlockEntity;
    }

    public boolean isValid(int index, ItemStack stack) {
        return switch (index) {
            case OUTPUT_SLOT -> false;
            case INPUT_SLOT_XP_BOTTLE -> stack.is(Items.EXPERIENCE_BOTTLE);
            case UPGRADE_SLOT_1 -> stack.is(AutoAnvil.AItems.LEVEL_UPGRADE);
            case UPGRADE_SLOT_2 -> stack.is(AutoAnvil.AItems.EFFICIENCY_UPGRADE);
            default -> true;
        };
    }

    public boolean canTake(int index, @Nullable Direction direction) {
        ItemStack stack = stacks.get(index);
        return switch (index) {
            case INPUT_SLOT_PRIMARY, INPUT_SLOT_SECONDARY -> false;
            case INPUT_SLOT_XP_BOTTLE -> stack.is(Items.GLASS_BOTTLE);
            case UPGRADE_SLOT_1, UPGRADE_SLOT_2 -> false;
            default -> true;
        };
    }

    public boolean canPlace(int index,ItemStack stack, @Nullable Direction direction) {
        return switch (index) {
            case OUTPUT_SLOT -> false;
            case INPUT_SLOT_XP_BOTTLE -> stack.is(Items.EXPERIENCE_BOTTLE);
            case INPUT_SLOT_PRIMARY, INPUT_SLOT_SECONDARY -> !stack.is(Items.EXPERIENCE_BOTTLE);
            case UPGRADE_SLOT_1 -> stack.is(AutoAnvil.AItems.LEVEL_UPGRADE);
            case UPGRADE_SLOT_2 -> stack.is(AutoAnvil.AItems.EFFICIENCY_UPGRADE);
            default -> true;
        };
    }

    public int getSlots() {
        return stacks.size();
    }

    public ItemStack get(int index) {
        return stacks.get(index);
    }

    public int getLevelUpgrades() {
        return get(UPGRADE_SLOT_1).getCount();
    }

    public int getEfficiencyUpgrades() {
        return get(UPGRADE_SLOT_2).getCount();
    }

    public void set(int index, ItemStack stack) {
        stacks.set(index, stack);
        onContentsChanged(index);
    }

    public int getSlotLimit(int index) {
        return 99;
    }

    public ItemStack extractWithContext(int slot, int amount, boolean simulate, @Nullable Direction direction) {
        if (!canTake(slot, direction)) return ItemStack.EMPTY;

        return extract(slot, amount, simulate);
    }

    public ItemStack extract(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;


        ItemStack existing = this.stacks.get(slot);

        if (existing.isEmpty())
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract) {
            if (!simulate) {
                this.stacks.set(slot, ItemStack.EMPTY);
                onContentsChanged(slot);
                return existing;
            } else {
                return existing.copy();
            }
        } else {
            if (!simulate) {
                this.stacks.set(slot, existing.copyWithCount(existing.getCount() - toExtract));
                onContentsChanged(slot);
            }

            return existing.copyWithCount(toExtract);
        }
    }

    public ItemStack insertWithContext(int slot, ItemStack stack, boolean simulate,@Nullable Direction direction) {
        if (!canPlace(slot,stack, direction)) return stack;
        return insert(slot, stack, simulate);
    }

    public ItemStack insert(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        if (!isValid(slot, stack))
            return stack;


        ItemStack existing = this.stacks.get(slot);

        int limit = getStackLimit(slot, stack);

        if (!existing.isEmpty()) {
            if (!ItemStack.isSameItemSameComponents(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate) {
            if (existing.isEmpty()) {
                this.stacks.set(slot, reachedLimit ? stack.copyWithCount(limit) : stack);
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
            onContentsChanged(slot);
        }

        return reachedLimit ? stack.copyWithCount(stack.getCount() - limit) : ItemStack.EMPTY;
    }

    protected int getStackLimit(int slot, ItemStack stack) {
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
    }


    public void onContentsChanged(int slot) {
        if (autoAnvilBlockEntity != null) {
            autoAnvilBlockEntity.checkInventory = true;
            autoAnvilBlockEntity.updateCapacity();
            autoAnvilBlockEntity.setChanged();
        }
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < stacks.size(); i++) {
            if (!stacks.get(i).isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                nbtTagList.add(stacks.get(i).save(provider, itemTag));
            }
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put("Items", nbtTagList);
        nbt.putInt("Size", stacks.size());
        return nbt;
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        ListTag tagList = nbt.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");

            if (slot >= 0 && slot < stacks.size()) {
                ItemStack.parse(provider, itemTags).ifPresent(stack -> stacks.set(slot, stack));
            }
        }
        //onLoad();
    }
}

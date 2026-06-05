package tfar.autoanvil;

import net.minecraft.core.Direction;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import tfar.autoanvil.inventory.AutoAnvilInventory;
import tfar.autoanvil.inventory.AutoAnvilSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import tfar.autoanvil.util.SideConfig;
import tfar.autoanvil.util.Util;

public class AutoAnvilMenu extends AbstractContainerMenu {

    protected final Player player;
    private final AutoAnvilInventory autoAnvilInventory;
    private final ContainerData data;

    public AutoAnvilMenu(int id, Inventory inventory) {
        this(id, inventory, new AutoAnvilInventory(null), new SimpleContainerData(AutoAnvilBlockEntity.DATA_SLOTS));
    }

    public AutoAnvilMenu(int windowId, Inventory inv, AutoAnvilInventory autoAnvilInventory, ContainerData data) {
        super(AutoAnvil.MenuTypes.AUTO_ANVIL, windowId);
        this.player = inv.player;
        this.autoAnvilInventory = autoAnvilInventory;
        this.data = data;

        int y = 38;

        this.addSlot(new AutoAnvilSlot(autoAnvilInventory, AutoAnvilInventory.OUTPUT_SLOT, 134, y+18));
        this.addSlot(new AutoAnvilSlot(autoAnvilInventory, AutoAnvilInventory.INPUT_SLOT_XP_BOTTLE, 27, y));
        this.addSlot(new AutoAnvilSlot(autoAnvilInventory, AutoAnvilInventory.INPUT_SLOT_PRIMARY, 27, y+18));
        this.addSlot(new AutoAnvilSlot(autoAnvilInventory, AutoAnvilInventory.INPUT_SLOT_SECONDARY,76 ,y+18));

        this.addSlot(new AutoAnvilSlot(autoAnvilInventory, AutoAnvilInventory.UPGRADE_SLOT_1, 177, 5));
        this.addSlot(new AutoAnvilSlot(autoAnvilInventory, AutoAnvilInventory.UPGRADE_SLOT_2, 177, 23));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, y+55 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inv, k, 8 + k * 18, 113+y));
        }
        addDataSlots(data);
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        switch (id) {
            case 0, 1, 2, 3, 4, 5 -> {
                SideConfig sideConfig = getSideConfig(Direction.from3DDataValue(id));
                data.set(id, Util.cycle(sideConfig).ordinal());
            }
        }
        return false;
    }

    public SideConfig getSideConfig(Direction side) {
        return SideConfig.values()[data.get(side.ordinal())];
    }

    public int getExperience() {
        return data.get(6);
    }

    public int getExperienceCapacity() {
        return data.get(7);
    }

    public int getProgress() {
        return data.get(8);
    }

    public int getLevelsRequired() {
        return data.get(9);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }
}

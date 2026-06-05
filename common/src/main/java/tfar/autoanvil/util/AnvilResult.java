package tfar.autoanvil.util;

import net.minecraft.world.item.ItemStack;

public record AnvilResult(ItemStack output, int requiredLevels, int materialCost) {

    public static final AnvilResult EMPTY = new AnvilResult(ItemStack.EMPTY, 0, 0);
    public int xpCost() {
        return Util.leveltoXPCost(requiredLevels);
    }
}

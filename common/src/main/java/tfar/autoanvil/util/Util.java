package tfar.autoanvil.util;


import net.minecraft.world.item.ItemStack;
import tfar.autoanvil.AutoAnvilBlockEntity;

public class Util {

  public static ItemStack getOutput(ItemStack input1, ItemStack input2, AutoAnvilBlockEntity container){
    return ItemStack.EMPTY;
  }

  public static int leveltoXPCost(int level){
      if (level < 17) return level*level + 6 * level;
      else if (level < 32) return (int)(2.5 * level*level - 40.5 * level + 360);
      else return (int)(4.5 * level*level - 162.5 * level + 2220);
    }

    public static final SideConfig[] values = SideConfig.values();
  public static <E extends Enum<E>> E cycle(E e) {
    E[] values = (E[]) e.getClass().getEnumConstants();
    return values[(e.ordinal() + 1) % values.length];
  }
}

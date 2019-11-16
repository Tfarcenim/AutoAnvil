package com.tfar.autoanvil;

import com.tfar.autoanvil.compat.Compat;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;

import java.util.Map;

public class Util {

  public static ItemStack getOutput(ItemStack input1, ItemStack input2,AutoAnvilBlockEntity container){
    int i = 0;
    int j = 0;
    int k = 0;
    if (input1.isEmpty()) {
      return ItemStack.EMPTY;
    } else {
      ItemStack input1copy = input1.copy();
      Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(input1copy);
      j = j + input1.getRepairCost() + (input2.isEmpty() ? 0 : input2.getRepairCost());
      boolean flag = false;

      if (!input2.isEmpty()) {

        AnvilUpdateEvent e = new AnvilUpdateEvent(input1, input2, "", j);
        //returns true if cancelled
        if (MinecraftForge.EVENT_BUS.post(e)) return ItemStack.EMPTY;
        if (!e.getOutput().isEmpty()){

          container.levelcost = e.getCost();
          container.materialCost = e.getMaterialCost();
          return e.getOutput();
        }

        flag = input2.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantments(input2).isEmpty();
        if (input1copy.isDamageable() && input1copy.getItem().getIsRepairable(input1, input2)) {
          int l2 = Math.min(input1copy.getDamage(), input1copy.getMaxDamage() / 4);
          if (l2 <= 0) {
            return ItemStack.EMPTY;
          }

          int i3;
          for(i3 = 0; l2 > 0 && i3 < input2.getCount(); ++i3) {
            int j3 = input1copy.getDamage() - l2;
            input1copy.setDamage(j3);
            ++i;
            l2 = Math.min(input1copy.getDamage(), input1copy.getMaxDamage() / 4);
          }

          container.materialCost = i3;
        } else {
          if (!flag && (input1copy.getItem() != input2.getItem() || !input1copy.isDamageable())) {
            return ItemStack.EMPTY;
          }

          if (input1copy.isDamageable() && !flag) {
            int l = input1.getMaxDamage() - input1.getDamage();
            int i1 = input2.getMaxDamage() - input2.getDamage();
            int j1 = i1 + input1copy.getMaxDamage() * 12 / 100;
            int k1 = l + j1;
            int l1 = input1copy.getMaxDamage() - k1;
            if (l1 < 0) {
              l1 = 0;
            }

            if (l1 < input1copy.getDamage()) {
              input1copy.setDamage(l1);
              i += 2;
            }
          }

          Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(input2);
          boolean flag2 = false;
          boolean flag3 = false;

          for(Enchantment enchantment1 : map1.keySet()) {
            if (enchantment1 != null) {
              int i2 = map.getOrDefault(enchantment1, 0);
              int j2 = map1.get(enchantment1);
              j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
              boolean canApplyEnchant = enchantment1.canApply(input1);
              if (input1.getItem() == Items.ENCHANTED_BOOK) {
                canApplyEnchant = true;
              }

              for(Enchantment enchantment : map.keySet()) {
                if (enchantment != enchantment1 && !enchantment1.isCompatibleWith(enchantment)) {
                  canApplyEnchant = false;
                  ++i;
                }
              }

              if (!canApplyEnchant) {
                flag3 = true;
              } else {
                flag2 = true;
                if (j2 > Compat.getMaxLevel(enchantment1)) {
                  j2 = Compat.getMaxLevel(enchantment1);
                }

                map.put(enchantment1, j2);
                int k3 = 0;
                switch(enchantment1.getRarity()) {
                  case COMMON:
                    k3 = 1;
                    break;
                  case UNCOMMON:
                    k3 = 2;
                    break;
                  case RARE:
                    k3 = 4;
                    break;
                  case VERY_RARE:
                    k3 = 8;
                }

                if (flag) {
                  k3 = Math.max(1, k3 / 2);
                }

                i += k3 * j2;
                if (input1.getCount() > 1) {
                  i = 40;
                }
              }
            }
          }

          if (flag3 && !flag2) {
            return ItemStack.EMPTY;
          }
        }
      }

  //    if (StringUtils.isBlank(this.repairedItemName)) {
  //      if (input1.hasDisplayName()) {
  //        k = 1;
  //        i += k;
  //        input1copy.clearCustomName();
  //      }
  //    } else if (!this.repairedItemName.equals(input1.getDisplayName().getString())) {
   //     k = 1;
   //     i += k;
  //      input1copy.setDisplayName(new StringTextComponent(this.repairedItemName));
   //   }
      if (flag && !input1copy.isBookEnchantable(input2)) input1copy = ItemStack.EMPTY;

      container.levelcost  = j + i;
      if (i <= 0) {
        input1copy = ItemStack.EMPTY;
      }

      if (!input1copy.isEmpty()) {
        EnchantmentHelper.setEnchantments(map, input1copy);
      }
      return input1copy;
    }
  }

  public static int leveltoXPCost(int level){
      if (level < 17) return level*level + 6 * level;
      else if (level < 32) return (int)(2.5 * level*level - 40.5 * level + 360);
      else return (int)(4.5 * level*level - 162.5 * level + 2220);
    }

    public static final SideConfig[] values = SideConfig.values();

  public static void cycleMode(AutoAnvilContainer container, int side) {
    AutoAnvilBlockEntity autoAnvilBlockEntity = container.blockEntity;
    cycle(autoAnvilBlockEntity,side);
  }

  public static void cycle(AutoAnvilBlockEntity autoAnvilBlockEntity,int side){
    int ordinal = autoAnvilBlockEntity.sideConfigs[side].ordinal();
    ordinal++;
    if (ordinal > 5)ordinal = 0;
    autoAnvilBlockEntity.sideConfigs[side] = values[ordinal];
    autoAnvilBlockEntity.markDirty();
  }

  public enum SideConfig {

    ALL,BOTH_INPUT,OUTPUT,INPUT1,INPUT2,NONE;

    public boolean allowsOutput(){
      return this == ALL || this == OUTPUT;
    }

    public boolean allowsInput(int slot){

      return this != NONE && (slot == 0 || slot == 1) &&
              (this == ALL || this == BOTH_INPUT || slot == 0 &&
                      this == INPUT1 || slot == 1 && this == INPUT2);

    }
  }
}

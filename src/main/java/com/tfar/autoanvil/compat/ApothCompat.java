package com.tfar.autoanvil.compat;

import net.minecraft.enchantment.Enchantment;
import shadows.apotheosis.ench.asm.EnchHooks;

public class ApothCompat extends Compat {
  public static int getActualMaxLevel(Enchantment ench){
    return EnchHooks.getMaxLevel(ench);
  }
}

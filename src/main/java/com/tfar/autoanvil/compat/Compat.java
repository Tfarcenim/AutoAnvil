package com.tfar.autoanvil.compat;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class Compat {
  public static final boolean isApothesisHere = ModList.get().isLoaded("apotheosis");
  public static final boolean isAnvilTweaksHere = ModList.get().isLoaded("anviltweaks");

  public static int getMaxLevel(Enchantment enchantment){
    return isApothesisHere ? ApothCompat.getActualMaxLevel(enchantment) : enchantment.getMaxLevel();
  }
}

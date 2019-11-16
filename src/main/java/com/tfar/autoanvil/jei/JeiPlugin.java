package com.tfar.autoanvil.jei;

import com.tfar.autoanvil.AutoAnvil;
import com.tfar.autoanvil.AutoAnvilContainer;
import com.tfar.autoanvil.AutoAnvilScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin, IGuiContainerHandler<AutoAnvilScreen> {
  @Override
  public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
    registration.addRecipeTransferHandler(AutoAnvilContainer.class, VanillaRecipeCategoryUid.ANVIL, 0, 2, 3+2, 36+2);
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(new ItemStack(AutoAnvil.Objects.Blocks.autoanvil), VanillaRecipeCategoryUid.ANVIL);
  }

  @Nonnull
  @Override
  public ResourceLocation getPluginUid() {
    return new ResourceLocation(AutoAnvil.MODID, AutoAnvil.MODID);
  }

  @Override
  public void registerGuiHandlers(IGuiHandlerRegistration registration) {
    registration.addGuiContainerHandler(AutoAnvilScreen.class,this);
  }

  @Nonnull
  @Override
  public List<Rectangle2d> getGuiExtraAreas(AutoAnvilScreen containerScreen) {
    List<Rectangle2d> areas = new ArrayList<>();
    if (containerScreen.isExpanded){
      int x = (containerScreen.width - 140) / 2 + 140;
      int y = (containerScreen.height - 180) / 2 + 64;
      areas.add(new Rectangle2d(x, y, 65, 60));
    }
    return areas;
  }
}

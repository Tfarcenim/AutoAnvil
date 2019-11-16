package com.tfar.autoanvil;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;

public class ToggleSidesGuiButton extends Button {

  private boolean isExpanded;
  private static final ResourceLocation TEXTURE =
          new ResourceLocation(AutoAnvil.MODID,"textures/gui/side_configuration_button.png");

  public ToggleSidesGuiButton(int xpos, int ypos, int width, int height, IPressable onPress, boolean isExpanded) {
    super(xpos, ypos, width, height, "", onPress);
    this.isExpanded = isExpanded;
  }

  public void toggle(){
    isExpanded = !isExpanded;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    if (visible) {
      Minecraft minecraft = Minecraft.getInstance();
      minecraft.getTextureManager().bindTexture(TEXTURE);
      GlStateManager.color4f(1,1,1,1);
      if (isExpanded) {
        blit(x, y, 0, 26, 80, 80,128,128);
      } else {
        blit(x, y, 0, 0, 26, 26,128,128);
      }
      //isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }
  }
}

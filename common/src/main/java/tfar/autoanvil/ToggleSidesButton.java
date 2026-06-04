package tfar.autoanvil;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ToggleSidesButton extends Button {

  private boolean expanded;
  private static final ResourceLocation TEXTURE = AutoAnvil.id("textures/gui/side_configuration_button.png");

  protected ToggleSidesButton(int x, int y, int width, int height, Component message, OnPress onPress, boolean wasExpanded) {
    super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
    this.expanded = wasExpanded;
  }


  public void toggle(){
    expanded = !expanded;
  }

  public void render(int mouseX, int mouseY, float partialTicks) {
   /* if (visible) {
      Minecraft minecraft = Minecraft.getInstance();
      minecraft.getTextureManager().bindTexture(TEXTURE);
      GlStateManager.color4f(1,1,1,1);
      if (expanded) {
        blit(x, y, 0, 26, 80, 80,128,128);
      } else {
        blit(x, y, 0, 0, 26, 26,128,128);
      }
      //isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }*/
  }

  @Override
  protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
  }
}

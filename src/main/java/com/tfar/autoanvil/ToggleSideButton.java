package com.tfar.autoanvil;

import com.mojang.blaze3d.platform.GlStateManager;
import com.tfar.autoanvil.network.C2SMessageToggleSideConfig;
import com.tfar.autoanvil.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class ToggleSideButton extends Button {

  protected Util.SideConfig sideConfig;
  protected final Direction side;

  private static final ResourceLocation TEXTURE =
          new ResourceLocation(AutoAnvil.MODID, "textures/gui/side_configuration_button.png");

  public ToggleSideButton(int xpos, int ypos, int width, int height, IPressable onPress, Util.SideConfig sideConfig,Direction side) {
    super(xpos, ypos, width, height, side.toString().substring(0,1), onPress);
    this.sideConfig = sideConfig;
    this.side = side;
  }

  public void toggle() {
    int ordinal = sideConfig.ordinal();
    ordinal++;
    if (ordinal >= Util.values.length) ordinal = 0;
    sideConfig = Util.values[ordinal];
    PacketHandler.INSTANCE.sendToServer(new C2SMessageToggleSideConfig(side.getIndex()));
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    visible = (Minecraft.getInstance().currentScreen instanceof AutoAnvilScreen) && ((AutoAnvilScreen) Minecraft.getInstance().currentScreen).isExpanded;
    if (visible) {
      switch (this.sideConfig) {
        case ALL:
          GlStateManager.color3f(1, 1, 1);
          break;
        case BOTH_INPUT:
          GlStateManager.color3f(.25f, .25f, 1);
          break;
        case INPUT1:
          GlStateManager.color3f(0, 1, 0);
          break;
        case INPUT2:
          GlStateManager.color3f(0.75f, .25f, 1);
          break;
        case OUTPUT:
          GlStateManager.color3f(1, 0.5f, 0);
          break;
        case NONE:
          GlStateManager.color3f(0.25f, 0.25f, 0.25f);
          break;
      }
      this.renderButton(mouseX, mouseY, partialTicks);
    }
  }


  @Override
  public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
    Minecraft minecraft = Minecraft.getInstance();
    FontRenderer fontrenderer = minecraft.fontRenderer;
    minecraft.getTextureManager().bindTexture(WIDGETS_LOCATION);
    // GlStateManager.color4f(1.0F, 1.0F, 1.0F, this.alpha);
    int i = this.getYImage(this.isHovered());
    GlStateManager.enableBlend();
    GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    this.blit(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
    this.blit(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
    this.renderBg(minecraft, p_renderButton_1_, p_renderButton_2_);
    int j = getFGColor();

    this.drawCenteredString(fontrenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
  }
}

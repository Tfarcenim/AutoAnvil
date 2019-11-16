package com.tfar.autoanvil;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class AutoAnvilScreen extends ContainerScreen<AutoAnvilContainer> {

  private static final ResourceLocation ANVIL_RESOURCE = new ResourceLocation(AutoAnvil.MODID,"textures/gui/autoanvil.png");
  public boolean isExpanded = false;

  public AutoAnvilScreen(AutoAnvilContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.xSize+=22;
  }

  @Override
  public void resize(Minecraft p_resize_1_, int p_resize_2_, int p_resize_3_) {
    super.resize(p_resize_1_, p_resize_2_, p_resize_3_);
  }

  @Override
  protected void init() {
    super.init();

    int yStart = guiTop + 47;

    addButton(new ToggleSidesGuiButton(guiLeft + 176,guiTop + 44,20,20,(b) -> {
      ((ToggleSidesGuiButton) b).toggle();
      this.isExpanded = !isExpanded;
    },isExpanded));
    addButton(new ToggleSideButton(guiLeft + 198,yStart,20,20,(b) -> {
      if (isExpanded)
      ((ToggleSideButton) b).toggle();
    },container.blockEntity.sideConfigs[Direction.UP.getIndex()], Direction.UP));

    yStart+=20;

    addButton(new ToggleSideButton(guiLeft + 178,yStart,20,20,(b) -> {
      if (isExpanded)
        ((ToggleSideButton) b).toggle();
    },container.blockEntity.sideConfigs[Direction.EAST.getIndex()],Direction.EAST));
    addButton(new ToggleSideButton(guiLeft + 198,yStart,20,20,(b) -> {
      if (isExpanded)
        ((ToggleSideButton) b).toggle();
    },container.blockEntity.sideConfigs[Direction.NORTH.getIndex()],Direction.NORTH));
    addButton(new ToggleSideButton(guiLeft + 218,yStart,20,20,(b) -> {
      if (isExpanded)
        ((ToggleSideButton) b).toggle();
    },container.blockEntity.sideConfigs[Direction.WEST.getIndex()],Direction.WEST));

    yStart+=20;

    addButton(new ToggleSideButton(guiLeft + 198,yStart,20,20,(b) -> {
      if (isExpanded)
        ((ToggleSideButton) b).toggle();
    },container.blockEntity.sideConfigs[Direction.DOWN.getIndex()],Direction.DOWN));
    addButton(new ToggleSideButton(guiLeft + 218,yStart,20,20,(b) -> {
      if (isExpanded)
        ((ToggleSideButton) b).toggle();
    },container.blockEntity.sideConfigs[Direction.SOUTH.getIndex()],Direction.SOUTH));
  }

  public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
    this.renderBackground();
    super.render(p_render_1_, p_render_2_, p_render_3_);
    this.renderHoveredToolTip(p_render_1_, p_render_2_);
    GlStateManager.disableLighting();
    GlStateManager.disableBlend();
    //this.nameField.render(p_render_1_, p_render_2_, p_render_3_);
  }

  @Override
  protected void renderHoveredToolTip(int mouseX, int mouseY) {
    super.renderHoveredToolTip(mouseX, mouseY);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;

    if (isPointInRegion(9,6,8,70,mouseX,mouseY)){
      List<String> tooltip = new ArrayList<>();
      tooltip.add(this.container.blockEntity.fluidStorage.getFluidAmount()+" xp");
      GuiUtils.drawHoveringText(tooltip,mouseX,mouseY,this.width,this.height,100,this.font);
    }
  }

  /**
   * Draws the background layer of this container (behind the items).
   *
   * @param partialTicks
   * @param mouseX
   * @param mouseY
   */
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.minecraft.getTextureManager().bindTexture(ANVIL_RESOURCE);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this.blit(i, j, 0, 0, this.xSize, this.ySize);
//    31
    int x1 = 206;
    int x2 = x1 - 8;
    int y1 = 17;
    double scaledbar = 64d * this.container.blockEntity.fluidStorage.getFluidAmount()/this.container.blockEntity.fluidStorage.getCapacity();
    int y3 = (int)scaledbar;

    //energy bar
    GlStateManager.color3f(0,1,0);
    this.blit(i + 9, j + 10, x1, y1, 8, 64);
    this.blit(i + 9, j + 10 + 64 - y3, x2, y1, 8, y3);
  }
}

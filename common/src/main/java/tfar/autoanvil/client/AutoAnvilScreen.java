package tfar.autoanvil.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import tfar.autoanvil.AutoAnvil;
import tfar.autoanvil.AutoAnvilBlockEntity;
import tfar.autoanvil.AutoAnvilMenu;
import tfar.autoanvil.util.SideConfig;
import tfar.autoanvil.util.Util;

public class AutoAnvilScreen extends AbstractContainerScreen<AutoAnvilMenu> {

  private static final ResourceLocation ANVIL_RESOURCE = AutoAnvil.id("textures/gui/autoanvil.png");
  public static final ResourceLocation ARROW = AutoAnvil.id("arrow");
  public boolean expanded;

  ExperienceLabel experienceLabel;

  public AutoAnvilScreen(AutoAnvilMenu screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.imageWidth+=22;
    titleLabelX+=44;
    inventoryLabelY+=9;
    imageHeight+=9;
  }

  @Override
  protected void init() {
    super.init();

    int yStart = topPos + 47;

    //addRenderableWidget(new ToggleSidesButton(leftPos + 176,topPos + 44,20,20,Component.empty(),(b) ->
    //        ((ToggleSidesButton) b).toggle()));


    experienceLabel = new ExperienceLabel(leftPos+8,topPos+8,8,64,Component.empty());
    addRenderableWidget(experienceLabel);

    addRenderableWidget(new ToggleSideButton(leftPos + 198,yStart,20,20,Component.empty(),
            (b) -> ((ToggleSideButton) b).toggle(), Direction.UP));

    yStart+=20;

    addRenderableWidget(new ToggleSideButton(leftPos + 178,yStart,20,20,Component.empty(),(b) -> ((ToggleSideButton) b).toggle(),Direction.EAST));
    addRenderableWidget(new ToggleSideButton(leftPos + 198,yStart,20,20,Component.empty(),(b) -> ((ToggleSideButton) b).toggle(),
            Direction.NORTH));
    addRenderableWidget(new ToggleSideButton(leftPos + 218,yStart,20,20,Component.empty(),(b) -> {
        ((ToggleSideButton) b).toggle();
    },Direction.WEST));

    yStart+=20;

    addRenderableWidget(new ToggleSideButton(leftPos + 198,yStart,20,20,Component.empty(),(b) -> ((ToggleSideButton) b).toggle(),Direction.DOWN));
    addRenderableWidget(new ToggleSideButton(leftPos + 218,yStart,20,20,Component.empty(),
            (b) -> ((ToggleSideButton) b).toggle(),Direction.SOUTH));
  }

  @Override
  protected void containerTick() {
    super.containerTick();
    experienceLabel.setTooltip(Tooltip.create(Component.literal("Experience: "+menu.getExperience()+"/"+menu.getExperienceCapacity())));
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    renderBackground(guiGraphics, mouseX, mouseY, partialTick);
    super.render(guiGraphics, mouseX, mouseY, partialTick);
    renderTooltip(guiGraphics,mouseX,mouseY);
  }

  void send(int id) {
    minecraft.gameMode.handleInventoryButtonClick(menu.containerId, id);
  }

  /*@Override
  protected void renderHoveredToolTip(int mouseX, int mouseY) {
    super.renderHoveredToolTip(mouseX, mouseY);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;

    if (isPointInRegion(9,6,8,70,mouseX,mouseY)){
      List<String> tooltip = new ArrayList<>();
      tooltip.add(this.container.blockEntity.fluidStorage.getFluidAmount()+" xp");
      GuiUtils.drawHoveringText(tooltip,mouseX,mouseY,this.width,this.height,100,this.font);
    }
  }*/

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    guiGraphics.blit(ANVIL_RESOURCE,this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);


    float ratio = (float)menu.getProgress() / AutoAnvilBlockEntity.PROGRESS_MAX;

    int w = (int) (ratio * 22);

    guiGraphics.blit(ANVIL_RESOURCE,this.leftPos+100, this.topPos+56, 198, 0, w, 16);


    //guiGraphics.blitSprite(ARROW,leftPos+100, topPos+1,0,1, 16);

  }

  public class ToggleSideButton extends Button {

    protected final Direction side;

    private static final ResourceLocation TEXTURE = AutoAnvil.id("textures/gui/side_configuration_button.png");

    protected ToggleSideButton(int x, int y, int width, int height, Component message, OnPress onPress,Direction side) {
      super(x, y, width, height, message, onPress,DEFAULT_NARRATION);
      this.side = side;
    }


    public void toggle() {
      send(side.ordinal());
    }
    
    SideConfig getSideConfig() {
      return menu.getSideConfig(side);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
      visible = expanded;
      if (visible) {
        SideConfig sideConfig = getSideConfig();
        RenderSystem.setShaderColor(sideConfig.splitColor[0], sideConfig.splitColor[1], sideConfig.splitColor[2], sideConfig.splitColor[3]);
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
      }
    }
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    super.renderLabels(guiGraphics, mouseX, mouseY);
    int level = menu.getLevelsRequired();
    int stored = menu.getExperience();
    int capacity = menu.getExperienceCapacity();

    int xpPoints = Util.leveltoXPCost(level);

    int color = 0x007f00;

    if (xpPoints>capacity) {
      color = 0x7f0000;
    } else if (xpPoints>stored) {
      color = 0x9f9f00;
    }

    guiGraphics.drawString(font,Component.literal("Lvl: "+level+" | XP: "+xpPoints),50,40,0xff000000| color,false);
  }

  public class ToggleSidesButton extends Button {

    private static final ResourceLocation TEXTURE = AutoAnvil.id("textures/gui/side_configuration_button.png");

    protected ToggleSidesButton(int x, int y, int width, int height, Component message, OnPress onPress) {
      super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
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

  public class ExperienceLabel extends AbstractWidget {

    protected static final ResourceLocation XP_BAR = AutoAnvil.id("xp_bar");
    protected static final ResourceLocation XP_BAR_BACKGROUND = AutoAnvil.id("xp_bar_background");


    public ExperienceLabel(int x, int y, int width, int height, Component message) {
      super(x, y, width, height, message);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
      RenderSystem.setShaderColor(0,1,0,1);
      //RenderSystem.enableBlend();
      guiGraphics.blitSprite(XP_BAR_BACKGROUND,getX(),getY(),0,width,height);

      float ratio = (float)menu.getExperience() / (Math.max(menu.getExperienceCapacity(),1));

      if (ratio > 1) {ratio = 1;}

      int y0 = (int) (ratio * 64);

      guiGraphics.blitSprite(XP_BAR,getX(), getY() + 64 - y0,0,width, y0);

      RenderSystem.setShaderColor(1,1,1,1);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
  }

}

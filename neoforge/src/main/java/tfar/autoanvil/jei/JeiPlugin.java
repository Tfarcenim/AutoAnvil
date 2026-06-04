package tfar.autoanvil.jei;

//@mezz.jei.api.JeiPlugin
public class JeiPlugin {
  /*@Override
  public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
    registration.addRecipeTransferHandler(AutoAnvilContainer.class, VanillaRecipeCategoryUid.ANVIL, 0, 2, 3+2, 36+2);
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(new ItemStack(com.tfar.autoanvil.AutoAnvilNeoForge.Objects.Blocks.AUTOANVIL), VanillaRecipeCategoryUid.ANVIL);
  }

  @Nonnull
  @Override
  public ResourceLocation getPluginUid() {
    return new ResourceLocation(com.tfar.autoanvil.AutoAnvilNeoForge.MODID, com.tfar.autoanvil.AutoAnvilNeoForge.MODID);
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
  }*/
}

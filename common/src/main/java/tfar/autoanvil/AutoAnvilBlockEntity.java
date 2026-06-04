package tfar.autoanvil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import tfar.autoanvil.inventory.AutoAnvilFluidInventory;
import tfar.autoanvil.inventory.AutoAnvilInventory;
import tfar.autoanvil.inventory.CFluidStack;
import tfar.autoanvil.util.SideConfig;
import tfar.autoanvil.util.Util;

public class AutoAnvilBlockEntity extends BlockEntity implements MenuProvider {

  public AutoAnvilInventory anvilInventory = new AutoAnvilInventory(this);
  public AutoAnvilFluidInventory fluidInventory = new AutoAnvilFluidInventory(this);

  public SideConfig[] sideConfigs = new SideConfig[]{SideConfig.ALL,SideConfig.ALL,SideConfig.ALL,SideConfig.ALL,SideConfig.ALL,SideConfig.ALL};

  public static final int DATA_SLOTS = 8;

  private final ContainerData data = new ContainerData() {
    @Override
    public int get(int index) {
      if (index < 6) {
        return sideConfigs[index].ordinal();
      } else if (index == 6) {
        return fluidInventory.getAmount();
      } else if (index == 7) {
        return fluidInventory.getCapacity();
      }

      return 0;
    }

    @Override
    public void set(int index, int value) {
      if (index < 6) {
        sideConfigs[index] = SideConfig.values()[value];
      }
    }

    @Override
    public int getCount() {
      return DATA_SLOTS;
    }
  };

    public static void serverTick(Level level1, BlockPos blockPos, BlockState blockState, AutoAnvilBlockEntity t) {
      t.serverTick();
    }

  public void updateCapacity(){
    fluidInventory.setCapacity(Util.leveltoXPCost(40 + anvilInventory.getLevelUpgrades()));
  }

  public AutoAnvilBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(AutoAnvil.BlockEntityTypes.AUTO_ANVIL,blockPos,blockState);
  }

  int levelcost = 0;
  int materialCost = 0;

  public void serverTick() {

    checkXPSlot();

    ItemStack result = Util.getOutput(anvilInventory.get(AutoAnvilInventory.INPUT_SLOT_PRIMARY), anvilInventory
            .get(AutoAnvilInventory.INPUT_SLOT_SECONDARY),this);
    if (!result.isEmpty()) {
      if (fluidInventory.getAmount() >= getXpCost()){
        combine(result);
        fluidInventory.drain(getXpCost(), false);
      }
    }
  }

  void checkXPSlot() {
    ItemStack stack = anvilInventory.get(AutoAnvilInventory.INPUT_SLOT_XP_BOTTLE);
    if (stack.is(Items.EXPERIENCE_BOTTLE)) {
      int remaining = fluidInventory.getCapacity() - fluidInventory.getAmount();
      int estimated = 7 * stack.getCount();
      if (estimated <= remaining) {
        fluidInventory.fill(new CFluidStack(AutoAnvil.AFluids.XP,estimated),false);
        anvilInventory.set(AutoAnvilInventory.INPUT_SLOT_XP_BOTTLE, new ItemStack(Items.GLASS_BOTTLE,stack.getCount()));
      }
    }
  }

  public void combine(ItemStack output){
    ItemStack existing = anvilInventory.get(2);
    if (existing.isEmpty()){
      anvilInventory.set(2,output);
      anvilInventory.set(0, ItemStack.EMPTY);
      if (this.materialCost > 0) {
        ItemStack input2 = anvilInventory.get(1);
        if (!input2.isEmpty() && input2.getCount() > this.materialCost) {
          input2.shrink(this.materialCost);
          anvilInventory.set(1, input2);
        } else {
          anvilInventory.set(1, ItemStack.EMPTY);
        }
      } else {
        anvilInventory.set(1, ItemStack.EMPTY);
      }
    }
  }

  public int getXpCost(){
    return (int) (Math.pow(.90, anvilInventory.get(1).getCount()) * Util.leveltoXPCost(levelcost));
  }

  @Override
  public Component getDisplayName() {
    return AutoAnvil.ABlocks.AUTO_ANVIL.getName();
  }

  public SideConfig getSideConfig(Direction direction){
    return sideConfigs[direction.ordinal()];
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    tag.put("inventory", anvilInventory.serializeNBT(registries));
    tag.put("fluid_inventory",fluidInventory.serializeNBT(registries));
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    anvilInventory.deserializeNBT(registries, tag.getCompound("inventory"));
    fluidInventory.deserializeNBT(registries, tag.getCompound("fluid_inventory"));

  }

  public void updateClient(){
    this.level.sendBlockUpdated(worldPosition, this.getBlockState(), this.getBlockState(), 2);
  }

  @Override
  public void setChanged() {
    super.setChanged();
    updateClient();
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    CompoundTag tag = super.getUpdateTag(registries);
    saveAdditional(new CompoundTag(),registries);
    return tag;
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }



  @Override
  public @org.jetbrains.annotations.Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
    return new AutoAnvilMenu(i,inventory,anvilInventory,data);
  }

}

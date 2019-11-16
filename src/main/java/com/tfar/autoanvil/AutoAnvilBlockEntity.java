package com.tfar.autoanvil;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AutoAnvilBlockEntity extends TileEntity implements INamedContainerProvider,ITickableTileEntity {

  public AutomationSensitiveItemStackHandler inv = new BlockEntityStackHandler(3);
  public ItemStackHandler upgrades = new ItemStackHandler(2);
  public Util.SideConfig[] sideConfigs = new Util.SideConfig[]{Util.values[0],Util.values[0],Util.values[0],Util.values[0],Util.values[0],Util.values[0]};

  public void setCapacity(){
    AutoAnvilBlockEntity.this.fluidStorage.setCapacity(Util.leveltoXPCost(40 + upgrades.getStackInSlot(0).getCount()));
  }

  public LazyOptional<IItemHandler> itemLazyOptional = LazyOptional.of(() -> inv);


  public FluidTank fluidStorage = new FluidTank(Util.leveltoXPCost(40), f -> f.getFluid().isIn(AutoAnvil.xptag)){
    @Override
    protected void onContentsChanged() {
      super.onContentsChanged();
      AutoAnvilBlockEntity.this.markDirty();
    }
  };

  public LazyOptional<IFluidHandler> fluidLazyOptional = LazyOptional.of(() -> fluidStorage);

  public AutoAnvilBlockEntity() {
    super(AutoAnvil.Objects.Tiles.autoanvil);
  }

  int levelcost = 0;
  int materialCost = 0;

  @Override
  public void tick() {
    if (!world.isRemote){
    ItemStack result = Util.getOutput(inv.getStackInSlot(0),inv.getStackInSlot(1),this).copy();
    if (!result.isEmpty()) {
         if (fluidStorage.getFluidAmount() >= getXpCost()){
           combine(result);
           fluidStorage.drain(getXpCost(), IFluidHandler.FluidAction.EXECUTE);
         }
      }
    }
  }

  public void combine(ItemStack output){
    ItemStack existing = inv.getStackInSlot(2);
    if (existing.isEmpty()){
      inv.setStackInSlot(2,output);
      inv.setStackInSlot(0, ItemStack.EMPTY);
      if (this.materialCost > 0) {
        ItemStack input2 = inv.getStackInSlot(1);
        if (!input2.isEmpty() && input2.getCount() > this.materialCost) {
          input2.shrink(this.materialCost);
          inv.setStackInSlot(1, input2);
        } else {
          inv.setStackInSlot(1, ItemStack.EMPTY);
        }
      } else {
        inv.setStackInSlot(1, ItemStack.EMPTY);
      }
    }
  }

  public int getXpCost(){
    return (int) (Math.pow(.90,upgrades.getStackInSlot(1).getCount()) * Util.leveltoXPCost(levelcost));
  }

  @Nonnull
  @Override
  public ITextComponent getDisplayName(){return new StringTextComponent("no");
  }

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
    return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && getSideConfig(side) != Util.SideConfig.NONE ? getItemHandlerLazyOptional(side).cast() :
            cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? fluidLazyOptional.cast() :
                    super.getCapability(cap, side);
  }

  public Util.SideConfig getSideConfig(Direction direction){
    return sideConfigs[direction.getIndex()];
  }

  public LazyOptional<IItemHandler> getItemHandlerLazyOptional(Direction dir) {
    return LazyOptional.of(() -> new ConfigurableWrapper(dir,this));
  }

  @Nonnull
  @Override
  public CompoundNBT write(CompoundNBT tag) {
    CompoundNBT inv = this.inv.serializeNBT();
    tag.put("inv", inv);
    CompoundNBT upgrades = this.upgrades.serializeNBT();
    tag.putIntArray("sideconfig",new int[]{this.sideConfigs[0].ordinal(),this.sideConfigs[1].ordinal(),
            this.sideConfigs[2].ordinal(),this.sideConfigs[3].ordinal(),this.sideConfigs[4].ordinal(),this.sideConfigs[5].ordinal()});
    tag.put("upgrades", upgrades);
  //  if (this.customName != null) {
  //    tag.putString("CustomName", ITextComponent.Serializer.toJson(this.customName));
 //   }
    tag.putInt("capacity", fluidStorage.getCapacity());
    tag.putInt("xp", fluidStorage.getFluidAmount());
    return super.write(tag);
  }

  @Override
  public void read(CompoundNBT tag) {
    CompoundNBT invTag = tag.getCompound("inv");
    inv.deserializeNBT(invTag);
    CompoundNBT upgrades = tag.getCompound("upgrades");
    this.upgrades.deserializeNBT(upgrades);
    int [] ordinals = tag.getIntArray("sideconfig");
     this.sideConfigs = new Util.SideConfig[]{Util.values[ordinals[0]],Util.values[ordinals[1]],Util.values[ordinals[2]],Util.values[ordinals[3]],Util.values[ordinals[4]],Util.values[ordinals[5]]};
    if (tag.contains("CustomName", 8)) {
    //  this.customName = ITextComponent.Serializer.fromJson(tag.getString("CustomName"));
    }
    fluidStorage.setCapacity(tag.getInt("capacity"));
    fluidStorage.setFluid(new FluidStack(AutoAnvil.xp.get(),tag.getInt("xp")));
    super.read(tag);
  }

  public void updateClient(){
    this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), 2);
  }

  @Override
  public void markDirty() {
    super.markDirty();
    updateClient();
  }

  @Override
  public CompoundNBT getUpdateTag() {
    return this.write(new CompoundNBT());
  }

  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(getPos(), 1, getUpdateTag());
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
    this.read(packet.getNbtCompound());
  }

  @Nullable
  @Override
  public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
    return new AutoAnvilContainer(p_createMenu_1_, world, pos, p_createMenu_2_,p_createMenu_3_);
  }

  //checks to see if item can be inserted
  public AutomationSensitiveItemStackHandler.IAcceptor getAcceptor() {
    return (slot, stack, automation) -> !automation || ((slot == 0) || slot == 1);
  }

  //checks to see if item can be removed
  public AutomationSensitiveItemStackHandler.IRemover getRemover() {
    return (slot, automation) -> !automation || slot == 2;
  }

  protected class BlockEntityStackHandler extends AutomationSensitiveItemStackHandler {

    protected BlockEntityStackHandler(int slots) {
      super(slots);
    }

    @Override
    public AutomationSensitiveItemStackHandler.IAcceptor getAcceptor() {
      return AutoAnvilBlockEntity.this.getAcceptor();
    }

    @Override
    public AutomationSensitiveItemStackHandler.IRemover getRemover() {
      return AutoAnvilBlockEntity.this.getRemover();
    }

    @Override
    protected void onContentsChanged(int slot) {
      super.onContentsChanged(slot);
      markDirty();
    }
  }
}

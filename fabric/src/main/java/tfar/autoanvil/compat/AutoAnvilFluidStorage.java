package tfar.autoanvil.compat;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.minecraft.core.Direction;
import tfar.autoanvil.AutoAnvilBlockEntity;

public class AutoAnvilFluidStorage extends SingleFluidStorage {
    private final AutoAnvilBlockEntity blockEntity;
    private final Direction direction;

    public AutoAnvilFluidStorage(AutoAnvilBlockEntity blockEntity, Direction direction) {
        this.blockEntity = blockEntity;
        this.direction = direction;
    }

    @Override
    protected long getCapacity(FluidVariant variant) {
        return blockEntity.fluidInventory.getCapacity();
    }

    @Override
    protected void onFinalCommit() {
        super.onFinalCommit();
        blockEntity.setChanged();
    }
}

package tfar.autoanvil;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import tfar.autoanvil.compat.AutoAnvilFluidStorage;
import tfar.autoanvil.compat.AutoAnvilSingleSlotStorage;
import tfar.autoanvil.inventory.AutoAnvilInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoAnvilFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        AutoAnvil.init();
        AutoAnvil.register();
        ItemStorage.SIDED.registerForBlockEntity(AutoAnvilFabric::getStorage, AutoAnvil.BlockEntityTypes.AUTO_ANVIL);
        FluidStorage.SIDED.registerForBlockEntity(AutoAnvilFabric::getFluidStorage, AutoAnvil.BlockEntityTypes.AUTO_ANVIL);
    }


    static Map<BlockEntity, CombinedStorage<ItemVariant, AutoAnvilSingleSlotStorage>> MAP = new HashMap<>();

    //item api

    public static CombinedStorage<ItemVariant, AutoAnvilSingleSlotStorage> getStorage(AutoAnvilBlockEntity blockEntity, Direction direction) {

        AutoAnvilInventory anvilInventory = blockEntity.anvilInventory;

        CombinedStorage<ItemVariant, AutoAnvilSingleSlotStorage> storage = MAP.get(blockEntity);

        if (storage != null && storage.parts.size() != anvilInventory.getSlots()) {
            storage = null;
        }
        if (storage == null) {
            storage = create(anvilInventory,direction);
            MAP.put(blockEntity, storage);
        }
        return storage;
    }

    public static CombinedStorage<ItemVariant, AutoAnvilSingleSlotStorage> create(AutoAnvilInventory inventory, Direction direction) {
        int slots = inventory.getSlots();

        List<AutoAnvilSingleSlotStorage> storages = new ArrayList<>(slots);

        for (int i = 0; i < slots; i++) {
            AutoAnvilSingleSlotStorage storage = new AutoAnvilSingleSlotStorage(inventory, i,direction);
            storages.add(storage);
        }

        return new CombinedStorage<>(storages);
    }

    //fluid api

    public static final Map<BlockEntity, AutoAnvilFluidStorage> FLUID_MAP = new HashMap<>();

    public static AutoAnvilFluidStorage getFluidStorage(AutoAnvilBlockEntity blockEntity, Direction direction) {

        AutoAnvilFluidStorage storage = FLUID_MAP.get(blockEntity);

        if (storage == null) {
            storage = new AutoAnvilFluidStorage(blockEntity, direction);
            FLUID_MAP.put(blockEntity, storage);
        }
        return storage;
    }
}

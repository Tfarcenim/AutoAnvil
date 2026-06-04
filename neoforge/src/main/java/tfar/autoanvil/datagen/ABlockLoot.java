package tfar.autoanvil.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import tfar.autoanvil.AutoAnvil;

import java.util.List;
import java.util.Set;

public class ABlockLoot extends BlockLootSubProvider {
    protected ABlockLoot(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.VANILLA_SET, registries);
    }

    @Override
    protected void generate() {
        dropSelf(AutoAnvil.ABlocks.AUTO_ANVIL);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return List.of(AutoAnvil.ABlocks.AUTO_ANVIL);
    }
}

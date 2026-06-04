package tfar.autoanvil.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tfar.autoanvil.AutoAnvil;

import java.util.concurrent.CompletableFuture;

public class ADatagen {

    public static void gather(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        generator.addProvider(true,new BlockTagProvider(packOutput,lookupProvider,existingFileHelper));
        generator.addProvider(true,new BlockStatesProvider(packOutput,existingFileHelper));
        generator.addProvider(true,new ItemModelsProvider(packOutput,existingFileHelper));
    }


    static class BlockStatesProvider extends BlockStateProvider {

        public BlockStatesProvider(PackOutput output, ExistingFileHelper exFileHelper) {
            super(output, AutoAnvil.MOD_ID, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels() {
        }
    }

    static class ItemModelsProvider extends ItemModelProvider {
        public ItemModelsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
            super(output, AutoAnvil.MOD_ID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            getBuilder("xp_bucket")
                    .texture("layer0", ResourceLocation.fromNamespaceAndPath("neoforge","item/bucket_base"))
                    .texture("layer1", ResourceLocation.fromNamespaceAndPath("neoforge","item/bucket_fluid"))
                    .texture("layer2", ResourceLocation.fromNamespaceAndPath("neoforge","item/bucket_cover"));
        }
    }


    static class BlockTagProvider extends BlockTagsProvider {
        public BlockTagProvider(PackOutput output,CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
            super(output,lookupProvider, AutoAnvil.MOD_ID,existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(AutoAnvil.ABlocks.AUTO_ANVIL);
        }
    }
}

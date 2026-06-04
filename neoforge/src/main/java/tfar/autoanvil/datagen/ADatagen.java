package tfar.autoanvil.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.packs.*;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;
import tfar.autoanvil.AutoAnvil;

import java.util.List;
import java.util.Set;
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
        generator.addProvider(true,new FluidTagProvider(packOutput,lookupProvider,existingFileHelper));
        generator.addProvider(true,new Recipes(packOutput,lookupProvider));
        generator.addProvider(true,ALootTableProvider.create(packOutput,lookupProvider));
    }

    static class FluidTagProvider extends FluidTagsProvider {

        public FluidTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, provider, AutoAnvil.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(AutoAnvil.XP).add(AutoAnvil.AFluids.XP,AutoAnvil.AFluids.FLOWING_XP);
        }
    }

    static class Recipes extends RecipeProvider{
        public Recipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        protected RecipeOutput output;

        @Override
        protected void buildRecipes(RecipeOutput recipeOutput) {
            output = recipeOutput;
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AutoAnvil.AItems.AUTO_ANVIL)
                    .define('i', Tags.Items.INGOTS_IRON)
                    .define('R', Blocks.REDSTONE_BLOCK)
                    .define('A',Blocks.ANVIL)
                    .pattern("iii")
                    .pattern("iAi")
                    .pattern("iRi")
                    .unlockedBy(getHasName(Blocks.ANVIL),has(Blocks.ANVIL))
                    .save(output);

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AutoAnvil.AItems.EFFICIENCY_UPGRADE)
                    .define('g', Tags.Items.INGOTS_GOLD)
                    .define('G', Blocks.GLOWSTONE)
                    .define('I',Blocks.IRON_BLOCK)
                    .pattern("gGg")
                    .pattern("GIG")
                    .pattern("gGg")
                    .unlockedBy(getHasName(Blocks.IRON_BLOCK),has(Blocks.IRON_BLOCK))
                    .save(output);

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AutoAnvil.AItems.LEVEL_UPGRADE)
                    .define('e', Items.EXPERIENCE_BOTTLE)
                    .define('A',Blocks.ANVIL)
                    .pattern("eee")
                    .pattern("eAe")
                    .pattern("eee")
                    .unlockedBy(getHasName(Blocks.ANVIL),has(Blocks.ANVIL))
                    .save(output);
        }
    }

    static class ALootTableProvider extends LootTableProvider {

        static LootTableProvider create(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries){
            return new ALootTableProvider(
                    packOutput,
                    BuiltInLootTables.all(),
                    List.of(
                            new LootTableProvider.SubProviderEntry(ABlockLoot::new, LootContextParamSets.BLOCK)
                    ),
                    registries
            );
        }

        public ALootTableProvider(PackOutput output, Set<ResourceKey<LootTable>> requiredTables, List<SubProviderEntry> subProviders, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, requiredTables, subProviders, registries);
        }

        @Override
        protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {

        }
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
            /*getBuilder("xp_bucket")
                    .texture("layer0", ResourceLocation.fromNamespaceAndPath("neoforge","item/bucket_base"))
                    .texture("layer1", ResourceLocation.fromNamespaceAndPath("neoforge","item/bucket_fluid"))
                    .texture("layer2", ResourceLocation.fromNamespaceAndPath("neoforge","item/bucket_cover"));*/
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

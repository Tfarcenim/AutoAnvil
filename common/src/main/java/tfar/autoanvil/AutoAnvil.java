package tfar.autoanvil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.autoanvil.fluids.CFluidType;
import tfar.autoanvil.fluids.CSoundActions;
import tfar.autoanvil.fluids.FluidProperties;
import tfar.autoanvil.platform.Services;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class AutoAnvil {

    public static final String MOD_ID = "autoanvil";
    public static final String MOD_NAME = "AutoAnvil";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static final TagKey<Fluid> XP = TagKey.create(Registries.FLUID,
            ResourceLocation.fromNamespaceAndPath("c","xp"));

    public static final ResourceLocation FLUID_STILL = ResourceLocation.withDefaultNamespace("block/water_still");
    public static final ResourceLocation FLUID_FLOWING = ResourceLocation.withDefaultNamespace("block/water_flow");

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
    }

    public static void register() {
        ABlocks.init();
        AItems.init();
        BlockEntityTypes.init();
        AFluids.init();
        FluidTypes.init();
        MenuTypes.init();
    }

    public static final ResourceLocation AUTO_ANVIL = id("auto_anvil");

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static class ABlocks {
        public static final Block AUTO_ANVIL = new AutoAnvilBlock(Block.Properties.ofFullCopy(Blocks.ANVIL));


        public static final LiquidBlock XP = new LiquidBlock(AFluids.XP, Block.Properties.of().mapColor(MapColor.WATER)
                .replaceable()
                .noCollission()
                .strength(100.0F)
                .pushReaction(PushReaction.DESTROY)
                .noLootTable()
                .liquid()
                .sound(SoundType.EMPTY)){};

        static {
            Registry.register(BuiltInRegistries.BLOCK,AutoAnvil.AUTO_ANVIL, AUTO_ANVIL);
            Registry.register(BuiltInRegistries.BLOCK,AutoAnvil.id("xp"),XP);
        }

        static void init() {

        }
    }

    public static class AItems {
        public static final BlockItem AUTO_ANVIL = new BlockItem(ABlocks.AUTO_ANVIL,new Item.Properties());
        public static final Item LEVEL_UPGRADE = new Item(new Item.Properties());
        public static final Item EFFICIENCY_UPGRADE = new Item(new Item.Properties());

        public static final Item XP_BUCKET = new BucketItem(AFluids.XP, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));


        static {
            Registry.register(BuiltInRegistries.ITEM,AutoAnvil.AUTO_ANVIL, AUTO_ANVIL);
            Registry.register(BuiltInRegistries.ITEM,AutoAnvil.id("level_upgrade"), LEVEL_UPGRADE);
            Registry.register(BuiltInRegistries.ITEM,AutoAnvil.id("efficiency_upgrade"), EFFICIENCY_UPGRADE);
            Registry.register(BuiltInRegistries.ITEM,AutoAnvil.id("xp_bucket"), XP_BUCKET);
        }

        public static void init() {

        }
    }

    public static class MenuTypes {
        public static final MenuType<AutoAnvilMenu> AUTO_ANVIL = new MenuType<AutoAnvilMenu>(AutoAnvilMenu::new, FeatureFlags.VANILLA_SET);

        static {
            Registry.register(BuiltInRegistries.MENU,AutoAnvil.AUTO_ANVIL, AUTO_ANVIL);
        }

        static void init() {

        }
    }

    public static class BlockEntityTypes {
        public static final BlockEntityType<AutoAnvilBlockEntity> AUTO_ANVIL = BlockEntityType.Builder.of((BlockPos blockPos, BlockState blockState) -> new AutoAnvilBlockEntity(blockPos, blockState),
                ABlocks.AUTO_ANVIL).build(null);

        static {
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,AutoAnvil.AUTO_ANVIL, AUTO_ANVIL);
        }

        public static void init() {

        }
    }

    public static class AFluids {
        public static FlowingFluid XP = Services.PLATFORM.createXPSourceFluid(new FluidProperties(() -> FluidTypes.XP,() -> AFluids.XP,() -> AFluids.FLOWING_XP));
        public static FlowingFluid FLOWING_XP = Services.PLATFORM.createXPFlowingFluid(
                new FluidProperties(() -> FluidTypes.XP,() -> AFluids.XP,() -> AFluids.FLOWING_XP));


        static {
            Registry.register(BuiltInRegistries.FLUID,AutoAnvil.id("xp"), XP);
            Registry.register(BuiltInRegistries.FLUID,AutoAnvil.id("xp_flowing"), FLOWING_XP);
        }

        static void init() {}
    }

    public static class FluidTypes {
        public static final CFluidType XP = new CFluidType(CFluidType.Properties.create()
                .density(1024).viscosity(1024)
                .sound(CSoundActions.BUCKET_FILL, Sounds.BUCKET_FILL_XP)
                .sound(CSoundActions.BUCKET_EMPTY, Sounds.BUCKET_EMPTY_XP));
        static {

        }

        static void init() {}
    }

    public static class Sounds {
        public static final SoundEvent BUCKET_FILL_XP = SoundEvents.BUCKET_FILL;
        public static final SoundEvent BUCKET_EMPTY_XP = SoundEvents.BUCKET_EMPTY;

        static {
        }

    }
}
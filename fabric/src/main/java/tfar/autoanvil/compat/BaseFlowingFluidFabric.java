package tfar.autoanvil.compat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;
import tfar.autoanvil.fluids.CFluidType;
import tfar.autoanvil.fluids.FluidProperties;

import java.util.function.Supplier;

public abstract class BaseFlowingFluidFabric extends FlowingFluid {
    private final Supplier<? extends CFluidType> fluidType;
    private final Supplier<? extends Fluid> flowing;
    private final Supplier<? extends Fluid> still;
    @Nullable
    private final Supplier<? extends Item> bucket;
    @Nullable
    private final Supplier<? extends LiquidBlock> block;
    private final int slopeFindDistance;
    private final int levelDecreasePerBlock;
    private final float explosionResistance;
    private final int tickRate;

    protected BaseFlowingFluidFabric(FluidProperties properties) {
        this.fluidType = properties.getFluidType();
        this.flowing = properties.getFlowing();
        this.still = properties.getStill();
        this.bucket = properties.getBucket();
        this.block = properties.getBlock();
        this.slopeFindDistance = properties.getSlopeFindDistance();
        this.levelDecreasePerBlock = properties.getLevelDecreasePerBlock();
        this.explosionResistance = properties.getExplosionResistance();
        this.tickRate = properties.getTickRate();
    }

    public CFluidType getFluidType() {
        return this.fluidType.get();
    }

    @Override
    protected boolean canConvertToSource(Level level) {
        return false;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor worldIn, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? worldIn.getBlockEntity(pos) : null;
        Block.dropResources(state, worldIn, pos, blockEntity);
    }

    @Override
    public Fluid getFlowing() {
        return flowing.get();
    }

    @Override
    public Fluid getSource() {
        return still.get();
    }


    @Override
    protected int getSlopeFindDistance(LevelReader worldIn) {
        return slopeFindDistance;
    }

    @Override
    protected int getDropOff(LevelReader worldIn) {
        return levelDecreasePerBlock;
    }

    @Override
    public Item getBucket() {
        return bucket != null ? bucket.get() : Items.AIR;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluidIn, Direction direction) {
        // Based on the water implementation, may need to be overriden for mod fluids that shouldn't behave like water.
        return direction == Direction.DOWN && !isSame(fluidIn);
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return tickRate;
    }

    @Override
    protected float getExplosionResistance() {
        return explosionResistance;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        if (block != null)
            return block.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
        return Blocks.AIR.defaultBlockState();
    }


    public static class Flowing extends BaseFlowingFluidFabric {
        public Flowing(FluidProperties properties) {
            super(properties);
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }


        @Override
        public boolean isSource(FluidState state) {
            return false;
        }

        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }


    }

    public static class Source extends BaseFlowingFluidFabric {
        public Source(FluidProperties properties) {
            super(properties);
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }

        public int getAmount(FluidState state) {
            return 8;
        }

    }
}

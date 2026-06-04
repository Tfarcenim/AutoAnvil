package tfar.autoanvil.fluids;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class FluidProperties {
    private Supplier<? extends CFluidType> fluidType;
    private Supplier<? extends Fluid> still;
    private Supplier<? extends Fluid> flowing;
    private Supplier<? extends Item> bucket;
    private Supplier<? extends LiquidBlock> block;
    private int slopeFindDistance = 4;
    private int levelDecreasePerBlock = 1;
    private float explosionResistance = 1;
    private int tickRate = 5;

    public FluidProperties(Supplier<? extends CFluidType> fluidType, Supplier<? extends Fluid> still, Supplier<? extends Fluid> flowing) {
        this.fluidType = fluidType;
        this.still = still;
        this.flowing = flowing;
    }

    public FluidProperties bucket(Supplier<? extends Item> bucket) {
        this.bucket = bucket;
        return this;
    }

    public FluidProperties block(Supplier<? extends LiquidBlock> block) {
        this.block = block;
        return this;
    }

    public FluidProperties slopeFindDistance(int slopeFindDistance) {
        this.slopeFindDistance = slopeFindDistance;
        return this;
    }

    public FluidProperties levelDecreasePerBlock(int levelDecreasePerBlock) {
        this.levelDecreasePerBlock = levelDecreasePerBlock;
        return this;
    }

    public FluidProperties explosionResistance(float explosionResistance) {
        this.explosionResistance = explosionResistance;
        return this;
    }

    public FluidProperties tickRate(int tickRate) {
        this.tickRate = tickRate;
        return this;
    }

    public float getExplosionResistance() {
        return explosionResistance;
    }

    public int getLevelDecreasePerBlock() {
        return levelDecreasePerBlock;
    }

    public int getSlopeFindDistance() {
        return slopeFindDistance;
    }

    public Supplier<? extends CFluidType> getFluidType() {
        return fluidType;
    }

    public Supplier<? extends LiquidBlock> getBlock() {
        return block;
    }

    public int getTickRate() {
        return tickRate;
    }

    public Supplier<? extends Fluid> getFlowing() {
        return flowing;
    }

    public Supplier<? extends Fluid> getStill() {
        return still;
    }

    public Supplier<? extends Item> getBucket() {
        return bucket;
    }
}

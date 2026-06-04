package tfar.autoanvil.fluids;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CFluidType {

    private String descriptionId;
    private final double motionScale;
    private final boolean canPushEntity;
    private final boolean canSwim;
    private final boolean canDrown;
    private final float fallDistanceModifier;
    private final boolean canExtinguish;
    private final boolean canConvertToSource;
    private final boolean supportsBoating;
    @Nullable
    private final PathType pathType, adjacentPathType;
    private final boolean canHydrate;
    private final int lightLevel;
    private final int density;
    private final int temperature;
    private final int viscosity;
    private final Rarity rarity;
    @Nullable
    private final DripstoneDripInfo dripInfo;

    private final Map<CSoundAction, SoundEvent> sounds;

    public CFluidType(Properties properties) {
        this.descriptionId = properties.descriptionId;
        this.motionScale = properties.motionScale;
        this.canPushEntity = properties.canPushEntity;
        this.canSwim = properties.canSwim;
        this.canDrown = properties.canDrown;
        this.fallDistanceModifier = properties.fallDistanceModifier;
        this.canExtinguish = properties.canExtinguish;
        this.canConvertToSource = properties.canConvertToSource;
        this.supportsBoating = properties.supportsBoating;
        this.pathType = properties.pathType;
        this.adjacentPathType = properties.adjacentPathType;
        this.sounds = ImmutableMap.copyOf(properties.sounds);
        this.canHydrate = properties.canHydrate;
        this.lightLevel = properties.lightLevel;
        this.density = properties.density;
        this.temperature = properties.temperature;
        this.viscosity = properties.viscosity;
        this.rarity = properties.rarity;
        this.dripInfo = properties.dripInfo;
    }

    /**
     * Returns the density of the fluid.
     *
     * <p>Note: This is an arbitrary number. Negative or zero values indicate
     * that the fluid is lighter than air. If not specified, the density is
     * approximately equivalent to the real-life density of water in {@code kg/m^3}.
     *
     * @return the density of the fluid
     */
    public int getDensity() {
        return this.density;
    }

    /**
     * Returns the temperature of the fluid.
     *
     * <p>Note: This is an arbitrary number. Higher temperature values indicate
     * that the fluid is hotter. If not specified, the temperature is approximately
     * equivalent to the real-life room temperature of water in {@code Kelvin}.
     *
     * @return the temperature of the fluid
     */
    public int getTemperature() {
        return this.temperature;
    }

    /**
     * Returns the viscosity, or thickness, of the fluid.
     *
     * <p>Note: This is an arbitrary number. The value should never be negative.
     * Higher viscosity values indicate that the fluid flows more slowly. If not
     * specified, the viscosity is approximately equivalent to the real-life
     * viscosity of water in {@code m/s^2}.
     *
     * @return the viscosity of the fluid
     */
    public int getViscosity() {
        return this.viscosity;
    }

    /**
     * Returns a sound to play when a certain action is performed. If no
     * sound is present, then the sound will be {@code null}.
     *
     * @param action the action being performed
     * @return the sound to play when performing the action
     */
    @Nullable
    public SoundEvent getSound(CSoundAction action) {
        return this.sounds.get(action);
    }

    public Map<CSoundAction, SoundEvent> sounds() {
        return this.sounds;
    }

    public static final class Properties {
        private String descriptionId;
        private double motionScale = 0.014D;
        private boolean canPushEntity = true;
        private boolean canSwim = true;
        private boolean canDrown = true;
        private float fallDistanceModifier = 0.5F;
        private boolean canExtinguish = false;
        private boolean canConvertToSource = false;
        private boolean supportsBoating = false;
        @Nullable
        private PathType pathType = PathType.WATER,
                adjacentPathType = PathType.WATER_BORDER;
        private final Map<CSoundAction, SoundEvent> sounds = new HashMap<>();
        private boolean canHydrate = false;
        private int lightLevel = 0,
                density = 1000,
                temperature = 300,
                viscosity = 1000;
        private Rarity rarity = Rarity.COMMON;
        @Nullable
        private DripstoneDripInfo dripInfo;

        private Properties() {}

        /**
         * Creates a new instance of the properties.
         *
         * @return the property holder instance
         */
        public static Properties create() {
            return new Properties();
        }

        /**
         * Sets the identifier representing the name of the fluid type.
         *
         * @param descriptionId the identifier representing the name of the fluid type
         * @return the property holder instance
         */
        public Properties descriptionId(String descriptionId) {
            this.descriptionId = descriptionId;
            return this;
        }

        /**
         * Sets how much the velocity of the fluid should be scaled by.
         *
         * @param motionScale a scalar to multiply to the fluid velocity
         * @return the property holder instance
         */
        public Properties motionScale(double motionScale) {
            this.motionScale = motionScale;
            return this;
        }

        /**
         * Sets whether the fluid can push an entity.
         *
         * @param canPushEntity if the fluid can push an entity
         * @return the property holder instance
         */
        public Properties canPushEntity(boolean canPushEntity) {
            this.canPushEntity = canPushEntity;
            return this;
        }

        /**
         * Sets whether the fluid can be swum in.
         *
         * @param canSwim if the fluid can be swum in
         * @return the property holder instance
         */
        public Properties canSwim(boolean canSwim) {
            this.canSwim = canSwim;
            return this;
        }

        /**
         * Sets whether the fluid can drown something.
         *
         * @param canDrown if the fluid can drown something
         * @return the property holder instance
         */
        public Properties canDrown(boolean canDrown) {
            this.canDrown = canDrown;
            return this;
        }

        /**
         * Sets how much the fluid should scale the damage done when hitting
         * the ground per tick.
         *
         * @param fallDistanceModifier a scalar to multiply to the fall damage
         * @return the property holder instance
         */
        public Properties fallDistanceModifier(float fallDistanceModifier) {
            this.fallDistanceModifier = fallDistanceModifier;
            return this;
        }

        /**
         * Sets whether the fluid can extinguish.
         *
         * @param canExtinguish if the fluid can extinguish
         * @return the property holder instance
         */
        public Properties canExtinguish(boolean canExtinguish) {
            this.canExtinguish = canExtinguish;
            return this;
        }

        /**
         * Sets whether the fluid can create a source.
         *
         * @param canConvertToSource if the fluid can create a source
         * @return the property holder instance
         */
        public Properties canConvertToSource(boolean canConvertToSource) {
            this.canConvertToSource = canConvertToSource;
            return this;
        }

        /**
         * Sets whether the fluid supports boating.
         *
         * @param supportsBoating if the fluid supports boating
         * @return the property holder instance
         */
        public Properties supportsBoating(boolean supportsBoating) {
            this.supportsBoating = supportsBoating;
            return this;
        }

        /**
         * Sets the path type of this fluid.
         *
         * @param pathType the path type of this fluid
         * @return the property holder instance
         */
        public Properties pathType(@Nullable PathType pathType) {
            this.pathType = pathType;
            return this;
        }

        /**
         * Sets the path type of the adjacent fluid. Path types with a negative
         * malus are not traversable. Pathfinding will favor paths consisting of
         * a lower malus.
         *
         * @param adjacentPathType the path type of this fluid
         * @return the property holder instance
         */
        public Properties adjacentPathType(@Nullable PathType adjacentPathType) {
            this.adjacentPathType = adjacentPathType;
            return this;
        }

        /**
         * Sets a sound to play when a certain action is performed.
         *
         * @param action the action being performed
         * @param sound  the sound to play when performing the action
         * @return the property holder instance
         */
        public Properties sound(CSoundAction action, SoundEvent sound) {
            this.sounds.put(action, sound);
            return this;
        }

        /**
         * Sets whether the fluid can hydrate.
         *
         * <p>Hydration is an arbitrary word which depends on the implementation.
         *
         * @param canHydrate if the fluid can hydrate
         * @return the property holder instance
         */
        public Properties canHydrate(boolean canHydrate) {
            this.canHydrate = canHydrate;
            return this;
        }

        /**
         * Sets the light level emitted by the fluid.
         *
         * @param lightLevel the light level emitted by the fluid
         * @return the property holder instance
         * @throws IllegalArgumentException if light level is not between [0,15]
         */
        public Properties lightLevel(int lightLevel) {
            if (lightLevel < 0 || lightLevel > 15)
                throw new IllegalArgumentException("The light level should be between [0,15].");
            this.lightLevel = lightLevel;
            return this;
        }

        /**
         * Sets the density of the fluid.
         *
         * @param density the density of the fluid
         * @return the property holder instance
         */
        public Properties density(int density) {
            this.density = density;
            return this;
        }

        /**
         * Sets the temperature of the fluid.
         *
         * @param temperature the temperature of the fluid
         * @return the property holder instance
         */
        public Properties temperature(int temperature) {
            this.temperature = temperature;
            return this;
        }

        /**
         * Sets the viscosity, or thickness, of the fluid.
         *
         * @param viscosity the viscosity of the fluid
         * @return the property holder instance
         * @throws IllegalArgumentException if viscosity is negative
         */
        public Properties viscosity(int viscosity) {
            if (viscosity < 0)
                throw new IllegalArgumentException("The viscosity should never be negative.");
            this.viscosity = viscosity;
            return this;
        }

        /**
         * Sets the rarity of the fluid.
         *
         * @param rarity the rarity of the fluid
         * @return the property holder instance
         */
        public Properties rarity(Rarity rarity) {
            this.rarity = rarity;
            return this;
        }

        /**
         * Allows this fluid to drip from Pointed Dripstone stalactites and fill cauldrons below.
         *
         * @param chance       the chance that the cauldron below will be filled every time the Pointed Dripstone is randomly ticked
         * @param dripParticle the particle that spawns randomly from the tip of the Pointed Dripstone when this fluid is above it
         * @param cauldron     the block the Pointed Dripstone should replace an empty cauldron with when it successfully tries to fill the cauldron
         * @param fillSound    the sound that plays when the Pointed Dripstone successfully tries to fill an empty cauldron. If null, no sound will play. Note that if your block class does not extend {@link CauldronBlock}, this sound will not play regardless.
         * @return the property holder instance
         */
        public Properties addDripstoneDripping(float chance, ParticleOptions dripParticle, Block cauldron, @Nullable SoundEvent fillSound) {
            if (fillSound != null) {
                this.sounds.put(CSoundActions.CAULDRON_DRIP, fillSound);
            }
            this.dripInfo = new DripstoneDripInfo(chance, dripParticle, cauldron);
            return this;
        }
    }

    /**
     * A record that holds some information to let a fluid drip from Pointed Dripstone stalactites and fill cauldrons below.
     *
     * @param chance         the chance that the cauldron below will be filled every time the Pointed Dripstone is randomly ticked. This number should be some value between 0.0 and 1.0
     * @param dripParticle   the particle that spawns randomly from the tip of the Pointed Dripstone when this fluid is above it
     * @param filledCauldron the block the Pointed Dripstone should replace an empty cauldron with when it successfully tries to fill the cauldron
     */
    public record DripstoneDripInfo(float chance, @Nullable ParticleOptions dripParticle, Block filledCauldron) {}
}

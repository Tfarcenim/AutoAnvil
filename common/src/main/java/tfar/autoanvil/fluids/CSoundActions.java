package tfar.autoanvil.fluids;

public interface CSoundActions {
    /**
     * When a bucket is being filled by a fluid.
     */
    CSoundAction BUCKET_FILL = CSoundAction.get("bucket_fill");

    /**
     * When a bucket is emptying a fluid.
     */
    CSoundAction BUCKET_EMPTY = CSoundAction.get("bucket_empty");

    /**
     * When the fluid is being vaporized.
     */
    CSoundAction FLUID_VAPORIZE = CSoundAction.get("fluid_vaporize");

    /**
     * When a Pointed Dripstone drips this fluid into an empty cauldron.
     */
    CSoundAction CAULDRON_DRIP = CSoundAction.get("cauldron_drip");
}

package tfar.autoanvil.inventory;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import tfar.autoanvil.util.DataComponentUtil;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class CFluidStack implements DataComponentHolder {

    private static final Logger LOGGER = LogUtils.getLogger();
    public static final CFluidStack EMPTY = new CFluidStack(null);
    private int amount;
    private final Fluid fluid;
    private final PatchedDataComponentMap components;

    public PatchedDataComponentMap getComponents() {
        return this.components;
    }

    public DataComponentPatch getComponentsPatch() {
        return !this.isEmpty() ? this.components.asPatch() : DataComponentPatch.EMPTY;
    }

    public CFluidStack(Holder<Fluid> fluid, int amount, DataComponentPatch patch) {
        this(fluid.value(), amount, PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, patch));
    }

    public CFluidStack(Holder<Fluid> fluid, int amount) {
        this(fluid.value(), amount);
    }

    public CFluidStack(Fluid fluid, int amount) {
        this(fluid, amount, new PatchedDataComponentMap(DataComponentMap.EMPTY));
    }

    private CFluidStack(Fluid fluid, int amount, PatchedDataComponentMap components) {
        this.fluid = fluid;
        this.amount = amount;
        this.components = components;
    }

    private CFluidStack(@Nullable Void unused) {
        this.fluid = null;
        this.components = new PatchedDataComponentMap(DataComponentMap.EMPTY);
    }


    /**
     * Tries to parse a fluid stack. Empty stacks cannot be parsed with this method.
     */
    public static Optional<CFluidStack> parse(HolderLookup.Provider lookupProvider, Tag tag) {
        return CODEC.parse(lookupProvider.createSerializationContext(NbtOps.INSTANCE), tag)
                .resultOrPartial(error -> LOGGER.error("Tried to load invalid fluid: '{}'", error));
    }

    /**
     * Tries to parse a fluid stack, defaulting to {@link #EMPTY} on parsing failure.
     */
    public static CFluidStack parseOptional(HolderLookup.Provider lookupProvider, CompoundTag tag) {
        return tag.isEmpty() ? EMPTY : parse(lookupProvider, tag).orElse(EMPTY);
    }

    public boolean isEmpty() {
        return this == EMPTY || this.fluid == Fluids.EMPTY || this.amount <= 0;
    }

    /**
     * Returns the fluid in this stack, or {@link Fluids#EMPTY} if this stack is empty.
     */
    public Fluid getFluid() {
        return this.isEmpty() ? Fluids.EMPTY : this.fluid;
    }

    public Holder<Fluid> getFluidHolder() {
        return this.getFluid().builtInRegistryHolder();
    }

    public boolean is(TagKey<Fluid> tag) {
        return this.getFluid().builtInRegistryHolder().is(tag);
    }

    public boolean is(Fluid fluid) {
        return this.getFluid() == fluid;
    }

    public boolean is(Predicate<Holder<Fluid>> holderPredicate) {
        return holderPredicate.test(this.getFluidHolder());
    }

    public boolean is(Holder<Fluid> holder) {
        return is(holder.value());
    }

    public boolean is(HolderSet<Fluid> holderSet) {
        return holderSet.contains(this.getFluidHolder());
    }

    /**
     * Saves this stack to a tag, directly writing the keys into the passed tag.
     *
     * @throws IllegalStateException if this stack is empty
     */
    public Tag save(HolderLookup.Provider lookupProvider, Tag prefix) {
        if (this.isEmpty()) {
            throw new IllegalStateException("Cannot encode empty FluidStack");
        } else {
            return DataComponentUtil.wrapEncodingExceptions(this, CODEC, lookupProvider, prefix);
        }
    }

    /**
     * Saves this stack to a new tag.
     *
     * @throws IllegalStateException if this stack is empty
     */
    public Tag save(HolderLookup.Provider lookupProvider) {
        if (this.isEmpty()) {
            throw new IllegalStateException("Cannot encode empty FluidStack");
        } else {
            return DataComponentUtil.wrapEncodingExceptions(this, CODEC, lookupProvider);
        }
    }

    /**
     * Saves this stack to a new tag. Empty stacks are supported and will be saved as an empty tag.
     */
    public Tag saveOptional(HolderLookup.Provider lookupProvider) {
        return this.isEmpty() ? new CompoundTag() : this.save(lookupProvider, new CompoundTag());
    }


    /**
     * Returns the amount of this stack.
     */
    public int getAmount() {
        return this.isEmpty() ? 0 : this.amount;
    }

    /**
     * Sets the amount of this stack.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Creates a copy of this fluid stack.
     */
    public CFluidStack copy() {
        if (this.isEmpty()) {
            return EMPTY;
        } else {
            return new CFluidStack(this.fluid, this.amount, this.components.copy());
        }
    }

    /**
     * Adds the given amount to this stack.
     */
    public void grow(int addedAmount) {
        this.setAmount(this.getAmount() + addedAmount);
    }

    /**
     * Removes the given amount from this stack.
     */
    public void shrink(int removedAmount) {
        this.grow(-removedAmount);
    }

    /**
     * Creates a copy of this fluid stack with the given amount.
     */
    public CFluidStack copyWithAmount(int amount) {
        if (this.isEmpty()) {
            return EMPTY;
        } else {
            CFluidStack fluidStack = this.copy();
            fluidStack.setAmount(amount);
            return fluidStack;
        }
    }

    /**
     * Checks if the two fluid stacks are equal. This checks the fluid, amount, and components.
     *
     * @return {@code true} if the two fluid stacks have equal fluid, amount, and components
     */
    public static boolean matches(CFluidStack first, CFluidStack second) {
        if (first == second) {
            return true;
        } else {
            return first.getAmount() == second.getAmount() && isSameFluidSameComponents(first, second);
        }
    }

    /**
     * Checks if the two fluid stacks have the same fluid. Ignores amount and components.
     *
     * @return {@code true} if the two fluid stacks have the same fluid
     */
    public static boolean isSameFluid(CFluidStack first, CFluidStack second) {
        return first.is(second.getFluid());
    }

    /**
     * Checks if the two fluid stacks have the same fluid and components. Ignores amount.
     *
     * @return {@code true} if the two fluid stacks have the same fluid and components
     */
    public static boolean isSameFluidSameComponents(CFluidStack first, CFluidStack second) {
        if (!first.is(second.getFluid())) {
            return false;
        } else {
            return first.isEmpty() && second.isEmpty() || Objects.equals(first.components, second.components);
        }
    }


    public static final Codec<Holder<Fluid>> FLUID_NON_EMPTY_CODEC = BuiltInRegistries.FLUID.holderByNameCodec().validate(holder -> {
        return holder.is(Fluids.EMPTY.builtInRegistryHolder()) ? DataResult.error(() -> {
            return "Fluid must not be minecraft:empty";
        }) : DataResult.success(holder);
    });
    /**
     * A standard codec for fluid stacks that does not accept empty stacks.
     */
    public static final Codec<CFluidStack> CODEC = Codec.lazyInitialized(
            () -> RecordCodecBuilder.create(
                    instance -> instance.group(
                                    FLUID_NON_EMPTY_CODEC.fieldOf("id").forGetter(CFluidStack::getFluidHolder),
                                    ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(CFluidStack::getAmount), // note: no .orElse(1) compared to ItemStack
                                    DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY)
                                            .forGetter(stack -> stack.components.asPatch()))
                            .apply(instance, CFluidStack::new)));

    /**
     * A standard codec for fluid stacks that accepts empty stacks, serializing them as {@code {}}.
     */
    public static final Codec<CFluidStack> OPTIONAL_CODEC = ExtraCodecs.optionalEmptyMap(CODEC)
            .xmap(optional -> optional.orElse(CFluidStack.EMPTY), stack -> stack.isEmpty() ? Optional.empty() : Optional.of(stack));
    /**
     * A stream codec for fluid stacks that accepts empty stacks.
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, CFluidStack> OPTIONAL_STREAM_CODEC = new StreamCodec<>() {
        private static final StreamCodec<RegistryFriendlyByteBuf, Holder<Fluid>> FLUID_STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.FLUID);

        @Override
        public CFluidStack decode(RegistryFriendlyByteBuf buf) {
            int amount = buf.readVarInt();
            if (amount <= 0) {
                return CFluidStack.EMPTY;
            } else {
                Holder<Fluid> holder = FLUID_STREAM_CODEC.decode(buf);
                DataComponentPatch patch = DataComponentPatch.STREAM_CODEC.decode(buf);
                return new CFluidStack(holder, amount, patch);
            }
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, CFluidStack stack) {
            if (stack.isEmpty()) {
                buf.writeVarInt(0);
            } else {
                buf.writeVarInt(stack.getAmount());
                FLUID_STREAM_CODEC.encode(buf, stack.getFluidHolder());
                DataComponentPatch.STREAM_CODEC.encode(buf, stack.components.asPatch());
            }
        }
    };
    /**
     * A stream codec for fluid stacks that does not accept empty stacks.
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, CFluidStack> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public CFluidStack decode(RegistryFriendlyByteBuf buf) {
            CFluidStack stack = CFluidStack.OPTIONAL_STREAM_CODEC.decode(buf);
            if (stack.isEmpty()) {
                throw new DecoderException("Empty FluidStack not allowed");
            } else {
                return stack;
            }
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, CFluidStack stack) {
            if (stack.isEmpty()) {
                throw new EncoderException("Empty FluidStack not allowed");
            } else {
                CFluidStack.OPTIONAL_STREAM_CODEC.encode(buf, stack);
            }
        }
    };
}

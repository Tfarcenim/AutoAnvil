package tfar.autoanvil;

import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.SoundAction;
import net.neoforged.neoforge.fluids.FluidType;
import tfar.autoanvil.fluids.CFluidType;
import tfar.autoanvil.fluids.CSoundAction;
import tfar.autoanvil.fluids.FluidProperties;

import java.util.HashMap;
import java.util.Map;

public class NeoforgeFluidTypes {

    private static final Map<CFluidType, FluidType> LOOKUP = new HashMap<>();

    public static final FluidType XP = convertToNeoforge(AutoAnvil.FluidTypes.XP);
    public static FluidType convertToNeoforge(CFluidType type) {
        FluidType fluidType = new FluidType(convertProperties(type));
        LOOKUP.put(type, fluidType);
        return fluidType;
    }

    public static FluidType.Properties convertProperties(CFluidType type) {
        FluidType.Properties p = FluidType.Properties.create();

        p.density(type.getDensity()).viscosity(type.getViscosity());

        Map<CSoundAction, SoundEvent> sounds = type.sounds();

        for (Map.Entry<CSoundAction, SoundEvent> entry : sounds.entrySet()) {
            CSoundAction action = entry.getKey();
            SoundEvent sound = entry.getValue();
            p.sound(convertSoundAction(action), sound);
        }

        return p;
    }

    public static FluidType lookup(CFluidType fluidType) {
        return LOOKUP.get(fluidType);
    }

    public static SoundAction convertSoundAction(CSoundAction action) {
        return SoundAction.get(action.name());
    }

}

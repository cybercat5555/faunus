package cybercat5555.faunus.core;

import cybercat5555.faunus.Faunus;
import cybercat5555.faunus.core.effect.*;
import cybercat5555.faunus.util.FaunusID;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static cybercat5555.faunus.Faunus.MODID;

public final class EffectStatusRegistry {

    public static StatusEffect STINKY_EFFECT = new StinkyEffect();
    public static Identifier STINKY_ID = FaunusID.content("rancid");

    public static StatusEffect RANCID_EFFECT = new RancidEffect();
    public static Identifier RANCID_ID = FaunusID.content("stinky");

    public static StatusEffect LEECHING_EFFECT = new LeechingEffect();
    public static Identifier LEECHING_ID = FaunusID.content("leeching");

    public static StatusEffect STOP_HEALING_EFFECT = new StopHealingEffect();
    public static Identifier STOP_HEALING_ID = FaunusID.content("stop_healing");

    public static StatusEffect CLEAR_EFFECTS_EFFECT = new ClearEffectsEffect();
    public static Identifier CLEAR_EFFECTS_ID = FaunusID.content("clear_effects");


    public static void init() {
        Faunus.LOG.info("Registering effect status for " + MODID);

        Registry.register(Registries.STATUS_EFFECT, RANCID_ID, RANCID_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, STINKY_ID, STINKY_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, LEECHING_ID, LEECHING_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, STOP_HEALING_ID, STOP_HEALING_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, CLEAR_EFFECTS_ID, CLEAR_EFFECTS_EFFECT);
    }
}

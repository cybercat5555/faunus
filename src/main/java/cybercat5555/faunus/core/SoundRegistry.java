package cybercat5555.faunus.core;

import cybercat5555.faunus.Faunus;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundRegistry {
    public static final SoundEvent QUETZAL_IDLE = register("entity.quetzal.idle");

    public static final SoundEvent CAPUCHIN_ANGRY = register("entity.capuchin.angry");
    public static final SoundEvent CAPUCHIN_HURT = register("entity.capuchin.hurt");
    public static final SoundEvent CAPUCHIN_IDLE = register("entity.capuchin.idle");
    public static final SoundEvent CAPUCHIN_TAMED_IDLE = register("entity.capuchin.tamed_idle");

    public static final SoundEvent TROPICAL_BIRD_AMBIANCE = register("entity.tropical_bird.ambiance");
    public static final SoundEvent TROPICAL_BIRD_HURT = register("entity.tropical_bird.hurt");

    public static final SoundEvent TAPIR_DEATH = register("entity.tapir.death");
    public static final SoundEvent TAPIR_HURT = register("entity.tapir.hurt");
    public static final SoundEvent TAPIR_IDLE = register("entity.tapir.idle");

    public static final SoundEvent YACARE_BABY = register("entity.yacare.baby");
    public static final SoundEvent YACARE_DEATH = register("entity.yacare.death");
    public static final SoundEvent YACARE_HURT = register("entity.yacare.hurt");
    public static final SoundEvent YACARE_IDLE = register("entity.yacare.idle");


    /**
     * Register a sound event with the given name.
     * @param name The name of the sound event.
     * @return The sound event.
     */
    private static SoundEvent register(String name) {
        Identifier id = Identifier.of(Faunus.MODID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void init() {
        Faunus.LOG.info("Registering sounds for " + Faunus.MODID);
    }
}

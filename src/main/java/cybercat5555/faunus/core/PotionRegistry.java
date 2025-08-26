package cybercat5555.faunus.core;

import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import static cybercat5555.faunus.Faunus.LOG;
import static cybercat5555.faunus.Faunus.MODID;


public class PotionRegistry {
    public static final Potion SWAMP_CRAWLER = registerPotion("swamp_crawler", new Potion(
            new StatusEffectInstance(StatusEffects.RESISTANCE, 1200),
            new StatusEffectInstance(StatusEffects.WATER_BREATHING, 1200),
            new StatusEffectInstance(StatusEffects.LUCK, 1200),
            new StatusEffectInstance(StatusEffects.HUNGER, 1200),
            new StatusEffectInstance(StatusEffects.WEAKNESS, 1200)));

    public static final Potion RANCID = registerPotion("rancid", new Potion(
            new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(EffectStatusRegistry.RANCID_EFFECT), 1200)));

    public static final Potion STINKY = registerPotion("stinky", new Potion(
            new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(EffectStatusRegistry.STINKY_EFFECT), 1200)));

    public static final Potion LEECHING = registerPotion("leeching", new Potion(
            new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(EffectStatusRegistry.STOP_HEALING_EFFECT), 40, 1, false, false, false)));


    public static Potion registerPotion(String name, Potion potion) {
        return Registry.register(Registries.POTION, Identifier.of(MODID, name), potion);
    }

    public static void registerRecipes() {
        LOG.info("Registering potion recipes for " + MODID);

        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(Potions.AWKWARD, ItemRegistry.HOATZIN_FEATHER, RegistryEntry.of(RANCID));
            builder.registerPotionRecipe(Potions.AWKWARD, ItemRegistry.BOTTLED_STINK, RegistryEntry.of(STINKY));
            builder.registerPotionRecipe(Potions.AWKWARD, ItemRegistry.BLUE_CRAYFISH, RegistryEntry.of(SWAMP_CRAWLER));
            builder.registerPotionRecipe(Potions.AWKWARD, ItemRegistry.BOTTLED_LEECH, RegistryEntry.of(LEECHING));
        });

    }

    public static void init() {
        LOG.info("Registering potions for " + MODID);
        registerRecipes();
    }
}

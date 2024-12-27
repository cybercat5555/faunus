package cybercat5555.faunus.common.tags;

import cybercat5555.faunus.Faunus;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class FaunusBiomeTags {
    public static final TagKey<Biome> SPAWNS_ARAPAIMA = FaunusBiomeTags.of("spawns_arapaima");
    public static final TagKey<Biome> SPAWNS_CAPUCHIN = FaunusBiomeTags.of("spawns_capuchin");
    public static final TagKey<Biome> SPAWNS_CONSTRICTOR = FaunusBiomeTags.of("spawns_constrictor");
    public static final TagKey<Biome> SPAWNS_CRAYFISH = FaunusBiomeTags.of("spawns_crayfish");
    public static final TagKey<Biome> SPAWNS_HOATZIN = FaunusBiomeTags.of("spawns_hoatzin");
    public static final TagKey<Biome> SPAWNS_IGUANA = FaunusBiomeTags.of("spawns_iguana");
    public static final TagKey<Biome> SPAWNS_LEECH = FaunusBiomeTags.of("spawns_leech");
    public static final TagKey<Biome> SPAWNS_PIRANHA = FaunusBiomeTags.of("spawns_piranha");
    public static final TagKey<Biome> SPAWNS_QUETZAL = FaunusBiomeTags.of("spawns_quetzal");
    public static final TagKey<Biome> SPAWNS_SNAPPING_TURTLE = FaunusBiomeTags.of("spawns_snapping_turtle");
    public static final TagKey<Biome> SPAWNS_SONGBIRD = FaunusBiomeTags.of("spawns_songbird");
    public static final TagKey<Biome> SPAWNS_TAPIR = FaunusBiomeTags.of("spawns_tapir");
    public static final TagKey<Biome> SPAWNS_TARANTULA = FaunusBiomeTags.of("spawns_tarantula");
    public static final TagKey<Biome> SPAWNS_YACARE = FaunusBiomeTags.of("spawns_yacare");
    public static final TagKey<Biome> SPAWNS_YACARE_MANEATER = FaunusBiomeTags.of("spawns_yacare_maneater");

    // TODO: changes to c:is_swamp in 1.21
    public static final TagKey<Biome> SWAMP = FaunusBiomeTags.of(Identifier.of("c", "swamp"));

    @SuppressWarnings("UnnecessaryReturnStatement")
    private FaunusBiomeTags() {
        return;
    }

    private static TagKey<Biome> of(String path) {
        return FaunusBiomeTags.of(Identifier.of(Faunus.MODID, path));
    }

    private static TagKey<Biome> of(Identifier id) {
        return TagKey.of(RegistryKeys.BIOME, id);
    }
}

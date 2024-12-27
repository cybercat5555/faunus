package cybercat5555.faunus.common.config;

import com.mojang.datafixers.util.Pair;
import cybercat5555.faunus.Faunus;
import cybercat5555.faunus.util.ConfigRegistry;
import cybercat5555.faunus.util.SimpleConfig;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class MobSpawningConfig {
    public static SimpleConfig config;
    private static ConfigRegistry configRegistry;

    public static int ARAPAIMA_SPAWN_WEIGHT, ARAPAIMA_SPAWN_MIN_GROUP, ARAPAIMA_SPAWN_MAX_GROUP;
    public static int CAPUCHIN_SPAWN_WEIGHT, CAPUCHIN_SPAWN_MIN_GROUP, CAPUCHIN_SPAWN_MAX_GROUP;
    public static int CONSTRICTOR_SPAWN_WEIGHT, CONSTRICTOR_SPAWN_MIN_GROUP, CONSTRICTOR_SPAWN_MAX_GROUP;
    public static int CRAYFISH_SPAWN_WEIGHT, CRAYFISH_SPAWN_MIN_GROUP, CRAYFISH_SPAWN_MAX_GROUP;
    public static int HOATZIN_SPAWN_WEIGHT, HOATZIN_SPAWN_MIN_GROUP, HOATZIN_SPAWN_MAX_GROUP;
    public static int IGUANA_SPAWN_WEIGHT, IGUANA_SPAWN_MIN_GROUP, IGUANA_SPAWN_MAX_GROUP;
    public static int LEECH_SPAWN_WEIGHT, LEECH_SPAWN_MIN_GROUP, LEECH_SPAWN_MAX_GROUP;
    public static int PIRANHA_SPAWN_WEIGHT, PIRANHA_SPAWN_MIN_GROUP, PIRANHA_SPAWN_MAX_GROUP;
    public static int QUETZAL_SPAWN_WEIGHT, QUETZAL_SPAWN_MIN_GROUP, QUETZAL_SPAWN_MAX_GROUP;
    public static int SNAPPING_TURTLE_SPAWN_WEIGHT, SNAPPING_TURTLE_SPAWN_MIN_GROUP, SNAPPING_TURTLE_SPAWN_MAX_GROUP;
    public static int SONGBIRD_SPAWN_WEIGHT, SONGBIRD_SPAWN_MIN_GROUP, SONGBIRD_SPAWN_MAX_GROUP;
    public static int TAPIR_SPAWN_WEIGHT, TAPIR_SPAWN_MIN_GROUP, TAPIR_SPAWN_MAX_GROUP;
    public static int TARANTULA_SPAWN_WEIGHT, TARANTULA_SPAWN_MIN_GROUP, TARANTULA_SPAWN_MAX_GROUP;
    public static int YACARE_SPAWN_WEIGHT, YACARE_SPAWN_MIN_GROUP, YACARE_SPAWN_MAX_GROUP;
    public static int YACARE_MANEATER_SPAWN_WEIGHT, YACARE_MANEATER_MIN_GROUP, YACARE_MANEATER_MAX_GROUP;


    public static void init(Path configPath) {
        configRegistry = new ConfigRegistry();
        createConfig();

        config = SimpleConfig.of(configPath + FileSystems.getDefault().getSeparator() + Faunus.MODID + "-mob-spawning-config").provider(configRegistry).request();
        assignConfig();
    }


    private static void createConfig() {
        configRegistry.addComment("Mob Spawning Config");

        configRegistry.addComment("Arapaima");
        configRegistry.addPairData(new Pair<>("arapaima_spawn_weight", 5), "Arapaima spawn weight");
        configRegistry.addPairData(new Pair<>("arapaima_spawn_min_group", 1), "Arapaima spawn min group");
        configRegistry.addPairData(new Pair<>("arapaima_spawn_max_group", 3), "Arapaima spawn max group");

        configRegistry.addComment("Capuchin");
        configRegistry.addPairData(new Pair<>("capuchin_spawn_weight", 5), "Capuchin spawn weight");
        configRegistry.addPairData(new Pair<>("capuchin_spawn_min_group", 1), "Capuchin spawn min group");
        configRegistry.addPairData(new Pair<>("capuchin_spawn_max_group", 3), "Capuchin spawn max group");

        configRegistry.addComment("Constrictor");
        configRegistry.addPairData(new Pair<>("constrictor_spawn_weight", 5), "Constrictor spawn weight");
        configRegistry.addPairData(new Pair<>("constrictor_spawn_min_group", 1), "Constrictor spawn min group");
        configRegistry.addPairData(new Pair<>("constrictor_spawn_max_group", 3), "Constrictor spawn max group");

        configRegistry.addComment("Crayfish");
        configRegistry.addPairData(new Pair<>("crayfish_spawn_weight", 15), "Crayfish spawn weight");
        configRegistry.addPairData(new Pair<>("crayfish_spawn_min_group", 1), "Crayfish spawn min group");
        configRegistry.addPairData(new Pair<>("crayfish_spawn_max_group", 3), "Crayfish spawn max group");

        configRegistry.addComment("Hoatzin");
        configRegistry.addPairData(new Pair<>("hoatzin_spawn_weight", 25), "Hoatzin spawn weight");
        configRegistry.addPairData(new Pair<>("hoatzin_spawn_min_group", 1), "Hoatzin spawn min group");
        configRegistry.addPairData(new Pair<>("hoatzin_spawn_max_group", 3), "Hoatzin spawn max group");

        configRegistry.addComment("Iguana");
        configRegistry.addPairData(new Pair<>("iguana_spawn_weight", 25), "Iguana spawn weight");
        configRegistry.addPairData(new Pair<>("iguana_spawn_min_group", 1), "Iguana spawn min group");
        configRegistry.addPairData(new Pair<>("iguana_spawn_max_group", 3), "Iguana spawn max group");

        configRegistry.addComment("Leech");
        configRegistry.addPairData(new Pair<>("leech_spawn_weight", 15), "Leech spawn weight");
        configRegistry.addPairData(new Pair<>("leech_spawn_min_group", 1), "Leech spawn min group");
        configRegistry.addPairData(new Pair<>("leech_spawn_max_group", 3), "Leech spawn max group");

        configRegistry.addComment("Piranha");
        configRegistry.addPairData(new Pair<>("piranha_spawn_weight", 10), "Piranha spawn weight");
        configRegistry.addPairData(new Pair<>("piranha_spawn_min_group", 1), "Piranha spawn min group");
        configRegistry.addPairData(new Pair<>("piranha_spawn_max_group", 3), "Piranha spawn max group");

        configRegistry.addComment("Quetzal");
        configRegistry.addPairData(new Pair<>("quetzal_spawn_weight", 25), "Quetzal spawn weight");
        configRegistry.addPairData(new Pair<>("quetzal_spawn_min_group", 1), "Quetzal spawn min group");
        configRegistry.addPairData(new Pair<>("quetzal_spawn_max_group", 3), "Quetzal spawn max group");

        configRegistry.addComment("Snapping Turtle");
        configRegistry.addPairData(new Pair<>("snapping_turtle_spawn_weight", 15), "Snapping Turtle spawn weight");
        configRegistry.addPairData(new Pair<>("snapping_turtle_spawn_min_group", 1), "Snapping Turtle spawn min group");
        configRegistry.addPairData(new Pair<>("snapping_turtle_spawn_max_group", 3), "Snapping Turtle spawn max group");

        configRegistry.addComment("Songbird");
        configRegistry.addPairData(new Pair<>("songbird_spawn_weight", 25), "Songbird spawn weight");
        configRegistry.addPairData(new Pair<>("songbird_spawn_min_group", 1), "Songbird spawn min group");
        configRegistry.addPairData(new Pair<>("songbird_spawn_max_group", 3), "Songbird spawn max group");

        configRegistry.addComment("Tapir");
        configRegistry.addPairData(new Pair<>("tapir_spawn_weight", 10), "Tapir spawn weight");
        configRegistry.addPairData(new Pair<>("tapir_spawn_min_group", 1), "Tapir spawn min group");
        configRegistry.addPairData(new Pair<>("tapir_spawn_max_group", 3), "Tapir spawn max group");

        configRegistry.addComment("Tarantula");
        configRegistry.addPairData(new Pair<>("tarantula_spawn_weight", 5), "Tarantula spawn weight");
        configRegistry.addPairData(new Pair<>("tarantula_spawn_min_group", 1), "Tarantula spawn min group");
        configRegistry.addPairData(new Pair<>("tarantula_spawn_max_group", 3), "Tarantula spawn max group");

        configRegistry.addComment("Yacare");
        configRegistry.addPairData(new Pair<>("yacare_spawn_weight", 10), "Yacare spawn weight");
        configRegistry.addPairData(new Pair<>("yacare_spawn_min_group", 1), "Yacare spawn min group");
        configRegistry.addPairData(new Pair<>("yacare_spawn_max_group", 3), "Yacare spawn max group");

        configRegistry.addComment("Yacare Maneater");
        configRegistry.addPairData(new Pair<>("yacare_maneater_spawn_weight", 5), "Yacare Maneater spawn weight");
        configRegistry.addPairData(new Pair<>("yacare_maneater_spawn_min_group", 1), "Yacare Maneater spawn min group");
        configRegistry.addPairData(new Pair<>("yacare_maneater_spawn_max_group", 3), "Yacare Maneater spawn max group");
    }

    private static void assignConfig() {
        ARAPAIMA_SPAWN_WEIGHT = Integer.parseInt(config.getOrDefault("arapaima_spawn_weight", "0"));
        ARAPAIMA_SPAWN_MIN_GROUP = Integer.parseInt(config.getOrDefault("arapaima_spawn_min_group", "0"));
        ARAPAIMA_SPAWN_MAX_GROUP = Integer.parseInt(config.getOrDefault("arapaima_spawn_max_group", "0"));

        CAPUCHIN_SPAWN_WEIGHT = Integer.parseInt(config.getOrDefault("capuchin_spawn_weight", "0"));
        CAPUCHIN_SPAWN_MIN_GROUP = Integer.parseInt(config.getOrDefault("capuchin_spawn_min_group", "0"));
        CAPUCHIN_SPAWN_MAX_GROUP = Integer.parseInt(config.getOrDefault("capuchin_spawn_max_group", "0"));

        CONSTRICTOR_SPAWN_WEIGHT = Integer.parseInt(config.getOrDefault("constrictor_spawn_weight", "0"));
        CONSTRICTOR_SPAWN_MIN_GROUP = Integer.parseInt(config.getOrDefault("constrictor_spawn_min_group", "0"));
        CONSTRICTOR_SPAWN_MAX_GROUP = Integer.parseInt(config.getOrDefault("constrictor_spawn_max_group", "0"));

        CRAYFISH_SPAWN_WEIGHT = Integer.parseInt(config.getOrDefault("crayfish_spawn_weight", "0"));
        CRAYFISH_SPAWN_MIN_GROUP = Integer.parseInt(config.getOrDefault("crayfish_spawn_min_group", "0"));
        CRAYFISH_SPAWN_MAX_GROUP = Integer.parseInt(config.getOrDefault("crayfish_spawn_max_group", "0"));

        HOATZIN_SPAWN_WEIGHT = Integer.parseInt(config.getOrDefault("hoatzin_spawn_weight", "0"));
        HOATZIN_SPAWN_MIN_GROUP = Integer.parseInt(config.getOrDefault("hoatzin_spawn_min_group", "0"));
        HOATZIN_SPAWN_MAX_GROUP = Integer.parseInt(config.getOrDefault("hoatzin_spawn_max_group", "0"));

        IGUANA_SPAWN_WEIGHT = Integer.parseInt(config.getOrDefault("iguana_spawn_weight", "0"));
        IGUANA_SPAWN_MIN_GROUP = Integer.parseInt(config.getOrDefault("iguana_spawn_min_group", "0"));
        IGUANA_SPAWN_MAX_GROUP = Integer.parseInt(config.getOrDefault("iguana_spawn_max_group", "0"));

        LEECH_SPAWN_WEIGHT = Integer.parseInt(config.getOrDefault("leech_spawn_weight", "0"));
        LEECH_SPAWN_MIN_GROUP = Integer.parseInt(config.getOrDefault("leech_spawn_min_group", "0"));
        LEECH_SPAWN_MAX_GROUP = Integer.parseInt(config.getOrDefault("leech_spawn_max_group", "0"));

        PIRANHA_SPAWN_WEIGHT = Integer.parseInt(config.getOrDefault("piranha_spawn_weight", "0"));
        PIRANHA_SPAWN_MIN_GROUP = Integer.parseInt(config.getOrDefault("piranha_spawn_min_group", "0"));
        PIRANHA_SPAWN_MAX_GROUP = Integer.parseInt(config.getOrDefault("piranha_spawn_max_group", "0"));

        QUETZAL_SPAWN_WEIGHT = Integer.parseInt(config.getOrDefault("quetzal_spawn_weight", "0"));
        QUETZAL_SPAWN_MIN_GROUP = Integer.parseInt(config.getOrDefault("quetzal_spawn_min_group", "0"));
        QUETZAL_SPAWN_MAX_GROUP = Integer.parseInt(config.getOrDefault("quetzal_spawn_max_group", "0"));

        SNAPPING_TURTLE_SPAWN_WEIGHT = Integer.parseInt(config.getOrDefault("snapping_turtle_spawn_weight", "0"));
        SNAPPING_TURTLE_SPAWN_MIN_GROUP = Integer.parseInt(config.getOrDefault("snapping_turtle_spawn_min_group", "0"));
        SNAPPING_TURTLE_SPAWN_MAX_GROUP = Integer.parseInt(config.getOrDefault("snapping_turtle_spawn_max_group", "0"));

        SONGBIRD_SPAWN_WEIGHT = Integer.parseInt(config.getOrDefault("songbird_spawn_weight", "0"));
        SONGBIRD_SPAWN_MIN_GROUP = Integer.parseInt(config.getOrDefault("songbird_spawn_min_group", "0"));
        SONGBIRD_SPAWN_MAX_GROUP = Integer.parseInt(config.getOrDefault("songbird_spawn_max_group", "0"));

        TAPIR_SPAWN_WEIGHT = Integer.parseInt(config.getOrDefault("tapir_spawn_weight", "0"));
        TAPIR_SPAWN_MIN_GROUP = Integer.parseInt(config.getOrDefault("tapir_spawn_min_group", "0"));
        TAPIR_SPAWN_MAX_GROUP = Integer.parseInt(config.getOrDefault("tapir_spawn_max_group", "0"));

        TARANTULA_SPAWN_WEIGHT = Integer.parseInt(config.getOrDefault("tarantula_spawn_weight", "0"));
        TARANTULA_SPAWN_MIN_GROUP = Integer.parseInt(config.getOrDefault("tarantula_spawn_min_group", "0"));
        TARANTULA_SPAWN_MAX_GROUP = Integer.parseInt(config.getOrDefault("tarantula_spawn_max_group", "0"));

        YACARE_SPAWN_WEIGHT = Integer.parseInt(config.getOrDefault("yacare_spawn_weight", "0"));
        YACARE_SPAWN_MIN_GROUP = Integer.parseInt(config.getOrDefault("yacare_spawn_min_group", "0"));
        YACARE_SPAWN_MAX_GROUP = Integer.parseInt(config.getOrDefault("yacare_spawn_max_group", "0"));

        YACARE_MANEATER_SPAWN_WEIGHT = Integer.parseInt(config.getOrDefault("yacare_maneater_spawn_weight", "0"));
        YACARE_MANEATER_MIN_GROUP = Integer.parseInt(config.getOrDefault("yacare_maneater_spawn_min_group", "0"));
        YACARE_MANEATER_MAX_GROUP = Integer.parseInt(config.getOrDefault("yacare_maneater_spawn_max_group", "0"));
    }
}

package cybercat5555.faunus.core;

import cybercat5555.faunus.Faunus;
import cybercat5555.faunus.core.entity.livingEntity.variant.CrayfishVariant;
import cybercat5555.faunus.core.item.BottledLeech;
import cybercat5555.faunus.core.item.Crayfish;
import cybercat5555.faunus.core.item.QuetzalFeatherTrinket;
import cybercat5555.faunus.util.FaunusID;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;

public final class ItemRegistry {
    private ItemRegistry() {
    }



    // Iguana
    public static final Item IGUANA_SPAWN_EGG = new SpawnEggItem(EntityRegistry.IGUANA, 0x4A8B4B, 0x3D3E3A, new Item.Settings());
    public static final Item IGUANA_RAW_TAIL = new Item(new Item.Settings().food(FoodRegistry.IGUANA_RAW_TAIL));
    public static final Item IGUANA_TAIL_COOKED = new Item(new Item.Settings().food(FoodRegistry.IGUANA_COOKED_TAIL));
    public static final Item IGUANA_RAW_MEAT = new Item(new Item.Settings().food(FoodRegistry.IGUANA_RAW_MEAT));
    public static final Item IGUANA_COOKED_MEAT = new Item(new Item.Settings().food(FoodRegistry.IGUANA_COOKED_MEAT));

    // Songbird
    public static final Item SONGBIRD_SPAWN_EGG = new SpawnEggItem(EntityRegistry.SONGBIRD, 0xeeeeee, 0xaaaaaa, new Item.Settings());

    // Capuchin
    public static final Item CAPUCHIN_SPAWN_EGG = new SpawnEggItem(EntityRegistry.CAPUCHIN, 0x2F2721, 0xE8E1D4, new Item.Settings());

    // Tapir
    public static final Item TAPIR_SPAWN_EGG = new SpawnEggItem(EntityRegistry.TAPIR, 0xa3D2F29, 0xB0874F, new Item.Settings());
    public static final Item TAPIR_MEAT = new Item(new Item.Settings().food(FoodRegistry.TAPIR_MEAT));
    public static final Item COOKED_TAPIR_MEAT = new Item(new Item.Settings().food(FoodRegistry.COOKED_TAPIR_MEAT));
    public static final Item BOTTLED_STINK = new Item(new Item.Settings());


    // Constrictor
    public static final Item CONSTRICTOR_SPAWN_EGG = new SpawnEggItem(EntityRegistry.CONSTRICTOR, 0x30a050, 0x101010, new Item.Settings());

    // Quetzal
    public static final Item QUETZAL_SPAWN_EGG = new SpawnEggItem(EntityRegistry.QUETZAL, 0x00A55F, 0x940E0E, new Item.Settings());
    public static final Item QUETZAL_FEATHER = new Item(new Item.Settings());
    public static final Item QUETZAL_FEATHER_TRINKET = new QuetzalFeatherTrinket(new Item.Settings().maxCount(1));


    // Hoatzin
    public static final Item HOATZIN_SPAWN_EGG = new SpawnEggItem(EntityRegistry.HOATZIN, 0xA85537, 0xEECFAA, new Item.Settings());
    public static final Item HOATZIN_FEATHER = new Item(new Item.Settings());

    // Piranha
    public static final Item PIRANHA_SPAWN_EGG = new SpawnEggItem(EntityRegistry.PIRANHA, 0x5B5B5B, 0xC61F17, new Item.Settings());
    public static final Item PIRANHA = new Item(new Item.Settings().food(FoodRegistry.PIRANHA));
    public static final Item COOKED_PIRANHA = new Item(new Item.Settings().food(FoodRegistry.COOKED_PIRANHA));
    public static final Item PIRANHA_BUCKET = new EntityBucketItem(EntityRegistry.PIRANHA, Fluids.WATER, SoundEvents.ITEM_BUCKET_EMPTY_FISH, new Item.Settings().recipeRemainder(Items.BUCKET));

    // Arapaima
    public static final Item ARAPAIMA_SPAWN_EGG = new SpawnEggItem(EntityRegistry.ARAPAIMA, 0x7F7A5C, 0xB13030, new Item.Settings());
    public static final Item ARAPAIMA = new Item(new Item.Settings().food(FoodRegistry.ARAPAIMA));
    public static final Item ARAPAIMA_EGG = new BlockItem(BlockRegistry.ARAPAIMA_EGG, new Item.Settings());
    public static final Item COOKED_ARAPAIMA = new Item(new Item.Settings().food(FoodRegistry.COOKED_ARAPAIMA));
    public static final Item ARAPAIMA_SCALE = new Item(new Item.Settings().rarity(Rarity.RARE));
    public static final Item ARAPAIMA_BUCKET = new EntityBucketItem(EntityRegistry.ARAPAIMA, Fluids.WATER, SoundEvents.ITEM_BUCKET_EMPTY_FISH, new Item.Settings().recipeRemainder(Items.BUCKET));

    // Snapping Turtle
    public static final Item SNAPPING_TURTLE_SPAWN_EGG = new SpawnEggItem(EntityRegistry.SNAPPING_TURTLE, 0x2C2A1D, 0x645C51, new Item.Settings());

    // Crayfish
    public static final Item CRAYFISH_SPAWN_EGG = new SpawnEggItem(EntityRegistry.CRAYFISH, 0x6B6130, 0xCD572F, new Item.Settings());
    public static final Item CRAYFISH = new Crayfish(new Item.Settings().food(FoodRegistry.CRAYFISH), CrayfishVariant.DEFAULT);
    public static final Item BLUE_CRAYFISH = new Crayfish(new Item.Settings().food(FoodRegistry.BLUE_CRAYFISH), CrayfishVariant.BLUE);
    public static final Item COOKED_CRAYFISH = new Item(new Item.Settings().food(FoodRegistry.COOKED_CRAYFISH));


    // Leech
    public static final Item LEECH_SPAWN_EGG = new SpawnEggItem(EntityRegistry.LEECH, 0x202314, 0xC67824, new Item.Settings());
    public static final Item BOTTLED_LEECH = new BottledLeech(new Item.Settings());


    // Yacare
    public static final Item YACARE_SPAWN_EGG = new SpawnEggItem(EntityRegistry.YACARE, 0xA39263, 0x483E2C, new Item.Settings());
    public static final Item COOKED_YACARE = new Item(new Item.Settings().food(FoodRegistry.COOKED_YACARE));
    public static final Item RAW_YACARE = new Item(new Item.Settings().food(FoodRegistry.RAW_YACARE));
    public static final Item YACARE_EGG = new BlockItem(BlockRegistry.YACARE_EGG, new Item.Settings());


    /**
     * Item group for Faunus mod items
     */
    private static final ItemGroup GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(QUETZAL_FEATHER))
            .displayName(Text.translatable("itemGroup.faunus.item_group"))
            .entries((context, entries) ->
            {
                // misc
                entries.add(QUETZAL_FEATHER);
                entries.add(QUETZAL_FEATHER_TRINKET);
                entries.add(HOATZIN_FEATHER);
                entries.add(ARAPAIMA_SCALE);
                entries.add(BOTTLED_LEECH);
                entries.add(BOTTLED_STINK);
                entries.add(BLUE_CRAYFISH);
                entries.add(CRAYFISH);
                // food
                entries.add(TAPIR_MEAT);
                entries.add(COOKED_TAPIR_MEAT);
                entries.add(PIRANHA);
                entries.add(COOKED_PIRANHA);
                entries.add(ARAPAIMA);
                entries.add(COOKED_ARAPAIMA);
                entries.add(COOKED_CRAYFISH);
                entries.add(COOKED_YACARE);
                entries.add(RAW_YACARE);
                entries.add(IGUANA_RAW_TAIL);
                entries.add(IGUANA_TAIL_COOKED);
                entries.add(IGUANA_RAW_MEAT);
                entries.add(IGUANA_COOKED_MEAT);
                // buckets
                entries.add(PIRANHA_BUCKET);
                entries.add(ARAPAIMA_BUCKET);
                // spawn eggs
                entries.add(SONGBIRD_SPAWN_EGG);
                entries.add(CAPUCHIN_SPAWN_EGG);
                entries.add(TAPIR_SPAWN_EGG);
                entries.add(QUETZAL_SPAWN_EGG);
                entries.add(HOATZIN_SPAWN_EGG);
                entries.add(PIRANHA_SPAWN_EGG);
                entries.add(ARAPAIMA_SPAWN_EGG);
                entries.add(SNAPPING_TURTLE_SPAWN_EGG);
                entries.add(CRAYFISH_SPAWN_EGG);
                entries.add(LEECH_SPAWN_EGG);
                entries.add(YACARE_SPAWN_EGG);
                entries.add(IGUANA_SPAWN_EGG);
            })
            .build();

    /**
     * Registers all items for the mod
     */
    public static void init() {
        Faunus.LOG.info("Registering items for " + Faunus.MODID);

        register("songbird_spawn_egg", SONGBIRD_SPAWN_EGG);
        register("capuchin_spawn_egg", CAPUCHIN_SPAWN_EGG);
        register("tapir_spawn_egg", TAPIR_SPAWN_EGG);
        register("tapir_meat", TAPIR_MEAT);
        register("cooked_tapir_meat", COOKED_TAPIR_MEAT);
        register("constrictor_spawn_egg", CONSTRICTOR_SPAWN_EGG);
        register("quetzal_spawn_egg", QUETZAL_SPAWN_EGG);
        register("quetzal_feather", QUETZAL_FEATHER);
        register("quetzal_feather_trinket", QUETZAL_FEATHER_TRINKET);
        register("hoatzin_spawn_egg", HOATZIN_SPAWN_EGG);
        register("hoatzin_feather", HOATZIN_FEATHER);
        register("piranha_spawn_egg", PIRANHA_SPAWN_EGG);
        register("piranha", PIRANHA);
        register("cooked_piranha", COOKED_PIRANHA);
        register("piranha_bucket", PIRANHA_BUCKET);
        register("arapaima_spawn_egg", ARAPAIMA_SPAWN_EGG);
        register("arapaima", ARAPAIMA);
        register("cooked_arapaima", COOKED_ARAPAIMA);
        register("arapaima_scale", ARAPAIMA_SCALE);
        register("arapaima_bucket", ARAPAIMA_BUCKET);
        register("snapping_turtle_spawn_egg", SNAPPING_TURTLE_SPAWN_EGG);
        register("crayfish_spawn_egg", CRAYFISH_SPAWN_EGG);
        register("leech_spawn_egg", LEECH_SPAWN_EGG);
        register("yacare_spawn_egg", YACARE_SPAWN_EGG);
        register("bottled_leech", BOTTLED_LEECH);
        register("bottled_stink", BOTTLED_STINK);
        register("crayfish", CRAYFISH);
        register("blue_crayfish", BLUE_CRAYFISH);
        register("cooked_crayfish", COOKED_CRAYFISH);
        register("yacare_filet_cooked", COOKED_YACARE);
        register("yacare_filet_raw", RAW_YACARE);
        register("iguana_spawn_egg", IGUANA_SPAWN_EGG);
        register("iguana_raw_tail", IGUANA_RAW_TAIL);
        register("iguana_tail_cooked", IGUANA_TAIL_COOKED);
        register("iguana_raw_meat", IGUANA_RAW_MEAT);
        register("iguana_cooked_meat", IGUANA_COOKED_MEAT);
        register("yacare_egg", YACARE_EGG);
        register("arapaima_egg", ARAPAIMA_EGG);

        Registry.register(Registries.ITEM_GROUP, FaunusID.content("item_group"), GROUP);
    }

    private static void register(String name, Item item) {
        Registry.register(Registries.ITEM, FaunusID.content(name), item);
    }
}
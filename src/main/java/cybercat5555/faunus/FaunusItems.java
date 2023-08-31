package cybercat5555.faunus;

import cybercat5555.faunus.util.FaunusID;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;

public final class FaunusItems
{
	private FaunusItems() {}

	public static final Item CAPUCHIN_SPAWN_EGG = new SpawnEggItem(FaunusEntities.CAPUCHIN, 0x302005, 0xeeddbb, new FabricItemSettings());
	public static final Item TAPIR_SPAWN_EGG = new SpawnEggItem(FaunusEntities.TAPIR, 0xa09010, 0x60400a, new FabricItemSettings());
	public static final Item TAPIR_MEAT = new Item(new FabricItemSettings().food(FaunusFoodComponents.TAPIR_MEAT));
	public static final Item COOKED_TAPIR_MEAT = new Item(new FabricItemSettings().food(FaunusFoodComponents.COOKED_TAPIR_MEAT));
	public static final Item QUETZAL_SPAWN_EGG = new SpawnEggItem(FaunusEntities.QUETZAL, 0x10cb6f, 0x800528, new FabricItemSettings());
	public static final Item QUETZAL_FEATHER = new Item(new FabricItemSettings());
	public static final Item PIRANHA_SPAWN_EGG = new SpawnEggItem(FaunusEntities.PIRANHA, 0x303030, 0xbb1020, new FabricItemSettings());
	public static final Item PIRANHA = new Item(new FabricItemSettings().food(FaunusFoodComponents.PIRANHA));
	public static final Item COOKED_PIRANHA = new Item(new FabricItemSettings().food(FaunusFoodComponents.COOKED_PIRANHA));
	public static final Item PIRANHA_BUCKET = new EntityBucketItem(FaunusEntities.PIRANHA, Fluids.WATER, SoundEvents.ITEM_BUCKET_EMPTY_FISH, new FabricItemSettings().recipeRemainder(Items.BUCKET));
	public static final Item ARAPAIMA_SPAWN_EGG = new SpawnEggItem(FaunusEntities.ARAPAIMA, 0x80a080, 0xee3030, new FabricItemSettings());
	public static final Item ARAPAIMA = new Item(new FabricItemSettings().food(FaunusFoodComponents.ARAPAIMA));
	public static final Item COOKED_ARAPAIMA = new Item(new FabricItemSettings().food(FaunusFoodComponents.COOKED_ARAPAIMA));
	public static final Item ARAPAIMA_SCALE = new Item(new FabricItemSettings().rarity(Rarity.RARE));
	public static final Item ARAPAIMA_BUCKET = new EntityBucketItem(FaunusEntities.ARAPAIMA, Fluids.WATER, SoundEvents.ITEM_BUCKET_EMPTY_FISH, new FabricItemSettings().recipeRemainder(Items.BUCKET));

	private static final ItemGroup GROUP = FabricItemGroup.builder()
	.icon(() -> new ItemStack(QUETZAL_FEATHER))
	.displayName(Text.translatable("itemGroup.faunus.item_group"))
	.entries((context, entries) ->
	{
		// misc
		entries.add(QUETZAL_FEATHER);
		entries.add(ARAPAIMA_SCALE);
		// food
		entries.add(TAPIR_MEAT);
		entries.add(COOKED_TAPIR_MEAT);
		entries.add(PIRANHA);
		entries.add(COOKED_PIRANHA);
		entries.add(ARAPAIMA);
		entries.add(COOKED_ARAPAIMA);
		// buckets
		entries.add(PIRANHA_BUCKET);
		entries.add(ARAPAIMA_BUCKET);
		// spawn eggs
		entries.add(CAPUCHIN_SPAWN_EGG);
		entries.add(TAPIR_SPAWN_EGG);
		entries.add(QUETZAL_SPAWN_EGG);
		entries.add(PIRANHA_SPAWN_EGG);
		entries.add(ARAPAIMA_SPAWN_EGG);
	})
	.build();

	public static void init()
	{
		register("capuchin_spawn_egg", CAPUCHIN_SPAWN_EGG);
		register("tapir_spawn_egg", TAPIR_SPAWN_EGG);
		register("tapir_meat", TAPIR_MEAT);
		register("cooked_tapir_meat", COOKED_TAPIR_MEAT);
		register("quetzal_spawn_egg", QUETZAL_SPAWN_EGG);
		register("quetzal_feather", QUETZAL_FEATHER);
		register("piranha_spawn_egg", PIRANHA_SPAWN_EGG);
		register("piranha", PIRANHA);
		register("cooked_piranha", COOKED_PIRANHA);
		register("piranha_bucket", PIRANHA_BUCKET);
		register("arapaima_spawn_egg", ARAPAIMA_SPAWN_EGG);
		register("arapaima", ARAPAIMA);
		register("cooked_arapaima", COOKED_ARAPAIMA);
		register("arapaima_scale", ARAPAIMA_SCALE);
		register("arapaima_bucket", ARAPAIMA_BUCKET);

		Registry.register(Registries.ITEM_GROUP, FaunusID.content("item_group"), GROUP);
	}

	private static void register(String name, Item item)
	{
		Registry.register(Registries.ITEM, FaunusID.content(name), item);
	}
}

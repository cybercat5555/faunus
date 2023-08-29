package cybercat5555.faunus;

import cybercat5555.faunus.util.FaunusID;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;

public final class FaunusItems
{
	private FaunusItems() {}

	public static final Item TAPIR_SPAWN_EGG = new SpawnEggItem(FaunusEntities.TAPIR, 0xa09010, 0x60400a, new FabricItemSettings());
	public static final Item TAPIR_MEAT = new Item(new FabricItemSettings().food(FaunusFoodComponents.TAPIR_MEAT));
	public static final Item COOKED_TAPIR_MEAT = new Item(new FabricItemSettings().food(FaunusFoodComponents.COOKED_TAPIR_MEAT));
	public static final Item PIRANHA_SPAWN_EGG = new SpawnEggItem(FaunusEntities.PIRANHA, 0x303030, 0xbb1020, new FabricItemSettings());
	public static final Item PIRANHA = new Item(new FabricItemSettings().food(FaunusFoodComponents.PIRANHA));
	public static final Item COOKED_PIRANHA = new Item(new FabricItemSettings().food(FaunusFoodComponents.COOKED_PIRANHA));
	public static final Item ARAPAIMA_SPAWN_EGG = new SpawnEggItem(FaunusEntities.ARAPAIMA, 0x80a080, 0xee3030, new FabricItemSettings());
	public static final Item ARAPAIMA = new Item(new FabricItemSettings().food(FaunusFoodComponents.ARAPAIMA));
	public static final Item COOKED_ARAPAIMA = new Item(new FabricItemSettings().food(FaunusFoodComponents.COOKED_ARAPAIMA));
	public static final Item ARAPAIMA_SCALE = new Item(new FabricItemSettings().rarity(Rarity.RARE));

	private static final ItemGroup GROUP = FabricItemGroup.builder()
	.icon(() -> new ItemStack(Items.LILY_PAD))
	.displayName(Text.translatable("itemGroup.faunus.item_group"))
	.entries((context, entries) ->
	{
		entries.add(TAPIR_MEAT);
		entries.add(COOKED_TAPIR_MEAT);
		entries.add(PIRANHA);
		entries.add(COOKED_PIRANHA);
		entries.add(ARAPAIMA);
		entries.add(COOKED_ARAPAIMA);
		entries.add(TAPIR_SPAWN_EGG);
		entries.add(PIRANHA_SPAWN_EGG);
		entries.add(ARAPAIMA_SPAWN_EGG);
	})
	.build();

	public static void init()
	{
		Registry.register(Registries.ITEM, FaunusID.content("tapir_spawn_egg"), TAPIR_SPAWN_EGG);
		Registry.register(Registries.ITEM, FaunusID.content("tapir_meat"), TAPIR_MEAT);
		Registry.register(Registries.ITEM, FaunusID.content("cooked_tapir_meat"), COOKED_TAPIR_MEAT);
		Registry.register(Registries.ITEM, FaunusID.content("piranha_spawn_egg"), PIRANHA_SPAWN_EGG);
		Registry.register(Registries.ITEM, FaunusID.content("piranha"), PIRANHA);
		Registry.register(Registries.ITEM, FaunusID.content("cooked_piranha"), COOKED_PIRANHA);
		Registry.register(Registries.ITEM, FaunusID.content("arapaima_spawn_egg"), ARAPAIMA_SPAWN_EGG);
		Registry.register(Registries.ITEM, FaunusID.content("arapaima"), ARAPAIMA);
		Registry.register(Registries.ITEM, FaunusID.content("cooked_arapaima"), COOKED_ARAPAIMA);
		Registry.register(Registries.ITEM, FaunusID.content("arapaima_scale"), ARAPAIMA_SCALE);

		Registry.register(Registries.ITEM_GROUP, FaunusID.content("item_group"), GROUP);
	}
}

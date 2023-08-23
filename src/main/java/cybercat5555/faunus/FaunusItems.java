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

public final class FaunusItems
{
	private FaunusItems() {}

	public static final Item TAPIR_SPAWN_EGG = new SpawnEggItem(FaunusEntities.TAPIR, 0xa09010, 0x60400a, new FabricItemSettings());

	private static final ItemGroup GROUP = FabricItemGroup.builder()
	.icon(() -> new ItemStack(Items.LILY_PAD))
	.displayName(Text.translatable("itemGroup.faunus.item_group"))
	.entries((context, entries) ->
	{
		entries.add(TAPIR_SPAWN_EGG);
	})
	.build();

	public static void init()
	{
		Registry.register(Registries.ITEM, FaunusID.content("tapir_spawn_egg"), TAPIR_SPAWN_EGG);
		
		Registry.register(Registries.ITEM_GROUP, FaunusID.content("item_group"), GROUP);
	}
}

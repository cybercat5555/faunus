package cybercat5555.faunus;

import net.minecraft.item.FoodComponent;

/**
 * This class is largely unnecessary, but serves as a place for the team
 * to easily edit the food stats. Vanilla uses the same setup also
 * 
 */
public final class FaunusFoodComponents
{
	private FaunusFoodComponents() {}

	public static final FoodComponent TAPIR_MEAT = new FoodComponent.Builder().hunger(4).saturationModifier(0.3f).meat().build();
	public static final FoodComponent COOKED_TAPIR_MEAT = new FoodComponent.Builder().hunger(9).saturationModifier(0.8f).meat().build();

}

package cybercat5555.faunus.core;


import net.minecraft.component.type.FoodComponent;

/**
 * This class is largely unnecessary, but serves as a place for the team
 * to easily edit the food stats. Vanilla uses the same setup also
 */
public final class FoodRegistry {
    private FoodRegistry() {

    }


    public static final FoodComponent TAPIR_MEAT = new FoodComponent.Builder().nutrition(4).saturationModifier(0.3f).build();
    public static final FoodComponent COOKED_TAPIR_MEAT = new FoodComponent.Builder().nutrition(9).saturationModifier(0.8f).build();
    public static final FoodComponent PIRANHA = new FoodComponent.Builder().nutrition(2).saturationModifier(0.1f).build();
    public static final FoodComponent COOKED_PIRANHA = new FoodComponent.Builder().nutrition(4).saturationModifier(0.5f).build();
    public static final FoodComponent ARAPAIMA = new FoodComponent.Builder().nutrition(3).saturationModifier(0.15f).build();
    public static final FoodComponent COOKED_ARAPAIMA = new FoodComponent.Builder().nutrition(6).saturationModifier(0.7f).build();
    public static final FoodComponent RAW_YACARE = new FoodComponent.Builder().nutrition(2).saturationModifier(0.15f).build();
    public static final FoodComponent COOKED_YACARE = new FoodComponent.Builder().nutrition(5).saturationModifier(0.6f).build();
    public static final FoodComponent CRAYFISH = new FoodComponent.Builder().nutrition(2).saturationModifier(0.15f).build();
    public static final FoodComponent BLUE_CRAYFISH = new FoodComponent.Builder().nutrition(4).saturationModifier(0.6f).build();
    public static final FoodComponent COOKED_CRAYFISH = new FoodComponent.Builder().nutrition(5).saturationModifier(0.7f).build();
    public static final FoodComponent IGUANA_RAW_TAIL = new FoodComponent.Builder().nutrition(1).saturationModifier(0.1f).build();
    public static final FoodComponent IGUANA_COOKED_TAIL = new FoodComponent.Builder().nutrition(3).saturationModifier(0.3f).build();
    public static final FoodComponent IGUANA_RAW_MEAT = new FoodComponent.Builder().nutrition(2).saturationModifier(0.2f).build();
    public static final FoodComponent IGUANA_COOKED_MEAT = new FoodComponent.Builder().nutrition(6).saturationModifier(0.6f).build();
}
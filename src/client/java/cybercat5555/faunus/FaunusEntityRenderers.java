package cybercat5555.faunus;

import cybercat5555.faunus.entity.ArapaimaEntityRenderer;
import cybercat5555.faunus.entity.CapuchinEntityRenderer;
import cybercat5555.faunus.entity.CrayfishEntityRenderer;
import cybercat5555.faunus.entity.HoatzinEntityRenderer;
import cybercat5555.faunus.entity.LeechEntityRenderer;
import cybercat5555.faunus.entity.PiranhaEntityRenderer;
import cybercat5555.faunus.entity.QuetzalEntityRenderer;
import cybercat5555.faunus.entity.SnappingTurtleEntityRenderer;
import cybercat5555.faunus.entity.TapirEntityRenderer;
import cybercat5555.faunus.entity.YacareEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public final class FaunusEntityRenderers
{
	private FaunusEntityRenderers() {}

	public static void init()
	{
		EntityRendererRegistry.register(FaunusEntities.CAPUCHIN, context -> new CapuchinEntityRenderer(context));
		EntityRendererRegistry.register(FaunusEntities.TAPIR, context -> new TapirEntityRenderer(context));
		EntityRendererRegistry.register(FaunusEntities.QUETZAL, context -> new QuetzalEntityRenderer(context));
		EntityRendererRegistry.register(FaunusEntities.HOATZIN, context -> new HoatzinEntityRenderer(context));
		EntityRendererRegistry.register(FaunusEntities.PIRANHA, context -> new PiranhaEntityRenderer(context));
		EntityRendererRegistry.register(FaunusEntities.ARAPAIMA, context -> new ArapaimaEntityRenderer(context));
		EntityRendererRegistry.register(FaunusEntities.SNAPPING_TURTLE, context -> new SnappingTurtleEntityRenderer(context));
		EntityRendererRegistry.register(FaunusEntities.CRAYFISH, context -> new CrayfishEntityRenderer(context));
		EntityRendererRegistry.register(FaunusEntities.LEECH, context -> new LeechEntityRenderer(context));
		EntityRendererRegistry.register(FaunusEntities.YACARE, context -> new YacareEntityRenderer(context));
	}
}

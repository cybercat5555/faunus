package cybercat5555.faunus;

import cybercat5555.faunus.entity.TapirEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public final class FaunusEntityRenderers
{
	private FaunusEntityRenderers() {}

	public static void init()
	{
		EntityRendererRegistry.register(FaunusEntities.TAPIR, (context) ->
		{
			return new TapirEntityRenderer(context);
		});
	}
}

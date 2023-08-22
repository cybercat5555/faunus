package cybercat5555.faunus;

import net.fabricmc.api.ClientModInitializer;

public class FaunusClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		FaunusEntityRenderers.init();
	}
}
package cybercat5555.faunus;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Faunus implements ModInitializer
{
	public static final String MODID = "faunus";
	public static final Logger LOG = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize()
	{
		LOG.info("Hello Fabric world!");
	}
}
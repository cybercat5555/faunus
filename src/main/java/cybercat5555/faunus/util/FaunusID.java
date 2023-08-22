package cybercat5555.faunus.util;

import cybercat5555.faunus.Faunus;
import net.minecraft.util.Identifier;

public final class FaunusID
{
	private FaunusID() {}

	public static Identifier content(String name)
	{
		return new Identifier(Faunus.MODID, name);
	}
}

package cybercat5555.faunus.entity;

import cybercat5555.faunus.util.FaunusID;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class QuetzalEntityRenderer extends GeoEntityRenderer<QuetzalEntity>
{
	public QuetzalEntityRenderer(Context renderManager)
	{
		super(renderManager, new DefaultedEntityGeoModel<QuetzalEntity>(FaunusID.content("quetzal"), true));
	}
	
}

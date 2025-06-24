package co.juanxxo.junta.entity.renderer;

import co.juanxxo.junta.client.model.ZeroPointOrbModel;
import co.juanxxo.junta.entity.ZeroPointOrbEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ZeroPointOrbRenderer extends GeoEntityRenderer<ZeroPointOrbEntity> {

    public ZeroPointOrbRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new ZeroPointOrbModel());
    }
}

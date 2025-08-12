package co.juanxxo.junta.entity.renderer;

import co.juanxxo.junta.client.model.DefaultJuntaRaccoonModel;
import co.juanxxo.junta.entity.DefaultJuntaRaccoonEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DefaultJuntaRaccoonRenderer extends GeoEntityRenderer<DefaultJuntaRaccoonEntity> {

    public DefaultJuntaRaccoonRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new DefaultJuntaRaccoonModel());
    }
}

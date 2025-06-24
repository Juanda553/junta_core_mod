package co.juanxxo.junta.entity.renderer;

import co.juanxxo.junta.entity.HerobrineRangedAttackEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

@Environment(EnvType.CLIENT)
public class HerobrineRangedAttackRenderer extends FlyingItemEntityRenderer<HerobrineRangedAttackEntity> {

    public HerobrineRangedAttackRenderer(EntityRendererFactory.Context context) {
        super(context);
    }
}

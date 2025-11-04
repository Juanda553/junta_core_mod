package co.juanxxo.junta.client.model;

import co.juanxxo.junta.JuntaCoreMain;
import co.juanxxo.junta.entity.DefaultJuntaRaccoonEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class DefaultJuntaRaccoonModel extends GeoModel<DefaultJuntaRaccoonEntity> {
    @Override
    public Identifier getModelResource(DefaultJuntaRaccoonEntity zeroPointOrbEntity) {
        return Identifier.of(JuntaCoreMain.MOD_ID, "geo/mapahce_default.geo.json");
    }

    @Override
    public Identifier getTextureResource(DefaultJuntaRaccoonEntity zeroPointOrbEntity) {
        return Identifier.of(JuntaCoreMain.MOD_ID, "textures/entity/raccoon_default.png");
    }

    @Override
    public Identifier getAnimationResource(DefaultJuntaRaccoonEntity zeroPointOrbEntity) {
        return Identifier.of(JuntaCoreMain.MOD_ID, "animations/raccoon_default_mod.animation.json");
    }
}

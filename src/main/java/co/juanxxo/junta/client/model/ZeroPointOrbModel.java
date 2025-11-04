package co.juanxxo.junta.client.model;

import co.juanxxo.junta.JuntaCoreMain;
import co.juanxxo.junta.entity.ZeroPointOrbEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class ZeroPointOrbModel extends GeoModel<ZeroPointOrbEntity> {
    @Override
    public Identifier getModelResource(ZeroPointOrbEntity zeroPointOrbEntity) {
        return Identifier.of(JuntaCoreMain.MOD_ID, "geo/zero_point_orb.geo.json");
    }

    @Override
    public Identifier getTextureResource(ZeroPointOrbEntity zeroPointOrbEntity) {
        return Identifier.of(JuntaCoreMain.MOD_ID, "textures/entity/zeropoint_orb.png");
    }

    @Override
    public Identifier getAnimationResource(ZeroPointOrbEntity zeroPointOrbEntity) {
        return Identifier.of(JuntaCoreMain.MOD_ID, "animations/zero_point_orb.animation.json");
    }
}

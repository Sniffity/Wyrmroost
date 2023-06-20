package com.github.shannieann.wyrmroost.client.render.entity.dragon;

import com.github.shannieann.wyrmroost.client.model.entity.dragon.ModelLesserDesertwyrm;
import com.github.shannieann.wyrmroost.entities.dragon.EntityLesserDesertwyrm;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderLesserDeserwyrm extends WRDragonRender<EntityLesserDesertwyrm> {
    public RenderLesserDeserwyrm(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelLesserDesertwyrm());
        //TODO: Shadow?
        this.shadowRadius = 0.0F;
    }
}


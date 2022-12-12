package com.github.shannieann.wyrmroost.client.render.entity.dragon.placeholder;

import com.github.shannieann.wyrmroost.client.model.entity.GeckoCanariWyvernModel;
import com.github.shannieann.wyrmroost.entities.dragon.EntityCanariWyvern;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class CanariWyvernRenderer<T extends EntityCanariWyvern> extends GeoEntityRenderer<T> {
    public CanariWyvernRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GeckoCanariWyvernModel<>());
    }

}

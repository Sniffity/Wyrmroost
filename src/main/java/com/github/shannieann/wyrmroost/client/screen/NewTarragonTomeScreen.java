package com.github.shannieann.wyrmroost.client.screen;

import com.github.shannieann.wyrmroost.Wyrmroost;
import com.github.shannieann.wyrmroost.containers.NewTarragonTomeContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


public class NewTarragonTomeScreen extends AbstractContainerScreen<NewTarragonTomeContainer> {
    private static final ResourceLocation TEXTURE = Wyrmroost.id("textures/gui/container/dragon_container.png");
    public NewTarragonTomeScreen(NewTarragonTomeContainer container, Inventory playerInv, Component title) {
        super(container, playerInv, title);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 256;
        this.imageHeight = 256;
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        blit(pPoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    // FOR BUTTONS LATER

    /*@Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new ExtendedButton(xPos, yPos, width, height, title, btn -> {
            // Do stuff when you click the button
        }));
    }*/

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }


    /*@Override
    /protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderLabels(pPoseStack, pMouseX, pMouseY);
    }*/
    // Might include dragon's name at the top, we'll see though



}

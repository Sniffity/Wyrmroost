package com.github.shannieann.wyrmroost.client;

import com.github.shannieann.wyrmroost.WRConfig;
import com.github.shannieann.wyrmroost.Wyrmroost;
import com.github.shannieann.wyrmroost.client.render.RenderHelper;
import com.github.shannieann.wyrmroost.client.render.entity.dragon.*;
import com.github.shannieann.wyrmroost.client.render.entity.dragon.placeholder.CanariWyvernRenderer;
import com.github.shannieann.wyrmroost.client.render.entity.dragon.placeholder.OWDrakeRenderer;
import com.github.shannieann.wyrmroost.client.render.entity.dragon.placeholder.SilverGliderRenderer;
import com.github.shannieann.wyrmroost.client.render.entity.dragon_egg.RenderDragonEgg;
import com.github.shannieann.wyrmroost.client.render.entity.effect.RenderLightningNova;
import com.github.shannieann.wyrmroost.client.render.entity.projectile.BreathWeaponRenderer;
import com.github.shannieann.wyrmroost.client.render.entity.projectile.GeodeTippedArrowRenderer;
import com.github.shannieann.wyrmroost.entity.dragon.EntityButterflyLeviathan;
import com.github.shannieann.wyrmroost.entity.dragon.WRDragonEntity;
import com.github.shannieann.wyrmroost.item.LazySpawnEggItem;
import com.github.shannieann.wyrmroost.registry.*;
import com.github.shannieann.wyrmroost.util.WRModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class ClientEvents
{
    public static Set<UUID> dragonRiders = new HashSet<>();
    public static boolean keybindFlight = true;

    public static void init()
    {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        modBus.addListener(ClientEvents::clientSetup);
        modBus.addListener(ClientEvents::stitchTextures);
        modBus.addListener(ClientEvents::itemColors);
        modBus.addListener(ClientEvents::bakeParticles);
        modBus.addListener(ClientEvents::registerRenderers);
        //bus.addListener(ClientEvents::bakeModels);


        forgeBus.addListener(RenderHelper::renderWorld);
        forgeBus.addListener(RenderHelper::renderOverlay);
        forgeBus.addListener(RenderHelper::renderEntities);
        forgeBus.addListener(ClientEvents::cameraPerspective);
        forgeBus.addListener(ClientEvents::preLivingRender);
        forgeBus.addListener(ClientEvents::onRenderWorldLast);
        //forgeBus.addListener(ClientEvents::postLivingRender);
        forgeBus.addListener(ClientEvents::dragonRidingFOV);
        forgeBus.addListener(ClientEvents::onKeyInput);

        //WRDimensionRenderInfo.init();
    }

    // ====================
    //       Mod Bus
    // ====================

    private static void clientSetup(final FMLClientSetupEvent event)
    {
        WRKeybind.registerKeys();

        /*ThinLogBlock.setCutoutRendering(WRBlocks.DYING_CORIN_WOOD);
        ThinLogBlock.setCutoutRendering(WRBlocks.RED_CORIN_WOOD);
        ThinLogBlock.setCutoutRendering(WRBlocks.TEAL_CORIN_WOOD);
        ThinLogBlock.setCutoutRendering(WRBlocks.SILVER_CORIN_WOOD);
        ThinLogBlock.setCutoutRendering(WRBlocks.PRISMARINE_CORIN_WOOD);*/
        WRIO.screenSetup();
        event.enqueueWork(() ->
        {

            //WoodType.values().filter(w -> w.name().contains(Wyrmroost.MOD_ID)).forEach(Atlases::addWoodType);

            //for (TileEntityType<?> entry : ModUtils.getRegistryEntries(WRBlockEntities.REGISTRY))
                //if (entry instanceof WRBlockEntities<?>) ((WRBlockEntities<?>) entry).callBack();
        });
    }

    private static void bakeParticles(ParticleFactoryRegisterEvent event)
    {
        for (ParticleType<?> entry : WRModUtils.getRegistryEntries(WRParticles.REGISTRY))
            if (entry instanceof WRParticles<?>) ((WRParticles<?>) entry).bake();
    }

    private static void stitchTextures(TextureStitchEvent.Pre evt)
    {
        if (evt.getAtlas().location() == TextureAtlas.LOCATION_BLOCKS)
            evt.addSprite(BreathWeaponRenderer.BLUE_FIRE);
    }

    private static void itemColors(ColorHandlerEvent.Item evt)
    {
        ItemColors handler = evt.getItemColors();
        ItemColor eggFunc = (stack, tintIndex) -> ((LazySpawnEggItem<?>) stack.getItem()).getColor(tintIndex);
        for (LazySpawnEggItem<?> e : LazySpawnEggItem.SPAWN_EGGS) handler.register(eggFunc, e);

        handler.register((stack, index) -> ((DyeableLeatherItem) stack.getItem()).getColor(stack), WRItems.LEATHER_DRAGON_ARMOR.get());
    }

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(WREntityTypes.ROYAL_RED.get(), RenderRoyalRed::new);
        event.registerEntityRenderer(WREntityTypes.BUTTERFLY_LEVIATHAN.get(), RenderButterflyLeviathan::new);

        event.registerEntityRenderer(WREntityTypes.LESSER_DESERTWYRM.get(), RenderLesserDesertwyrm::new);
        event.registerEntityRenderer(WREntityTypes.ROOST_STALKER.get(), RenderRoostStalker::new);

        //event.registerEntityRenderer(WREntityTypes.ROOST_STALKER.get(), RoostStalkerRenderer2::new);
        event.registerEntityRenderer(WREntityTypes.CANARI_WYVERN.get(), CanariWyvernRenderer::new);
        event.registerEntityRenderer(WREntityTypes.SILVER_GLIDER.get(), SilverGliderRenderer::new);
        event.registerEntityRenderer(WREntityTypes.OVERWORLD_DRAKE.get(), OWDrakeRenderer::new);


        event.registerEntityRenderer(WREntityTypes.SOUL_CRYSTAL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(WREntityTypes.GEODE_TIPPED_ARROW.get(), GeodeTippedArrowRenderer::new);
        event.registerEntityRenderer(WREntityTypes.DRAGON_EGG.get(), RenderDragonEgg::new);
        event.registerEntityRenderer(WREntityTypes.FIRE_BREATH.get(), BreathWeaponRenderer::new);
        event.registerEntityRenderer(WREntityTypes.LIGHTNING_NOVA.get(), RenderLightningNova::new);
    }
    // =====================
    //      Forge Bus
    // =====================

    private static void cancelIfRidingDragon(RenderLivingEvent event){
        Entity entity = event.getEntity().getVehicle();
        if (entity instanceof WRDragonEntity) {
            if (dragonRiders.contains(event.getEntity().getUUID())) event.setCanceled(true); // Don't render the real player if they're riding a dragon
            CameraType camera = getClient().options.getCameraType();
            if (getClient().player == event.getEntity() && camera == CameraType.FIRST_PERSON) event.setCanceled(true); // Don't render the "fake" player if the player is in 1st person
        }
    }
    private static void preLivingRender(RenderLivingEvent.Pre event){
        cancelIfRidingDragon(event);
    }





    private static void cameraPerspective(EntityViewRenderEvent.CameraSetup event) {
        Minecraft mc = getClient();
        Entity entity = mc.player.getVehicle();
        if (!(entity instanceof WRDragonEntity dragon)) return;
        CameraType view = mc.options.getCameraType();
        // Third person camera views
        if (view != CameraType.FIRST_PERSON)
            dragon.setThirdPersonMountCameraAngles(view == CameraType.THIRD_PERSON_BACK, event, mc.player);
        else{ // 1st person
            // Set camera rotations based on bone values set in dragon renderer
            UUID uuid = mc.player.getUUID();
            float xRot = -dragon.cameraRotVector.x();
            float yRot = dragon.cameraRotVector.y();
            float zRot = dragon.cameraRotVector.z();
            //System.out.println(xRot + ", " + yRot +", " + zRot);
            Vector3d bonePos = dragon.cameraBonePos.get(uuid);
            if (bonePos != null) {
                Vec3 vecBonePos = new Vec3(bonePos.x, bonePos.y+dragon.getMountCameraYOffset(), bonePos.z);
                // Set camera position
                //Sniffity: Previous method was forcing the camera position to update to the Dragon's position.
                //This essentially caused the camera to no longer transition smoothly from position A to B if the dragon was moving
                //It would instead jump from position A to B..
                //Getting the camera position and adding an offset on top of that is a safer way of doing things.
                //On vecBonePos, we adjust the Y offset, which will always compensate for the camera being too high up...
                //Last thing to do is properly moving the camera slightly backwards alongside the player-aligned x-axis
                //This prevents the camera from being too far "ahead" of the dragon...
                Vec3 cameraPos = event.getCamera().getPosition();
                event.getCamera().setPosition(cameraPos.add(vecBonePos)); // Not using move() here because it does weird stuff when you look around... (center of rotation messed up)
                //event.getCamera().move(-calcCameraDistance(1.0, dragon), 0, 0);

            }

            // Allows for complete alteration of camera rotations for some sick clips
            event.setPitch(xRot + event.getPitch());
            event.setYaw(yRot + event.getYaw());
            event.setRoll(zRot + event.getRoll());
        }
    }

    // TODO maybe change FOV during flight, like if a dragon is diving for example?
    // Remove the sprint fov change when you're on a dragon, it doesn't do anything in the first place.
    // Also, this opens the door for us to change fov in certain circumstances (see above)
    // This would probably be a client config option, along with camera rotations.
    private static void dragonRidingFOV(EntityViewRenderEvent.FieldOfView event){
        LocalPlayer player = getClient().player;
        if (player == null || !(player.getVehicle() instanceof WRDragonEntity)) return;
        double fov = event.getFOV();
        event.setFOV(fov);
    }

    public static double getViewCollisionDistance(double cameraDistance, Entity entity, Player player) {
        Camera camera = getClient().gameRenderer.getMainCamera();
        Vec3 cameraPosition = player.position().subtract(0, 0, cameraDistance);
        Vector3f cameraLookVector = camera.getLookVector();

        // Array of Vectors defining a Cube
        Vec3[] offsets = {
                // Corners of the cube
                new Vec3(-0.1F, -0.1F, -0.1F),
                new Vec3( 0.1F, -0.1F, -0.1F),
                new Vec3(-0.1F,  0.1F, -0.1F),
                new Vec3( 0.1F,  0.1F, -0.1F),
                new Vec3(-0.1F, -0.1F,  0.1F),
                new Vec3( 0.1F, -0.1F,  0.1F),
                new Vec3(-0.1F,  0.1F,  0.1F),
                new Vec3( 0.1F,  0.1F,  0.1F),
        };

        // Checks a cube of positions around the camera position
        for (Vec3 offset : offsets) {
            //Offset the start position to set the cube
            Vec3 startPoint = cameraPosition.add(offset.x, offset.y, offset.z);
            //Define the endpoint
            Vec3 viewEndPoint = cameraPosition.add(new Vec3(cameraLookVector).scale(cameraDistance));


            HitResult rtr = entity.level.clip(new ClipContext(startPoint, viewEndPoint, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, entity));
            if (rtr.getType() != HitResult.Type.MISS) {
                double collisionDistance = rtr.getLocation().distanceTo(cameraPosition);
                // If hit, update the minimum collision distance
                if (collisionDistance < cameraDistance)
                    cameraDistance = collisionDistance;
            }
        }
        return cameraDistance;
    }


    // =====================

    // for class loading issues
    public static Minecraft getClient()
    {
        return Minecraft.getInstance();
    }

    public static ClientLevel getLevel()
    {
        return getClient().level;
    }

    public static Player getPlayer()
    {
        return getClient().player;
    }

    public static Vec3 getProjectedView()
    {
        return getClient().gameRenderer.getMainCamera().getPosition();
    }

    public static float getPartialTicks()
    {
        return getClient().getFrameTime();
    }

    public static void onKeyInput(TickEvent.ClientTickEvent event) {
        Minecraft game = Minecraft.getInstance();
        if (game.player != null) {
                if (ClientEvents.KEY_TEST.isDown()) {
                    Wyrmroost.NETWORK.sendToServer(new PacketKey());
                }
        }
    }
    public static void onRenderWorldLast(RenderLevelStageEvent event) {

        if (WRConfig.DEBUG_MODE.get()) {
            RenderLevelStageEvent.Stage stage = event.getStage();
            if (stage == RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {
                Minecraft mc = Minecraft.getInstance();
                Camera camera = mc.gameRenderer.getMainCamera();
                Vec3 viewPosition = camera.getPosition();
                PoseStack matrix_stack = event.getPoseStack();
                matrix_stack.pushPose();
                matrix_stack.translate(-viewPosition.x, -viewPosition.y, -viewPosition.z);
                List<EntityButterflyLeviathan> entityList = mc.level.getEntitiesOfClass(EntityButterflyLeviathan.class, new AABB(mc.player.getOnPos()).inflate(20));
                if (!entityList.isEmpty()) {
                    for (int i = 0; i<entityList.size(); i++) {
                        List<AABB> attackBoxes = entityList.get(i).generateAttackBoxes();
                        LevelRenderer.renderLineBox(matrix_stack, mc.renderBuffers().bufferSource().getBuffer(RenderType.lines()), attackBoxes.get(0), 1,0,0,1);
                        LevelRenderer.renderLineBox(matrix_stack, mc.renderBuffers().bufferSource().getBuffer(RenderType.lines()), attackBoxes.get(1), 0,1,0,1);
                        LevelRenderer.renderLineBox(matrix_stack, mc.renderBuffers().bufferSource().getBuffer(RenderType.lines()), attackBoxes.get(2), 0,0,1,1);
                    }
                    matrix_stack.popPose();
                    mc.renderBuffers().bufferSource().endBatch();
                }
            }
        }
    }

    public static final KeyMapping KEY_TEST = new KeyMapping("key.test",  GLFW.GLFW_KEY_J, "key.wyrmroost.category");

}

package dev.sterner.dogma.foundation.event;

import dev.sterner.dogma.client.layer.abyss.CradleRenderLayer;
import dev.sterner.dogma.client.layer.necro.ShoulderCropseFeatureRenderer;
import dev.sterner.dogma.client.model.necro.entity.BagEntityModel;
import dev.sterner.dogma.client.renderer.abyss.block.CurseWardingBoxBlockEntityRenderer;
import dev.sterner.dogma.client.renderer.abyss.gui.hud.CurseHudRenderer;
import dev.sterner.dogma.foundation.registry.DogmaBlockEntityTypeRegistry;
import dev.sterner.dogma.foundation.registry.DogmaParticleTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DogmaClientSetupEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.ARMOR_LEVEL.id(), "curse", (gui, poseStack, partialTick, width, height) ->
                CurseHudRenderer.renderCurseHud(gui, poseStack, width, height));

    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public static void entityRender(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BagEntityModel.LAYER_LOCATION, BagEntityModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(DogmaBlockEntityTypeRegistry.CURSE_WARDING_BOX.get(), CurseWardingBoxBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
        DogmaParticleTypeRegistry.registerParticleFactory(event);
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.AddLayers event) {
        event.getSkins().forEach(skin -> {
            EntityRenderer<?> renderer = event.getSkin(skin);
            if (renderer instanceof MobRenderer livingRenderer) {
                livingRenderer.addLayer(new CradleRenderLayer<>(livingRenderer));
            }
            if (renderer instanceof PlayerRenderer playerRenderer) {
                playerRenderer.addLayer(new ShoulderCropseFeatureRenderer<>(playerRenderer, event.getEntityModels(), event.getContext().getEntityRenderDispatcher()));
            }
        });
    }
}

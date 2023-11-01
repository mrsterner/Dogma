package dev.sterner.dogma.api;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.PacketDistributor;

public abstract class DogmaPlayerCapability {

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        throw new UnsupportedOperationException("Subclasses must implement registerCapabilities");
    }
}

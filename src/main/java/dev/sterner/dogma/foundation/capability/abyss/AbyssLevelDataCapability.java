package dev.sterner.dogma.foundation.capability.abyss;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.DogmaPackets;
import dev.sterner.dogma.foundation.abyss.abyss.Abyss;
import dev.sterner.dogma.foundation.networking.SyncLevelCapabilityPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.network.PacketDistributor;
import team.lodestar.lodestone.systems.capability.LodestoneCapability;
import team.lodestar.lodestone.systems.capability.LodestoneCapabilityProvider;

import java.util.ArrayList;
import java.util.List;

public class AbyssLevelDataCapability implements LodestoneCapability {

    private final List<Abyss> abyssList = new ArrayList<>();

    public static Capability<AbyssLevelDataCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public AbyssLevelDataCapability() {
    }

    public void addAbyss(Level level, Abyss abyss) {
        abyssList.add(abyss);
        sync(level);
    }


    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(AbyssLevelDataCapability.class);
    }

    public static void attachWorldCapability(AttachCapabilitiesEvent<Level> event) {
        final AbyssLevelDataCapability capability = new AbyssLevelDataCapability();
        event.addCapability(Dogma.id("abyss_level_data"), new LodestoneCapabilityProvider<>(AbyssLevelDataCapability.CAPABILITY, () -> capability));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (Abyss abyss : abyssList) {
            CompoundTag nbtCompound = new CompoundTag();
            abyss.writeAbyssNbt(nbtCompound);
            list.add(nbtCompound);
        }
        if (!list.isEmpty()) {
            tag.put(Constants.Nbt.ABYSS, list);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        abyssList.clear();

        ListTag nbtList = nbt.getList(Constants.Nbt.ABYSS, Tag.TAG_COMPOUND);

        for (int i = 0; i < nbtList.size(); ++i) {
            CompoundTag nbtCompound = nbtList.getCompound(i);
            Abyss abyss = Abyss.readAbyssNbt(nbtCompound);
            abyssList.add(abyss);
        }
    }

    public static LazyOptional<AbyssLevelDataCapability> getCapabilityOptional(Level level) {
        return level.getCapability(CAPABILITY);
    }

    public static void sync(Level level) {
        getCapabilityOptional(level).ifPresent(
                c -> DogmaPackets.DOGMA_CHANNEL.send(PacketDistributor.DIMENSION.with(level::dimension),
                        new SyncLevelCapabilityPacket(c.serializeNBT())));
    }
}
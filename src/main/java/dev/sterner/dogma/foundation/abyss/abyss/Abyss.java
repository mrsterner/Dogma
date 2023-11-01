package dev.sterner.dogma.foundation.abyss.abyss;

import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.abyss.curse.Curse;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class Abyss {

    public Level world;
    public List<AbyssLayer> layerList;
    public List<ChunkPos> chunkPosList;

    private Abyss() {

    }

    public static Abyss readAbyssNbt(CompoundTag nbt) {
        Abyss abyss = new Abyss();
        for (AbyssLayer abyssLayer : abyss.layerList) {
            AbyssLayer.readAbyssLayerToNbt(abyssLayer, nbt);
        }

        if (nbt.contains(Constants.Nbt.CHUNK_LIST)) {
            ListTag nbtList = nbt.getList(Constants.Nbt.CHUNK_LIST, Tag.TAG_COMPOUND);

            for (int i = 0; i < nbtList.size(); i++) {
                CompoundTag nbtCompound = nbtList.getCompound(i);
                abyss.chunkPosList.add(new ChunkPos(nbtCompound.getInt(Constants.Nbt.CHUNK_X), nbtCompound.getInt(Constants.Nbt.CHUNK_Z)));
            }
        }


        return abyss;
    }

    public void writeAbyssNbt(CompoundTag nbt) {

        for (AbyssLayer abyssLayer : layerList) {
            AbyssLayer.writeAbyssLayerToNbt(abyssLayer, nbt);
        }

        ListTag nbtList = new ListTag();

        for (ChunkPos chunkPos : chunkPosList) {
            CompoundTag nbtCompound = new CompoundTag();
            nbt.putInt(Constants.Nbt.CHUNK_X, chunkPos.x);
            nbt.putInt(Constants.Nbt.CHUNK_Z, chunkPos.z);
            nbtList.add(nbtCompound);
        }

        if (!nbtList.isEmpty()) {
            nbt.put(Constants.Nbt.CHUNK_LIST, nbtList);
        }
    }

    public static class Builder {
        private final Abyss abyss = new Abyss();
        private final ChunkPos centerChunk;

        public Builder(Level world, ChunkPos chunkPos) {
            this.abyss.world = world;
            this.abyss.chunkPosList.add(chunkPos);
            this.centerChunk = chunkPos;
        }

        public Abyss build() {
            return abyss;
        }

        public Builder addChunksRadius(int radiusInChunks) {
            int centerX = centerChunk.x;
            int centerZ = centerChunk.z;

            List<ChunkPos> surroundingChunks = new ArrayList<>();

            for (int x = centerX - radiusInChunks; x <= centerX + radiusInChunks; x++) {
                for (int z = centerZ - radiusInChunks; z <= centerZ + radiusInChunks; z++) {
                    ChunkPos chunkPos = new ChunkPos(x, z);
                    surroundingChunks.add(chunkPos);
                }
            }

            abyss.chunkPosList.addAll(surroundingChunks);

            return this;
        }

        public Builder addLayer(AbyssLayer layer) {
            this.abyss.layerList.add(layer);
            return this;
        }

        public Builder addLayer(int topY, int bottomY, Curse curse) {
            return addLayer(new AbyssLayer(topY, bottomY, curse));
        }
    }
}
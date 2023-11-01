package dev.sterner.dogma.foundation.util;

import dev.sterner.dogma.foundation.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DogmaUtils {
    /**
     * Rotates a VoxelShape on any axis a certain amount of times
     *
     * @param times amount of times to rotate
     * @param shape the shape input
     * @param axis  which axis to rotate on
     * @return new VoxelShape after rotation
     */
    public static VoxelShape rotateShape(int times, VoxelShape shape, char axis) {
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};
        VoxelShape emptyShape = Shapes.empty();
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
                switch (axis) {
                    case 'x' -> buffer[1] = Shapes.join(buffer[1],
                            Shapes.box(minX, 1 - maxZ, minY, maxX, 1 - minZ, maxY), BooleanOp.OR);
                    case 'y' -> buffer[1] = Shapes.join(buffer[1],
                            Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX), BooleanOp.OR);
                    case 'z' -> buffer[1] = Shapes.join(buffer[1],
                            Shapes.box(minY, minX, 1 - maxZ, maxY, maxX, 1 - minZ), BooleanOp.OR);
                    default -> throw new IllegalArgumentException("Invalid axis argument: " + axis);
                }
            });
            buffer[0] = buffer[1];
            buffer[1] = emptyShape;
        }
        return buffer[0];
    }

    /**
     * Rotates the VoxelShape on y-axis
     *
     * @param from  direction
     * @param to    direction
     * @param shape shape
     * @return new voxel shape for the new direction
     */
    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};

        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.join(buffer[1], Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX), BooleanOp.OR));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }
        return buffer[0];
    }

    /**
     * Parse an nbt to a vec3d
     *
     * @param compound nbt
     * @return vec3d
     */
    public static Vec3 toVec3d(CompoundTag compound) {
        return new Vec3(compound.getDouble("X"), compound.getDouble("Y"), compound.getDouble("Z"));
    }

    /**
     * Make a nbt from a Vec3d
     *
     * @param pos vec3d position
     * @return nbt
     */
    public static CompoundTag fromVec3d(Vec3 pos) {
        CompoundTag nbtCompound = new CompoundTag();
        nbtCompound.putDouble("X", pos.x());
        nbtCompound.putDouble("Y", pos.y());
        nbtCompound.putDouble("Z", pos.z());
        return nbtCompound;
    }

    public static void writeChancesNbt(CompoundTag nbt, NonNullList<Float> floats) {
        ListTag nbtList = new ListTag();
        for (float aFloat : floats) {
            CompoundTag nbtCompound = new CompoundTag();
            nbtCompound.putFloat(Constants.Nbt.CHANCE, aFloat);
            nbtList.add(nbtCompound);
        }
        nbt.put(Constants.Nbt.CHANCES, nbtList);
    }

    public static void readChanceNbt(CompoundTag nbt, NonNullList<Float> floats) {
        ListTag nbtList = nbt.getList(Constants.Nbt.CHANCES, Tag.TAG_COMPOUND);
        for (int i = 0; i < nbtList.size(); ++i) {
            CompoundTag nbtCompound = nbtList.getCompound(i);
            float j = nbtCompound.getFloat(Constants.Nbt.CHANCE);
            floats.set(i, j);
        }
    }

    /**
     * Gets the closest entity of a specific type
     *
     * @param entityList list of entities to test
     * @param entityType entityType to look for
     * @param pos        position to measure distance from
     * @return Entity closest to pos from entityList
     */

    @Nullable
    public static <T extends LivingEntity> T getClosestEntity(List<? extends T> entityList, EntityType<?> entityType, BlockPos pos) {
        Optional<? extends T> closestEntity = entityList.stream().filter(entity -> entity.getType() == entityType)
                .min(Comparator.comparingDouble(entity -> entity.distanceToSqr(pos.getX(), pos.getY(), pos.getZ())));
        return closestEntity.orElse(null);
    }
}

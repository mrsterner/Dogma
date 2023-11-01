package dev.sterner.dogma.content.block_entity.abyss;

import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.capability.abyss.AbyssLivingEntityDataCapability;
import dev.sterner.dogma.foundation.listener.MeatToEntityDataReloadListener;
import dev.sterner.dogma.foundation.registry.DogmaBlockEntityTypeRegistry;
import dev.sterner.dogma.foundation.registry.DogmaBlockRegistry;
import mod.azure.azurelib.animatable.GeoBlockEntity;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.systems.multiblock.MultiBlockCoreEntity;
import team.lodestar.lodestone.systems.multiblock.MultiBlockStructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CurseWardingBoxBlockEntity extends MultiBlockCoreEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public Item item = null;
    private boolean isOpening = false;
    private boolean isClosing = false;
    private int openingTimer = 20 * 2;
    private long progress = 0;

    public static final Supplier<MultiBlockStructure> STRUCTURE = () ->
            MultiBlockStructure.of(genStruct(DogmaBlockRegistry.CURSE_WARDING_BOX_COMPONENT.get().defaultBlockState(), new ArrayList<>())
                    .toArray(MultiBlockStructure.StructurePiece[]::new));


    public static List<MultiBlockStructure.StructurePiece> genStruct(BlockState state, List<MultiBlockStructure.StructurePiece> list) {
        for (int x = 0; x <= 1; x++) {
            for (int y = 0; y <= 1; y++) {
                for (int z = 0; z <= 1; z++) {
                    list.add(new MultiBlockStructure.StructurePiece(x, y, z, state));
                }
            }
        }

        return list;
    }

    public CurseWardingBoxBlockEntity(BlockPos pos, BlockState state) {
        this(DogmaBlockEntityTypeRegistry.CURSE_WARDING_BOX.get(), STRUCTURE.get(), pos, state);
    }

    public CurseWardingBoxBlockEntity(BlockEntityType<?> type, MultiBlockStructure structure, BlockPos pos, BlockState state) {
        super(type, structure, pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        if (item != null && !isOpen()) {
            progress++;
            setChanged();
        }

        if (isOpening && openingTimer > 0) {
            openingTimer--;
            setChanged();
        }

        if (isOpen() && progress > 20 * 10) {
            Direction direction = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
            Vec3 offset = switch (direction) {
                case EAST -> new Vec3(2, 1, 0.5);
                case WEST -> new Vec3(-1, 1, 0.5);
                case NORTH -> new Vec3(0.5, 1, -1);
                default -> new Vec3(0.5, 1, 2);
            };

            spawnEntity(level, getBlockPos().getCenter().add(offset));
        }

    }

    public void changeState() {
        isClosing = isOpen();
        isOpening = !isOpen();

        openingTimer = 20 * 2;
        setChanged();
    }

    public boolean spawnEntity(Level level, Vec3 spawnPos) {
        boolean bl = false;
        EntityType<?> entityType = MeatToEntityDataReloadListener.getEntity(item);
        if (entityType != null && getLevel() != null) {
            Entity entity = entityType.create(getLevel());
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.setPos(spawnPos);//TODO rotate and place in front

                AbyssLivingEntityDataCapability.setRevived(livingEntity, progress);
                level.addFreshEntity(livingEntity);
                bl = true;
            }
        }
        item = null;
        progress = 0;
        setChanged();
        return bl;
    }

    @Override
    public InteractionResult onUse(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        boolean exit = false;
        for (Map.Entry<ResourceLocation, MeatToEntityDataReloadListener.MeatData> i : MeatToEntityDataReloadListener.MEAT_DATA.entrySet()) {

            if (exit) {
                break;
            }

            for (Item item : i.getValue().items) {
                if (item.equals(itemStack.getItem())) {
                    this.item = item;
                    itemStack.shrink(1);
                    exit = true;
                    break;
                }
            }
        }

        return super.onUse(player, hand);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        pTag.putBoolean(Constants.Nbt.OPEN, this.isOpening);
        pTag.putBoolean(Constants.Nbt.CLOSING, this.isClosing);
        pTag.putInt(Constants.Nbt.OPENING_TIMER, this.openingTimer);

        if (item != null) {
            pTag.put(Constants.Nbt.ITEM, this.item.getDefaultInstance().save(new CompoundTag()));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        this.isOpening = pTag.getBoolean(Constants.Nbt.OPEN);
        this.isClosing = pTag.getBoolean(Constants.Nbt.CLOSING);
        this.openingTimer = pTag.getInt(Constants.Nbt.OPENING_TIMER);

        CompoundTag itemTag = pTag.getCompound(Constants.Nbt.ITEM);
        if (!itemTag.isEmpty()) {
            this.item = ItemStack.of(itemTag).getItem();
        }
    }

    public boolean isOpen() {
        return isOpening && openingTimer == 0;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return super.getRenderBoundingBox().inflate(2);
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, state -> {

            if (this.isOpening && !this.isClosing) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("opening"));
            } else if (this.isClosing) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("closing"));
            }

            return state.setAndContinue(RawAnimation.begin().thenPlay("closed"));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
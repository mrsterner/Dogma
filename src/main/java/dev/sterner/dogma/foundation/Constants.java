package dev.sterner.dogma.foundation;

import dev.sterner.dogma.Dogma;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public class Constants {

    public interface DataTrackers {
        EntityDataAccessor<CompoundTag> PLAYER_CORPSE_ENTITY = SynchedEntityData.defineId(Player.class, EntityDataSerializers.COMPOUND_TAG);
    }

    public interface Tags {
        TagKey<EntityType<?>> BUTCHERABLE = TagKey.create(Registries.ENTITY_TYPE, Dogma.id("butcherable"));
        TagKey<EntityType<?>> CAGEABLE_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, Dogma.id("cageable_blacklist"));

        TagKey<EntityType<?>> SOUL_WEAK = TagKey.create(Registries.ENTITY_TYPE, Dogma.id("soul_weak"));
        TagKey<EntityType<?>> SOUL_REGULAR = TagKey.create(Registries.ENTITY_TYPE, Dogma.id("soul_regular"));
        TagKey<EntityType<?>> SOUL_STRONG = TagKey.create(Registries.ENTITY_TYPE, Dogma.id("soul_strong"));
    }

    public interface Nbt {
        String CHUNK_X = "ChunkX";
        String CHUNK_Z = "ChunkZ";
        String CHUNK_LIST = "ChunkList";
        String TOP_Y = "TopY";
        String BOTTOM_Y = "BottomY";
        String CURSE = "Curse";
        String CURSE_INTENSITY = "CurseIntensity";
        String COOLDOWN = "Cooldown";
        String CURRENT_Y = "CurrentY";
        String IMMUNE = "Immune";
        String IS_IN_CURSE = "IsInCurse";
        String TIME_Y = "TimeY";
        String TIME_AGE = "TimeAge";
        String ABYSS = "Abyss";
        String ITEM = "Item";
        String ENTITY = "Entity";
        String OPEN = "Open";
        String IS_REVIVED = "IsRevived";
        String REVIVED_TIMER = "RevivedTimer";
        String OPENING_TIMER = "OpeningTimer";
        String CLOSING = "Closing";
        String HOST = "Host";

        //Necro
        String CORPSE_ENTITY = "CorpseEntity";
        String HOOKED_AGE = "HookedAge";
        String ALL_BLACK = "AllBlack";
        String HAS_LEGEMETON = "HasBotD";
        String HAS_EMERALD_TABLET = "HasEmeraldTablet";
        String NECRO_RITUAL = "NecroRitual";
        String TIMER = "Timer";
        String RITUAL_POS = "RitualPos";
        String PEDESTAL_ITEM = "PedestalItem";
        String CRAFTING = "Crafting";
        String RITUAL_RECIPE = "RitualRecipe";
        String SHOULD_RUN = "ShouldRun";
        String CLIENT_TIMER = "ClientTimer";
        String DURATION = "Duration";
        String TARGET_Y = "TargetY";
        String BUTCHERING_LEVEL = "ButcheringLevel";
        String IS_CORPSE = "IsCorpse";
        String HEAD_VISIBLE = "HeadVisible";
        String RIGHT_ARM_VISIBLE = "RightArmVisible";
        String LEFT_ARM_VISIBLE = "LeftArmVisible";
        String RIGHT_LEG_VISIBLE = "RightLegVisible";
        String LEFT_LEG_VISIBLE = "LeftLegVisible";
        String CONTRACT = "Contract";
        String NAME = "Name";
        String UUID = "Uuid";
        String STATUS_EFFECT_INSTANCE = "StatusEffectInstance";
        String STATUS_EFFECT = "StatusEffect";
        String AMPLIFIER = "Amplifier";
        String MORPHINE = "Morphine";
        String ADRENALINE = "Adrenaline";
        String FILTHY = "Filthy";
        String CLEANING = "Cleaning";
        String LATTER = "Latter";
        String NECROMANCER_LEVEL = "NecromancerLevel";
        String HEALTH_DEBUFF = "HealthDebuff";
        String SATURATION_DEBUFF = "SaturationDebuff";
        String EXPERIENCE_DEBUFF = "ExperienceDebuff";
        String AGGRESSION_DEBUFF = "AggressionDebuff";
        String REPUTATION_DEBUFF = "ReputationDebuff";
        String MOB_SPAWN_RATE_DEBUFF = "MobSpawnRateDebuff";
        String INSANITY_DEBUFF = "InsanityDebuff";
        String UNDEAD_AGGRESSION_BUFF = "UndeadAggressionBuff";
        String RESISTANCE_BUFF = "ResistanceBuff";
        String NECRO_AURA_BUFF = "NecroAuraBuff";
        String EXTRA_LIVES = "ExtraLives";
        String DISPATCHED_MINIONS = "DispatchedMinions";
        String PHANTOM_IMMUNITY = "PhantomImmunity";
        String DEATHS_TOUCH_BUFF = "DeathsTouch";
        String VARIANT = "Variant";
        String OWNER = "Owner";
        String MODE = "Mode";
        String BLOOD = "Blood";
        String STORED_ENTITY = "StoredEntity";
        String IS_LICH = "IsLich";
        String ENTANGLED = "Entangled";
        String ID = "Id";
        String BRAIN = "Brain";
        String LIQUID_TYPE = "LiquidType";
        String LIQUID_LEVEL = "LiquidLevel";
        String BLOOD_LEVEL = "BloodLevel";
        String SKIN_PROFILE = "SkinProfile";
        String COLOR = "Color";
        String HEAT_TIMER = "HeatTimer";
        String PROGRESS = "Progress";
        String HAS_LIQUID = "HasLiquid";
        String IS_NECRO = "IsNecro";
        String REFRESH = "Refresh";
        String CHANCE = "Chance";
        String CHANCES = "Chances";
        String KNOWLEDGE = "Knowledge";
        String KNOWLEDGE_DATA = "KnowledgeData";
        String POINTS = "Points";
        String IS_SPECIAL = "IsSpecial";
        String SANITY = "Sanity";
        String ADVICE = "Advice";
    }

    public class Values {
        public static final int BLEEDING = 120;//TODO check value
    }
}

package dev.sterner.dogma.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Optional;

public interface IHauler {
    static Optional<IHauler> of(Object context) {
        if (context instanceof IHauler) {
            return Optional.of(((IHauler) context));
        }
        return Optional.empty();
    }

    /**
     * Gets the living entity version of the corpse
     *
     * @return corpse
     */
    LivingEntity getCorpseEntity(Level level);

    /**
     * Gets the saved Nbt of the corpse
     *
     * @return nbt
     */
    CompoundTag getCorpseData();

    /**
     * Sets the IHaulers corpse from a living entity
     *
     * @param corpse to be corpse
     */
    void setCorpseEntity(Player player, LivingEntity corpse);

    void setCorpseData(CompoundTag nbt);

    /**
     * @return boolean if the corpses head is visible
     */
    default boolean getHeadVisible() {
        return true;
    }

    /**
     * @return boolean if the corpses right arm is visible
     */
    default boolean getRArmVisible() {
        return true;
    }

    /**
     * @return boolean if the corpses left arm is visible
     */
    default boolean getLArmVisible() {
        return true;
    }

    /**
     * @return boolean if the corpses right leg is visible
     */
    default boolean getRLegVisible() {
        return true;
    }

    /**
     * @return boolean if the corpses left leg is visible
     */
    default boolean getLLegVisible() {
        return true;
    }

    /**
     * Set if the head of the corpse should be visible
     */
    void setHeadVisible(boolean visible);

    /**
     * Set if the right arm of the corpse should be visible
     */
    void setRArmVisible(boolean visible);

    /**
     * Set if the left arm of the corpse should be visible
     */
    void setLArmVisible(boolean visible);

    /**
     * Set if the right leg of the corpse should be visible
     */
    void setRLegVisible(boolean visible);

    /**
     * Set if the left leg of the corpse should be visible
     */
    void setLLegVisible(boolean visible);

    /**
     * Resets all the corpses body parts to visible
     */
    default void setAllVisible() {
        setHeadVisible(true);
        setLArmVisible(true);
        setLLegVisible(true);
        setRArmVisible(true);
        setRLegVisible(true);
    }
}

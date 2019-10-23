package com.deflatedpickle.bugmagic.api;

import com.deflatedpickle.bugmagic.common.item.ItemWand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;

/**
 * A spell that can be cast with the {@link ItemWand}
 *
 * @author DeflatedPickle
 */
public abstract class ASpell extends IForgeRegistryEntry.Impl<ASpell> {
    public abstract @NotNull String getName();

    /**
     * The amount of mana this spell costs to cast
     *
     * @return The mana cost
     */
    public abstract int getManaCost();

    /**
     * The amount of time it will take to cast this spell
     *
     * @return The casting time
     */
    public int getCastingTime() {
        return 32;
    }

    /**
     * The maximum amount of times this spell can be simultaneously cast
     *
     * @return The max count
     */
    // TODO: Make use of this
    public int getMaxCount() {
        return 1;
    }

    /**
     * The amount of time in between being able to cast this spell
     *
     * @return The time before this spell can be cast again
     */
    public int getMaxCooldown() {
        return 1;
    }

    /**
     * An enum of the spell tiers
     */
    public enum Tier {
        COMMON,
        RARE,
        /**
         * A debug tier
         */
        DEBUG
    }

    /**
     * The tier of the spell. The higher the {@link Tier#ordinal()}, the more shapes are projected, increasing in size
     *
     * @return The spell tier
     */
    public abstract @NotNull Tier getTier();

    /**
     * The particle spouted from end of the wand whilst this spell is being cast
     *
     * @return The casting particle
     */
    public @Nullable EnumParticleTypes getCastingParticle() {
        return null;
    }

    /**
     * The particle emitted from the players location when the casting finishes
     *
     * @return The finishing particle
     */
    public @Nullable EnumParticleTypes getFinishingParticle() {
        return null;
    }

    /**
     * The shape projected under the player during casting
     *
     * @return The casting shape
     */
    public int getCastingShapePoints() {
        return 8;
    }

    public float getRadius() {
        return 1.2f;
    }

    public Pair<@NotNull Float, @NotNull Float> getRadiusMultiplier() {
        return Pair.of(1.6f, 1.8f);
    }

    /**
     * The thickness of the casting shape
     *
     * @return The casting shape thickness
     */
    public float getCastingShapeThickness() {
        return 0.5f;
    }

    /**
     * A range that's linearly interpolated between from when the casting begins to when it ends
     *
     * @return The spin speed ramp
     */
    public @NotNull Pair<@NotNull Float, @NotNull Float> getSpinSpeedRamp() {
        return Pair.of(1f, 1f);
    }

    // TODO: Add a method for a pulsating effect that takes a Pair specifying the small and big speeds

    /**
     * The intensity of the casting shapes glow
     *
     * @return The glow intensity
     */
    public float getGlowIntensity() {
        return 0.2f;
    }

    /**
     * The colour of the glow
     *
     * @return The glow colour
     */
    public @NotNull ReadableColor getGlowColour() {
        return Color.WHITE;
    }

    /**
     * Casts this spell
     */
    public abstract void cast();

    /**
     * Uncasts this spell
     */
    public abstract void uncast();
}

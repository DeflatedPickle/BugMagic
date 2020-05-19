/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.capability;

import com.deflatedpickle.bugmagic.api.spell.Spell;
import com.deflatedpickle.bugmagic.common.capability.SpellCasterCapability;
import java.util.HashMap;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An interface for things that can cast spells
 *
 * <p>Officially, this is for items, such as a wand, but this count be added to anything
 *
 * @author DeflatedPickle
 * @see SpellLearner
 * @see SpellCasterCapability
 */
public interface SpellCaster {
  /**
   * Returns the current amount of each spell casted
   *
   * <p>Newly cast spells will increase the count of the spell Uncast spells will decrease the
   * count, unless decreasing it would result in 0, then the spell key is removed.
   *
   * @return A map of cast spells.
   */
  // TODO: Save/load this map on log out/in
  @NotNull
  HashMap<Spell, Integer> getCastSpellMap();

  /**
   * Sets the owner of the spell caster
   *
   * <p>Officially, this is for a player that owns a wand, but if applied to a player, this would be
   * the player. The player would need to be a {@link SpellLearner} for this to be of much use.
   *
   * @param value The ID of the owner.
   */
  void setOwner(@Nullable UUID value);

  /**
   * Gets the owner of the spell caster
   *
   * @return The ID of the owner
   */
  @Nullable
  UUID getOwner();

  /**
   * Sets the spell caster as casting
   *
   * @param value The value to use
   */
  void setCasting(boolean value);

  /**
   * Whether the spell caster is casting
   *
   * @return The casting state
   */
  boolean isCasting();

  /**
   * Sets the current time for which the spell caster has been casting
   *
   * @param value The current cast time
   */
  void setCastingCurrent(float value);

  /**
   * Gets the time the spell caster has been casting
   *
   * @return The current cast time
   */
  float getCastingCurrent();
}

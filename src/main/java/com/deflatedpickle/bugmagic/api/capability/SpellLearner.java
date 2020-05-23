/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.capability;

import com.deflatedpickle.bugmagic.api.spell.Spell;
import com.deflatedpickle.bugmagic.common.capability.SpellLearnerCapability;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An interface for things that can learn spells
 *
 * @author DeflatedPickle
 * @see SpellCaster
 * @see SpellLearnerCapability
 */
public interface SpellLearner {
  /**
   * Sets the list of spells
   *
   * @param value A list of spells
   */
  void setSpellList(@NotNull List<Spell> value);

  /**
   * A list of learnt spells
   *
   * @return The list of spells
   */
  @NotNull
  List<Spell> getSpellList();

  /**
   * Sets the current index
   *
   * @param value The value to use
   */
  void setCurrentIndex(int value);

  /**
   * Gets the current index
   *
   * @return The current index
   */
  int getCurrentIndex();

  /**
   * Gets the spell currently selected
   *
   * <p>This method is simply a shortcut to ease use
   *
   * @return The spell currently selected
   */
  default @Nullable Spell getCurrentSpell() {
    if (this.getSpellList().size() <= 0) return null;
    return this.getSpellList().get(this.getCurrentIndex());
  }

  /**
   * Learns and returns a spell
   *
   * @param spell The spell
   * @return The spell
   */
  Spell learnSpell(@NotNull Spell spell);
}

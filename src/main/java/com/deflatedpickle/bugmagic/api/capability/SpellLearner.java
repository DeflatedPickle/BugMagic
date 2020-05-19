/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.capability;

import com.deflatedpickle.bugmagic.api.spell.Spell;
import java.util.List;

import com.deflatedpickle.bugmagic.common.capability.SpellLearnerCapability;
import org.jetbrains.annotations.NotNull;

/**
 * An interface for things that can learn spells
 *
 * @author DeflatedPickle
 * @see SpellCaster
 * @see SpellLearnerCapability
 */
public interface SpellLearner {
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
  default Spell getCurrentSpell() {
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

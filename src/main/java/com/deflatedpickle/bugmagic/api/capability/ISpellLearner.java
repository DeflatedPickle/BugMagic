/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.capability;

import com.deflatedpickle.bugmagic.api.spell.ASpell;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * An interface for things that can learn spells
 *
 * @author DeflatedPickle
 * @see ISpellCaster
 * @see com.deflatedpickle.bugmagic.common.capability.SpellLearner
 */
public interface ISpellLearner {
  /**
   * A list of learnt spells
   *
   * @return The list of spells
   */
  @NotNull
  List<ASpell> getSpellList();

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
  default ASpell getCurrentSpell() {
    return this.getSpellList().get(this.getCurrentIndex());
  }

  /**
   * Learns and returns a spell
   *
   * @param spell The spell
   * @return The spell
   */
  ASpell learnSpell(@NotNull ASpell spell);
}

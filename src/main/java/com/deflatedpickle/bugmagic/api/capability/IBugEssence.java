/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.capability;

/**
 * An interface for things with bug essence
 *
 * @author DeflatedPickle
 * @see com.deflatedpickle.bugmagic.common.capability.BugEssenceCapability
 */
public interface IBugEssence {
  /**
   * Sets the maximum bug essence
   *
   * @param value The value to use
   */
  void setMax(int value);

  /**
   * Gets the maximum bug essence
   *
   * @return The max value
   */
  int getMax();

  /**
   * Sets the current bug essence
   *
   * @param value The value to use
   */
  void setCurrent(int value);

  /**
   * Gets the current bug essence
   *
   * @return The current value
   */
  int getCurrent();
}

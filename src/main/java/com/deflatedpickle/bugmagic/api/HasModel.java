/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api;

public interface HasModel {
  default boolean hasModel() {
    return false;
  }
}

/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api;

import java.util.List;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * Applied to a block that should render multiple bounding boxes
 *
 * @see com.deflatedpickle.bugmagic.common.block.SpellTable
 */
public interface IBoundingBox {
  /**
   * A list of bounding boxes to render
   *
   * @return A list of bounding boxes
   */
  List<AxisAlignedBB> getBoundingBoxList();
}

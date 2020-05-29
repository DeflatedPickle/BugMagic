/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api;

import com.deflatedpickle.bugmagic.common.block.SpellTableBlock;
import com.deflatedpickle.bugmagic.client.event.BlockHighlightEventHandler;

import java.util.List;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;

/**
 * Applied to a block that should render multiple bounding boxes
 *
 * @author DeflatedPickle
 * @see BlockHighlightEventHandler#onDrawBlockHighlight(DrawBlockHighlightEvent)
 * @see SpellTableBlock
 */
public interface BoundingBox {
  /**
   * A list of bounding boxes to render
   *
   * @return A list of bounding boxes
   */
  List<AxisAlignedBB> getBoundingBoxList();
}

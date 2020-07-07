package com.deflatedpickle.bugmagic.api;

import com.deflatedpickle.bugmagic.api.common.block.TotemBlock;

/**
 * The type of a {@link TotemBlock}
 */
public enum TotemType {
	/**
	 * Gathers bug essence from the surroundings
	 * Doesn't require feed
	 */
	GATHERER,
	/**
	 * Has bug essence dumped into it
	 */
	DUMP,
	/**
	 * Generates bug essence by using input
	 * Feeds off items and/or blocks
	 */
	GENERATOR,
}

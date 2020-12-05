/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.spell;

import com.deflatedpickle.bugmagic.Reference;
import com.deflatedpickle.bugmagic.api.common.spell.SpellIngredient;
import com.deflatedpickle.bugmagic.common.init.FluidInit;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A recipe for a {@link Spell}
 *
 * @author DeflatedPickle
 */
public abstract class SpellRecipe extends IForgeRegistryEntry.Impl<SpellRecipe> {
  public SpellRecipe() {
    this.setRegistryName(
        Reference.MOD_ID, this.getSpell().getName().toLowerCase().replace(' ', '_'));
  }

  public abstract @NotNull Spell getSpell();

  /**
   * The ingredients to craft this recipe
   *
   * @return A list of item stacks
   */
  public @Nullable List<SpellIngredient> getIngredients() {
    return null;
  }

	/**
	 * The kind of fluid required to craft this recipe
	 * @return The fluid instance
	 */
	public String getFluidType() {
  	return FluidInit.INSTANCE.getBUG_ESSENCE().getUnlocalizedName();
  }

  /**
   * The amount of fluid, in milli-bucket, to craft the recipe
   *
   * @return The fluid in MBs
   */
  public int getFluidAmount() {
    return 200;
  }

	/**
	 * The amount of ink needed to write this recipe
	 * @return The amount of ink
	 */
	public float getInkAmount() {
  	return 1f / (Spell.Tier.values().length - this.getSpell().getTier().ordinal());
	}

  /**
   * The amount of times the block must be clicked
   *
   * @return The amount of clicks
   */
  public int getCraftingTime() {
    return 60;
  }
}

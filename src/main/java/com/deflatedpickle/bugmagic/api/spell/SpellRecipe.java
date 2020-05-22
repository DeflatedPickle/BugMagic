/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.spell;

import com.deflatedpickle.bugmagic.Reference;
import com.deflatedpickle.bugmagic.api.common.spell.SpellIngredient;
import java.util.ArrayList;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
  public @Nullable ArrayList<SpellIngredient> getIngredients() {
    return null;
  }

  /**
   * The amount of fluid, in milli-bucket, to craft tbe recipe
   *
   * @return The fluid in MBs
   */
  public int getFluidAmount() {
    return 1000;
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

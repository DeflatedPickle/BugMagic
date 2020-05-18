package com.deflatedpickle.bugmagic.api.spell;

import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class SpellRecipe extends IForgeRegistryEntry.Impl<SpellRecipe> {
    public SpellRecipe() {
        this.setRegistryName(this.getSpell().getName().toLowerCase().replace(' ', '_'));
    }

    public abstract @NotNull Spell getSpell();

    /**
     * The ingredients to craft this recipe
     *
     * @return A list of item stacks
     */
    public ArrayList<ItemStack> getIngredients() {
        return new ArrayList<>();
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

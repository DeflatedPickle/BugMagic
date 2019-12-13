package com.deflatedpickle.bugmagic.api.entity.mob;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A super of {@link EntityTameable} for simplicity
 *
 * @author DeflatedPickle
 */
public class EntityCastable extends EntityTameable {
    public EntityCastable(World worldIn) {
        super(worldIn);

        this.isImmuneToFire = true;

        this.enablePersistence();
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canBeLeashedTo(EntityPlayer player) {
        return false;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Nullable
    @Override
    public EntityAgeable createChild(@NotNull EntityAgeable ageable) {
        return null;
    }
}

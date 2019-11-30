package com.deflatedpickle.bugmagic.api;

import com.deflatedpickle.bugmagic.common.item.Wand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A spell that can be cast with the {@link Wand}
 *
 * @author DeflatedPickle
 */
public abstract class ASpell extends IForgeRegistryEntry.Impl<ASpell> {
    public ASpell() {
        this.setRegistryName(this.getName().toLowerCase().replace(' ', '_'));
    }

    public abstract @NotNull String getName();

    /**
     * The amount of mana this spell costs to cast
     *
     * @return The mana lost
     */
    public abstract int getManaLoss();

    /**
     * The amount of mana this spell rewards to uncast
     *
     * @return The mana gained
     */
    public int getManaGain() {
        return (getManaLoss() / 3) * 2;
    }

    /**
     * The amount of time it will take to cast this spell
     *
     * @return The casting time
     */
    public int getCastingTime() {
        return 32;
    }

    /**
     * The maximum amount of times this spell can be simultaneously cast
     *
     * @return The max count
     */
    public int getMaxCount() {
        return 1;
    }

    /**
     * The amount of time in between being able to cast this spell
     *
     * @return The time before this spell can be cast again
     */
    public int getMaxCooldown() {
        return 32;
    }

    /**
     * An enum of the spell tiers
     */
    public enum Tier {
        COMMON,
        RARE,
        /**
         * A debug tier
         */
        DEBUG
    }

    /**
     * The tier of the spell. The higher the {@link Tier#ordinal()}, the more shapes are projected, increasing in size
     *
     * @return The spell tier
     */
    public abstract @NotNull Tier getTier();

    /**
     * An enum of the spell types
     */
    public enum Type {
        /**
         * Spawns a mob
         */
        CONJURE,
        /**
         * Changes the player
         */
        AUGMENT
    }

    /**
     * The type of spell
     *
     * @return The spell type
     */
    public @NotNull Type getType() {
        return Type.CONJURE;
    }

    public enum Cult {
        ANY(TextFormatting.WHITE),
        /**
         * Spiders, etc
         */
        ARACHNID(TextFormatting.RED),
        /**
         * Snails, etc
         */
        GASTROPOD(TextFormatting.BLUE),
        /**
         * Bees, Wasps, etc
         */
        APOIDEA(TextFormatting.YELLOW),
        /**
         * Beetles, etc
         */
        INSECT(TextFormatting.DARK_AQUA),
        /**
         * Mushrooms, mob controller, etc
         */
        FUNGAL(TextFormatting.DARK_PURPLE);

        public final TextFormatting colour;

        Cult(TextFormatting colour) {
            this.colour = colour;
        }
    }

    /**
     * Spells can only be successful if the caster belongs to the cult, otherwise the spell is messed up
     *
     * @return The cult type
     */
    public @NotNull Cult getCult() {
        return Cult.ANY;
    }

    /**
     * The particle emitted randomly around the player whilst this spell is being cast
     *
     * @return The casting particle
     */
    public @Nullable EnumParticleTypes getCastingParticle() {
        return EnumParticleTypes.CRIT_MAGIC;
    }

    /**
     * The particle emitted from the players location if the casting is canceled
     *
     * @return The canceling particle
     */
    public @Nullable EnumParticleTypes getCancelingParticle() {
        return EnumParticleTypes.EXPLOSION_HUGE;
    }

    /**
     * The particle emitted from the players location when the casting finishes
     *
     * @return The finishing particle
     */
    public @Nullable EnumParticleTypes getFinishingParticle() {
        return EnumParticleTypes.DRAGON_BREATH;
    }

    /**
     * The particle emitted from any entities when the spell is uncast
     *
     * @return The uncasting particle
     */
    public @Nullable EnumParticleTypes getUncastingParticle() {
        return EnumParticleTypes.SMOKE_NORMAL;
    }

    /**
     * The shape projected under the player during casting
     *
     * @return The casting shape
     */
    public int getCastingShapePoints() {
        return 8;
    }

    public float getRadius() {
        return 1.2f;
    }

    public Pair<@NotNull Float, @NotNull Float> getRadiusMultiplier() {
        return Pair.of(1.6f, 1.8f);
    }

    /**
     * The thickness of the casting shape
     *
     * @return The casting shape thickness
     */
    public float getCastingShapeThickness() {
        return 0.5f;
    }

    /**
     * The intensity of the casting shapes glow
     *
     * @return The glow intensity
     */
    public float getGlowIntensity() {
        return 0.2f;
    }

    /**
     * The colour of the glow
     *
     * @return The glow colour
     */
    public @NotNull ReadableColor getGlowColour() {
        return Color.WHITE;
    }

    // TODO: Separate cast/uncast methods for the client and server

    /**
     * Casts this spell
     */
    public abstract void cast(EntityPlayer entityPlayer, ItemStack wandIn);

    /**
     * Uncasts this spell
     */
    public abstract void uncast(EntityPlayer entityPlayer, ItemStack wandIn);

    /**
     * Summons an given amount of entity
     *
     * @param entityClass  The class of the entity
     * @param entityPlayer The player
     * @param wandIn       The wand
     * @param count        The amount of entities that will be spawned
     * @param <T>          The type of entity
     * @return A list of the summoned entities
     */
    public <T extends EntityTameable> List<T> summonEntity(Class<T> entityClass, EntityPlayer entityPlayer, ItemStack wandIn, Integer count) {
        if (!wandIn.hasTagCompound()) {
            wandIn.setTagCompound(new NBTTagCompound());
        }

        List<T> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            T entity = null;
            try {
                entity = entityClass.getConstructor(World.class).newInstance(entityPlayer.world);
            }
            catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            assert entity != null;

            entity.setOwnerId(entityPlayer.getUniqueID());
            entity.setCustomNameTag("[" + entityPlayer.getName() + "] " + this.getName());
            entity.setAlwaysRenderNameTag(true);
            entity.setPosition(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);

            entityPlayer.world.spawnEntity(entity);

            NBTTagCompound compound = wandIn.getTagCompound();
            NBTTagCompound uuid = NBTUtil.createUUIDTag(entity.getUniqueID());
            NBTTagList listTag = compound.getTagList("uuidList", Constants.NBT.TAG_COMPOUND);
            listTag.appendTag(uuid);
            compound.setTag("uuidList", listTag);

            list.add(entity);
        }

        return list;
    }

    /**
     * Kills all entities that are currently summoned
     *
     * @param wand  The wand
     * @param world The world
     * @return A list of killed entities
     */
    public List<EntityTameable> killAllEntities(ItemStack wand, WorldServer world) {
        assert wand.getTagCompound() != null;
        NBTTagList uuidList = wand.getTagCompound().getTagList("uuidList", Constants.NBT.TAG_COMPOUND);

        List<EntityTameable> list = new ArrayList<>();

        EntityTameable entity;
        for (int i = 0; i < uuidList.tagCount(); i++) {
            UUID uuid = NBTUtil.getUUIDFromTag(uuidList.getCompoundTagAt(i));
            entity = (EntityTameable) world.getEntityFromUuid(uuid);

            if (entity != null) {
                list.add(entity);
                entity.setDead();
            }
        }

        return list;
    }
}

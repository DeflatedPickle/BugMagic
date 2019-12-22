/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.spell;

import com.deflatedpickle.bugmagic.common.item.Wand;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

/**
 * A spell that can be cast with something capable of {@link
 * com.deflatedpickle.bugmagic.api.capability.ISpellCaster}
 *
 * <p>Officially, this can be cast with the {@link Wand}, however more spell casters can be added,
 * by making them capable of @{@link com.deflatedpickle.bugmagic.api.capability.ISpellCaster}
 *
 * @author DeflatedPickle
 */
// TODO: Move all rendering and crafting methods to new classes
public abstract class ASpell extends IForgeRegistryEntry.Impl<ASpell> {
  public ASpell() {
    this.setRegistryName(this.getName().toLowerCase().replace(' ', '_'));
  }

  /**
   * Gets the name of the spell
   *
   * @return The name
   */
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
   * The amount of mobs this spell will summon
   *
   * @return The amount casted
   */
  public int getCastCount() {
    return 1;
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

  /** An enum of the spell tiers */
  public enum Tier {
    COMMON,
    UNCOMMON,
    RARE,
    LEGENDARY,
    /** A debug tier */
    DEBUG
  }

  /**
   * The tier of the spell. The higher the {@link Tier#ordinal()}, the more shapes are projected,
   * increasing in size
   *
   * @return The spell tier
   */
  public abstract @NotNull Tier getTier();

  /** An enum of the spell types */
  public enum Type {
    /** Spawns a mob */
    CONJURE,
    /** Changes the player */
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

  /** An enum of the cults */
  public enum Cult {
    ANY(TextFormatting.WHITE),
    /** Spiders, etc */
    ARACHNID(TextFormatting.RED),
    /** Snails, etc */
    GASTROPOD(TextFormatting.BLUE),
    /** Bees, Wasps, etc */
    APOIDEA(TextFormatting.YELLOW),
    /** Beetles, etc */
    INSECT(TextFormatting.DARK_AQUA),
    /** Mushrooms, mob controller, etc */
    FUNGAL(TextFormatting.DARK_PURPLE);

    /** The colour of the cult */
    public final TextFormatting colour;

    Cult(TextFormatting colour) {
      this.colour = colour;
    }
  }

  /**
   * Returns the cult of the spell
   *
   * <p>Spells from a cult different from yours will be more expensive to cast They will also return
   * less mana when uncasted
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
   * @see com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
   */
  public @Nullable EnumParticleTypes getCastingParticle() {
    return EnumParticleTypes.CRIT_MAGIC;
  }

  /**
   * The particle emitted from the players location if the casting is canceled
   *
   * @return The canceling particle
   * @see com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
   */
  public @Nullable EnumParticleTypes getCancelingParticle() {
    return EnumParticleTypes.EXPLOSION_HUGE;
  }

  /**
   * The particle emitted from the players location when the casting finishes
   *
   * @return The finishing particle
   * @see com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
   */
  public @Nullable EnumParticleTypes getFinishingParticle() {
    return EnumParticleTypes.DRAGON_BREATH;
  }

  /**
   * The particle emitted from any entities when the spell is uncast
   *
   * @return The uncasting particle
   * @see com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
   */
  public @Nullable EnumParticleTypes getUncastingParticle() {
    return EnumParticleTypes.SMOKE_NORMAL;
  }

  /**
   * The shape projected under the player during casting
   *
   * @return The casting shape
   * @see com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
   */
  public int getCastingShapePoints() {
    return 8;
  }

  /**
   * The base radius of the shape
   *
   * @return The radius
   * @see com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
   */
  public float getRadius() {
    return 1.2f;
  }

  /**
   * Two values that are linearly interpolated between, that the radius is multiplied by
   *
   * @return A pair containing the lower and higher values
   * @see com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
   */
  public Pair<@NotNull Float, @NotNull Float> getRadiusMultiplier() {
    return Pair.of(1.6f, 1.8f);
  }

  /**
   * The thickness of the casting shape
   *
   * @return The casting shape thickness
   * @see com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
   */
  public float getCastingShapeThickness() {
    return 0.5f;
  }

  /**
   * The intensity of the casting shapes glow
   *
   * @return The glow intensity
   * @see com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
   */
  public float getGlowIntensity() {
    return 0.2f;
  }

  /**
   * The colour of the glow
   *
   * @return The glow colour
   * @see com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
   */
  public @NotNull ReadableColor getGlowColour() {
    return Color.WHITE;
  }

  /**
   * The ingredients to craft this recipe
   *
   * @return A list of item stacks
   */
  public ArrayList<ItemStack> getCraftingIngredients() {
    return new ArrayList<>();
  }

  /**
   * The amount of fluid, in milli-bucket, to craft tbe recipe
   *
   * @return The fluid in MBs
   */
  public int getCraftingFluidAmount() {
    return 1000;
  }

  /**
   * The amount of time (in seconds) that the recipe takes to craft
   *
   * @return The time in seconds
   */
  public int getCraftingTime() {
    return 20 * 30;
  }

  /**
   * Casts this spell
   *
   * @see com.deflatedpickle.bugmagic.api.capability.ISpellCaster
   * @see Wand
   */
  // TODO: Separate cast/uncast methods for the client and server
  public abstract void cast(EntityPlayer entityPlayer, ItemStack wandIn);

  /**
   * Uncasts this spell
   *
   * @see com.deflatedpickle.bugmagic.api.capability.ISpellCaster
   * @see Wand
   */
  public abstract void uncast(EntityPlayer entityPlayer, ItemStack wandIn);

  /**
   * Summons a given amount of an entity
   *
   * @param entityClass The class of the entity
   * @param entityPlayer The player
   * @param wandIn The wand
   * @param <T> The type of entity
   * @return A list of the summoned entities
   */
  public <T extends EntityTameable> List<T> summonEntity(
      Class<T> entityClass, EntityPlayer entityPlayer, ItemStack wandIn) {
    if (!wandIn.hasTagCompound()) {
      wandIn.setTagCompound(new NBTTagCompound());
    }

    List<T> list = new ArrayList<>();

    for (int i = 0; i < this.getCastCount(); i++) {
      T entity = null;
      try {
        entity = entityClass.getConstructor(World.class).newInstance(entityPlayer.world);
      } catch (InstantiationException
          | NoSuchMethodException
          | InvocationTargetException
          | IllegalAccessException e) {
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
      assert compound != null;
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
   * @param entityClass The class of the entity
   * @param entityPlayer The player
   * @param wand The wand
   * @return A list of killed entities
   */
  public <T extends EntityTameable> List<EntityTameable> killAllEntities(
      Class<T> entityClass, EntityPlayer entityPlayer, ItemStack wand) {
    assert wand.getTagCompound() != null;
    NBTTagList uuidList = wand.getTagCompound().getTagList("uuidList", Constants.NBT.TAG_COMPOUND);

    List<EntityTameable> list = new ArrayList<>();

    EntityTameable entity;
    for (int i = 0; i < uuidList.tagCount(); i++) {
      UUID uuid = NBTUtil.getUUIDFromTag(uuidList.getCompoundTagAt(i));
      entity = (EntityTameable) ((WorldServer) entityPlayer.world).getEntityFromUuid(uuid);

      if (entityClass.isInstance(entity) && entity.isOwner(entityPlayer)) {
        list.add(entity);
        entity.setDead();
      }
    }

    return list;
  }
}

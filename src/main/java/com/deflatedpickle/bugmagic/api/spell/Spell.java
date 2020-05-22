/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.spell;

import com.deflatedpickle.bugmagic.Reference;
import com.deflatedpickle.bugmagic.api.capability.SpellCaster;
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
 * A spell that can be cast with something capable of {@link SpellCaster}
 *
 * <p>Officially, this can be cast with the {@link Wand}, however more spell casters can be added,
 * by making them capable of @{@link SpellCaster}
 *
 * @author DeflatedPickle
 */
@SuppressWarnings("unused")
public abstract class Spell extends IForgeRegistryEntry.Impl<Spell> {
  private @NotNull String name;
  private int manaLoss;
  private int castTime = 32;
  private int castCount = 1;
  private int maxCount = 1;
  private int maxCooldown = 32;
  private @NotNull Tier tier = Tier.COMMON;
  private @NotNull Type type = Type.CONJURE;
  private @NotNull Cult cult = Cult.ANY;
  private @Nullable EnumParticleTypes castingParticle = EnumParticleTypes.CRIT_MAGIC;
  private @Nullable EnumParticleTypes cancelingParticle = EnumParticleTypes.EXPLOSION_HUGE;
  private @Nullable EnumParticleTypes finishingParticle = EnumParticleTypes.DRAGON_BREATH;
  private @Nullable EnumParticleTypes uncastingParticle = EnumParticleTypes.SMOKE_NORMAL;
  // OKAY herobrine i see the stop sign BUT i need to find an OCTAGON
  // if i STOP at the STOP sign, how can i find an octagon?
  // HOW herobrine HOW?!
  // wait a minute...
  // look!
  // this casting shape has 1, 2, 3, 4, 5, 6, 7, 8 glorious SIDES
  // and 1, 2, 3, 4, 5, 6, 7, 8 stunning ANGLES
  // this casting shape is an OCTAGON
  // we found an octagon!
  // OCTAGON uwu
  private int castingShapePoints = 8;
  private float radius = 1.2f;
  private @NotNull Pair<@NotNull Float, @NotNull Float> radiusMultiplier = Pair.of(1.6f, 1.8f);
  private float castingShapeThickness = 0.5f;
  private float glowIntensity = 0.2f;
  private @NotNull ReadableColor glowColor = Color.WHITE;

  public Spell(@NotNull String name) {
    this.name = name;
    this.setRegistryName(Reference.MOD_ID, this.getName().toLowerCase().replace(' ', '_'));
  }

  /**
   * Gets the name of the spell
   *
   * @return The name
   */
  public @NotNull String getName() {
    return this.name;
  }

  protected void setName(String value) {
    this.name = value;
  }

  /**
   * The amount of mana this spell costs to cast
   *
   * @return The mana lost
   */
  public int getManaLoss() {
    return this.manaLoss;
  }

  protected void setManaLoss(int value) {
    this.manaLoss = value;
  }

  /**
   * The amount of mana this spell rewards to uncast
   *
   * @return The mana gained
   */
  public int getManaGain() {
    return (this.getManaLoss() / 3) * 2;
  }

  /**
   * The amount of time it will take to cast this spell
   *
   * @return The casting time
   */
  public int getCastingTime() {
    return this.castTime;
  }

  public void setCastingTime(int value) {
    this.castTime = value;
  }

  /**
   * The amount of mobs this spell will summon
   *
   * @return The amount casted
   */
  public int getCastCount() {
    return this.castCount;
  }

  protected void setCastCount(int value) {
    this.castCount = value;
  }

  /**
   * The maximum amount of times this spell can be simultaneously cast
   *
   * @return The max count
   */
  public int getMaxCount() {
    return this.maxCount;
  }

  protected void setMaxCount(int value) {
    this.maxCount = value;
  }

  /**
   * The amount of time in between being able to cast this spell
   *
   * @return The time before this spell can be cast again
   */
  public int getMaxCooldown() {
    return this.maxCooldown;
  }

  protected void setMaxCooldown(int value) {
    this.maxCooldown = value;
  }

  /** An enum of the spell tiers */
  @SuppressWarnings("unused")
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
  public @NotNull Tier getTier() {
    return this.tier;
  }

  protected void setTier(@NotNull Tier value) {
    this.tier = value;
  }

  /** An enum of the spell types */
  @SuppressWarnings("unused")
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
    return this.type;
  }

  protected void setType(@NotNull Type value) {
    this.type = value;
  }

  /** An enum of the cults */
  @SuppressWarnings("unused")
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
    return this.cult;
  }

  protected void setCult(@NotNull Cult value) {
    this.cult = value;
  }

  /**
   * The particle emitted randomly around the player whilst this spell is being cast
   *
   * @return The casting particle
   * @see com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
   */
  public @Nullable EnumParticleTypes getCastingParticle() {
    return this.castingParticle;
  }

  protected void setCastingParticle(@Nullable EnumParticleTypes value) {
    this.castingParticle = value;
  }

  /**
   * The particle emitted from the players location if the casting is canceled
   *
   * @return The canceling particle
   * @see com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
   */
  public @Nullable EnumParticleTypes getCancelingParticle() {
    return this.cancelingParticle;
  }

  protected void setCancelingParticle(@Nullable EnumParticleTypes value) {
    this.cancelingParticle = value;
  }

  /**
   * The particle emitted from the players location when the casting finishes
   *
   * @return The finishing particle
   * @see com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
   */
  public @Nullable EnumParticleTypes getFinishingParticle() {
    return this.finishingParticle;
  }

  protected void setFinishingParticle(@Nullable EnumParticleTypes value) {
    this.finishingParticle = value;
  }

  /**
   * The particle emitted from any entities when the spell is uncast
   *
   * @return The uncasting particle
   * @see com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
   */
  public @Nullable EnumParticleTypes getUncastingParticle() {
    return this.uncastingParticle;
  }

  protected void setUncastingParticle(@Nullable EnumParticleTypes value) {
    this.uncastingParticle = value;
  }

  /**
   * The shape projected under the player during casting
   *
   * @return The casting shape
   * @see com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
   */
  public int getCastingShapePoints() {
    return this.castingShapePoints;
  }

  protected void setCastingShapePoints(int value) {
    this.castingShapePoints = value;
  }

  /**
   * The base radius of the shape
   *
   * @return The radius
   * @see com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
   */
  public float getRadius() {
    return this.radius;
  }

  protected void setRadius(float value) {
    this.radius = value;
  }

  /**
   * Two values that are linearly interpolated between, that the radius is multiplied by
   *
   * @return A pair containing the lower and higher values
   * @see com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
   */
  public @NotNull Pair<@NotNull Float, @NotNull Float> getRadiusMultiplier() {
    return this.radiusMultiplier;
  }

  protected void setRadiusMultiplier(@NotNull Pair<@NotNull Float, @NotNull Float> value) {
    this.radiusMultiplier = value;
  }

  /**
   * The thickness of the casting shape
   *
   * @return The casting shape thickness
   * @see com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
   */
  public float getCastingShapeThickness() {
    return this.castingShapeThickness;
  }

  protected void setCastingShapeThickness(float value) {
    this.castingShapeThickness = value;
  }

  /**
   * The intensity of the casting shapes glow
   *
   * @return The glow intensity
   * @see com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
   */
  public float getGlowIntensity() {
    return this.glowIntensity;
  }

  protected void setGlowIntensity(float value) {
    this.glowIntensity = value;
  }

  /**
   * The colour of the glow
   *
   * @return The glow colour
   * @see com.deflatedpickle.bugmagic.client.render.entity.layer.LayerCastingShape
   */
  public @NotNull ReadableColor getGlowColour() {
    return this.glowColor;
  }

  protected void setGlowColor(@NotNull ReadableColor value) {
    this.glowColor = value;
  }

  /**
   * Casts this spell
   *
   * @see SpellCaster
   * @see Wand
   */
  // TODO: Separate cast/uncast methods for the client and server
  public abstract void cast(EntityPlayer entityPlayer, ItemStack wandIn);

  /**
   * Uncasts this spell
   *
   * @see SpellCaster
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

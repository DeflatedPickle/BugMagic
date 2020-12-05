/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.entity.mob;

import com.deflatedpickle.bugmagic.BugMagic;
import com.deflatedpickle.bugmagic.api.common.util.AITaskString;
import com.deflatedpickle.bugmagic.api.common.util.extension.EntityAITaskEntryKt;
import com.deflatedpickle.bugmagic.common.networking.message.MessageEntityTasks;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A super of {@link EntityTameable} for simplicity
 *
 * @author DeflatedPickle
 */
public class EntityCastable extends EntityTameable {
  public Set<AITaskString> clientTasks;
  public Set<AITaskString> clientExecutingTasks;

  public static DataParameter<BlockPos> dataHomePosition =
      EntityDataManager.createKey(EntityCastable.class, DataSerializers.BLOCK_POS);

  public EntityCastable(World worldIn) {
    super(worldIn);

    this.isImmuneToFire = true;

    this.enablePersistence();
  }

  @Override
  protected void entityInit() {
    super.entityInit();

    this.dataManager.register(dataHomePosition, BlockPos.ORIGIN);
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

  @Override
  protected void updateAITasks() {
    super.updateAITasks();

    // Send a packet to notify the client of AI tasks
    BugMagic.INSTANCE
        .getCHANNEL()
        .sendToAll(
            new MessageEntityTasks(
                this.getEntityId(),
                this.tasks.taskEntries.stream()
                    .map(EntityAITaskEntryKt::toTaskString)
                    .collect(Collectors.toSet()),
                this.tasks.executingTaskEntries.stream()
                    .map(EntityAITaskEntryKt::toTaskString)
                    .collect(Collectors.toSet())));
  }
}

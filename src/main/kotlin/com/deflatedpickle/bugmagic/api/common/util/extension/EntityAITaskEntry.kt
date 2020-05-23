/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.common.util.extension

import com.deflatedpickle.bugmagic.api.common.util.AITaskString
import net.minecraft.entity.ai.EntityAITasks

fun EntityAITasks.EntityAITaskEntry.toTaskString(): AITaskString = AITaskString(this.priority, this.using, this.action::class.simpleName!!)

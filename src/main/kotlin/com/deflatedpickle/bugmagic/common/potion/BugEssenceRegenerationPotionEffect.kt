/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.potion

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.common.capability.BugEssenceCapability
import com.deflatedpickle.bugmagic.common.networking.message.MessagePlayerBugEssence
import kotlin.math.min
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.potion.Potion

/**
 * A potion effect that regenerates bug essence
 */
class BugEssenceRegenerationPotionEffect : Potion(false, 0xBBFF70) {
    init {
        this.setPotionName("effect.bug_essence_regeneration")
        this.setIconIndex(0, 0)
        setBeneficial()
    }

    override fun performEffect(entityLivingBaseIn: EntityLivingBase, amplifier: Int) {
        if (!entityLivingBaseIn.world.isRemote) {
            if (entityLivingBaseIn.hasCapability(BugEssenceCapability.Provider.CAPABILITY, null)) {
                with(entityLivingBaseIn.getCapability(BugEssenceCapability.Provider.CAPABILITY, null)!!) {
                    this.current = min(this.current + 1, this.max)

                    BugMagic.CHANNEL.sendTo(MessagePlayerBugEssence(entityLivingBaseIn.entityId, this.max, this.current), entityLivingBaseIn as EntityPlayerMP)
                }
            }
        }
    }

    override fun isReady(duration: Int, amplifier: Int): Boolean {
        with(18 shr amplifier) {
            return if (this > 0) {
                duration % this == 0
            } else {
                true
            }
        }
    }
}

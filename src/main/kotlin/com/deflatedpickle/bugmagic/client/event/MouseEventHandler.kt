/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.client.event

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.common.item.Wand
import net.minecraftforge.client.event.MouseEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * An event handler for [MouseEventHandler]
 *
 * @author DeflatedPickle
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
object MouseEventHandler {
    @SubscribeEvent
    fun onMouse(event: MouseEvent) {
        val player = BugMagic.proxy!!.getPlayer()
        if (player!!.heldItemMainhand.item is Wand && player.isSneaking &&
                (event.dwheel == -120 || event.dwheel == 120)) {
            // FIXME: If you scroll too quickly, it can still sometimes move the selected slot
            // Maybe don't actually fix this, because it's fine if you scroll slowly
            // Anyone who reports this was rapidly scrolling through their spells like an idiot
            (player.heldItemMainhand.item as Wand).onMouseEvent()

            event.isCanceled = true
            // You thought it was a MouseEvent. But it was I, Dio!
        }
    }
}

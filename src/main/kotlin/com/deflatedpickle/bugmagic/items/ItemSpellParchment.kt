package com.deflatedpickle.bugmagic.items

import com.deflatedpickle.bugmagic.spells.SpellBase
import ladylib.client.ItemRenderRegistrationHandler
import ladylib.registration.ItemRegistrar
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumAction
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

class ItemSpellParchment(stackSize: Int, private val spell: SpellBase?) : Item(), ItemRenderRegistrationHandler {
    init {
        setMaxStackSize(stackSize)
    }
    override fun onItemUseFinish(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase): ItemStack {
        if (spell != null) {
            if (entityLiving is EntityPlayer) {
                spell.caster = entityLiving
                spell.parchment = stack
                spell.learn()
            }
        }

        return super.onItemUseFinish(stack, worldIn, entityLiving)
    }

    override fun getMaxItemUseDuration(stack: ItemStack): Int {
        return if (spell != null) {
            32
        }
        else {
            0
        }
    }

    override fun getItemUseAction(stack: ItemStack): EnumAction {
        return if (spell != null) {
            EnumAction.BOW
        }
        else {
            EnumAction.NONE
        }
    }

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        if (spell != null) {
            playerIn.activeHand = handIn
            return ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn))
        }
        else {
            return ActionResult(EnumActionResult.FAIL, playerIn.getHeldItem(handIn))
        }
    }

    override fun registerRender(item: Item) {
        ItemRegistrar.registerRender(item, ModelResourceLocation("bugmagic:spell_parchment"))
    }
//    override fun getCustomMeshDefinition(): ItemMeshDefinition {
//        return ItemMeshDefinition { ModelResourceLocation("bugmagic:spell_parchment") }
//    }
}
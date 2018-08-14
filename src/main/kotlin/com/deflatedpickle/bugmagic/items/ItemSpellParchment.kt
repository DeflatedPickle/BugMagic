package com.deflatedpickle.bugmagic.items

import com.deflatedpickle.bugmagic.spells.SpellBase
import com.deflatedpickle.picklelib.item.ItemBase
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumAction
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

class ItemSpellParchment(name: String, stackSize: Int, creativeTab: CreativeTabs, val spell: SpellBase) : ItemBase(name, stackSize, creativeTab) {
    override fun onItemUseFinish(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase): ItemStack {
        if (entityLiving is EntityPlayer) {
            spell.caster = entityLiving
            spell.parchment = stack
            spell.learn()
        }

        return super.onItemUseFinish(stack, worldIn, entityLiving)
    }

    override fun getMaxItemUseDuration(stack: ItemStack): Int {
        return 32
    }

    override fun getItemUseAction(stack: ItemStack): EnumAction {
        return EnumAction.BOW
    }

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        playerIn.activeHand = handIn
        return ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn))
    }

    override fun getCustomMeshDefinition(): ItemMeshDefinition {
        return ItemMeshDefinition { ModelResourceLocation("bugmagic:spell_parchment") }
    }
}
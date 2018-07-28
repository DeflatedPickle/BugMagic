package com.deflatedpickle.bugmagic.items

import com.deflatedpickle.bugmagic.spells.SpellBase
import com.deflatedpickle.picklelib.item.ItemBase
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

class ItemSpellParchment(name: String, stackSize: Int, creativeTab: CreativeTabs, val spell: SpellBase) : ItemBase(name, stackSize, creativeTab) {
    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        // Check if the player already knows this spell
            // If so, send a message to the player
            // If not, run spell#learn
        spell.caster = playerIn
        spell.parchment = playerIn.getHeldItem(handIn)
        spell.learn()

        return super.onItemRightClick(worldIn, playerIn, handIn)
    }
}
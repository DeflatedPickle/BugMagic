package com.deflatedpickle.bugmagic.items

import com.deflatedpickle.bugmagic.util.SpellUtil
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumAction
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.World
import org.lwjgl.input.Mouse

class ItemWand : Item() {
    // TODO: Allow the player to uncast their spells at will
    private var hasBeenScrolled = false
    private val playerSpells: MutableList<String> = mutableListOf()
    private var currentSpellIndex = 0

    override fun onItemUseFinish(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase): ItemStack {
        if (!stack.hasTagCompound()) {
            stack.tagCompound = NBTTagCompound()
        }

        if (entityLiving is EntityPlayer) {
            if (!entityLiving.isSneaking) {
                // if (!entityLiving.world.isRemote) {
                    val spell = stack.tagCompound!!.getString("currentSpell")

                    if (spell != "") {
                        val spellClass = SpellUtil.spellMap[spell]!!
                        spellClass.caster = entityLiving
                        spellClass.cast()
                    }
                // }
            }
        }

        return super.onItemUseFinish(stack, worldIn, entityLiving)
    }

    fun onMouseEvent() {
        hasBeenScrolled = true
    }

    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
        if (isSelected) {
            if (entityIn is EntityPlayer) {
                if (entityIn.isSneaking) {
                    if (!stack.hasTagCompound()) {
                        stack.tagCompound = NBTTagCompound()
                    }

                    playerSpells.clear()
                    val tagString = entityIn.entityData.getTag("bugmagic.spells").toString()
                    // println(tagString)
                    // println(SpellUtil.spellMap)
                    // playerSpells = tagString.substring(1, tagString.length - 1).split(",")
                    // playerSpells = Regex("[A-Z]\\w+").findAll(tagString).toList()

                    for (spell in Regex("[A-Z]\\w+").findAll(tagString).toList()) {
                        playerSpells.add(spell.value)
                    }

                    if (hasBeenScrolled) {
                        val mouseWheel = Mouse.getEventDWheel()

                        if (mouseWheel == -120) {
                            // Scrolled forward
                            
                            if (currentSpellIndex + 1 < playerSpells.count()) {
                                currentSpellIndex++
                            }

                            // currentSpellIndex = 1
                        }
                        else if (mouseWheel == 120) {
                            // Scrolled backwards

                            if (currentSpellIndex - 1 > -1) {
                                currentSpellIndex--
                            }

                            // currentSpellIndex = 0
                        }

                        if (playerSpells.count() > 0) {
                            val currentSpell = SpellUtil.spellMap[playerSpells[currentSpellIndex]]

                            if (entityIn.world.isRemote) {
                                entityIn.sendStatusMessage(TextComponentString(currentSpell!!.name), true)

                                // This is fired only on the client, so let's packet it through
                                SpellUtil.setCurrentSpell(stack, currentSpell.name)
                            }
                        }

                        hasBeenScrolled = false
                    }
                }
            }
        }

        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected)
    }

    override fun getMaxItemUseDuration(stack: ItemStack?): Int {
        return 32
    }

    override fun getItemUseAction(stack: ItemStack?): EnumAction {
        return EnumAction.BOW
    }

    override fun onItemRightClick(worldIn: World?, playerIn: EntityPlayer?, handIn: EnumHand?): ActionResult<ItemStack> {
        playerIn!!.activeHand = handIn!!
        return ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn))
    }
}
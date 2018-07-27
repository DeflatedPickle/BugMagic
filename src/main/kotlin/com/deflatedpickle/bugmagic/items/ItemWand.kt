package com.deflatedpickle.bugmagic.items

import com.deflatedpickle.bugmagic.util.BugUtil
import com.deflatedpickle.bugmagic.util.SpellUtil
import com.deflatedpickle.picklelib.item.ItemBase
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.World
import org.lwjgl.input.Mouse

class ItemWand(name: String, stackSize: Int, creativeTab: CreativeTabs) : ItemBase(name, stackSize, creativeTab) {
    // TODO: Take the players spells and put them in to a list upon sneaking
    var hasBeenScrolled = false
    val playerSpells: MutableList<String> = mutableListOf()
    var currentSpellIndex = 0

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        val stack = playerIn.getHeldItem(handIn)

        // TODO: Figure out a way to change spells

        if (!stack.hasTagCompound()) {
            stack.tagCompound = NBTTagCompound()
        }

        if (!playerIn.isSneaking) {
            // TODO: Check the kind of wizard before casting the spell
            if (!playerIn.world.isRemote) {
                BugUtil.useCappedBugPower(playerIn, 5)

                val spell = stack.tagCompound!!.getString("currentSpell")

                if (spell != "") {
                    val spellClass = SpellUtil.spellMap[spell]!!
                    spellClass.caster = playerIn
                    spellClass.cast()
                }
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn)
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

                        // TODO: Scroll through the players spells
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

                            if (entityIn is EntityPlayerSP) {
                                entityIn.sendStatusMessage(TextComponentString(currentSpell!!.name), true)
                                // stack.tagCompound!!.setString("currentSpell", currentSpell.name)

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
        return 36000
    }
}
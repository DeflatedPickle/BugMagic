/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.common.item.food

import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

/**
 * A generic [ItemFood] class that provides most settings as constructor arguments
 *
 * @author DeflatedPickle
 */
class GenericFood(
    name: String,
    creativeTab: CreativeTabs? = CreativeTabs.FOOD,
    private val useDuration: Int = 32,
    healAmount: Int,
    saturation: Float,
    isWolfFood: Boolean = false,
    alwaysEdible: Boolean = false,
    potionEffect: PotionEffect? = null,
    potionEffectProbability: Float = 1.0f,
    val creativeEdible: Boolean = false,
    val tooltip: Collection<String> = listOf()
) : ItemFood(healAmount, saturation, isWolfFood) {
    init {
        this.translationKey = name
        this.creativeTab = creativeTab

        if (alwaysEdible) {
            this.setAlwaysEdible()
        }

        if (potionEffect != null) {
            setPotionEffect(potionEffect, potionEffectProbability)
        }
    }

    override fun getMaxItemUseDuration(stack: ItemStack): Int = this.useDuration

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        val stack = playerIn.getHeldItem(handIn)

        return if (this.creativeEdible) {
            return if (playerIn.isCreative) {
                playerIn.activeHand = handIn
                ActionResult(EnumActionResult.SUCCESS, stack)
            } else {
                super.onItemRightClick(worldIn, playerIn, handIn)
            }
        } else {
            super.onItemRightClick(worldIn, playerIn, handIn)
        }
    }

    override fun addInformation(
        stack: ItemStack,
        worldIn: World?,
        tooltip: MutableList<String>,
        flagIn: ITooltipFlag
    ) {
        tooltip.addAll(this.tooltip)
    }
}

/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.common.item

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.EnumAction
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

/**
 * A generic [Item] with a constructor to set most values
 *
 * @author DeflatedPickle
 */
open class GenericItem(
    name: String,
    creativeTab: CreativeTabs?,
    canRepair: Boolean = true,
    stackLimit: Int = 64,
    private val isDamageable: Boolean = true,
    private val isMap: Boolean = false,
    @Suppress("SpellCheckingInspection") private val itemEnchantability: Int = 0,
    private val isRepairable: Boolean = false,
    private val showDurabilityBar: Boolean = false,
    private val maxDamage: Int = 0,
    private val actionItemUse: EnumAction = EnumAction.NONE,
    private val itemUseDuration: Int = 0
) : Item() {
    init {
        this.translationKey = name
        this.creativeTab = creativeTab

        this.canRepair = canRepair
        this.maxStackSize = stackLimit
    }

    override fun isDamageable(): Boolean = this.isDamageable
    override fun isMap(): Boolean = this.isMap
    override fun getItemEnchantability(): Int = this.itemEnchantability
    override fun isRepairable(): Boolean = this.isRepairable
    override fun showDurabilityBar(stack: ItemStack): Boolean = this.showDurabilityBar
    override fun getMaxDamage(): Int = this.maxDamage

    override fun getItemUseAction(stack: ItemStack): EnumAction = this.actionItemUse
    override fun getMaxItemUseDuration(stack: ItemStack): Int = this.itemUseDuration
}

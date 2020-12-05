/* Copyright (c) 2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.api.common.tab

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.enchantment.EnumEnchantmentType
import net.minecraft.item.ItemStack

class GenericTab(
    name: String,
    val iconStack: ItemStack,
    texturePath: String = "items.png",
    drawTitle: Boolean = true,
    index: Int = getNextID(),
    relevantEnchantmentTypes: Array<EnumEnchantmentType> = arrayOf(),
    private val hasScrollbar: Boolean = true,
    private val hasSearchBar: Boolean = false,
    private val searchBarWidth: Int = 89,
    // private val translationKey: String,
    private val isAlignedRight: Boolean = false,
    private val labelColour: Int = 4210752,
    // private val page: Int = 1,
    private val isTopRow: Boolean = true
) : CreativeTabs(index, name) {
    init {
        if (this.hasSearchBar && texturePath == "items.png") {
            this.backgroundImageName = "item_search.png"
        } else {
            this.backgroundImageName = texturePath
        }

        this.setRelevantEnchantmentTypes(*relevantEnchantmentTypes)

        if (!drawTitle) {
            this.setNoTitle()
        }
    }

    override fun createIcon(): ItemStack = this.iconStack

    override fun hasScrollbar(): Boolean = this.hasScrollbar
    override fun hasSearchBar(): Boolean = this.hasSearchBar

    override fun getSearchbarWidth(): Int = this.searchBarWidth
    // override fun getTranslationKey(): String = this.translationKey
    override fun getLabelColor(): Int = this.labelColour
    // override fun getTabPage(): Int = this.page

    override fun isAlignedRight(): Boolean = this.isAlignedRight
    override fun isOnTopRow(): Boolean = this.isTopRow
}

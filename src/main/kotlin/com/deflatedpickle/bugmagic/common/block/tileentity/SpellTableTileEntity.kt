/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.block.tileentity

import com.deflatedpickle.bugmagic.api.common.util.extension.containsAll
import com.deflatedpickle.bugmagic.client.render.tileentity.SpellTableTileEntitySpecialRender
import com.deflatedpickle.bugmagic.common.block.SpellTableBlock
import com.deflatedpickle.bugmagic.common.init.SpellRecipeInit
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidTank
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemStackHandler

/**
 * Stores the items and fluid for the SpellTable
 *
 * @author DeflatedPickle
 * @see [SpellTableBlock]
 * @see [SpellTableTileEntitySpecialRender]
 */
class SpellTableTileEntity(stackLimit: Int = 32) : TileEntity() {
    companion object {
        const val invalidRecipe = ""
    }

    // Crafting ingredients inventory
    val itemStackHandler = ItemStackHandler(stackLimit)
    // Bug essence tank
    val fluidTank = FluidTank(Fluid.BUCKET_VOLUME)
    val wandStackHandler = object : ItemStackHandler(1) {
        override fun getSlotLimit(slot: Int): Int = 1
    }
    // Stores a feather, to write the recipe with
    val featherStackHandler = object : ItemStackHandler(1) {
        override fun getSlotLimit(slot: Int): Int = 1
    }

	// Range: 0f..1f
    var ink = 0f

    var validRecipe = invalidRecipe
	// Range: 0f..1f
	// Increased by 1f/SpellRecipe.getCraftingTime
    var recipeProgression = 0f

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        this.itemStackHandler.deserializeNBT(compound.getCompoundTag("inventory"))
        this.fluidTank.readFromNBT(compound)
        this.wandStackHandler.deserializeNBT(compound.getCompoundTag("wand"))
        this.featherStackHandler.deserializeNBT(compound.getCompoundTag("feather"))
        this.ink = compound.getFloat("ink")
        this.validRecipe = compound.getString("valid")
        this.recipeProgression = compound.getFloat("progression")
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(compound)
        compound.setTag("inventory", this.itemStackHandler.serializeNBT())
        this.fluidTank.writeToNBT(compound)
        compound.setTag("wand", this.wandStackHandler.serializeNBT())
        compound.setTag("feather", this.featherStackHandler.serializeNBT())
        compound.setFloat("ink", this.ink)
        compound.setString("valid", this.validRecipe)
        compound.setFloat("progression", this.recipeProgression)
        return compound
    }

    override fun getUpdatePacket(): SPacketUpdateTileEntity? {
        return SPacketUpdateTileEntity(this.pos, 3, this.updateTag)
    }

    override fun getUpdateTag(): NBTTagCompound {
        return this.writeToNBT(NBTTagCompound())
    }

    override fun onDataPacket(net: NetworkManager, pkt: SPacketUpdateTileEntity) {
        super.onDataPacket(net, pkt)
        handleUpdateTag(pkt.nbtCompound)
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
    }

    override fun <T : Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        return when (capability) {
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY -> when (facing) {
                EnumFacing.EAST -> wandStackHandler as T
                EnumFacing.UP -> itemStackHandler as T
                else -> super.getCapability(capability, facing)
            }
            CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY -> when (facing) {
                EnumFacing.EAST -> fluidTank as T
                else -> super.getCapability(capability, facing)
            }
            else -> super.getCapability(capability, facing)
        }
    }

    override fun markDirty() {
        for ((_, spellRecipe) in SpellRecipeInit.registry.entries) {
            val stacks = mutableListOf<ItemStack>()

            for (i in 0 until this.itemStackHandler.slots) {
                this.itemStackHandler.getStackInSlot(i).apply {
                    if (this.item != Items.AIR) {
                        stacks.add(this)
                    }
                }
            }

            if (spellRecipe.ingredients != null &&
                    spellRecipe.ingredients!!.isNotEmpty() && stacks.isNotEmpty()) {
                if (spellRecipe.containsAll(stacks)) {
                    spellRecipe.registryName?.let {
                        validRecipe = it.toString()
                    }
                    break
                } else {
                    validRecipe = invalidRecipe
                }
            } else {
                validRecipe = invalidRecipe
            }
        }

        super.markDirty()
    }
}

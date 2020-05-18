/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.block.tileentity

import com.deflatedpickle.bugmagic.common.init.Spell
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fluids.Fluid as VFluid
import net.minecraftforge.fluids.FluidTank
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemStackHandler

/**
 * Stores the items and fluid for the SpellTable
 *
 * @see com.deflatedpickle.bugmagic.common.block.SpellTable
 * @see com.deflatedpickle.bugmagic.client.render.tileentity.SpellTable
 */
class SpellTable(stackLimit: Int = 32) : TileEntity() {
    companion object {
        const val invalidRecipe = ""
    }

    val itemStackHandler = ItemStackHandler(stackLimit)
    val fluidTank = FluidTank(VFluid.BUCKET_VOLUME)
    val wandStackHandler = object : ItemStackHandler(1) {
        override fun getSlotLimit(slot: Int): Int = 1
    }
    val featherStackHandler = object : ItemStackHandler(1) {
        override fun getSlotLimit(slot: Int): Int = 1
    }

    var ink = 0f

    var validRecipe = invalidRecipe
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
        for ((_, spell) in Spell.registry.entries) {
            val list = mutableListOf<ItemStack>()

            for (i in 0 until this.itemStackHandler.slots) {
                this.itemStackHandler.getStackInSlot(i).apply {
                    if (this.item != Items.AIR) {
                        list.add(this)
                    }
                }
            }

            if (spell.craftingIngredients.isNotEmpty() && list.isNotEmpty()) {
                // This isn't the best way to validate it but I can't think of a better way
                // If you can, feel free to make a pull request
                if (spell.craftingIngredients.size == list.size && list.map {
                            it.item to it.count
                        }
                                .containsAll(spell.craftingIngredients.map {
                                    it.item to it.count
                                })) {
                    spell.registryName?.let {
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

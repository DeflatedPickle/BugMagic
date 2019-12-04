/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.capability

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.ASpell
import com.deflatedpickle.bugmagic.api.capability.ISpellCaster
import java.util.UUID
import java.util.concurrent.Callable
import kotlin.collections.HashMap
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.ICapabilitySerializable

object SpellCaster {
    val NAME = ResourceLocation(Reference.MOD_ID, "spell_caster")

    fun isCapable(stack: ItemStack): ISpellCaster? = stack.getCapability(Provider.CAPABILITY, null)

    class Implementation : ISpellCaster {
        private val castSpellMap = hashMapOf<ASpell, Int>()
        private var owner: UUID? = null
        private var isCasting = false
        private var castingFor = 0f

        override fun getCastSpellMap(): HashMap<ASpell, Int> = castSpellMap

        override fun setOwner(value: UUID?) {
            owner = value
        }

        override fun getOwner(): UUID? = owner

        override fun setCasting(value: Boolean) {
            if (!value) castingFor = 0f
            isCasting = value
        }

        override fun isCasting(): Boolean = isCasting

        override fun setCastingCurrent(value: Float) {
            castingFor = value
        }

        override fun getCastingCurrent(): Float = castingFor
    }

    class Storage : Capability.IStorage<ISpellCaster> {
        override fun readNBT(capability: Capability<ISpellCaster>?, instance: ISpellCaster?, side: EnumFacing?, nbt: NBTBase?) {
            if (instance is Implementation) {
                with(nbt as NBTTagCompound) {
                    instance.isCasting = this.getBoolean("isCasting")
                    instance.castingCurrent = this.getFloat("castingCurrent")
                }
            } else {
                throw IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation")
            }
        }

        override fun writeNBT(capability: Capability<ISpellCaster>?, instance: ISpellCaster?, side: EnumFacing?): NBTBase? {
            if (instance != null) {
                return NBTTagCompound().apply {
                    setBoolean("isCasting", instance.isCasting)
                    setFloat("castingCurrent", instance.castingCurrent)
                }
            }
            return null
        }
    }

    class Factory : Callable<ISpellCaster> {
        override fun call(): ISpellCaster {
            return Implementation()
        }
    }

    class Provider : ICapabilitySerializable<NBTBase> {
        companion object {
            @JvmStatic
            @CapabilityInject(ISpellCaster::class)
            lateinit var CAPABILITY: Capability<ISpellCaster>
        }

        val INSTANCE = CAPABILITY.defaultInstance

        override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean = capability == CAPABILITY
        override fun <T : Any> getCapability(capability: Capability<T>, facing: EnumFacing?): T? = if (capability == CAPABILITY) CAPABILITY.cast(this.INSTANCE) else null

        override fun serializeNBT(): NBTBase = CAPABILITY.storage.writeNBT(CAPABILITY, this.INSTANCE, null)!!
        override fun deserializeNBT(nbt: NBTBase) = CAPABILITY.storage.readNBT(CAPABILITY, this.INSTANCE, null, nbt)
    }

    fun register() {
        CapabilityManager.INSTANCE.register(ISpellCaster::class.java, Storage(), Factory())
    }
}

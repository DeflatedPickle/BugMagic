/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.capability

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.capability.SpellCaster
import com.deflatedpickle.bugmagic.api.spell.Spell
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

/**
 * Stores the casted spells, user, casting state and casting progress
 */
object SpellCasterCapability {
    val NAME = ResourceLocation(Reference.MOD_ID, "spell_caster")

    fun isCapable(stack: ItemStack): SpellCaster? = stack.getCapability(Provider.CAPABILITY, null)

    class Implementation : SpellCaster {
        private val castSpellMap = hashMapOf<Spell, Int>()
        private var owner: UUID? = null
        private var isCasting = false
        private var castingFor = 0f

        /**
         * Newly cast spells will increase the count of the spell
         * Uncast spells will decrease the count, unless decreasing it would result in 0, then the spell key is removed
         */
        override fun getCastSpellMap(): HashMap<Spell, Int> = castSpellMap

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

    class Storage : Capability.IStorage<SpellCaster> {
        override fun readNBT(capability: Capability<SpellCaster>?, instance: SpellCaster?, side: EnumFacing?, nbt: NBTBase?) {
            if (instance is Implementation) {
                with(nbt as NBTTagCompound) {
                    instance.isCasting = this.getBoolean("isCasting")
                    instance.castingCurrent = this.getFloat("castingCurrent")
                }
            } else {
                throw IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation")
            }
        }

        override fun writeNBT(capability: Capability<SpellCaster>?, instance: SpellCaster?, side: EnumFacing?): NBTBase? {
            if (instance != null) {
                return NBTTagCompound().apply {
                    setBoolean("isCasting", instance.isCasting)
                    setFloat("castingCurrent", instance.castingCurrent)
                }
            }
            return null
        }
    }

    class Factory : Callable<SpellCaster> {
        override fun call(): SpellCaster {
            return Implementation()
        }
    }

    class Provider : ICapabilitySerializable<NBTBase> {
        companion object {
            @JvmStatic
            @CapabilityInject(SpellCaster::class)
            lateinit var CAPABILITY: Capability<SpellCaster>
        }

        val INSTANCE = CAPABILITY.defaultInstance

        override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean = capability == CAPABILITY
        override fun <T : Any> getCapability(capability: Capability<T>, facing: EnumFacing?): T? = if (capability == CAPABILITY) CAPABILITY.cast(this.INSTANCE) else null

        override fun serializeNBT(): NBTBase = CAPABILITY.storage.writeNBT(CAPABILITY, this.INSTANCE, null)!!
        override fun deserializeNBT(nbt: NBTBase) = CAPABILITY.storage.readNBT(CAPABILITY, this.INSTANCE, null, nbt)
    }

    fun register() {
        CapabilityManager.INSTANCE.register(SpellCaster::class.java, Storage(), Factory())
    }
}

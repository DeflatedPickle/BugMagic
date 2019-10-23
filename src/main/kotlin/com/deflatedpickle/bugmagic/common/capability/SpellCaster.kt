package com.deflatedpickle.bugmagic.common.capability

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.capability.IBugEssence
import com.deflatedpickle.bugmagic.api.capability.ISpellCaster
import com.deflatedpickle.bugmagic.api.capability.ISpellLearner
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagIntArray
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import java.util.*
import java.util.concurrent.Callable
import kotlin.math.max
import kotlin.math.min

object SpellCaster {
    val NAME = ResourceLocation(Reference.MOD_ID, "spell_caster")

    fun isCapable(stack: ItemStack): ISpellCaster? {
        if (stack.hasCapability(Provider.CAPABILITY!!, null)) {
            stack.getCapability(Provider.CAPABILITY!!, null)!!.also {
                return it
            }
        }
        return null
    }

    class Implementation : ISpellCaster {
        private var owner: UUID? = null
        private var isCasting = false
        private var castingFor = 0f

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
            }
            else {
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
            var CAPABILITY: Capability<ISpellCaster>? = null
        }

        val INSTANCE = CAPABILITY?.defaultInstance

        override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
            return capability == CAPABILITY
        }

        override fun <T : Any> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
            return if (capability == CAPABILITY) CAPABILITY!!.cast(this.INSTANCE) else null
        }

        override fun serializeNBT(): NBTBase {
            return CAPABILITY!!.storage.writeNBT(CAPABILITY, this.INSTANCE, null)!!
        }

        override fun deserializeNBT(nbt: NBTBase) {
            CAPABILITY!!.storage.readNBT(CAPABILITY, this.INSTANCE, null, nbt)
        }
    }

    fun register() {
        CapabilityManager.INSTANCE.register(ISpellCaster::class.java, Storage(), Factory())
    }
}
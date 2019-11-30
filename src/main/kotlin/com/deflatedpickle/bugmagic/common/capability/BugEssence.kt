package com.deflatedpickle.bugmagic.common.capability

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.capability.IBugEssence
import net.minecraft.entity.EntityLivingBase
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagIntArray
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import java.util.concurrent.Callable
import kotlin.math.max
import kotlin.math.min

object BugEssence {
    val NAME = ResourceLocation(Reference.MOD_ID, "bug_essence")

    fun isCapable(entity: EntityLivingBase): IBugEssence? = entity.getCapability(Provider.CAPABILITY, null)

    class Implementation : IBugEssence {
        private var max = 0
        private var current = 0

        override fun setMax(value: Int) { this.max = value }
        override fun getMax(): Int = this.max

        override fun setCurrent(value: Int) { this.current = max(min(value, this.max), 0) }
        override fun getCurrent(): Int = this.current
    }

    class Storage : Capability.IStorage<IBugEssence> {
        override fun readNBT(capability: Capability<IBugEssence>?, instance: IBugEssence?, side: EnumFacing?, nbt: NBTBase?) {
            if (instance is Implementation) {
                with(nbt as NBTTagIntArray) {
                    instance.max = this.intArray[0]
                    instance.current = this.intArray[1]
                }
            }
            else {
                throw IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation")
            }
        }

        override fun writeNBT(capability: Capability<IBugEssence>?, instance: IBugEssence?, side: EnumFacing?): NBTBase? {
            if (instance != null) {
                return NBTTagIntArray(intArrayOf(instance.max, instance.current))
            }
            return null
        }
    }

    class Factory : Callable<IBugEssence> {
        override fun call(): IBugEssence {
            return Implementation()
        }
    }

    class Provider : ICapabilitySerializable<NBTBase> {
        companion object {
            @JvmStatic
            @CapabilityInject(IBugEssence::class)
            lateinit var CAPABILITY: Capability<IBugEssence>
        }

        val INSTANCE = CAPABILITY.defaultInstance

        override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean = capability == CAPABILITY
        override fun <T : Any> getCapability(capability: Capability<T>, facing: EnumFacing?): T? = if (capability == CAPABILITY) CAPABILITY.cast(this.INSTANCE) else null

        override fun serializeNBT(): NBTBase = CAPABILITY.storage.writeNBT(CAPABILITY, this.INSTANCE, null)!!
        override fun deserializeNBT(nbt: NBTBase) = CAPABILITY.storage.readNBT(CAPABILITY, this.INSTANCE, null, nbt)
    }

    fun register() {
        CapabilityManager.INSTANCE.register(IBugEssence::class.java, Storage(), Factory())
    }
}
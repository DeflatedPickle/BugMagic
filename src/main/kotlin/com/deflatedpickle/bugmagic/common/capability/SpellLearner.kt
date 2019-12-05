/* Copyright (c) 2019 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.capability

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.ASpell
import com.deflatedpickle.bugmagic.api.capability.ISpellLearner
import com.deflatedpickle.bugmagic.common.init.Spell
import java.util.concurrent.Callable
import net.minecraft.entity.EntityLivingBase
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import net.minecraftforge.fml.common.registry.GameRegistry

object SpellLearner {
    val NAME = ResourceLocation(Reference.MOD_ID, "spell_learner")

    fun isCapable(entity: EntityLivingBase): ISpellLearner? = entity.getCapability(Provider.CAPABILITY, null)

    class Implementation : ISpellLearner {
        private val spellList = mutableListOf(
                Spell.ITEM_COLLECTOR,
                Spell.ESSENCE_COLLECTOR,
                Spell.AUTO_HOE
        )
        private var currentIndex = 0

        override fun getSpellList(): MutableList<ASpell> = spellList

        override fun setCurrentIndex(value: Int) {
            currentIndex = value
        }

        override fun getCurrentIndex(): Int = currentIndex

        override fun learnSpell(spell: ASpell): ASpell {
            with(spell) {
                if (!spellList.contains(this)) {
                    spellList.add(this)
                }
                return this
            }
        }
    }

    class Storage : Capability.IStorage<ISpellLearner> {
        override fun readNBT(capability: Capability<ISpellLearner>?, instance: ISpellLearner?, side: EnumFacing?, nbt: NBTBase?) {
            if (instance is Implementation) {
                with(nbt as NBTTagList) {
                    for (i in 0..this.tagCount()) {
                        with(this.getStringTagAt(i)) {
                            if (this != "") {
                                instance.spellList.add(GameRegistry.findRegistry(ASpell::class.java).getValue(ResourceLocation(this))!!)
                            }
                        }
                    }
                }
            } else {
                throw IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation")
            }
        }

        override fun writeNBT(capability: Capability<ISpellLearner>?, instance: ISpellLearner?, side: EnumFacing?): NBTBase? {
            if (instance != null) {
                with(NBTTagList()) {
                    for (i in instance.spellList.map { it.registryName!! }) {
                        this.appendTag(NBTTagString(i.toString()))
                    }
                    return this
                }
            }
            return null
        }
    }

    class Factory : Callable<ISpellLearner> {
        override fun call(): ISpellLearner = Implementation()
    }

    class Provider : ICapabilitySerializable<NBTBase> {
        companion object {
            @JvmStatic
            @CapabilityInject(ISpellLearner::class)
            lateinit var CAPABILITY: Capability<ISpellLearner>
        }

        val INSTANCE = CAPABILITY.defaultInstance

        override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean = capability == CAPABILITY
        override fun <T : Any> getCapability(capability: Capability<T>, facing: EnumFacing?): T? = if (capability == CAPABILITY) CAPABILITY.cast(this.INSTANCE) else null

        override fun serializeNBT(): NBTBase = CAPABILITY.storage.writeNBT(CAPABILITY, this.INSTANCE, null)!!
        override fun deserializeNBT(nbt: NBTBase) = CAPABILITY.storage.readNBT(CAPABILITY, this.INSTANCE, null, nbt)
    }

    fun register() {
        CapabilityManager.INSTANCE.register(ISpellLearner::class.java, Storage(), Factory())
    }
}

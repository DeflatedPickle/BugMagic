/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.capability

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.capability.SpellLearner
import com.deflatedpickle.bugmagic.api.spell.Spell
import java.util.concurrent.Callable
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import net.minecraftforge.fml.common.registry.GameRegistry

/**
 * Stores the players list of learnt spells and selected index
 */
object SpellLearnerCapability {
    val NAME = ResourceLocation(Reference.MOD_ID, "spell_learner")

    fun isCapable(thing: ICapabilityProvider): SpellLearner? = thing.getCapability(Provider.CAPABILITY, null)

    class Implementation : SpellLearner {
        private var spellList = mutableListOf<Spell>()
        private var currentIndex = 0

        override fun setSpellList(value: MutableList<Spell>) {
            this.spellList = value
        }

        override fun getSpellList(): MutableList<Spell> = this.spellList

        override fun setCurrentIndex(value: Int) {
            this.currentIndex = value
        }

        override fun getCurrentIndex(): Int = this.currentIndex

        override fun learnSpell(spell: Spell): Spell {
            if (!this.spellList.any { it::class == spell::class }) {
                this.spellList.add(spell)
            }
            return spell
        }
    }

    class Storage : Capability.IStorage<SpellLearner> {
        override fun readNBT(capability: Capability<SpellLearner>?, instance: SpellLearner?, side: EnumFacing?, nbt: NBTBase?) {
            if (instance is Implementation) {
                with(nbt as NBTTagList) {
                    for (i in 0..this.tagCount()) {
                        with(this.getStringTagAt(i)) {
                            if (this.isNotEmpty()) {
                                instance.spellList.add(
                                    GameRegistry
                                        .findRegistry(Spell::class.java)
                                        .getValue(ResourceLocation(this))!!
                                )
                            }
                        }
                    }
                }
            } else {
                throw IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation")
            }
        }

        override fun writeNBT(capability: Capability<SpellLearner>?, instance: SpellLearner?, side: EnumFacing?): NBTBase? {
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

    class Factory : Callable<SpellLearner> {
        override fun call(): SpellLearner = Implementation()
    }

    class Provider : ICapabilitySerializable<NBTBase> {
        companion object {
            @JvmStatic
            @CapabilityInject(SpellLearner::class)
            lateinit var CAPABILITY: Capability<SpellLearner>
        }

        val INSTANCE = CAPABILITY.defaultInstance

        override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean = capability == CAPABILITY
        override fun <T : Any> getCapability(capability: Capability<T>, facing: EnumFacing?): T? = if (capability == CAPABILITY) CAPABILITY.cast(this.INSTANCE) else null

        override fun serializeNBT(): NBTBase = CAPABILITY.storage.writeNBT(CAPABILITY, this.INSTANCE, null)!!
        override fun deserializeNBT(nbt: NBTBase) = CAPABILITY.storage.readNBT(CAPABILITY, this.INSTANCE, null, nbt)
    }

    fun register() {
        CapabilityManager.INSTANCE.register(SpellLearner::class.java, Storage(), Factory())
    }
}

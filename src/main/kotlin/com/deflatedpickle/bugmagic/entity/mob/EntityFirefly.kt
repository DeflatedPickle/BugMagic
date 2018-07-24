package com.deflatedpickle.bugmagic.entity.mob

import com.deflatedpickle.bugmagic.entity.ai.EntityAIModHoverToOwner
import com.elytradev.mirage.event.GatherLightsEvent
import com.elytradev.mirage.lighting.IEntityLightEventConsumer
import com.elytradev.mirage.lighting.Light
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityAgeable
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.EntityAIFollowOwner
import net.minecraft.entity.passive.EntityTameable
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import net.minecraftforge.fml.common.Optional


@Optional.Interface(iface="com.elytradev.mirage.lighting.IEntityLightEventConsumer", modid="mirage")
class EntityFirefly(worldIn: World) : EntityTameable(worldIn), IEntityLightEventConsumer {
    init {
        this.isImmuneToFire = true

        setSize(0.4f, 0.8f)

        isTamed = true

        enablePersistence()
    }

    override fun canBeLeashedTo(player: EntityPlayer?): Boolean {
        return false
    }

    override fun canDespawn(): Boolean {
        return false
    }

    // TODO: Make a model and texture for the firefly, and then use CTM to define a glowing part
    // @SideOnly(Side.CLIENT)
    // override fun getBrightnessForRender(): Int {
    //     return 16
    // }

    @Optional.Method(modid = "mirage")
    override fun gatherLights(event: GatherLightsEvent, entity: Entity) {
        event.add(Light.builder()
                .pos(this.posX, this.posY, this.posZ)
                .color(1f, 0.9f, 0.3f)
                .radius(3f)
                .build())
    }

    override fun initEntityAI() {
        // this.tasks.addTask(2, EntityAIFollowOwner(this, 3.0, 0.5f, 50.0f))
        this.tasks.addTask(2, EntityAIModHoverToOwner(this, 0.11f, 1f, 1f))
    }

    override fun applyEntityAttributes() {
        super.applyEntityAttributes()
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).baseValue = 35.0
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).baseValue = 0.2
    }

    override fun createChild(ageable: EntityAgeable?): EntityAgeable? {
        return null
    }
}
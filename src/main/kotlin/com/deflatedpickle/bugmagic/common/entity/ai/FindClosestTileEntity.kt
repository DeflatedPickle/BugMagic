/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.entity.ai

import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.network.datasync.DataParameter
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i

class FindClosestTileEntity(private val entityIn: EntityLiving, private val key: DataParameter<*>, private val filter: (TileEntity) -> Boolean, private val withTileEntity: (TileEntity?) -> Unit) : EntityAIBase() {
    override fun shouldExecute(): Boolean {
        return entityIn.dataManager.get(key) == BlockPos.ORIGIN ||
                entityIn.world.getTileEntity(entityIn.dataManager.get(key) as BlockPos) == null
    }

    override fun updateTask() {
        var closest: TileEntity? = null
        for (tileEntity in entityIn.world.loadedTileEntityList.filter {
            if (closest != null) {
                with(closest!!.pos) {
                    it.pos.getDistance(x, y, z) > 10.0
                }
            }
            filter(it)
        }) {
            if (closest == null) {
                closest = tileEntity
            } else {
                if (entityIn.position.distanceSq(tileEntity.pos as Vec3i) < entityIn.position.distanceSq(closest.pos as Vec3i)) {
                    closest = tileEntity
                }
            }
        }

        withTileEntity(closest)
    }
}

package com.deflatedpickle.bugmagic.blocks

import com.deflatedpickle.bugmagic.init.ModCreativeTabs
import com.deflatedpickle.bugmagic.init.ModItems
import com.deflatedpickle.bugmagic.items.ItemBugPart
import com.deflatedpickle.bugmagic.particle.ParticleBubble
import com.deflatedpickle.bugmagic.tileentity.TileEntityCauldron
import com.deflatedpickle.picklelib.block.BlockBase
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.init.PotionTypes
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionUtils
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.commons.lang3.RandomUtils
import org.apache.commons.lang3.tuple.ImmutablePair
import java.util.*

class BlockCauldron(name: String, private val stirsRequired: Int) : BlockBase(name, Material.IRON, 2f, 10f, ImmutablePair("pickaxe", 0), ModCreativeTabs.tabGeneral), ITileEntityProvider {
    // TODO: Add a custom bounding box
    override fun isFullCube(state: IBlockState?): Boolean {
        return false
    }

    override fun isOpaqueCube(state: IBlockState?): Boolean {
        return false
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState?, playerIn: EntityPlayer?, hand: EnumHand?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val tileEntity = worldIn.getTileEntity(pos)

        if (worldIn.isRemote) {
            if (tileEntity is TileEntityCauldron) {
                if (hand == EnumHand.MAIN_HAND) {
                    val itemStack = playerIn!!.getHeldItem(hand)

                    val posX = pos.x + RandomUtils.nextDouble(0.2, 0.8)
                    val posY = pos.y + 0.15 + (0.95 - 0.15) * tileEntity.waterAmount
                    val posZ = pos.z + RandomUtils.nextDouble(0.2, 0.8)

                    if (itemStack.isEmpty) {
                        // Stir the cauldron, if it has a stirring stick
                        // TODO: Add sounds for stirring
                        if (tileEntity.hasStirrer) {
                            if (tileEntity.getPartAmount() > 0 && tileEntity.waterAmount > 0.0) {
                                tileEntity.stirsRequired = stirsRequired / (tileEntity.getPartAmount() * tileEntity.waterAmount).toDouble()

                                if (worldIn.getBlockState(pos.down()).block == Blocks.FIRE) {
                                    tileEntity.stirsRequired /= 1.5
                                    // println("Fire!")
                                }

                                if (tileEntity.stirAmount >= tileEntity.stirsRequired) {
                                    tileEntity.fullyStirred = true
                                    tileEntity.resetParts()

                                    worldIn.spawnParticle(EnumParticleTypes.CRIT, posX, pos.up().y + 0.2, posZ, 0.0, 0.0, 0.0)
                                    // println("Fully stirred!")
                                }
                                else {
                                    if (tileEntity.waterAmount <= tileEntity.maxWater) {
                                        tileEntity.stirAmount += 2 / tileEntity.waterAmount

                                        // TODO: Make the particle "follow" the stirrer
                                        for (i in 1..RandomUtils.nextInt(3, 6)) {
                                            worldIn.spawnParticle(EnumParticleTypes.WATER_SPLASH, posX, posY, posZ, 0.0, 0.0, 0.0)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // TODO: Add sounds for adding water/items
                    else {
                        var splash = false

                        when (itemStack.item) {
                            Items.STICK -> {
                                // Has a stirring stick
                                if (!tileEntity.hasStirrer) {
                                    tileEntity.hasStirrer = true
                                    itemStack.shrink(1)

                                    splash = true
                                }
                            }
                            Items.WATER_BUCKET -> {
                                // Fill the cauldron
                                tileEntity.increaseWater(1f)
                                itemStack.shrink(1)
                                playerIn.inventory.addItemStackToInventory(ItemStack(Items.BUCKET))

                                tileEntity.stirAmount = 0.0

                                splash = true
                            }
                            Items.BUCKET -> {
                                // Empty the cauldron
                                if (tileEntity.waterAmount == tileEntity.maxWater) {
                                    itemStack.shrink(1)

                                    if (tileEntity.fullyStirred) {
                                        // TODO: Create a fluid for bug resonance
                                        tileEntity.resetWater()
                                        tileEntity.stirAmount = 0.0
                                    }
                                    else {
                                        playerIn.inventory.addItemStackToInventory(ItemStack(Items.WATER_BUCKET))

                                        tileEntity.resetWater()
                                        tileEntity.stirAmount = 0.0
                                    }
                                }
                            }
                            Items.POTIONITEM -> {
                                if (itemStack.tagCompound?.getString("Potion") == "minecraft:water") {
                                    itemStack.shrink(1)
                                    playerIn.inventory.addItemStackToInventory(ItemStack(Items.GLASS_BOTTLE))

                                    tileEntity.increaseWater(0.1f)

                                    if (tileEntity.stirAmount - 3 > 0) {
                                        tileEntity.stirAmount -= 3
                                    }
                                    else {
                                        tileEntity.stirAmount = 0.0
                                    }

                                    splash = true
                                }
                            }
                            Items.GLASS_BOTTLE -> {
                                if (tileEntity.waterAmount > 0.1f) {
                                    if (tileEntity.fullyStirred) {
                                        itemStack.shrink(1)
                                        playerIn.inventory.addItemStackToInventory(ItemStack(ModItems.bugJuice))

                                        tileEntity.decreaseWater(0.1f)
                                    }
                                    else {
                                        itemStack.shrink(1)
                                        playerIn.inventory.addItemStackToInventory(PotionUtils.addPotionToItemStack(ItemStack(Items.POTIONITEM), PotionTypes.WATER))

                                        tileEntity.decreaseWater(0.1f)
                                    }
                                }
                                else {
                                    tileEntity.resetWater()
                                    tileEntity.stirAmount = 0.0
                                }
                            }
                            is ItemBugPart -> {
                                // Add a part to the mix
                                if (tileEntity.getPartAmount() < tileEntity.maxParts) {
                                    // tileEntity.addPartAmout(1)
                                    tileEntity.addPart(itemStack.item as ItemBugPart)
                                    itemStack.shrink(1)

                                    splash = true
                                }
                            }
                        }

                        if (splash) {
                            for (i in 1..2) {
                                worldIn.spawnParticle(EnumParticleTypes.WATER_SPLASH, posX, posY, posZ, 0.0, 0.0, 0.0)
                            }
                        }
                    }
                }
            }
        }

        return true
    }

    @SideOnly(Side.CLIENT)
    override fun randomDisplayTick(stateIn: IBlockState, worldIn: World, pos: BlockPos, rand: Random) {
        val tileEntity = worldIn.getTileEntity(pos)

        if (tileEntity is TileEntityCauldron) {
            if (tileEntity.waterAmount >= 0.1f) {
                if (worldIn.getBlockState(pos.down()).block == Blocks.FIRE) {
                    val posX = pos.x + RandomUtils.nextDouble(0.2, 0.8)
                    val posY = pos.y + 0.15 + (0.95 - 0.15) * tileEntity.waterAmount
                    val posZ = pos.z + RandomUtils.nextDouble(0.2, 0.8)

                    // worldIn.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX, posY, posZ, 0.0, 0.0, 0.0)
                    Minecraft.getMinecraft().effectRenderer.addEffect(ParticleBubble(worldIn, posX, posY, posZ, 0.0, 0.0, 0.0))
                }
            }
        }
    }

    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity {
        return TileEntityCauldron(8)
    }
}
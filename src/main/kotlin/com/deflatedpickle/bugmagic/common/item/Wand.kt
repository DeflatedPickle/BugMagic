/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.item

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.common.item.GenericItem
import com.deflatedpickle.bugmagic.api.common.util.WorldUtil
import com.deflatedpickle.bugmagic.client.networking.message.MessageSelectedSpell
import com.deflatedpickle.bugmagic.common.capability.BugEssenceCapability
import com.deflatedpickle.bugmagic.common.capability.SpellCasterCapability
import com.deflatedpickle.bugmagic.common.capability.SpellLearnerCapability
import com.deflatedpickle.bugmagic.common.networking.message.MessageBugEssence
import com.deflatedpickle.bugmagic.common.networking.message.MessageSpellCaster
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.EnumAction
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import org.lwjgl.input.Mouse
import java.awt.Color
import java.util.concurrent.ThreadLocalRandom

class Wand(name: String, val essenceAmount: Int) : GenericItem(name, CreativeTabs.TOOLS) {
	companion object {
		const val SPELL_INDEX = "spell_index"
		const val HAS_BEEN_SCROLLED = "has_been_scrolled"
	}

	override fun getItemStackLimit(): Int = 1

	override fun onItemUse(player: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
		if (worldIn.getTileEntity(pos) != null) {
			player.getHeldItem(EnumHand.MAIN_HAND).tagCompound = NBTUtil.createPosTag(pos)
			return EnumActionResult.SUCCESS
		}

		return EnumActionResult.PASS
	}

	override fun onEntitySwing(entityLiving: EntityLivingBase, stack: ItemStack): Boolean {
		if (entityLiving is EntityPlayer) {
			val bugEssence = BugEssenceCapability.isCapable(stack)
			val spellLearner = SpellLearnerCapability.isCapable(entityLiving)
			val spellCaster = SpellCasterCapability.isCapable(stack)

			if (bugEssence != null && spellLearner != null && spellCaster != null) {
				if (!entityLiving.world.isRemote) {
					if (spellCaster.castSpellMap.containsKey(spellLearner.currentSpell)) {
						spellLearner.currentSpell?.let { spell ->
							if (bugEssence.current + spell.manaGain < bugEssence.max) {
								bugEssence.current += spell.manaGain
								BugMagic.CHANNEL.sendTo(MessageBugEssence(entityLiving.entityId, bugEssence.max, bugEssence.current), entityLiving as EntityPlayerMP)
							} else {
								bugEssence.current = bugEssence.max
								BugMagic.CHANNEL.sendTo(MessageBugEssence(entityLiving.entityId, bugEssence.max, bugEssence.current), entityLiving as EntityPlayerMP)
							}

							if (spellCaster.castSpellMap[spellLearner.currentSpell]!! <= 0) {
								spell.uncast(entityLiving, stack)
								spellCaster.castSpellMap.remove(spellLearner.currentSpell)
							} else {
								spell.uncast(entityLiving, stack)
								spellCaster.castSpellMap[spellLearner.currentSpell] = spellCaster.castSpellMap[spellLearner.currentSpell]!! - 1
							}
						}
					}

					return true
				} else {
					spellLearner.currentSpell?.let { spell ->
						if (spell.uncastingParticle != null) {
							entityLiving.world.spawnParticle(spell.uncastingParticle!!,
								entityLiving.posX,
								entityLiving.posY,
								entityLiving.posZ,
								0.0, 0.0, 0.0)
						}
					}
				}
			}
		}

		return false
	}

	// I used to change the use time. Can you believe that?
	// Thanks Upcraft, you've saved this mod once again uwu
	private fun whenFinished(
		stack: ItemStack,
		worldIn: World,
		entityLiving: EntityLivingBase
	): ItemStack {
		if (entityLiving is EntityPlayer) {
			val bugEssence = BugEssenceCapability.isCapable(stack)
			val spellLearner = SpellLearnerCapability.isCapable(entityLiving)
			val spellCaster = SpellCasterCapability.isCapable(stack)

			if (bugEssence != null && spellLearner != null && spellCaster != null) {
				spellLearner.currentSpell?.let { spell ->
					val manaCost = spell.manaLoss

					if (bugEssence.current - manaCost >= 0) {
						if (!worldIn.isRemote) {
							bugEssence.current -= manaCost

							if (!spellCaster.castSpellMap.containsKey(spellLearner.currentSpell)) {
								spell.cast(entityLiving, stack)
								spellCaster.castSpellMap[spellLearner.currentSpell] = 1
							} else if (spellCaster.castSpellMap[spellLearner.currentSpell]!!
								< spell.maxCount) {
								spell.cast(entityLiving, stack)
								spellCaster.castSpellMap[spellLearner.currentSpell] =
									spellCaster.castSpellMap[spellLearner.currentSpell]!! + 1
							}

							entityLiving.cooldownTracker.setCooldown(this, spell.maxCooldown)

							spellCaster.isCasting = false

							BugMagic.CHANNEL.sendTo(
								MessageSpellCaster(
									entityLiving.entityId,
									spellCaster.isCasting,
									spellCaster.castingCurrent
								),
								entityLiving as EntityPlayerMP?
							)
						} else {
							if (spell.finishingParticle != null) {
								entityLiving.world.spawnParticle(spell.finishingParticle!!,
									entityLiving.posX,
									entityLiving.posY + entityLiving.eyeHeight,
									entityLiving.posZ,
									0.0, 0.0, 0.0)
							}
						}
					}
				}
			}
		}

		return stack
	}

	override fun onUsingTick(stack: ItemStack, player: EntityLivingBase, count: Int) {
		if (player.world.isRemote) {
			val spellLearner = SpellLearnerCapability.isCapable(player)

			if (spellLearner != null) {
				spellLearner.currentSpell?.let { spell ->
					spell.castingParticle?.let { particle ->
						val size = (spell.radius + spell.castingShapeThickness) * (spell.tier.ordinal.toDouble() + 1)

						player.world.spawnParticle(
							particle,
							player.posX + ThreadLocalRandom.current().nextDouble(-size, size),
							player.posY + ThreadLocalRandom.current().nextDouble(-size, size),
							player.posZ + ThreadLocalRandom.current().nextDouble(-size, size),
							0.0, 0.0, 0.0
						)
					}
				}
			}
		} else {
			val spellCaster = SpellCasterCapability.isCapable(stack)

			if (spellCaster != null) {
				val spellLearner = SpellLearnerCapability.isCapable(player)

				if (spellLearner != null) {
					val spell = spellLearner.currentSpell
					if (spell == null) {
						if (player is EntityPlayer) {
							// They gotta be punished, right?
							player.cooldownTracker.setCooldown(this, WorldUtil.TICK * 5)
						}
					} else if (WorldUtil.DAY - count == spell.castingTime) {
						this.whenFinished(stack, player.world, player)
					}
				}
			}
		}
	}

	// This happens on both sides
	override fun onPlayerStoppedUsing(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase, timeLeft: Int) {
		if (entityLiving is EntityPlayer) {
			val spellLearner = SpellLearnerCapability.isCapable(entityLiving)
			val spellCaster = SpellCasterCapability.isCapable(stack)

			if (spellLearner != null && spellCaster != null) {
				spellCaster.isCasting = false

				if (worldIn.isRemote) {
					spellLearner.currentSpell?.let { spell ->
						spell.castingParticle?.let { particle ->
							if (spell.cancelingParticle != null) {
								entityLiving.world.spawnParticle(
									particle,
									entityLiving.posX,
									entityLiving.posY,
									entityLiving.posZ,
									0.0, 0.0, 0.0
								)
							}
						}
					}
				}
			}
		}
	}

	override fun onDroppedByPlayer(item: ItemStack, player: EntityPlayer): Boolean {
		val spellCaster = SpellCasterCapability.isCapable(item)

		if (spellCaster != null) {
			spellCaster.isCasting = false
			spellCaster.owner = null
		}

		return true
	}

	// Client-side only!
	fun onMouseEvent() {
		val player = Minecraft.getMinecraft().player

		// You'll have to trust me here, it's nullable
		@Suppress("SENSELESS_COMPARISON")
		if (player.activeHand == null) {
			player.activeHand = EnumHand.MAIN_HAND
		}

		val item = player.getHeldItem(player.activeHand)

		item.getOrCreateSubCompound(Reference.MOD_ID).setBoolean(HAS_BEEN_SCROLLED, true)
	}

	private fun getOrSetScrollState(itemStack: ItemStack, state: Boolean): Boolean {
		val nbtTagCompound = itemStack.getOrCreateSubCompound(Reference.MOD_ID)

		return if (nbtTagCompound.hasKey(HAS_BEEN_SCROLLED)) {
			nbtTagCompound.getBoolean(HAS_BEEN_SCROLLED)
		} else {
			nbtTagCompound.setBoolean(HAS_BEEN_SCROLLED, state)
			state
		}
	}

	override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
		// Only runs on the client
		if (worldIn.isRemote) {
			if (!stack.hasTagCompound()) {
				stack.tagCompound = NBTTagCompound()
			}

			if (isSelected && entityIn is EntityPlayer) {
				val spellLearner = SpellLearnerCapability.isCapable(entityIn)
				val spellCaster = SpellCasterCapability.isCapable(stack)

				if (entityIn.isSneaking) {
					if (spellLearner != null) {
						if (spellLearner.spellList.size > 0) {
							if (this.getOrSetScrollState(stack, true)) {
								with(stack.tagCompound!!.getInteger(SPELL_INDEX)) {
									when (Mouse.getEventDWheel()) {
										// Forward
										-120 -> {
											stack.tagCompound!!.setInteger(SPELL_INDEX,
												if (this < spellLearner.spellList.lastIndex) this + 1 else 0)
										}
										// Backwards
										120 -> {
											stack.tagCompound!!.setInteger(SPELL_INDEX,
												if (this > 0) this - 1 else spellLearner.spellList.lastIndex)
										}
									}
								}

								Minecraft.getMinecraft().ingameGUI.setOverlayMessage(TextComponentString("" +
									// The last spell
									"${TextFormatting.GRAY}${
										spellLearner.spellList[
											if (stack.tagCompound!!.getInteger(SPELL_INDEX) - 1 < 0)
												spellLearner.spellList.lastIndex
											else
												stack.tagCompound!!.getInteger(SPELL_INDEX) - 1].name
									} < " +

									// This spell
									"${TextFormatting.WHITE}${spellLearner.spellList[stack.tagCompound!!.getInteger(SPELL_INDEX)].name}" +

									// The next spell
									"${TextFormatting.GRAY} > ${
										spellLearner.spellList[
											if (stack.tagCompound!!.getInteger(SPELL_INDEX) + 1 > spellLearner.spellList.lastIndex)
												0
											else
												stack.tagCompound!!.getInteger(SPELL_INDEX) + 1].name
									}"
								), true)

								BugMagic.CHANNEL.sendToServer(MessageSelectedSpell(stack.tagCompound!!.getInteger(SPELL_INDEX)))

								stack
									.getOrCreateSubCompound(Reference.MOD_ID)
									.setBoolean(HAS_BEEN_SCROLLED, false)
							}
						}
					}
				} else {
					if (spellCaster != null && spellCaster.isCasting) {
						spellCaster.castingCurrent++
					}
				}
			}
		}
	}

	// Make the wands in the creative inventory start with the right essence amount
	override fun getSubItems(tab: CreativeTabs, items: NonNullList<ItemStack>) {
		if (isInCreativeTab(tab)) {
			items.add(ItemStack(this).apply { setEssence(this) })
		}
	}

	// Called when the wand is crafted
	override fun onCreated(stack: ItemStack, worldIn: World, playerIn: EntityPlayer) {
		setEssence(stack)
	}

	private fun setEssence(stack: ItemStack) {
		val bugEssence = BugEssenceCapability.isCapable(stack)

		if (bugEssence != null) {
			bugEssence.max = this.essenceAmount
			bugEssence.current = this.essenceAmount
		}
	}

	override fun showDurabilityBar(stack: ItemStack): Boolean = true
	// Client-side only!
	override fun getDurabilityForDisplay(stack: ItemStack): Double {
		val spellCaster = SpellCasterCapability.isCapable(stack)
		val spellLearner = SpellLearnerCapability.isCapable(Minecraft.getMinecraft().player)
		val bugEssence = BugEssenceCapability.isCapable(stack)

		return if (spellCaster != null && spellLearner != null && bugEssence != null) {
			if (spellCaster.isCasting) {
				((spellLearner.currentSpell!!.castingTime - spellCaster.castingCurrent) / spellLearner.currentSpell!!.castingTime).toDouble()
			} else {
				// When going to craft a wand, it's max and current are 0
				// So we have to check this...
				if (bugEssence.current + bugEssence.max == 0) 1.0
				else (bugEssence.max - bugEssence.current) / bugEssence.max.toDouble()
			}
		} else {
			1.0
		}
	}

	override fun getRGBDurabilityForDisplay(stack: ItemStack): Int {
		val spellCaster = SpellCasterCapability.isCapable(stack)

		return if (spellCaster != null && spellCaster.isCasting) {
			// Magenta
			255 shl 16 or (0 shl 8) or 255
		} else {
			super.getRGBDurabilityForDisplay(stack)
		}
	}

	// We want to show the user how much energy their wand has
	override fun getItemStackDisplayName(stack: ItemStack): String {
		val bugEssence = BugEssenceCapability.isCapable(stack)

		return if (bugEssence != null) {
			super.getItemStackDisplayName(stack) +
				" (${TextFormatting.GREEN}${
					bugEssence.current
				}/${
					bugEssence.max
				}${TextFormatting.WHITE} BE)"
		} else {
			super.getItemStackDisplayName(stack)
		}
	}

	override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag) {
		if (stack.hasTagCompound() && worldIn!!.isRemote) {
			val spellCaster = SpellCasterCapability.isCapable(stack)

			if (spellCaster != null) {
				tooltip.add("Owner: (${spellCaster.owner})")
			}

			val spellLearner = SpellLearnerCapability.isCapable(BugMagic.proxy!!.getPlayer()!!)

			if (spellLearner != null && spellLearner.spellList.size > 0) {
				val currentSpell = spellLearner.spellList[stack.tagCompound!!.getInteger(SPELL_INDEX)]
				tooltip.add("Spell: ${currentSpell.name} (${currentSpell.cult.colour}${currentSpell.cult}${TextFormatting.GRAY})")
			} else {
				tooltip.add("This Bugoneer knows not a spell")
			}
		}
	}

	override fun getMaxItemUseDuration(stack: ItemStack): Int = WorldUtil.DAY
	override fun getItemUseAction(stack: ItemStack?): EnumAction = EnumAction.BOW
	override fun isDamageable(): Boolean = false

	override fun onItemRightClick(worldIn: World?, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
		playerIn.activeHand = handIn
		val stack = playerIn.getHeldItem(playerIn.activeHand)

		val spellCaster = SpellCasterCapability.isCapable(stack)
		val spellLearner = SpellLearnerCapability.isCapable(playerIn)
		val bugEssence = BugEssenceCapability.isCapable(stack)

		return if (spellCaster == null || spellLearner == null || bugEssence == null) {
			ActionResult(EnumActionResult.FAIL, playerIn.getHeldItem(handIn))
		} else {
			// They haven't selected a spell, they can't cast it
			if (spellLearner.currentSpell == null) return ActionResult(
				EnumActionResult.FAIL,
				playerIn.getHeldItem(handIn)
			)

			// If the player doesn't have enough mana to cast the selected spell, fail
			return if (bugEssence.current - spellLearner.currentSpell!!.manaLoss <= 0) {
				ActionResult(EnumActionResult.FAIL, playerIn.getHeldItem(handIn))
			} else {
				spellCaster.isCasting = true
				ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn))
			}
		}
	}

	override fun getNBTShareTag(stack: ItemStack): NBTTagCompound? {
		if (!stack.hasTagCompound()) {
			stack.tagCompound = NBTTagCompound()
		}

		return super.getNBTShareTag(stack)
	}
}

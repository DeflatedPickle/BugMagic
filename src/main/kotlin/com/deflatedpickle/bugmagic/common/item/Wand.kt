/* Copyright (c) 2019-2020 DeflatedPickle under the MIT license */

package com.deflatedpickle.bugmagic.common.item

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.api.capability.SpellCaster
import com.deflatedpickle.bugmagic.api.capability.SpellLearner
import com.deflatedpickle.bugmagic.api.client.util.extension.spawn
import com.deflatedpickle.bugmagic.api.common.item.GenericItem
import com.deflatedpickle.bugmagic.api.common.util.WorldUtil
import com.deflatedpickle.bugmagic.api.common.util.function.green
import com.deflatedpickle.bugmagic.api.common.util.function.minus
import com.deflatedpickle.bugmagic.api.common.util.function.white
import com.deflatedpickle.bugmagic.api.spell.Spell
import com.deflatedpickle.bugmagic.client.networking.message.MessageSelectedSpell
import com.deflatedpickle.bugmagic.common.capability.BugEssenceCapability
import com.deflatedpickle.bugmagic.common.capability.SpellCasterCapability
import com.deflatedpickle.bugmagic.common.capability.SpellLearnerCapability
import com.deflatedpickle.bugmagic.common.init.EnchantmentInit
import com.deflatedpickle.bugmagic.common.networking.message.MessagePlayerBugEssence
import com.deflatedpickle.bugmagic.common.networking.message.MessageSpellCaster
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.EnumAction
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import org.lwjgl.input.Mouse
import kotlin.math.max
import kotlin.math.min

/**
 * An item that can be used to cast a spell
 */
class Wand(
	name: String,
	private val essenceAmount: Int
) : GenericItem(
	name = name,
	creativeTab = BugMagic.tab,
	isDamageable = false,
	itemEnchantability = 12,
	showDurabilityBar = true,
	stackLimit = 1,
	itemUseDuration = WorldUtil.DAY,
	actionItemUse = EnumAction.BOW
) {
	companion object {
		const val SPELL_INDEX = "spell_index"
		const val HAS_BEEN_SCROLLED = "has_been_scrolled"
	}

	override fun onItemUse(
		player: EntityPlayer,
		worldIn: World,
		pos: BlockPos,
		hand: EnumHand,
		facing: EnumFacing,
		hitX: Float,
		hitY: Float,
		hitZ: Float
	): EnumActionResult {
		if (worldIn.getTileEntity(pos) != null) {
			player.getHeldItem(EnumHand.MAIN_HAND).tagCompound = NBTUtil.createPosTag(pos)
			return EnumActionResult.SUCCESS
		}

		return EnumActionResult.PASS
	}

	// Runs through logic that *could* uncast the selected spell
	override fun onEntitySwing(entityLiving: EntityLivingBase, stack: ItemStack): Boolean {
		if (entityLiving is EntityPlayer) {
			val bugEssence = BugEssenceCapability.isCapable(stack)
			val spellLearner = SpellLearnerCapability.isCapable(entityLiving)
			val spellCaster = SpellCasterCapability.isCapable(stack)

			if (bugEssence != null && spellLearner != null && spellCaster != null) {
				if (!entityLiving.world.isRemote) {
					if (spellCaster.castSpellMap.containsKey(spellLearner.currentSpell)) {
						spellLearner.currentSpell?.let { spell ->
							// If they have less mana than this would recover, add it
							if (bugEssence.current + spell.manaGain < bugEssence.max) {
								bugEssence.current += spell.manaGain
							}
							// If they'd have more than their max, set it to the max
							else {
								bugEssence.current = bugEssence.max
							}
							BugMagic.CHANNEL.sendTo(
								MessagePlayerBugEssence(
									entityLiving.entityId,
									bugEssence.max,
									bugEssence.current
								), entityLiving as EntityPlayerMP
							)

							// If they have 0 or less, uncast and remove it
							if (spellCaster.castSpellMap[spellLearner.currentSpell]!! <= 0) {
								spell.uncast(entityLiving, stack)
								// We can afford to remove any less than 0,
								// as we check if they have any cast before hand
								spellCaster.castSpellMap.remove(spellLearner.currentSpell)
							}
							// If they have more than 0, uncast and decrease their count
							else {
								spell.uncast(entityLiving, stack)
								spellCaster.castSpellMap[spellLearner.currentSpell] =
									spellCaster.castSpellMap[spellLearner.currentSpell]!! - 1
							}
						}
					}

					return true
				} else {
					spellLearner.currentSpell?.uncastingParticle?.spawn(entityLiving)
				}
			}
		}

		return false
	}

	// Checks if the spell cast was the first of it's kind
	// If so, cast the spell and add the spell as a new entry with a value of 1
	// If not, cast the spell and increase the cast count
	private fun checkNewCast(
		spellCaster: SpellCaster, spellLearner: SpellLearner,
		spell: Spell, entityLiving: EntityPlayer, stack: ItemStack
	) {
		if (!spellCaster.castSpellMap.containsKey(spellLearner.currentSpell)) {
			spell.cast(entityLiving, stack)
			spellCaster.castSpellMap[spellLearner.currentSpell] = 1
		} else if (spellCaster.castSpellMap[spellLearner.currentSpell]!!
			< spell.maxCount
		) {
			spell.cast(entityLiving, stack)
			spellCaster.castSpellMap[spellLearner.currentSpell] =
				spellCaster.castSpellMap[spellLearner.currentSpell]!! + 1
		}
	}

	// I used to change the use time. Can you believe that?
	// Thanks Upcraft, you've saved this mod once again uwu
	private fun whenFinished(
		stack: ItemStack,
		worldIn: World,
		entityLiving: EntityLivingBase
	): ItemStack {
		if (entityLiving is EntityPlayer) {
			val itemBugEssence = BugEssenceCapability.isCapable(stack)
			val entityBugEssence = BugEssenceCapability.isCapable(entityLiving)
			val spellLearner = SpellLearnerCapability.isCapable(entityLiving)
			val spellCaster = SpellCasterCapability.isCapable(stack)

			if (itemBugEssence != null && entityBugEssence != null &&
				spellLearner != null && spellCaster != null
			) {
				spellLearner.currentSpell?.let { spell ->
					val bloodleakLevel = EnchantmentHelper.getEnchantmentLevel(
						EnchantmentInit.BLOOD_LEAK,
						stack
					)
					val manaCost = spell.manaLoss

					if (bloodleakLevel > 0) {
						if (entityBugEssence.current - manaCost >= 0) {
							if (!worldIn.isRemote) {
								entityBugEssence.current -= manaCost
								// Entities don't automatically sync
								BugMagic.CHANNEL.sendTo(
									MessagePlayerBugEssence(
										entityLiving.entityId,
										entityBugEssence.max,
										entityBugEssence.current
									),
									entityLiving as EntityPlayerMP
								)

								this.checkNewCast(
									spellCaster, spellLearner,
									spell, entityLiving, stack
								)

								// TODO: Sync the cast spells to the client

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
									spell.finishingParticle!!.spawn(
										entityLiving,
										plusY = entityLiving.eyeHeight.toDouble()
									)
								}
							}
						}
					} else {
						if (itemBugEssence.current - manaCost >= 0) {
							if (!worldIn.isRemote) {
								itemBugEssence.current -= manaCost

								this.checkNewCast(
									spellCaster, spellLearner,
									spell, entityLiving, stack
								)

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
									spell.finishingParticle!!.spawn(
										entityLiving,
										plusY = entityLiving.eyeHeight.toDouble()
									)
								}
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
						particle.spawn(player, size)
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
					spellLearner
						.currentSpell
						?.cancelingParticle
						?.spawn(entityLiving, 0.0)
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
								// Scrolls the spell forward/backwards
								with(stack.tagCompound!!.getInteger(SPELL_INDEX)) {
									when (Mouse.getEventDWheel()) {
										// Forward
										-120 -> {
											stack.tagCompound!!.setInteger(
												SPELL_INDEX,
												if (this < spellLearner.spellList.lastIndex) this + 1 else 0
											)
										}
										// Backwards
										120 -> {
											stack.tagCompound!!.setInteger(
												SPELL_INDEX,
												if (this > 0) this - 1 else spellLearner.spellList.lastIndex
											)
										}
									}
								}

								// Displays the last, new and next spell above the hotbar
								Minecraft.getMinecraft().ingameGUI.setOverlayMessage(
									TextComponentString(
										"" +
											// The last spell
											"${TextFormatting.GRAY}${
												spellLearner.spellList[
													if (stack.tagCompound!!.getInteger(SPELL_INDEX) - 1 < 0)
														spellLearner.spellList.lastIndex
													else
														stack.tagCompound!!.getInteger(SPELL_INDEX) - 1].name
											} < " +

											// This spell
											"${TextFormatting.WHITE}${
												spellLearner.spellList[stack.tagCompound!!.getInteger(
													SPELL_INDEX
												)].name
											}" +

											// The next spell
											"${TextFormatting.GRAY} > ${
												spellLearner.spellList[
													if (stack.tagCompound!!.getInteger(SPELL_INDEX) + 1 > spellLearner.spellList.lastIndex)
														0
													else
														stack.tagCompound!!.getInteger(SPELL_INDEX) + 1].name
											}"
									), true
								)

								// Sends the new selected spell to the server
								BugMagic.CHANNEL.sendToServer(
									MessageSelectedSpell(
										stack.tagCompound!!.getInteger(
											SPELL_INDEX
										)
									)
								)

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

	// Client-side only!
	override fun getDurabilityForDisplay(stack: ItemStack): Double {
		val spellCaster = SpellCasterCapability.isCapable(stack)
		val spellLearner = SpellLearnerCapability.isCapable(Minecraft.getMinecraft().player)
		val bugEssence = BugEssenceCapability.isCapable(stack)

		return if (spellCaster != null && spellLearner != null && bugEssence != null) {
			if (spellCaster.isCasting) {
				// This shows the casting progress of the current spell
				max(
					0.0,
					((spellLearner.currentSpell!!.castingTime - spellCaster.castingCurrent) / spellLearner.currentSpell!!.castingTime).toDouble()
				)
			} else {
				// When going to craft a wand, it's max and current are 0
				// So we have to check this...
				if (bugEssence.current + bugEssence.max == 0) 1.0
				// This shows the durability as the amount of bug essence left
				else (bugEssence.max - bugEssence.current) / bugEssence.max.toDouble()
			}
		} else {
			1.0
		}
	}

	// This turns the durability bar purple when casting a spell
	override fun getRGBDurabilityForDisplay(stack: ItemStack): Int {
		val spellCaster = SpellCasterCapability.isCapable(stack)
		val spellLearner = SpellLearnerCapability.isCapable(Minecraft.getMinecraft().player)
		val bugEssence = BugEssenceCapability.isCapable(stack)

		@Suppress("VARIABLE_WITH_REDUNDANT_INITIALIZER", "CanBeVal")
		return if (spellCaster != null && spellLearner != null && bugEssence != null && spellCaster.isCasting) {
			val castUpdate = ((spellLearner.currentSpell!!.castingTime - spellCaster.castingCurrent) / spellLearner.currentSpell!!.castingTime).toDouble()

			var red = 0
			var green = 0
			var blue = 0

			if (castUpdate < 0.0) {
				// We don't have enough to cast it, make it red
				red = 255
			} else {
				// It's being cast, make it magenta
				red = 255
				blue = 255
			}

			red shl 16 or (green shl 8) or blue
		} else {
			super.getRGBDurabilityForDisplay(stack)
		}
	}

	// We want to show the user how much energy their wand has
	override fun getItemStackDisplayName(stack: ItemStack): String {
		val bugEssence = BugEssenceCapability.isCapable(stack)

		return if (bugEssence != null) {
			super.getItemStackDisplayName(stack) + "(" +
				(green - "${bugEssence.current}/${bugEssence.max}") + " " +
				(white - "BE") + ")"
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

	override fun isEnchantable(stack: ItemStack): Boolean = stack.item is Wand
}

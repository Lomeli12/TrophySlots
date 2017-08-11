package net.lomeli.trophyslots.core

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.capabilities.slots.SlotManager
import net.lomeli.trophyslots.core.triggers.AllTriggers
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.EnumRarity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.translation.I18n
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.input.Keyboard

class ItemTrophy : Item {
    constructor() : super() {
        this.setRegistryName(TrophySlots.MOD_ID, "trophy")
        this.creativeTab = CreativeTabs.MISC
        this.unlocalizedName = "${TrophySlots.MOD_ID}.trophy"
        setMaxStackSize(1)
    }

    fun fromVillager(stack: ItemStack?): Boolean = if (!stack!!.isEmpty && stack.hasTagCompound()) stack.tagCompound!!.getBoolean("fromVillager") else false

    @SideOnly(Side.CLIENT) fun safeKeyDown(keyCode: Int): Boolean {
        try {
            return Keyboard.isKeyDown(keyCode)
        } catch (e: Exception) {
            return false
        }
    }

    override fun getSubItems(tab: CreativeTabs?, subItems: NonNullList<ItemStack>?) {
        if (isInCreativeTab(tab) && subItems != null) {
            subItems.add(ItemStack(this))
            subItems.add(ItemStack(this, 1, 1))
        }
    }

    override fun onItemRightClick(world: World?, player: EntityPlayer?, hand: EnumHand?): ActionResult<ItemStack> {
        var result = EnumActionResult.FAIL
        var stack = player?.getHeldItem(hand)
        if (world != null && !world.isRemote && player != null && !stack!!.isEmpty) {
            val slotInfo = SlotManager.getPlayerSlotInfo(player)!!
            if (!TrophySlots.canBuyTrophy && fromVillager(stack))
                player.sendStatusMessage(TextComponentTranslation("msg.trophyslots.villager"), true)
            else if (!TrophySlots.canUseTrophy)
                player.sendStatusMessage(TextComponentTranslation("msg.trophyslots.trophy"), true)
            else if (hand == EnumHand.MAIN_HAND) {
                if (stack.itemDamage == 0) {
                    if (slotInfo.unlockSlot(1)) {
                        player.sendStatusMessage(TextComponentTranslation("msg.trophyslots.unlock"), true)
                        SlotManager.updateClient(player, slotInfo)
                        if (!player.capabilities.isCreativeMode)
                            stack.shrink(1)
                        if (player is EntityPlayerMP) AllTriggers.UNLOCK_SLOT.trigger(player)
                    }
                } else {
                    if (slotInfo.unlockSlot(slotInfo.getMaxSlots())) {
                        player.sendStatusMessage(TextComponentTranslation("msg.trophyslots.unlock_all"), true)
                        SlotManager.updateClient(player, slotInfo)
                        if (!player.capabilities.isCreativeMode)
                            stack.shrink(1)
                        if (player is EntityPlayerMP) AllTriggers.UNLOCK_SLOT.trigger(player)
                    }
                }
                result = EnumActionResult.PASS
            }
        }
        return ActionResult(result, stack!!)
    }

    override fun addInformation(stack: ItemStack?, worldIn: World?, tooltip: MutableList<String>?, flagIn: ITooltipFlag?) {
        if (tooltip == null && stack!!.isEmpty)
            return
        if (stack!!.itemDamage == 0) {
            if (safeKeyDown(Keyboard.KEY_LSHIFT)) {
                if (fromVillager(stack) && !TrophySlots.canBuyTrophy)
                    tooltip?.add(I18n.translateToLocal("subtext.torphyslots.trophy.villager"))
                tooltip?.add(I18n.translateToLocal("subtext.trophyslots.trophy"))
                if (TrophySlots.canUseTrophy)
                    tooltip?.add(I18n.translateToLocal("subtext.trophyslots.trophy.can_use"))
                else
                    tooltip?.add(I18n.translateToLocal("subtext.trophyslots.trophy.cannot_use"))
            } else {
                tooltip?.add(I18n.translateToLocal("subtext.trophyslots.info"))
                if (fromVillager(stack) && !TrophySlots.canBuyTrophy)
                    tooltip?.add(I18n.translateToLocal("subtext.torphyslots.trophy.villager"))
            }
        } else {
            if (fromVillager(stack) && !TrophySlots.canBuyTrophy)
                tooltip?.add(I18n.translateToLocal("subtext.torphyslots.trophy.villager"))
            tooltip?.add(I18n.translateToLocal("subtext.torphyslots.trophy.cheat"))
            if (TrophySlots.canUseTrophy)
                tooltip?.add(I18n.translateToLocal("subtext.trophyslots.trophy.can_use"))
            else
                tooltip?.add(I18n.translateToLocal("subtext.trophyslots.trophy.cannot_use"))
        }
    }

    override fun getRarity(stack: ItemStack?): EnumRarity? = if (stack!!.isEmpty) EnumRarity.COMMON else if (stack.itemDamage == 1) EnumRarity.RARE else EnumRarity.UNCOMMON
}
package net.lomeli.trophyslots.core

import net.lomeli.trophyslots.TrophySlots
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumRarity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.translation.I18n
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.input.Keyboard

class ItemTrophy : Item {
    constructor() : super() {
        this.creativeTab = CreativeTabs.MISC
        this.unlocalizedName = "${TrophySlots.MOD_ID}.trophy"
        setMaxStackSize(1)
    }

    fun fromVillager(stack: ItemStack): Boolean = if (stack.hasTagCompound()) stack.tagCompound!!.getBoolean("fromVillager") else false

    @SideOnly(Side.CLIENT) fun safeKeyDown(keyCode: Int): Boolean {
        try {
            return Keyboard.isKeyDown(keyCode)
        } catch (e: Exception) {
            return false
        }
    }

    @SideOnly(Side.CLIENT) override fun getSubItems(item: Item?, tab: CreativeTabs?, subItems: MutableList<ItemStack>?) {
        if (subItems != null) {
            subItems.add(ItemStack(item))
            subItems.add(ItemStack(item, 1, 1))
        }
    }

    override fun onItemRightClick(stack: ItemStack?, world: World?, player: EntityPlayer?, hand: EnumHand?): ActionResult<ItemStack>? {
        var result = EnumActionResult.FAIL;
        if (world != null && !world.isRemote && stack != null && player != null) {
            if (!TrophySlots.canBuyTrophy && fromVillager(stack))
                player.addChatComponentMessage(TextComponentTranslation("msg.trophyslots.villager"))
            else if (!TrophySlots.canUseTrophy)
                player.addChatComponentMessage(TextComponentTranslation("msg.trophyslots.trophy"))
            else if (hand == EnumHand.MAIN_HAND) {
                if (stack.itemDamage == 0) {
                    if (TrophySlots.proxy!!.unlockSlot(player)) {
                        if (!player.capabilities.isCreativeMode)
                            stack.stackSize--
                    }
                } else {
                    if (TrophySlots.proxy!!.unlockAllSlots(player)) {
                        if (!player.capabilities.isCreativeMode)
                            stack.stackSize--
                    }
                }
                result = EnumActionResult.PASS;
            }
        }
        return ActionResult<ItemStack>(result, stack);
    }

    @SideOnly(Side.CLIENT) override fun addInformation(stack: ItemStack?, playerIn: EntityPlayer?, tooltip: MutableList<String>?, advanced: Boolean) {
        if (tooltip == null || stack == null)
            return
        if (stack.itemDamage == 0) {
            if (safeKeyDown(Keyboard.KEY_LSHIFT)) {
                if (fromVillager(stack) && !TrophySlots.canBuyTrophy)
                    tooltip.add(I18n.translateToLocal("subtext.torphyslots.trophy.villager"))
                tooltip.add(I18n.translateToLocal("subtext.trophyslots.trophy"))
                if (TrophySlots.canUseTrophy)
                    tooltip.add(I18n.translateToLocal("subtext.trophyslots.trophy.canUse"))
                else
                    tooltip.add(I18n.translateToLocal("subtext.trophyslots.trophy.cannotUse"))
            } else {
                tooltip.add(I18n.translateToLocal("subtext.trophyslots.info"))
                if (fromVillager(stack) && !TrophySlots.canBuyTrophy)
                    tooltip.add(I18n.translateToLocal("subtext.torphyslots.trophy.villager"))
            }
        } else {
            if (fromVillager(stack) && !TrophySlots.canBuyTrophy)
                tooltip.add(I18n.translateToLocal("subtext.torphyslots.trophy.villager"))
            tooltip.add(I18n.translateToLocal("subtext.torphyslots.trophy.cheat"))
            if (TrophySlots.canUseTrophy)
                tooltip.add(I18n.translateToLocal("subtext.trophyslots.trophy.canUse"))
            else
                tooltip.add(I18n.translateToLocal("subtext.trophyslots.trophy.cannotUse"))
        }
    }

    override fun getRarity(stack: ItemStack?): EnumRarity? = if (stack == null) EnumRarity.COMMON else if (stack.itemDamage == 1) EnumRarity.RARE else EnumRarity.UNCOMMON
}
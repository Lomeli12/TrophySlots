package net.lomeli.trophyslots.core

import net.lomeli.trophyslots.TrophySlots
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumRarity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ChatComponentTranslation
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.input.Keyboard

public class ItemTrophy : Item {
    constructor() : super() {
        setCreativeTab(CreativeTabs.tabMisc)
        setMaxStackSize(1)
        setUnlocalizedName("${TrophySlots.MOD_ID}.trophy")
    }

    public fun fromVillager(stack: ItemStack): Boolean = if (stack.hasTagCompound()) stack.tagCompound.getBoolean("fromVillager") else false

    public fun safeKeyDown(keyCode: Int): Boolean {
        try {
            return Keyboard.isKeyDown(keyCode)
        } catch (e: Exception) {
            return false
        }
    }

    override fun getSubItems(item: Item?, tab: CreativeTabs?, subItems: MutableList<Any?>?) {
        if (subItems != null && item != null) {
            subItems.add(ItemStack(item))
            subItems.add(ItemStack(item, 1, 1))
        }
    }

    override fun onItemRightClick(stack: ItemStack?, world: World?, player: EntityPlayer?): ItemStack? {
        if (world != null && !world.isRemote && stack != null && player != null) {
            if (!TrophySlots.canBuyTrophy && fromVillager(stack))
                player.addChatComponentMessage(ChatComponentTranslation("msg.trophyslots.villager"))
            else if (!TrophySlots.canUseTrophy)
                player.addChatComponentMessage(ChatComponentTranslation("msg.trophyslots.trophy"))
            else {
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
            }
        }
        return stack;
    }

    @SideOnly(Side.CLIENT) override fun addInformation(stack: ItemStack?, player: EntityPlayer?, tooltip: MutableList<Any?>?, advanced: Boolean) {
        if (tooltip == null || stack == null)
            return
        if (stack.itemDamage == 0) {
            if (safeKeyDown(Keyboard.KEY_LSHIFT)) {
                if (fromVillager(stack) && !TrophySlots.canBuyTrophy)
                    tooltip.add(StatCollector.translateToLocal("subtext.torphyslots.trophy.villager"))
                tooltip.add(StatCollector.translateToLocal("subtext.trophyslots.trophy"))
                if (TrophySlots.canUseTrophy)
                    tooltip.add(StatCollector.translateToLocal("subtext.trophyslots.trophy.canUse"))
                else
                    tooltip.add(StatCollector.translateToLocal("subtext.trophyslots.trophy.cannotUse"))
            } else {
                tooltip.add(StatCollector.translateToLocal("subtext.trophyslots.info"))
                if (fromVillager(stack) && !TrophySlots.canBuyTrophy)
                    tooltip.add(StatCollector.translateToLocal("subtext.torphyslots.trophy.villager"))
            }
        } else {
            if (fromVillager(stack) && !TrophySlots.canBuyTrophy)
                tooltip.add(StatCollector.translateToLocal("subtext.torphyslots.trophy.villager"))
            tooltip.add(StatCollector.translateToLocal("subtext.torphyslots.trophy.cheat"))
            if (TrophySlots.canUseTrophy)
                tooltip.add(StatCollector.translateToLocal("subtext.trophyslots.trophy.canUse"))
            else
                tooltip.add(StatCollector.translateToLocal("subtext.trophyslots.trophy.cannotUse"))
        }
    }

    override fun getRarity(stack: ItemStack?): EnumRarity? = if (stack == null) EnumRarity.COMMON else if (stack.itemDamage == 1) EnumRarity.RARE else EnumRarity.UNCOMMON
}
package net.lomeli.trophyslots.core;

import org.lwjgl.input.Keyboard;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.trophyslots.TrophySlots;

public class ItemTrophy extends Item {
    public ItemTrophy() {
        super();
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setMaxStackSize(1);
        this.setUnlocalizedName(TrophySlots.MOD_ID + ".trophy");
        this.setTextureName(TrophySlots.MOD_ID + ":trophy");
    }

    public static boolean fromVillager(ItemStack stack) {
        if (stack.hasTagCompound())
            return stack.getTagCompound().getBoolean("fromVillager");
        return false;
    }

    public static boolean safeKeyDown(int keyCode) {
        try {
            return Keyboard.isKeyDown(keyCode);
        } catch (IndexOutOfBoundsException var2) {
            return false;
        }
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item));
        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            if (!TrophySlots.canBuyTrophy && fromVillager(stack))
                player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("msg.trophyslots.villager")));
            else if (!TrophySlots.canUseTrophy)
                player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("msg.trophyslots.trophy")));
            else {
                if (stack.getItemDamage() == 0) {
                    if (TrophySlots.proxy.unlockSlot(player)) {
                        if (!player.capabilities.isCreativeMode)
                            stack.stackSize--;
                    }
                } else {
                    if (TrophySlots.proxy.unlockAllSlots(player)) {
                        if (!player.capabilities.isCreativeMode)
                            stack.stackSize--;
                    }
                }
            }
        }
        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean var) {
        if (stack.getItemDamage() == 0) {
            if (safeKeyDown(Keyboard.KEY_LSHIFT)) {
                if (fromVillager(stack) && !TrophySlots.canBuyTrophy)
                    list.add(StatCollector.translateToLocal("subtext.torphyslots.trophy.villager"));
                list.add(StatCollector.translateToLocal("subtext.trophyslots.trophy"));
                list.add(StatCollector.translateToLocal(TrophySlots.canUseTrophy ? "subtext.trophyslots.trophy.canUse" : "subtext.trophyslots.trophy.cannotUse"));
            } else {
                list.add(StatCollector.translateToLocal("subtext.trophyslots.info"));
                if (fromVillager(stack) && !TrophySlots.canBuyTrophy)
                    list.add(StatCollector.translateToLocal("subtext.torphyslots.trophy.villager"));
            }
        } else {
            if (fromVillager(stack) && !TrophySlots.canBuyTrophy)
                list.add(StatCollector.translateToLocal("subtext.torphyslots.trophy.villager"));
            list.add(StatCollector.translateToLocal("subtext.torphyslots.trophy.cheat"));
            list.add(StatCollector.translateToLocal(TrophySlots.canUseTrophy ? "subtext.trophyslots.trophy.canUse" : "subtext.trophyslots.trophy.cannotUse"));
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return stack.getItemDamage() == 1 ? EnumRarity.rare : EnumRarity.uncommon;
    }
}

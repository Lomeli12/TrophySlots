package net.lomeli.trophyslots.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.ServerConfig;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;
import net.lomeli.trophyslots.core.criterion.ModCriteria;
import net.lomeli.trophyslots.core.network.MessageSlotClient;
import net.lomeli.trophyslots.core.network.PacketHandler;
import net.lomeli.trophyslots.utils.InventoryUtils;
import net.lomeli.trophyslots.utils.NBTUtils;

public class ItemTrophy extends Item {
    static final String VILLAGER_TROPHY = "fromVillager";
    private static final String SLOT_AMOUNTS = "slotAmounts";

    private TrophyType trophyType;

    public ItemTrophy(TrophyType trophyType) {
        super(new Item.Properties().maxStackSize(1).group(ItemGroup.MISC));
        this.setRegistryName(TrophySlots.MOD_ID, trophyType.getName());
        this.trophyType = trophyType;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        ActionResult<ItemStack> result = ActionResult.resultFail(stack);
        if (!world.isRemote && !stack.isEmpty()) {
            if (!ServerConfig.canBuyTrophy && fromVillager(stack))
                player.sendStatusMessage(new TranslationTextComponent("msg.trophyslots.villager"), false);
            else if (!ServerConfig.canUseTrophy)
                player.sendStatusMessage(new TranslationTextComponent("msg.trophyslots.trophy"), false);
            else if (hand == Hand.MAIN_HAND) {
                int amount = trophyType == TrophyType.MASTER ? InventoryUtils.getMaxUnlockableSlots() : getSlotAmounts(stack);
                if (amount != 0) {
                    String msg = amount == InventoryUtils.getMaxUnlockableSlots() ? "msg.trophyslots.unlock_all" :
                            amount == 1 ? "msg.trophyslots.unlock" :
                                    amount > 1 ? "msg.trophyslots.unlock_slot" :
                                            amount == -1 ? "msg.trophyslots.lost_slot" : "msg.trophyslots.lost_slot.multiple";
                    IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
                    if (playerSlots != null && playerSlots.unlockSlot(amount)) {
                        if (amount == 1 || amount == -1 || amount == InventoryUtils.getMaxUnlockableSlots())
                            player.sendStatusMessage(new TranslationTextComponent(msg), false);
                        else
                            player.sendStatusMessage(new TranslationTextComponent(msg, amount), false);

                        if (!player.abilities.isCreativeMode)
                            stack.shrink(1);
                        if (player instanceof ServerPlayerEntity) {
                            TrophySlots.log.info("Sending slot update packet to player {}.", player.getName().getString());
                            PacketHandler.sendToClient(new MessageSlotClient(playerSlots.getSlotsUnlocked()),
                                    (ServerPlayerEntity) player);
                            ModCriteria.UNLOCK_SLOT.trigger((ServerPlayerEntity) player);
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> toolTips, ITooltipFlag flag) {
        //if (ClientUtil.safeKeyDown(ClientUtil.LEFT_SHIFT)) {
        if (((ItemTrophy) stack.getItem()).getTrophyType() == TrophyType.MASTER)
            toolTips.add(new TranslationTextComponent("subtext.trophyslots.trophy.cheat"));
        else {
            int amount = getSlotAmounts(stack);
            if (amount > 1)
                toolTips.add(new TranslationTextComponent("subtext.trophyslots.trophy.x", amount));
            else if (amount == -1)
                toolTips.add(new TranslationTextComponent("subtext.trophyslots.trophy.x.neg.one"));
            else if (amount < -1)
                toolTips.add(new TranslationTextComponent("subtext.trophyslots.trophy.x.neg", amount * -1));
            else
                toolTips.add(new TranslationTextComponent("subtext.trophyslots.trophy"));
        }
        if (!ServerConfig.canUseTrophy)
            toolTips.add(new TranslationTextComponent("subtext.trophyslots.trophy.cannot_use"));
        if (fromVillager(stack) && !ServerConfig.canBuyTrophy)
            toolTips.add(new TranslationTextComponent("subtext.trophyslots.trophy.villager"));

        //} else
        //    toolTips.add(new TranslationTextComponent("subtext.trophyslots.info"));
    }

    private int getSlotAmounts(ItemStack stack) {
        int amount = NBTUtils.getInt(stack, SLOT_AMOUNTS);
        if (amount == 0) amount = 1;
        return amount;
    }

    private boolean fromVillager(ItemStack stack) {
        return NBTUtils.getBoolean(stack, VILLAGER_TROPHY);
    }

    private TrophyType getTrophyType() {
        return trophyType;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public Rarity getRarity(ItemStack stack) {
        return getTrophyType() == TrophyType.MASTER ? Rarity.EPIC : Rarity.RARE;
    }

    public enum TrophyType {
        NORMAL("trophy"), MASTER("master_trophy");

        final String name;

        TrophyType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
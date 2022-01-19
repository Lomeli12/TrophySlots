package net.lomeli.trophyslots.items;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.CommonConfig;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;
import net.lomeli.trophyslots.core.criterion.ModCriteria;
import net.lomeli.trophyslots.core.network.MessageSlotClient;
import net.lomeli.trophyslots.core.network.PacketHandler;
import net.lomeli.trophyslots.utils.InventoryUtils;
import net.lomeli.trophyslots.utils.NBTUtils;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemTrophy extends Item {
    public static final String VILLAGER_TROPHY = "fromVillager";
    private static final String SLOT_AMOUNTS = "slotAmounts";

    private final TrophyType trophyType;

    public ItemTrophy(TrophyType trophyType) {
        super(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_MISC));
        this.setRegistryName(TrophySlots.MOD_ID, trophyType.getName());
        this.trophyType = trophyType;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(stack);
        if (!level.isClientSide && !stack.isEmpty()) {
            if (!CommonConfig.canBuyTrophy && fromVillager(stack))
                player.sendMessage(new TranslatableComponent("msg.trophyslots.villager"), Util.NIL_UUID);
            else if (!CommonConfig.canUseTrophy)
                player.sendMessage(new TranslatableComponent("msg.trophyslots.trophy"), Util.NIL_UUID);
            else if (hand == InteractionHand.MAIN_HAND) {
                int amount = trophyType == TrophyType.MASTER ? InventoryUtils.getMaxUnlockableSlots() : getSlotAmounts(stack);
                if (amount != 0) {
                    String msg = amount == InventoryUtils.getMaxUnlockableSlots() ? "msg.trophyslots.unlock_all" :
                            amount == 1 ? "msg.trophyslots.unlock" :
                                    amount > 1 ? "msg.trophyslots.unlock_slot" :
                                            amount == -1 ? "msg.trophyslots.lost_slot" : "msg.trophyslots.lost_slot.multiple";
                    IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
                    if (playerSlots != null && playerSlots.unlockSlot(amount)) {
                        if (amount == 1 || amount == -1 || amount == InventoryUtils.getMaxUnlockableSlots())
                            player.sendMessage(new TranslatableComponent(msg), Util.NIL_UUID);
                        else
                            player.sendMessage(new TranslatableComponent(msg, amount), Util.NIL_UUID);

                        if (!player.isCreative())
                            stack.shrink(1);
                        if (player instanceof ServerPlayer) {
                            TrophySlots.log.info("Sending slot update packet to player {}.", player.getName().getString());
                            PacketHandler.sendToClient(new MessageSlotClient(playerSlots.getSlotsUnlocked()),
                                    (ServerPlayer) player);
                            ModCriteria.UNLOCK_SLOT.trigger((ServerPlayer) player);
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> toolTips, TooltipFlag flag) {
        //if (ClientUtil.safeKeyDown(ClientUtil.LEFT_SHIFT)) {
        if (((ItemTrophy) stack.getItem()).getTrophyType() == TrophyType.MASTER)
            toolTips.add(new TranslatableComponent("subtext.trophyslots.trophy.cheat"));
        else {
            int amount = getSlotAmounts(stack);
            if (amount > 1)
                toolTips.add(new TranslatableComponent("subtext.trophyslots.trophy.x", amount));
            else if (amount == -1)
                toolTips.add(new TranslatableComponent("subtext.trophyslots.trophy.x.neg.one"));
            else if (amount < -1)
                toolTips.add(new TranslatableComponent("subtext.trophyslots.trophy.x.neg", amount * -1));
            else
                toolTips.add(new TranslatableComponent("subtext.trophyslots.trophy"));
        }
        if (!CommonConfig.canUseTrophy)
            toolTips.add(new TranslatableComponent("subtext.trophyslots.trophy.cannot_use"));
        if (fromVillager(stack) && !CommonConfig.canBuyTrophy)
            toolTips.add(new TranslatableComponent("subtext.trophyslots.trophy.villager"));

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
package net.lomeli.trophyslots.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lomeli.knit.client.utils.ClientUtil;
import net.lomeli.knit.utils.ItemNBTUtils;
import net.lomeli.knit.utils.network.MessageUtil;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.ModConfig;
import net.lomeli.trophyslots.core.criterion.ModCriteria;
import net.lomeli.trophyslots.core.network.MessageSlotClient;
import net.lomeli.trophyslots.core.slots.ISlotHolder;
import net.lomeli.trophyslots.core.slots.PlayerSlotManager;
import net.lomeli.trophyslots.utils.InventoryUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class Trophy extends Item {
    static final String VILLAGER_TROPHY = "fromVillager";
    private static final String SLOT_AMOUNTS = "slotAmounts";

    private TrophyType trophyType;

    public Trophy(TrophyType trophyType) {
        super(new Settings().stackSize(1).itemGroup(ItemGroup.MISC));
        this.trophyType = trophyType;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ActionResult result = ActionResult.FAIL;
        ItemStack stack = player.getStackInHand(hand);
        if (world != null && !world.isClient && !stack.isEmpty() && player instanceof ISlotHolder) {
            PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
            if (!ModConfig.canBuyTrophy && fromVillager(stack))
                player.addChatMessage(new TranslatableTextComponent("msg.trophyslots.villager"), false);
            else if (!ModConfig.canUseTrophy)
                player.addChatMessage(new TranslatableTextComponent("msg.trophyslots.trophy"), false);
            else if (hand == Hand.MAIN) {
                int amount = trophyType == TrophyType.MASTER ? InventoryUtils.getMaxUnlockableSlots() : getSlotAmounts(stack);
                if (amount != 0) {
                    String msg = amount == InventoryUtils.getMaxUnlockableSlots() ? "msg.trophyslots.unlock_all" :
                            amount == 1 ? "msg.trophyslots.unlock" :
                                    amount > 1 ? "msg.trophyslots.unlock_slot" :
                                            amount == -1 ? "msg.trophyslots.lost_slot" : "msg.trophyslots.lost_slot.multiple";
                    if (slotManager.unlockSlot(amount)) {
                        if (amount == 1 || amount == -1 || amount == InventoryUtils.getMaxUnlockableSlots())
                            player.addChatMessage(new TranslatableTextComponent(msg), false);
                        else
                            player.addChatMessage(new TranslatableTextComponent(msg, amount), false);

                        if (!player.abilities.creativeMode)
                            stack.addAmount(-1);
                        if (player instanceof ServerPlayerEntity) {
                            TrophySlots.log.info("Sending slot update packet to player {}.", player.getName().getText());
                            MessageUtil.sendToClient(new MessageSlotClient(slotManager.getSlotsUnlocked()),
                                    (ServerPlayerEntity) player);
                            ModCriteria.UNLOCK_SLOT.trigger((ServerPlayerEntity) player);
                        }
                    }
                }
            }
        }
        return new TypedActionResult<>(result, stack);
    }

    private int getSlotAmounts(ItemStack stack) {
        int amount = ItemNBTUtils.getInt(stack, SLOT_AMOUNTS);
        if (amount == 0) amount = 1;
        return amount;
    }

    private boolean fromVillager(ItemStack stack) {
        return ItemNBTUtils.getBoolean(stack, VILLAGER_TROPHY);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void buildTooltip(ItemStack stack, World world, List<TextComponent> toolTipList, TooltipContext tooltipOptions) {
        if (!(stack.getItem() instanceof Trophy) || toolTipList == null) return;
        if (ClientUtil.safeKeyDown(ClientUtil.LEFT_SHIFT)) {
            if (((Trophy) stack.getItem()).getTrophyType() == TrophyType.MASTER)
                toolTipList.add(new TranslatableTextComponent("subtext.trophyslots.trophy.cheat"));
            else {
                int amount = getSlotAmounts(stack);
                if (amount > 1)
                    toolTipList.add(new TranslatableTextComponent("subtext.trophyslots.trophy.x", amount));
                else if (amount == -1)
                    toolTipList.add(new TranslatableTextComponent("subtext.trophyslots.trophy.x.neg.one"));
                else if (amount < -1)
                    toolTipList.add(new TranslatableTextComponent("subtext.trophyslots.trophy.x.neg", amount * -1));
                else
                    toolTipList.add(new TranslatableTextComponent("subtext.trophyslots.trophy"));
            }
            if (!ModConfig.canUseTrophy)
                toolTipList.add(new TranslatableTextComponent("subtext.trophyslots.trophy.cannot_use"));
            if (fromVillager(stack) && !ModConfig.canBuyTrophy)
                toolTipList.add(new TranslatableTextComponent("subtext.trophyslots.trophy.villager"));

        } else
            toolTipList.add(new TranslatableTextComponent("subtext.trophyslots.info"));
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return getTrophyType() == TrophyType.MASTER ? Rarity.RARE : Rarity.UNCOMMON;
    }

    private TrophyType getTrophyType() {
        return trophyType;
    }

    public enum TrophyType {
        NORMAL, MASTER
    }
}

package net.lomeli.trophyslots.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.client.ClientUtil;
import net.lomeli.trophyslots.core.config.ModConfig;
import net.lomeli.trophyslots.core.criterion.ModCriterions;
import net.lomeli.trophyslots.core.slots.ISlotHolder;
import net.lomeli.trophyslots.core.slots.PlayerSlotManager;
import net.minecraft.client.item.TooltipOptions;
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

import javax.annotation.Nullable;
import java.util.List;

public class Trophy extends Item {
    private static final String VILLAGER_TROPHY = "fromVillager";
    private static final String SLOT_AMOUNTS = "slotAmounts";

    private TrophyType trophyType;

    public Trophy(TrophyType trophyType) {
        super(new Settings().stackSize(1).itemGroup(ItemGroup.MISC));
        this.trophyType = trophyType;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ActionResult result = ActionResult.FAILURE;
        ItemStack stack = player.getStackInHand(hand);
        if (world != null && !world.isClient && !stack.isEmpty() && player instanceof ISlotHolder) {
            PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
            if (!ModConfig.canBuyTrophy && fromVillager(stack))
                player.addChatMessage(new TranslatableTextComponent("msg.trophyslots.villager"), false);
            else if (!ModConfig.canUseTrophy)
                player.addChatMessage(new TranslatableTextComponent("msg.trophyslots.trophy"), false);
            else if (hand == Hand.MAIN) {
                int amount = trophyType == TrophyType.MASTER ? slotManager.getMaxSlots() : getSlotAmounts(stack);
                if (amount != 0) {
                    String msg = amount == slotManager.getMaxSlots() ? "msg.trophyslots.unlock_all" :
                            amount == 1 ? "msg.trophyslots.unlock" :
                                    amount > 1 ? "msg.trophyslots.unlock_slot" : "msg.trophyslots.lost_slot";
                    if (slotManager.unlockSlot(amount)) {
                        if (amount == 1 || amount == slotManager.getMaxSlots())
                            player.addChatMessage(new TranslatableTextComponent(msg), false);
                        else
                            player.addChatMessage(new TranslatableTextComponent(msg, amount), false);

                        //TODO: Send client update packet
                        if (!player.abilities.creativeMode)
                            stack.addAmount(-1);
                        if (player instanceof ServerPlayerEntity)
                            ModCriterions.UNLOCK_SLOT.trigger((ServerPlayerEntity) player);
                    }
                }
            }
        }
        return new TypedActionResult<>(result, stack);
    }

    private int getSlotAmounts(ItemStack stack) {
        int amount = 1;
        try {
            if (!stack.isEmpty() && stack.hasTag())
                amount = stack.getTag().getInt(SLOT_AMOUNTS);
        } catch (NullPointerException ex) {
            TrophySlots.log.error("How the hell did this happen?", ex);
        }
        return amount;
    }

    private boolean fromVillager(ItemStack stack) {
        boolean flag = false;
        try {
            flag = !stack.isEmpty() && stack.hasTag() && stack.getTag().getBoolean(VILLAGER_TROPHY);
        } catch (NullPointerException ex) {
            TrophySlots.log.exception("How the hell did this happen?", ex);
        }
        return flag;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void buildTooltip(ItemStack stack, @Nullable World world, List<TextComponent> toolTipList, TooltipOptions tooltipOptions) {
        if (!(stack.getItem() instanceof Trophy) || toolTipList == null) return;
        if (ClientUtil.safeKeyDown(ClientUtil.LEFT_SHIFT)) {
            if (((Trophy) stack.getItem()).getTrophyType() == TrophyType.MASTER)
                toolTipList.add(new TranslatableTextComponent("subtext.torphyslots.trophy.cheat"));
            else {
                int amount = getSlotAmounts(stack);
                if (amount > 1)
                    toolTipList.add(new TranslatableTextComponent("subtext.trophyslots.trophy.x"));
                else
                    toolTipList.add(new TranslatableTextComponent("subtext.trophyslots.trophy"));
            }
            if (fromVillager(stack) && !ModConfig.canBuyTrophy)
                toolTipList.add(new TranslatableTextComponent("subtext.torphyslots.trophy.villager"));
            toolTipList.add(new TranslatableTextComponent(ModConfig.canUseTrophy ? "subtext.trophyslots.trophy.can_use" :
                    "subtext.trophyslots.trophy.cannot_use"));
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

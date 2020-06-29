package net.lomeli.trophyslots.core.criterion;

import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

import com.google.gson.JsonObject;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;

public class UnlockSlotTrigger extends AbstractCriterionTrigger<UnlockSlotTrigger.Instance> {
    private static final String CRITERION_ID = "unlock_slot";

    @Override
    @SuppressWarnings("NullableProblems")
    protected Instance func_230241_b_(JsonObject jsonObject, EntityPredicate.AndPredicate predicate, ConditionArrayParser arrayParser) {
        return new Instance(jsonObject.get("slots_unlocked").getAsInt(), predicate);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public ResourceLocation getId() {
        return new ResourceLocation(TrophySlots.MOD_ID, CRITERION_ID);
    }

    public void trigger(ServerPlayerEntity player) {
        // Test trigger
        this.func_235959_a_(player, (instance) -> instance.test(player));
    }

    static class Instance extends CriterionInstance {
        private final int minSlot;

        Instance(int minSlot, EntityPredicate.AndPredicate predicate) {
            super(new ResourceLocation(TrophySlots.MOD_ID, CRITERION_ID), predicate);
            this.minSlot = minSlot;
        }

        boolean test(ServerPlayerEntity player) {
            IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
            if (playerSlots != null)
                return minSlot == -1 ? playerSlots.maxSlotsUnlocked() : playerSlots.getSlotsUnlocked() >= minSlot;
            return false;
        }
    }
}

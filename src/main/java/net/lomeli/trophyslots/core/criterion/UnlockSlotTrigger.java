package net.lomeli.trophyslots.core.criterion;

import com.google.gson.JsonObject;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class UnlockSlotTrigger extends SimpleCriterionTrigger<UnlockSlotTrigger.Instance> {
    private static final String CRITERION_ID = "unlock_slot";
    private static final String COUNT_JSON_KEY = "slots_unlocked";

    @Override
    @SuppressWarnings("NullableProblems")
    protected Instance createInstance(JsonObject json, EntityPredicate.Composite comp, DeserializationContext context) {
        return new Instance(getId(), comp, json.get(COUNT_JSON_KEY).getAsInt());
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public ResourceLocation getId() {
        return new ResourceLocation(TrophySlots.MOD_ID, CRITERION_ID);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, (instance -> instance.test(player)));
    }

    static class Instance extends AbstractCriterionTriggerInstance {
        private final int minSlot;

        Instance(ResourceLocation id, EntityPredicate.Composite comp, int minSlot) {
            super(id, comp);
            this.minSlot = minSlot;
        }

        boolean test(ServerPlayer player) {
            IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
            if (playerSlots != null)
                return minSlot == -1 ? playerSlots.maxSlotsUnlocked() : playerSlots.getSlotsUnlocked() >= minSlot;
            return false;
        }
    }
}

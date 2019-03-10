package net.lomeli.trophyslots.core.criterion;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.slots.ISlotHolder;
import net.lomeli.trophyslots.core.slots.PlayerSlotManager;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class UnlockSlotTrigger implements Criterion<UnlockSlotTrigger.Instance> {
    private static final String CRITERION_ID = "unlock_slot";

    private Map<PlayerAdvancementTracker, Listeners> listeners = Maps.newHashMap();

    @Override
    public Identifier getId() {
        return new Identifier(TrophySlots.MOD_ID, CRITERION_ID);
    }

    @Override
    public void beginTrackingCondition(PlayerAdvancementTracker advTracker, ConditionsContainer<Instance> listener) {
        Listeners criterion = listeners.getOrDefault(advTracker, null);

        if (criterion == null) {
            criterion = new Listeners(advTracker);
            listeners.put(advTracker, criterion);
        }

        criterion.add(listener);
    }

    @Override
    public void endTrackingCondition(PlayerAdvancementTracker advTracker, ConditionsContainer<Instance> listener) {
        Listeners criterion = listeners.getOrDefault(advTracker, null);
        if (criterion != null) {
            criterion.remove(listener);
            if (criterion.isEmpty())
                listeners.remove(advTracker);
        }
    }

    @Override
    public void endTracking(PlayerAdvancementTracker advTracker) {
        listeners.remove(advTracker);
    }

    @Override
    public Instance deserializeConditions(JsonObject json, JsonDeserializationContext context) {
        return new Instance(json.get("slots_unlocked").getAsInt());
    }

    public void trigger(ServerPlayerEntity player) {
        if (listeners.containsKey(player.getAdvancementManager()))
            listeners.get(player.getAdvancementManager()).trigger(player);
    }

    static class Instance extends AbstractCriterionConditions {
        private final int minSlot;

        Instance(int minSlot) {
            super(new Identifier(TrophySlots.MOD_ID, CRITERION_ID));
            this.minSlot = minSlot;
        }

        boolean test(ServerPlayerEntity player) {
            if (player instanceof ISlotHolder) {
                PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
                return minSlot == -1 ? slotManager.maxSlotsUnlocked() : slotManager.getSlotsUnlocked() >= minSlot;
            }
            return false;
        }

    }

    private static class Listeners {
        private Set<ConditionsContainer<Instance>> listeners = Sets.newHashSet();
        private PlayerAdvancementTracker manager;

        Listeners(PlayerAdvancementTracker manager) {
            this.manager = manager;
        }

        boolean isEmpty() {
            return listeners.isEmpty();
        }

        void add(ConditionsContainer<Instance> listener) {
            listeners.add(listener);
        }

        void remove(ConditionsContainer<Instance> listener) {
            listeners.remove(listener);
        }

        void trigger(ServerPlayerEntity player) {
            List<ConditionsContainer<Instance>> list = null;
            for (ConditionsContainer<Instance> listener : listeners) {
                if (listener.getConditions().test(player)) {
                    if (list == null) list = Lists.newArrayList();
                    list.add(listener);
                }
            }

            if (list != null) {
                for (ConditionsContainer<Instance> listener : list) {
                    listener.apply(manager);
                }
            }
        }
    }
}

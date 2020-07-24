package net.lomeli.trophyslots.core.criterion;

import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;

public class UnlockSlotTrigger implements ICriterionTrigger<UnlockSlotTrigger.Instance> {
    private static final String CRITERION_ID = "unlock_slot";

    private final Map<PlayerAdvancements, Listeners> listenersMap = Maps.newHashMap();

    @Override
    @SuppressWarnings("NullableProblems")
    public ResourceLocation getId() {
        return new ResourceLocation(TrophySlots.MOD_ID, CRITERION_ID);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void addListener(PlayerAdvancements playerAdvancements, Listener<Instance> listener) {
        Listeners criterion = this.listenersMap.getOrDefault(playerAdvancements, null);

        if (criterion == null) {
            criterion = new Listeners(playerAdvancements);
            listenersMap.put(playerAdvancements, criterion);
        }
        criterion.add(listener);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void removeListener(PlayerAdvancements playerAdvancements, Listener<Instance> listener) {
        Listeners criterion = listenersMap.getOrDefault(playerAdvancements, null);
        if (criterion != null) {
            criterion.remove(listener);
            if (criterion.isEmpty())
                listenersMap.remove(playerAdvancements);
        }
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void removeAllListeners(PlayerAdvancements playerAdvancements) {
        listenersMap.remove(playerAdvancements);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public Instance deserializeInstance(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
        return new Instance(jsonObject.get("slots_unlocked").getAsInt());
    }

    public void trigger(ServerPlayerEntity player) {
        if (listenersMap.containsKey(player.getAdvancements()))
            listenersMap.get(player.getAdvancements()).trigger(player);
    }

    static class Instance extends CriterionInstance {
        private final int minSlot;

        Instance(int minSlot) {
            super(new ResourceLocation(TrophySlots.MOD_ID, CRITERION_ID));
            this.minSlot = minSlot;
        }

        boolean test(ServerPlayerEntity player) {
            IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
            if (playerSlots != null)
                return minSlot == -1 ? playerSlots.maxSlotsUnlocked() : playerSlots.getSlotsUnlocked() >= minSlot;
            return false;
        }

    }

    private static class Listeners {
        private final Set<Listener<Instance>> listeners = Sets.newHashSet();
        private final PlayerAdvancements manager;

        Listeners(PlayerAdvancements manager) {
            this.manager = manager;
        }

        boolean isEmpty() {
            return listeners.isEmpty();
        }

        void add(Listener<Instance> listener) {
            listeners.add(listener);
        }

        void remove(Listener<Instance> listener) {
            listeners.remove(listener);
        }

        void trigger(ServerPlayerEntity player) {
            List<Listener<Instance>> list = null;
            for (Listener<Instance> listener : listeners) {
                if (listener.getCriterionInstance().test(player)) {
                    if (list == null) list = Lists.newArrayList();
                    list.add(listener);
                }
            }

            if (list != null) {
                for (Listener<Instance> listener : list) {
                    listener.grantCriterion(manager);
                }
            }
        }
    }
}

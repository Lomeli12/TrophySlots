package net.lomeli.trophyslots.core.network;

import net.fabricmc.fabric.api.network.PacketContext;
import net.lomeli.knit.network.AbstractMessage;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.ModConfig;
import net.lomeli.trophyslots.core.slots.ISlotHolder;
import net.lomeli.trophyslots.core.slots.PlayerSlotManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class MessageSlotClient extends AbstractMessage<MessageSlotClient> {
    private static final Identifier SLOT_CLIENT = new Identifier(TrophySlots.MOD_ID, "slots_client");

    private int slots;

    public MessageSlotClient() {
    }

    public MessageSlotClient(int numSlots) {
        this.slots = numSlots;
    }

    @Override
    public MessageSlotClient createMessage() {
        return new MessageSlotClient();
    }

    @Override
    public void toBytes(PacketByteBuf byteBuf) {
        byteBuf.writeInt(slots);
    }

    @Override
    public void fromBytes(PacketByteBuf byteBuf) {
        slots = byteBuf.readInt();
    }

    @Override
    public Identifier getMessageID() {
        return SLOT_CLIENT;
    }

    @Override
    public void handle(PacketContext context, MessageSlotClient message) {
        if (message == null)
            return;
        if (context.getPlayer() instanceof ISlotHolder) {
            PlayerSlotManager slotManager = ((ISlotHolder) context.getPlayer()).getSlotManager();
            slotManager.setSlotsUnlocked(message.slots);
        }
    }
}

package net.lomeli.trophyslots.core.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.networking.PacketContext;
import net.fabricmc.loader.api.FabricLoader;
import net.lomeli.knit.Knit;
import net.lomeli.knit.network.AbstractMessage;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.slots.ISlotHolder;
import net.lomeli.trophyslots.core.slots.PlayerSlotManager;
import net.lomeli.trophyslots.items.Trophy;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class MessageSlotClient extends AbstractMessage<MessageSlotClient> {
    private static final Identifier SLOT_CLIENT = new Identifier(TrophySlots.MOD_ID, "slots_client");

    private int slots;
    private boolean reverse;

    public MessageSlotClient() {
    }

    public MessageSlotClient(int numSlots, boolean reverse) {
        this.slots = numSlots;
        this.reverse = reverse;
    }

    @Override
    public MessageSlotClient createMessage() {
        return new MessageSlotClient();
    }

    @Override
    public void toBytes(PacketByteBuf byteBuf) {
        byteBuf.writeInt(slots);
        byteBuf.writeBoolean(reverse);
    }

    @Override
    public void fromBytes(PacketByteBuf byteBuf) {
        slots = byteBuf.readInt();
        reverse = byteBuf.readBoolean();
    }

    @Override
    public Identifier getMessageID() {
        return SLOT_CLIENT;
    }

    @Override
    public void handle(PacketContext context, MessageSlotClient message) {
        Knit.log.info("Packet receiving side: %s", context.getPacketEnvironment());
        if (message != null) {
            if (context.getPlayer() instanceof ISlotHolder) {
                PlayerSlotManager slotManager = ((ISlotHolder) context.getPlayer()).getSlotManager();
                slotManager.setSlotsUnlockedClient(message.slots);
            }
        }
    }

    @Override
    public EnvType getTargetSide() {
        return EnvType.CLIENT;
    }
}

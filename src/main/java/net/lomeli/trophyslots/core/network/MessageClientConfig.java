package net.lomeli.trophyslots.core.network;

import net.fabricmc.fabric.api.network.PacketContext;
import net.lomeli.knit.utils.network.AbstractMessage;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.client.ClientConfig;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class MessageClientConfig extends AbstractMessage<MessageClientConfig> {
    private static final Identifier UPDATE_CLIENT_CONFIG = new Identifier(TrophySlots.MOD_ID, "update_client_config");

    private int renderType;
    private boolean sendRenderType;
    private boolean special;
    private boolean sendSpecial;

    public MessageClientConfig(int renderType, boolean sendRenderType, boolean special, boolean sendSpecial) {
        this.renderType = renderType;
        this.sendRenderType = sendRenderType;
        this.special = special;
        this.sendSpecial = sendSpecial;
    }

    public MessageClientConfig() {
    }

    @Override
    public MessageClientConfig createMessage() {
        return new MessageClientConfig();
    }

    @Override
    public void toBytes(PacketByteBuf byteBuf) {
        byteBuf.writeInt(renderType);
        byteBuf.writeBoolean(sendRenderType);
        byteBuf.writeBoolean(special);
        byteBuf.writeBoolean(sendSpecial);
    }

    @Override
    public void fromBytes(PacketByteBuf byteBuf) {
        renderType = byteBuf.readInt();
        sendRenderType = byteBuf.readBoolean();
        special = byteBuf.readBoolean();
        sendSpecial = byteBuf.readBoolean();
    }

    @Override
    public Identifier getMessageID() {
        return UPDATE_CLIENT_CONFIG;
    }

    @Override
    public void handle(PacketContext context, MessageClientConfig message) {
        if (message == null)
            return;
        if (message.sendRenderType)
            ClientConfig.slotRenderType = message.renderType;
        if (message.sendSpecial)
            ClientConfig.special = message.special;
    }
}

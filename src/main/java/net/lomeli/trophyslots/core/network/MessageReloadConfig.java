package net.lomeli.trophyslots.core.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.network.PacketContext;
import net.lomeli.knit.network.AbstractMessage;
import net.lomeli.trophyslots.TrophySlots;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class MessageReloadConfig extends AbstractMessage<MessageReloadConfig> {
    private static final Identifier RELOAD_CONFIG = new Identifier(TrophySlots.MOD_ID, "reload_config");

    public MessageReloadConfig() {
    }

    @Override
    public MessageReloadConfig createMessage() {
        return new MessageReloadConfig();
    }

    @Override
    public void toBytes(PacketByteBuf byteBuf) {
    }

    @Override
    public void fromBytes(PacketByteBuf byteBuf) {
    }

    @Override
    public Identifier getMessageID() {
        return RELOAD_CONFIG;
    }

    @Override
    public void handle(PacketContext context, MessageReloadConfig message) {
        TrophySlots.config.loadConfig();
    }

    @Override
    public EnvType getTargetSide() {
        return EnvType.CLIENT;
    }
}

package net.lomeli.trophyslots.core.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.SlotUtil;
import net.lomeli.trophyslots.core.network.MessageSlotsClient;

public class CommandRemoveAll extends CommandBase {
    @Override
    public String getCommandName() {
        return "remove-all";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "command.trophyslots.remove-all.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 1 || args.length == 2) {
            EntityPlayerMP player = args.length == 2 ? getPlayer(sender, args[1]) : getCommandSenderAsPlayer(sender);
            if (player != null) {
                if (SlotUtil.getSlotsUnlocked(player) == 0)
                    sender.addChatMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("command.trophyslots.remove-all.error"), player.getDisplayName())));
                else {
                    SlotUtil.setSlotsUnlocked(player, 0);
                    TrophySlots.packetHandler.sendTo(new MessageSlotsClient(0), player);
                    player.addChatMessage(new ChatComponentTranslation("msg.trophyslots.lostAll"));
                    sender.addChatMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("command.trophyslots.remove-all.success"), player.getDisplayName())));
                }
            }
        } else
            sender.addChatMessage(new ChatComponentTranslation(getCommandUsage(sender)));
    }
}

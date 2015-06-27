package net.lomeli.trophyslots.core.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;

import net.lomeli.trophyslots.core.SlotUtil;

public class CommandGetSlots extends CommandBase {
    @Override
    public String getCommandName() {
        return "get-slots";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "command.trophyslots.get-slots.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 1 || args.length == 2) {
            EntityPlayerMP player = args.length == 2 ? getPlayer(sender, args[1]) : getCommandSenderAsPlayer(sender);
            if (player != null) {
                int slots = SlotUtil.getSlotsUnlocked(player);
                sender.addChatMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("command.trophyslots.get-slots.success"), player.getDisplayName(), slots)));
            }
        } else
            sender.addChatMessage(new ChatComponentTranslation(getCommandUsage(sender)));
    }
}

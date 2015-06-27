package net.lomeli.trophyslots.core.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.SlotUtil;

public class CommandUnlockAll extends CommandBase {
    @Override
    public String getCommandName() {
        return "unlock-all";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "command.trophyslots.unlock-all.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 1 || args.length == 2) {
            EntityPlayerMP player = args.length == 2 ? getPlayer(sender, args[1]) : getCommandSenderAsPlayer(sender);
            if (player != null) {
                if (SlotUtil.hasUnlockedAllSlots(player))
                    sender.addChatMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("command.trophyslots.unlock-all.error"), player.getDisplayName())));
                else {
                    TrophySlots.proxy.unlockAllSlots(player);
                    sender.addChatMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("command.trophyslots.unlock-all.success"), player.getDisplayName())));
                }
            }
        } else
            sender.addChatMessage(new ChatComponentTranslation(getCommandUsage(sender)));
    }
}

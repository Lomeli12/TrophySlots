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

public class CommandUnlockSlots extends CommandBase {
    @Override
    public String getCommandName() {
        return "unlock-slots";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "command.trophyslots.unlock-slots.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 2 || args.length == 3) {
            EntityPlayerMP player = args.length == 2 ? getCommandSenderAsPlayer(sender) : getPlayer(sender, args[1]);
            int newSlots = args.length == 2 ? parseString(args[1]) : parseString(args[2]);
            if (player != null && (newSlots > 0 && newSlots <= SlotUtil.getMaxSlots())) {
                if (SlotUtil.hasUnlockedAllSlots(player))
                    sender.addChatMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("command.trophyslots.unlock-slots.error.all"), player.getDisplayName())));
                else {
                    int slots = SlotUtil.getSlotsUnlocked(player);
                    slots += newSlots;
                    if (slots >= SlotUtil.getMaxSlots())
                        TrophySlots.proxy.unlockAllSlots(player);
                    else {
                        SlotUtil.setSlotsUnlocked(player, slots);
                        TrophySlots.packetHandler.sendTo(new MessageSlotsClient(slots), player);
                        player.addChatMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("msg.trophyslots.unlockSlot"), newSlots)));
                    }
                    sender.addChatMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("command.trophyslots.unlock-slots.success"), newSlots, player.getDisplayName())));
                }
            } else
                sender.addChatMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("command.trophyslots.unlock-slots.error"), SlotUtil.getMaxSlots())));
        } else
            sender.addChatMessage(new ChatComponentTranslation(getCommandUsage(sender)));
    }

    private int parseString(String st) {
        try {
            return Integer.parseInt(st);
        } catch (Exception e) {
            return -1;
        }
    }
}

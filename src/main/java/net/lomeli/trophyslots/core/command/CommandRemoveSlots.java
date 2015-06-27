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

public class CommandRemoveSlots extends CommandBase {
    @Override
    public String getCommandName() {
        return "remove-slots";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "command.trophyslots.remove-slots.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 2 || args.length == 3) {
            EntityPlayerMP player = args.length == 2 ? getCommandSenderAsPlayer(sender) : getPlayer(sender, args[1]);
            int rmSlots = args.length == 2 ? parseString(args[1]) : parseString(args[2]);
            if (player != null && (rmSlots > 0 && rmSlots <= SlotUtil.getMaxSlots())) {
                int slots = SlotUtil.getSlotsUnlocked(player);
                if (slots <= 0)
                    sender.addChatMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("command.trophyslots.remove-slots.error.none"), player.getDisplayName())));
                else {
                    slots -= rmSlots;
                    if (slots < 0)
                        slots = 0;
                    SlotUtil.setSlotsUnlocked(player, slots);
                    TrophySlots.packetHandler.sendTo(new MessageSlotsClient(slots), player);
                    player.addChatMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("msg.trophyslots." + (slots == 0 ? "lostAll" : "lostSlot")), rmSlots)));
                    sender.addChatMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("command.trophyslots.remove-slots.success"), rmSlots, player.getDisplayName())));
                }
            } else
                sender.addChatMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("command.trophyslots.remove-slots.error"), SlotUtil.getMaxSlots())));
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

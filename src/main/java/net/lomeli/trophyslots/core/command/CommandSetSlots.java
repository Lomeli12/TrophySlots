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

public class CommandSetSlots extends CommandBase {
    @Override
    public String getCommandName() {
        return "set-slots";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "command.trophyslots.set-slots.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 2 || args.length == 3) {
            EntityPlayerMP player = args.length == 2 ? getCommandSenderAsPlayer(sender) : getPlayer(sender, args[1]);
            int slots = args.length == 2 ? parseString(args[1]) : parseString(args[2]);
            if (player != null && (slots >= 0 && slots <= SlotUtil.getMaxSlots())) {
                SlotUtil.setSlotsUnlocked(player, slots);
                TrophySlots.packetHandler.sendTo(new MessageSlotsClient(slots), player);
                sender.addChatMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("command.trophyslots.set-slots.success"), player.getDisplayName(), slots)));
                if (!player.func_147099_x().hasAchievementUnlocked(TrophySlots.firstSlot) && player.func_147099_x().canUnlockAchievement(TrophySlots.firstSlot))
                    player.addStat(TrophySlots.firstSlot, 1);
                if (slots >= SlotUtil.getMaxSlots() && !player.func_147099_x().hasAchievementUnlocked(TrophySlots.maxCapcity) && player.func_147099_x().canUnlockAchievement(TrophySlots.maxCapcity))
                    player.addStat(TrophySlots.maxCapcity, 1);
            } else
                sender.addChatMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("command.trophyslots.set-slots.error"), SlotUtil.getMaxSlots())));
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

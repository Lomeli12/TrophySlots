package net.lomeli.trophyslots.core.command

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.core.SlotUtil
import net.lomeli.trophyslots.core.network.MessageSlotsClient
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommand
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatComponentTranslation
import net.minecraft.util.StatCollector

public class CommandSetSlots : CommandBase() {
    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if (sender != null) {
            if (args != null && (args.size() == 2 || args.size() == 3)) {
                val player: EntityPlayerMP
                val slots: Int
                if (args.size() == 3) {
                    slots = parseString(args[2])
                    player = CommandBase.getPlayer(sender, args[1])
                } else {
                    slots = parseString(args[1])
                    player = CommandBase.getCommandSenderAsPlayer(sender)
                }
                if (player != null && (slots > 0 && slots <= SlotUtil.getMaxSlots())) {
                    SlotUtil.setSlotsUnlocked(player, slots);
                    TrophySlots.packetHandler.sendTo(MessageSlotsClient(slots), player);
                    sender.addChatMessage(ChatComponentText(StatCollector.translateToLocal("command.trophyslots.set-slots.success").format(player.displayName.unformattedText, slots)));
                    if (!player.statFile.hasAchievementUnlocked(TrophySlots.firstSlot) && player.statFile.canUnlockAchievement(TrophySlots.firstSlot))
                        player.addStat(TrophySlots.firstSlot, 1);
                    if (slots >= SlotUtil.getMaxSlots() && !player.statFile.hasAchievementUnlocked(TrophySlots.maxCapcity) && player.statFile.canUnlockAchievement(TrophySlots.maxCapcity))
                        player.addStat(TrophySlots.maxCapcity, 1);
                } else
                    sender.addChatMessage(ChatComponentText(StatCollector.translateToLocal("command.trophyslots.set-slots.error").format(SlotUtil.getMaxSlots())))
            } else
                sender.addChatMessage(ChatComponentTranslation(getCommandUsage(sender)));
        }
    }

    override fun getCommandName(): String? = "set-slots"

    override fun getCommandUsage(sender: ICommandSender?): String? = "command.trophyslots.set-slots.usage"

    override fun getRequiredPermissionLevel(): Int = 2

    override fun compareTo(command: ICommand?): Int = this.commandName!!.compareTo(command!!.commandName)

    private fun parseString(st: String): Int {
        try {
            return Integer.parseInt(st)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return -1
        }
    }
}
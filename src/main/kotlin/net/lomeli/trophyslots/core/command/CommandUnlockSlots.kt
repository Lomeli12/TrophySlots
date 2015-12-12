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

public class CommandUnlockSlots : CommandBase() {
    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if (sender != null) {
            if (args != null && (args.size() == 2 || args.size() == 3)) {
                val player: EntityPlayerMP
                val newSlots: Int
                if (args.size() == 3) {
                    newSlots = parseString(args.get(2))
                    player = CommandBase.getPlayer(sender, args.get(1))
                } else {
                    newSlots = parseString(args.get(1))
                    player = CommandBase.getCommandSenderAsPlayer(sender)
                }
                if (player != null && (newSlots > 0 && newSlots <= SlotUtil.getMaxSlots())) {
                    if (SlotUtil.hasUnlockedAllSlots(player))
                        sender.addChatMessage(ChatComponentText(StatCollector.translateToLocal("command.trophyslots.unlock-slots.error.all").format(player.displayName.unformattedText)))
                    else {
                        val slots = SlotUtil.getSlotsUnlocked(player) + newSlots
                        if (slots >= SlotUtil.getMaxSlots())
                            TrophySlots.proxy.unlockAllSlots(player)
                        else {
                            SlotUtil.setSlotsUnlocked(player, slots)
                            TrophySlots.packetHandler.sendTo(MessageSlotsClient(slots), player)
                            player.addChatMessage(ChatComponentText(StatCollector.translateToLocal("msg.trophyslots.unlockSlot").format(newSlots)))
                        }
                        sender.addChatMessage(ChatComponentText(StatCollector.translateToLocal("command.trophyslots.unlock-slots.success").format(newSlots, player.displayName.unformattedText)))
                    }
                } else
                    sender.addChatMessage(ChatComponentText(StatCollector.translateToLocal("command.trophyslots.unlock-slots.error").format(SlotUtil.getMaxSlots())))
            } else
                sender.addChatMessage(ChatComponentTranslation(getCommandUsage(sender)))
        }
    }

    override fun getCommandName(): String? = "unlock-slots"

    override fun getCommandUsage(sender: ICommandSender?): String? = "command.trophyslots.unlock-slots.usage"

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
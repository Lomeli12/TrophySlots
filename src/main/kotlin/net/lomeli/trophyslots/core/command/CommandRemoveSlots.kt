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

public class CommandRemoveSlots : CommandBase() {
    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if (sender != null) {
            if (args != null && (args.size() == 2 || args.size() == 3)) {
                val player: EntityPlayerMP
                val rmSlots: Int
                if (args.size() == 3) {
                    rmSlots = parseString(args.get(2))
                    player = CommandBase.getPlayer(sender, args.get(1))
                } else {
                    rmSlots = parseString(args.get(1))
                    player = CommandBase.getCommandSenderAsPlayer(sender)
                }
                if (player != null && (rmSlots > 0 && rmSlots <= SlotUtil.getMaxSlots())) {
                    var slots = SlotUtil.getSlotsUnlocked(player)
                    if (slots <= 0)
                        sender.addChatMessage(ChatComponentText(StatCollector.translateToLocal("command.trophyslots.remove-slots.error.none").format(player.displayName.unformattedText)))
                    else {
                        slots -= rmSlots
                        if (slots < 0)
                            slots = 0
                        SlotUtil.setSlotsUnlocked(player, slots)
                        TrophySlots.packetHandler.sendTo(MessageSlotsClient(slots), player)
                        if (slots == 0)
                            player.addChatMessage(ChatComponentTranslation("msg.trophyslots.lostAll"))
                        else
                            player.addChatMessage(ChatComponentText(StatCollector.translateToLocal("msg.trophyslots.lostSlot").format(rmSlots)))
                        sender.addChatMessage(ChatComponentText(StatCollector.translateToLocal("command.trophyslots.remove-slots.success").format(rmSlots, player.displayName.unformattedText)))
                    }
                } else
                    sender.addChatMessage(ChatComponentText(StatCollector.translateToLocal("command.trophyslots.remove-slots.error").format(SlotUtil.getMaxSlots())))
            } else
                sender.addChatMessage(ChatComponentTranslation(getCommandUsage(sender)));
        }
    }

    override fun getCommandName(): String? = "remove-slots"

    override fun getCommandUsage(sender: ICommandSender?): String? = "command.trophyslots.remove-slots.usage"

    override fun getRequiredPermissionLevel(): Int = 2

    override fun compareTo(command: ICommand?): Int = this.commandName!!.compareTo(command!!.commandName)

    override fun compareTo(other: Any?): Int = this.compareTo(other as ICommand)

    private fun parseString(st: String): Int {
        try {
            return Integer.parseInt(st)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return -1
        }
    }
}
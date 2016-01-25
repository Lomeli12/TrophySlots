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

public class CommandRemoveAll : CommandBase() {
    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if (sender != null) {
            if (args != null && (args.size == 1 || args.size == 2)) {
                val player: EntityPlayerMP
                if (args.size == 2)
                    player = CommandBase.getPlayer(sender, args[1])
                else
                    player = CommandBase.getCommandSenderAsPlayer(sender)
                if (player != null) {
                    if (SlotUtil.getSlotsUnlocked(player) <= 0)
                        sender.addChatMessage(ChatComponentText(StatCollector.translateToLocal("command.trophyslots.remove-all.error").format(player.displayName.unformattedText)))
                    else {
                        SlotUtil.setSlotsUnlocked(player, 0)
                        TrophySlots.packetHandler.sendTo(MessageSlotsClient(0), player)
                        player.addChatMessage(ChatComponentTranslation("msg.trophyslots.lostAll"))
                        sender.addChatMessage(ChatComponentText(StatCollector.translateToLocal("command.trophyslots.remove-all.success").format(player.displayName.unformattedText)));
                    }
                }
            } else
                sender.addChatMessage(ChatComponentTranslation(getCommandUsage(sender)));
        }
    }

    override fun getCommandName(): String? = "remove-all"

    override fun getCommandUsage(sender: ICommandSender?): String? = "command.trophyslots.remove-all.usage"

    override fun getRequiredPermissionLevel(): Int = 2
}
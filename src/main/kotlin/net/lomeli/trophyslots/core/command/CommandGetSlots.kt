package net.lomeli.trophyslots.core.command

import net.lomeli.trophyslots.core.SlotUtil
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommand
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatComponentTranslation
import net.minecraft.util.StatCollector

public class CommandGetSlots : CommandBase() {
    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if (sender != null) {
            if (args != null && (args.size() == 1 || args.size() == 2)) {
                val player: EntityPlayerMP
                if (args.size() == 2)
                    player = CommandBase.getPlayer(sender, args.get(1))
                else
                    player = CommandBase.getCommandSenderAsPlayer(sender)
                if (player != null) {
                    val slots = SlotUtil.getSlotsUnlocked(player);
                    sender.addChatMessage(ChatComponentText(StatCollector.translateToLocal("command.trophyslots.get-slots.success").format(player.displayName.unformattedText, "$slots")));
                }
            } else
                sender.addChatMessage(ChatComponentTranslation(getCommandUsage(sender)));
        }
    }

    override fun getCommandName(): String? = "get-slots"

    override fun getCommandUsage(sender: ICommandSender?): String? = "command.trophyslots.get-slots.usage"

    override fun getRequiredPermissionLevel(): Int = 0

    override fun compareTo(command: ICommand?): Int = this.commandName!!.compareTo(command!!.commandName)

    override fun compareTo(other: Any?): Int = this.compareTo(other as ICommand)
}
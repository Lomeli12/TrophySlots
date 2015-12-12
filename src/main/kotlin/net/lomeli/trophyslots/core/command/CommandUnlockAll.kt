package net.lomeli.trophyslots.core.command

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.core.SlotUtil
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommand
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatComponentTranslation
import net.minecraft.util.StatCollector

public class CommandUnlockAll : CommandBase() {

    override fun getRequiredPermissionLevel(): Int = 2

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if (sender != null) {
            if (args != null && (args.size() == 1 || args.size() == 2)) {
                val player: EntityPlayerMP
                if (args.size() == 2)
                    player = CommandBase.getPlayer(sender, args.get(1))
                else
                    player = CommandBase.getCommandSenderAsPlayer(sender)
                if (player != null) {
                    if (SlotUtil.hasUnlockedAllSlots(player))
                        sender.addChatMessage(ChatComponentText(StatCollector.translateToLocal("command.trophyslots.unlock-all.error").format(player.displayName.unformattedText)));
                    else {
                        TrophySlots.proxy?.unlockAllSlots(player);
                        sender.addChatMessage(ChatComponentText(StatCollector.translateToLocal("command.trophyslots.unlock-all.success").format(player.displayName.unformattedText)));
                    }
                }
            } else
                sender.addChatMessage(ChatComponentTranslation(getCommandUsage(sender)));
        }
    }

    override fun getCommandUsage(sender: ICommandSender?): String? = "command.trophyslots.unlock-all.usage"

    override fun getCommandName(): String? = "unlock-all"

    override fun compareTo(command: ICommand?): Int = this.commandName!!.compareTo(command!!.commandName)
}
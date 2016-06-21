package net.lomeli.trophyslots.core.command

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.core.SlotUtil
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.translation.I18n

class CommandUnlockAll : CommandBase() {

    override fun getRequiredPermissionLevel(): Int = 2

    override fun execute(server: MinecraftServer?, sender: ICommandSender?, args: Array<out String>?) {
        if (sender != null) {
            if (args != null && (args.size == 1 || args.size == 2)) {
                val player: EntityPlayerMP
                if (args.size == 2)
                    player = CommandBase.getPlayer(server, sender, args[1])
                else
                    player = CommandBase.getCommandSenderAsPlayer(sender)
                if (player != null) {
                    if (SlotUtil.hasUnlockedAllSlots(player))
                        sender.addChatMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.unlock-all.error").format(player.displayName.unformattedText)));
                    else {
                        TrophySlots.proxy?.unlockAllSlots(player);
                        sender.addChatMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.unlock-all.success").format(player.displayName.unformattedText)));
                    }
                }
            } else
                sender.addChatMessage(TextComponentTranslation(getCommandUsage(sender)));
        }
    }

    override fun getCommandUsage(sender: ICommandSender?): String? = "command.trophyslots.unlock-all.usage"

    override fun getCommandName(): String? = "unlock-all"
}
package net.lomeli.trophyslots.core.command

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.core.SlotUtil
import net.lomeli.trophyslots.core.network.MessageSlotsClient
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.translation.I18n

class CommandRemoveAll : CommandBase() {
    override fun execute(server: MinecraftServer?, sender: ICommandSender?, args: Array<out String>?) {
        if (sender != null) {
            if (args != null && (args.size == 1 || args.size == 2)) {
                val player: EntityPlayerMP
                if (args.size == 2)
                    player = CommandBase.getPlayer(server, sender, args[1])
                else
                    player = CommandBase.getCommandSenderAsPlayer(sender)
                if (player != null) {
                    if (SlotUtil.getSlotsUnlocked(player) <= 0)
                        sender.addChatMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.remove-all.error").format(player.displayName.unformattedText)))
                    else {
                        SlotUtil.setSlotsUnlocked(player, 0)
                        TrophySlots.packetHandler.sendTo(MessageSlotsClient(0), player)
                        player.addChatMessage(TextComponentTranslation("msg.trophyslots.lostAll"))
                        sender.addChatMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.remove-all.success").format(player.displayName.unformattedText)));
                    }
                }
            } else
                sender.addChatMessage(TextComponentTranslation(getCommandUsage(sender)));
        }
    }

    override fun getCommandName(): String? = "remove-all"

    override fun getCommandUsage(sender: ICommandSender?): String? = "command.trophyslots.remove-all.usage"

    override fun getRequiredPermissionLevel(): Int = 2
}
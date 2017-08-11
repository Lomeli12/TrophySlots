package net.lomeli.trophyslots.core.command

import net.lomeli.trophyslots.capabilities.slots.SlotManager
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
                    val slotInfo = SlotManager.getPlayerSlotInfo(player)!!
                    if (slotInfo.getSlotsUnlocked() <= 0)
                        sender.sendMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.remove_all.error").format(player.displayName.unformattedText)))
                    else {
                        slotInfo.setSlots(0)
                        SlotManager.updateClient(player, slotInfo)
                        player.sendMessage(TextComponentTranslation("msg.trophyslots.lost_all"))
                        sender.sendMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.remove_all.success").format(player.displayName.unformattedText)))
                    }
                }
            } else
                sender.sendMessage(TextComponentTranslation(getUsage(sender)))
        }
    }

    override fun getName(): String? = "remove-all"

    override fun getUsage(sender: ICommandSender?): String? = "command.trophyslots.remove_all.usage"

    override fun getRequiredPermissionLevel(): Int = 2
}
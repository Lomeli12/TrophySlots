package net.lomeli.trophyslots.core.command

import net.lomeli.trophyslots.capabilities.slots.SlotManager
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.translation.I18n

class CommandGetSlots : CommandBase() {
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
                    val slots = slotInfo.getSlotsUnlocked()
                    sender.sendMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.get_slots.success").format(player.displayName.unformattedText, "$slots")))
                }
            } else
                sender.sendMessage(TextComponentTranslation(getUsage(sender)))
        }
    }

    override fun getName(): String? = "get-slots"

    override fun getUsage(sender: ICommandSender?): String? = "command.trophyslots.get_slots.usage"

    override fun getRequiredPermissionLevel(): Int = 0
}
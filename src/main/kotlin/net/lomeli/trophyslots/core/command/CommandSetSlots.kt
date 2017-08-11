package net.lomeli.trophyslots.core.command

import net.lomeli.trophyslots.capabilities.slots.SlotManager
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.translation.I18n

class CommandSetSlots : CommandBase() {
    override fun execute(server: MinecraftServer?, sender: ICommandSender?, args: Array<out String>?) {
        if (sender != null) {
            if (args != null && (args.size == 2 || args.size == 3)) {
                val player: EntityPlayerMP
                val slots: Int
                if (args.size == 3) {
                    slots = parseInt(args[2])
                    player = CommandBase.getPlayer(server, sender, args[1])
                } else {
                    slots = parseInt(args[1])
                    player = CommandBase.getCommandSenderAsPlayer(sender)
                }
                val slotInfo = SlotManager.getPlayerSlotInfo(player)!!
                if (player != null && (slots > 0 && slots <= slotInfo.getMaxSlots())) {
                    slotInfo.setSlots(slots)
                    SlotManager.updateClient(player, slotInfo)
                    sender.sendMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.set_slots.success").format(player.displayName.unformattedText, slots)))
                } else
                    sender.sendMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.set_slots.error").format(slotInfo.getMaxSlots())))
            } else
                sender.sendMessage(TextComponentTranslation(getUsage(sender)))
        }
    }

    override fun getName(): String? = "set-slots"

    override fun getUsage(sender: ICommandSender?): String? = "command.trophyslots.set_slots.usage"

    override fun getRequiredPermissionLevel(): Int = 2
}
package net.lomeli.trophyslots.core.command

import net.lomeli.trophyslots.capabilities.slots.SlotManager
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.translation.I18n

class CommandUnlockSlots : CommandBase() {
    override fun execute(server: MinecraftServer?, sender: ICommandSender?, args: Array<out String>?) {
        if (sender != null) {
            if (args != null && (args.size == 2 || args.size == 3)) {
                val player: EntityPlayerMP
                val newSlots: Int
                if (args.size == 3) {
                    newSlots = parseInt(args[2])
                    player = CommandBase.getPlayer(server, sender, args[1])
                } else {
                    newSlots = parseInt(args[1])
                    player = CommandBase.getCommandSenderAsPlayer(sender)
                }
                val slotInfo = SlotManager.getPlayerSlotInfo(player)!!
                if (player != null && (newSlots > 0 && newSlots <= slotInfo.getMaxSlots())) {
                    if (slotInfo.isAtMaxSlots())
                        sender.sendMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.unlock_slots.error.all").format(player.displayName.unformattedText)))
                    else {
                        val slots = slotInfo.getSlotsUnlocked() + newSlots
                        if (slots >= slotInfo.getMaxSlots()) {
                            slotInfo.setSlots(slotInfo.getMaxSlots())
                            SlotManager.updateClient(player, slotInfo)
                            player.sendMessage(TextComponentString(I18n.translateToLocal("msg.trophyslots.unlock_all")))
                        } else {
                            slotInfo.setSlots(slots)
                            SlotManager.updateClient(player, slotInfo)
                            player.sendMessage(TextComponentString(I18n.translateToLocal("msg.trophyslots.unlock_slot").format(newSlots)))
                        }
                        sender.sendMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.unlock_slots.success").format(newSlots, player.displayName.unformattedText)))
                    }
                } else
                    sender.sendMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.unlock_slots.error").format(slotInfo.getMaxSlots())))
            } else
                sender.sendMessage(TextComponentTranslation(getUsage(sender)))
        }
    }

    override fun getName(): String? = "unlock-slots"

    override fun getUsage(sender: ICommandSender?): String? = "command.trophyslots.unlock_slots.usage"

    override fun getRequiredPermissionLevel(): Int = 2
}
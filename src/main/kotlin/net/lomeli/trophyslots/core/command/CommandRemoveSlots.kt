package net.lomeli.trophyslots.core.command

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.capabilities.slots.SlotManager
import net.lomeli.trophyslots.core.network.MessageSlotsClient
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.translation.I18n

class CommandRemoveSlots : CommandBase() {
    override fun execute(server: MinecraftServer?, sender: ICommandSender?, args: Array<out String>?) {
        if (sender != null) {
            if (args != null && (args.size == 2 || args.size == 3)) {
                val player: EntityPlayerMP
                val rmSlots: Int
                if (args.size == 3) {
                    rmSlots = parseInt(args[2])
                    player = CommandBase.getPlayer(server, sender, args[1])
                } else {
                    rmSlots = parseInt(args[1])
                    player = CommandBase.getCommandSenderAsPlayer(sender)
                }
                val slotInfo = SlotManager.getPlayerSlotInfo(player)!!
                if (player != null && (rmSlots > 0 && rmSlots <= slotInfo.getMaxSlots())) {
                    var slots = slotInfo.getSlotsUnlocked()
                    if (slots <= 0)
                        sender.sendMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.remove_slots.error.none").format(player.displayName.unformattedText)))
                    else {
                        slots -= rmSlots
                        if (slots < 0)
                            slots = 0
                        slotInfo.setSlots(slots)
                        TrophySlots.packetHandler?.sendTo(MessageSlotsClient(slots), player)
                        if (slots == 0)
                            player.sendMessage(TextComponentTranslation("msg.trophyslots.lost_all"))
                        else
                            player.sendMessage(TextComponentString(I18n.translateToLocal("msg.trophyslots.lost_slot").format(rmSlots)))
                        sender.sendMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.remove_slots.success").format(rmSlots, player.displayName.unformattedText)))
                    }
                } else
                    sender.sendMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.remove_slots.error").format(slotInfo.getMaxSlots())))
            } else
                sender.sendMessage(TextComponentTranslation(getUsage(sender)))
        }
    }

    override fun getName(): String? = "remove-slots"

    override fun getUsage(sender: ICommandSender?): String? = "command.trophyslots.remove_slots.usage"

    override fun getRequiredPermissionLevel(): Int = 2
}
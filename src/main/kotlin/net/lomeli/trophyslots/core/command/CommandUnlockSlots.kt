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
                if (player != null && (newSlots > 0 && newSlots <= SlotUtil.getMaxSlots())) {
                    if (SlotUtil.hasUnlockedAllSlots(player))
                        sender.addChatMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.unlock-slots.error.all").format(player.displayName.unformattedText)))
                    else {
                        val slots = SlotUtil.getSlotsUnlocked(player) + newSlots
                        if (slots >= SlotUtil.getMaxSlots())
                            TrophySlots.proxy?.unlockAllSlots(player)
                        else {
                            SlotUtil.setSlotsUnlocked(player, slots)
                            TrophySlots.packetHandler?.sendTo(MessageSlotsClient(slots), player)
                            player.addChatMessage(TextComponentString(I18n.translateToLocal("msg.trophyslots.unlockSlot").format(newSlots)))
                        }
                        sender.addChatMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.unlock-slots.success").format(newSlots, player.displayName.unformattedText)))
                    }
                } else
                    sender.addChatMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.unlock-slots.error").format(SlotUtil.getMaxSlots())))
            } else
                sender.addChatMessage(TextComponentTranslation(getCommandUsage(sender)))
        }
    }

    override fun getCommandName(): String? = "unlock-slots"

    override fun getCommandUsage(sender: ICommandSender?): String? = "command.trophyslots.unlock-slots.usage"

    override fun getRequiredPermissionLevel(): Int = 2
}
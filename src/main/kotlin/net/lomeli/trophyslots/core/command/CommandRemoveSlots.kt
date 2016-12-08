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
                if (player != null && (rmSlots > 0 && rmSlots <= SlotUtil.getMaxSlots())) {
                    var slots = SlotUtil.getSlotsUnlocked(player)
                    if (slots <= 0)
                        sender.addChatMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.remove-slots.error.none").format(player.displayName.unformattedText)))
                    else {
                        slots -= rmSlots
                        if (slots < 0)
                            slots = 0
                        SlotUtil.setSlotsUnlocked(player, slots)
                        TrophySlots.packetHandler?.sendTo(MessageSlotsClient(slots), player)
                        if (slots == 0)
                            player.addChatMessage(TextComponentTranslation("msg.trophyslots.lostAll"))
                        else
                            player.addChatMessage(TextComponentString(I18n.translateToLocal("msg.trophyslots.lostSlot").format(rmSlots)))
                        sender.addChatMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.remove-slots.success").format(rmSlots, player.displayName.unformattedText)))
                    }
                } else
                    sender.addChatMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.remove-slots.error").format(SlotUtil.getMaxSlots())))
            } else
                sender.addChatMessage(TextComponentTranslation(getCommandUsage(sender)));
        }
    }

    override fun getCommandName(): String? = "remove-slots"

    override fun getCommandUsage(sender: ICommandSender?): String? = "command.trophyslots.remove-slots.usage"

    override fun getRequiredPermissionLevel(): Int = 2
}
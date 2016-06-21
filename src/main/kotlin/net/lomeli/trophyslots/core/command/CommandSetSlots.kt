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
                if (player != null && (slots > 0 && slots <= SlotUtil.getMaxSlots())) {
                    SlotUtil.setSlotsUnlocked(player, slots);
                    TrophySlots.packetHandler.sendTo(MessageSlotsClient(slots), player);
                    sender.addChatMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.set-slots.success").format(player.displayName.unformattedText, slots)));
                    if (!player.statFile.hasAchievementUnlocked(TrophySlots.firstSlot) && player.statFile.canUnlockAchievement(TrophySlots.firstSlot))
                        player.addStat(TrophySlots.firstSlot, 1);
                    if (slots >= SlotUtil.getMaxSlots() && !player.statFile.hasAchievementUnlocked(TrophySlots.maxCapcity) && player.statFile.canUnlockAchievement(TrophySlots.maxCapcity))
                        player.addStat(TrophySlots.maxCapcity, 1);
                } else
                    sender.addChatMessage(TextComponentString(I18n.translateToLocal("command.trophyslots.set-slots.error").format(SlotUtil.getMaxSlots())))
            } else
                sender.addChatMessage(TextComponentTranslation(getCommandUsage(sender)));
        }
    }

    override fun getCommandName(): String? = "set-slots"

    override fun getCommandUsage(sender: ICommandSender?): String? = "command.trophyslots.set-slots.usage"

    override fun getRequiredPermissionLevel(): Int = 2
}
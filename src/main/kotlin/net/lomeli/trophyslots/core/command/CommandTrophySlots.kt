package net.lomeli.trophyslots.core.command

import com.google.common.collect.Lists
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation

class CommandTrophySlots : CommandBase {
    private val modCommands = Lists.newArrayList<CommandBase>()
    private val commands = Lists.newArrayList<String>()

    constructor() {
        modCommands.add(CommandUnlockAll())
        modCommands.add(CommandGetSlots())
        modCommands.add(CommandRemoveAll())
        modCommands.add(CommandRemoveSlots())
        modCommands.add(CommandSetSlots())
        modCommands.add(CommandUnlockSlots())

        var i = 0;
        while (i < modCommands.size) {
            commands.add(modCommands[i].name)
            ++i
        }
    }

    override fun getName(): String? = "tslots"

    override fun getRequiredPermissionLevel(): Int = 0

    override fun getUsage(sender: ICommandSender?): String? = "command.trophyslots.usage"

    override fun execute(server: MinecraftServer?, sender: ICommandSender?, args: Array<out String>?) {
        if (sender != null) {
            if (args != null && args.size >= 1) {
                for (i in modCommands.indices) {
                    val commandBase: CommandBase = modCommands[i];
                    if (commandBase.name.equals(args[0], true) && commandBase.checkPermission(server, sender))
                        commandBase.execute(server, sender, args)
                }
            } else
                sender.sendMessage(TextComponentTranslation("command.trophyslots.usage"))
        }
    }

    override fun getTabCompletions(server: MinecraftServer?, sender: ICommandSender?, args: Array<out String>?, pos: BlockPos?): MutableList<String> {
        if (sender != null && args != null) {
            if (args.size == 1)
                return CommandBase.getListOfStringsMatchingLastWord(args, commands)
            else if (args.size >= 2) {
                for (i in modCommands.indices) {
                    val command: CommandBase = modCommands[i];
                    if (command.name.equals(args[0], true))
                        return command.getTabCompletions(server, sender, args, pos)
                }
            }
        }
        return super.getTabCompletions(server, sender, args, pos)
    }
}
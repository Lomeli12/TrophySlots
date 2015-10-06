package net.lomeli.trophyslots.core.command

import com.google.common.collect.Lists
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.minecraft.util.ChatComponentTranslation
import java.util.*

public class CommandTrophySlots : CommandBase {
    private val modCommands: ArrayList<CommandBase>
    private val commands: ArrayList<String>

    constructor() {
        modCommands = Lists.newArrayList()
        commands = Lists.newArrayList()

        modCommands.add(CommandUnlockAll())
        modCommands.add(CommandGetSlots())

        for (commandBase: CommandBase in modCommands)
            commands.add(commandBase.commandName)
    }

    override fun getCommandName(): String? {
        return "tslots"
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun getCommandUsage(sender: ICommandSender?): String? {
        return "command.trophyslots.usage";
    }

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if (sender != null) {
            if (args != null && args.size() >= 1) {
                for (commandBase: CommandBase in modCommands) {
                    if (commandBase.commandName.equals(args.get(0), true) && commandBase.canCommandSenderUseCommand(sender))
                        commandBase.processCommand(sender, args)
                }
            } else
                sender.addChatMessage(ChatComponentTranslation("command.trophyslots.usage"))
        }
    }

    override fun addTabCompletionOptions(sender: ICommandSender?, args: Array<out String>?, pos: BlockPos?): MutableList<Any?>? {
        if (sender != null && args != null) {
            if (args.size() == 1)
                return CommandBase.getListOfStringsMatchingLastWord(args, commands);
            else if (args.size() >= 2) {
                for (command: CommandBase in modCommands) {
                    if (command.commandName.equals(args[0], true))
                        return command.addTabCompletionOptions(sender, args, pos)
                }
            }
        }
        return null
    }
}
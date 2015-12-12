package net.lomeli.trophyslots.core.command

import com.google.common.collect.Lists
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommand
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
        modCommands.add(CommandRemoveAll())
        modCommands.add(CommandRemoveSlots())
        modCommands.add(CommandSetSlots())
        modCommands.add(CommandUnlockSlots())

        var i = 0;
        while (i < modCommands.size()) {
            commands.add(modCommands[i].commandName)
            ++i
        }
    }

    override fun getCommandName(): String? = "tslots"

    override fun getRequiredPermissionLevel(): Int = 0

    override fun getCommandUsage(sender: ICommandSender?): String? = "command.trophyslots.usage";

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if (sender != null) {
            if (args != null && args.size() >= 1) {
                var i = 0;
                while (i < modCommands.size()) {
                    val commandBase : CommandBase = modCommands[i];
                    if (commandBase.commandName.equals(args.get(0), true) && commandBase.canCommandSenderUseCommand(sender))
                        commandBase.processCommand(sender, args)
                    ++i
                }
            } else
                sender.addChatMessage(ChatComponentTranslation("command.trophyslots.usage"))
        }
    }

    override fun addTabCompletionOptions(sender: ICommandSender?, args: Array<out String>?, pos: BlockPos?): MutableList<String>? {
        if (sender != null && args != null) {
            if (args.size() == 1)
                return CommandBase.getListOfStringsMatchingLastWord(args, commands);
            else if (args.size() >= 2) {
                var i = 0;
                while (i < modCommands.size()) {
                    val command : CommandBase = modCommands[i];
                    if (command.commandName.equals(args[0], true))
                        return command.addTabCompletionOptions(sender, args, pos)
                    ++i
                }
            }
        }
        return null;
    }

    override fun compareTo(command: ICommand?): Int = this.commandName!!.compareTo(command!!.commandName)
}
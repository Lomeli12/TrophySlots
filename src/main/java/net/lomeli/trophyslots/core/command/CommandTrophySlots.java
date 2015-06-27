package net.lomeli.trophyslots.core.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;

public class CommandTrophySlots extends CommandBase {
    private static List<CommandBase> modCommands = new ArrayList<CommandBase>();
    private static List<String> commands = new ArrayList<String>();

    static {
        modCommands.add(new CommandSetSlots());
        modCommands.add(new CommandGetSlots());
        modCommands.add(new CommandUnlockAll());
        modCommands.add(new CommandUnlockSlots());
        modCommands.add(new CommandRemoveAll());
        modCommands.add(new CommandRemoveSlots());

        for (CommandBase commandBase : modCommands) {
            commands.add(commandBase.getCommandName());
        }
    }

    @Override
    public String getCommandName() {
        return "tslots";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "command.trophyslots.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length >= 1) {
            for (CommandBase command : modCommands) {
                if (command.getCommandName().equalsIgnoreCase(args[0]) && command.canCommandSenderUseCommand(sender))
                    command.processCommand(sender, args);
            }
        } else
            sender.addChatMessage(new ChatComponentTranslation("command.trophyslots.usage"));
    }

    @Override
    public List addTabCompletionOptions(ICommandSender commandSender, String[] args) {
        if (args.length == 1)
            return getListOfStringsFromIterableMatchingLastWord(args, commands);
        else if (args.length >= 2) {
            for (CommandBase command : modCommands) {
                if (command.getCommandName().equalsIgnoreCase(args[0]))
                    return command.addTabCompletionOptions(commandSender, args);
            }
        }

        return null;
    }
}

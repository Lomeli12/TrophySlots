package net.lomeli.trophyslots.core.command;

import net.lomeli.knit.command.CommandManager;

public class ModCommands {
    private static final CommandManager MOD_COMMANDS = new CommandManager();

    public static void registerCommands() {
        MOD_COMMANDS.addCommand(new GetSlotsCommand());
        MOD_COMMANDS.addCommand(new SetSlotsCommand());
        MOD_COMMANDS.addCommand(new RemoveSlotsCommand());
        MOD_COMMANDS.addCommand(new UnlockSlotsCommand());
        MOD_COMMANDS.registerCommands();
    }
}

package net.lomeli.trophyslots.core.command;

import net.lomeli.knit.command.CommandManager;
import net.lomeli.knit.command.MasterCommand;
import net.lomeli.trophyslots.TrophySlots;

public class ModCommands {
    private static final CommandManager MOD_COMMANDS = new CommandManager();

    public static void registerCommands() {
        MasterCommand tsCommands = new MasterCommand(TrophySlots.MOD_ID);
        tsCommands.registerCommand(new GetSlotsCommand());
        tsCommands.registerCommand(new SetSlotsCommand());
        tsCommands.registerCommand(new RemoveSlotsCommand());
        tsCommands.registerCommand(new UnlockSlotsCommand());
        MOD_COMMANDS.addCommand(tsCommands);
        MOD_COMMANDS.addCommand(new TSConfigCommand());
        MOD_COMMANDS.registerCommands();
    }
}

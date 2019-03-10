package net.lomeli.trophyslots.client.command;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lomeli.knit.utils.command.CommandManager;

@Environment(EnvType.CLIENT)
public class ClientCommands {
    private static final CommandManager MOD_COMMANDS = new CommandManager();

    public static void registerCommands() {
        MOD_COMMANDS.addCommand(new TSClientConfigCommand());
        MOD_COMMANDS.registerCommands();
    }
}

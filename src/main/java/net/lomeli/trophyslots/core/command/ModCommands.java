package net.lomeli.trophyslots.core.command;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import com.google.common.collect.Lists;

import java.util.List;

public class ModCommands {
    private static final List<ISubCommand> commands = Lists.newArrayList();

    private static void registerCommands() {
        commands.add(new GetSlotsCommand());
        commands.add(new RemoveSlotsCommand());
        commands.add(new SetSlotsCommand());
        commands.add(new UnlockSlotsCommand());
    }

    public static void registerCommands(CommandDispatcher<CommandSource> dispatcher) {
        registerCommands();
        LiteralArgumentBuilder<CommandSource> argumentBuilder = Commands.literal("tslots");

        commands.forEach(sub -> sub.registerSubCommand(
                argumentBuilder.then(Commands.literal(sub.getName()))
        ));

        dispatcher.register(argumentBuilder);
    }
}

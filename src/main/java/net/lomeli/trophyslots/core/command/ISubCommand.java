package net.lomeli.trophyslots.core.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;

public interface ISubCommand {
    void registerSubCommand(LiteralArgumentBuilder<CommandSource> argumentBuilder);

    String getName();
}

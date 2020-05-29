package net.lomeli.trophyslots.core.command;

import net.minecraft.command.CommandSource;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

public interface ISubCommand {
    void registerSubCommand(LiteralArgumentBuilder<CommandSource> argumentBuilder);

    String getName();
}

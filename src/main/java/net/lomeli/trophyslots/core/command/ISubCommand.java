package net.lomeli.trophyslots.core.command;


import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public interface ISubCommand {
    void registerSubCommand(LiteralArgumentBuilder<CommandSourceStack> argumentBuilder);

    String getName();
}

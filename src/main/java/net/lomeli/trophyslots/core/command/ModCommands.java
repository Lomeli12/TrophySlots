package net.lomeli.trophyslots.core.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.lomeli.trophyslots.TrophySlots;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = TrophySlots.MOD_ID)
public class ModCommands {
    private static final List<ISubCommand> commands = Lists.newArrayList();

    private static void registerCommands() {
        commands.add(new GetSlotsCommand());
        commands.add(new RemoveSlotsCommand());
        commands.add(new SetSlotsCommand());
        commands.add(new UnlockSlotsCommand());
        commands.add(new TSClientConfigCommand());
        commands.add(new TSConfigCommand());
        commands.add(new AdvanceListCommand());
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        registerCommands();
        LiteralArgumentBuilder<CommandSource> argumentBuilder = Commands.literal("tslots");

        commands.forEach(sub -> sub.registerSubCommand(argumentBuilder.then(Commands.literal(sub.getName()))));

        event.getDispatcher().register(argumentBuilder);
    }
}

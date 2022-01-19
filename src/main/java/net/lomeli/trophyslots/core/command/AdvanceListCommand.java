package net.lomeli.trophyslots.core.command;

import com.google.common.io.Files;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.lomeli.trophyslots.TrophySlots;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AdvanceListCommand implements ISubCommand {
    private static final File OUTPUT_FILE = new File("advancementList.txt");

    @Override
    public void registerSubCommand(LiteralArgumentBuilder<CommandSourceStack> argumentBuilder) {
        argumentBuilder.then(Commands.literal(getName()).requires(source -> source.hasPermission(3))
                .executes(context -> printAdvancementsToFile(context.getSource())));
    }

    @SuppressWarnings("all")
    private int printAdvancementsToFile(CommandSourceStack source) {
        StringBuilder output = new StringBuilder();
        source.getServer().getAdvancements().getAllAdvancements().forEach(advancement -> {
            if (!advancement.getId().getNamespace().equalsIgnoreCase(TrophySlots.MOD_ID) &&
                    advancement.getDisplay() != null && advancement.getDisplay().shouldAnnounceChat()) {
                StringBuilder description = new StringBuilder();
                if (advancement.getDisplay().getTitle() != null)
                    description.append(advancement.getDisplay().getTitle().getString()).append(": ");
                output.append(String.format("%-50s %-50s", advancement.getId().toString(),
                        description.toString())).append("\n");
            }
        });

        try {
            if (OUTPUT_FILE.exists()) OUTPUT_FILE.delete();

            Files.write(output.toString(), OUTPUT_FILE, StandardCharsets.UTF_8);
            source.sendSuccess(new TranslatableComponent("command.trophyslots.advlist.success"), false);
        } catch (IOException ex) {
            TrophySlots.log.error("Could not write advancement list to file!", ex);
            source.sendSuccess(new TranslatableComponent("command.trophyslots.advlist.error"), false);
            return 1;
        }
        return 0;
    }

    @Override
    public String getName() {
        return "advlist";
    }
}

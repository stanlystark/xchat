package ru.xpixel.xchat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.luckperms.api.util.Tristate;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import ru.xpixel.xchat.ExtraChatMod;
import ru.xpixel.xchat.Message;
import ru.xpixel.xchat.integration.LuckPermsIntegration;

import java.util.Collection;

public class AnswerCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(CommandManager.literal("answer").requires(source -> (source.isExecutedByPlayer() && ExtraChatMod.Integrations.LuckPerms &&
                LuckPermsIntegration.checkPermission(source.getPlayer(), "xchat.chat.answer").equals(Tristate.TRUE)) || source.hasPermissionLevel(2))
            .then(CommandManager.argument("targets", EntityArgumentType.players()).requires(source -> source.hasPermissionLevel(2))
                .executes(context -> {
                    return 0;
                }).then(CommandManager.argument("message", StringArgumentType.greedyString())
                    .executes(context -> {
                        if (context.getSource().getPlayer() == null) return 0;
                        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "targets");
                        players.forEach(player -> {
                            Message.sendAnswerMessage(context.getSource().getPlayer(), player, Text.literal(StringArgumentType.getString(context, "message")));
                        });

                        return 1;
                    })
                )
            )
        );

    }
}

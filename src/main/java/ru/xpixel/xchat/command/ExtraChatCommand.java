package ru.xpixel.xchat.command;

import com.mojang.brigadier.CommandDispatcher;
import net.luckperms.api.util.Tristate;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import ru.xpixel.xchat.ExtraChatMod;
import ru.xpixel.xchat.Message;
import ru.xpixel.xchat.integration.LuckPermsIntegration;

public class ExtraChatCommand {
//    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
//
//        final LiteralCommandNode<ServerCommandSource> reloadCommand = dispatcher.register(CommandManager.literal("xchat")
//                .then(CommandManager.argument("message", StringArgumentType.greedyString())
//                        .executes(context -> {
//                            if(context.getSource().getPlayer() == null) return 0;
//                            Message.sendHelpMessage(context.getSource().getPlayer(), Text.literal(StringArgumentType.getString(context, "message")));
//                            return 1;
//                        })
//                )
//
//        );
//    }
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(CommandManager.literal("xchat").requires(source -> (source.isExecutedByPlayer() && ExtraChatMod.Integrations.LuckPerms &&
                        !LuckPermsIntegration.checkPermission(source.getPlayer(), "xchat.help").equals(Tristate.FALSE)) || source.hasPermissionLevel(2))
                .then(CommandManager.literal("help")
                        .executes(context -> {
                            if (context.getSource().getPlayer() == null) return 0;
                            Message.sendHelpInformation(context.getSource().getPlayer());
                            return 1;
                        })
                )
//                .then(CommandManager.literal("mute")
//                        .executes(ctx -> execute(ctx, ChatModeState.ChatMode.LOCAL))
//                        .then(CommandManager.argument("targets", EntityArgumentType.players()).requires(source -> source.hasPermissionLevel(2))
//                                .executes(ctx -> execute(ctx, ChatModeState.ChatMode.LOCAL, EntityArgumentType.getPlayers(ctx, "targets")))
//                        )
//                )
//                .then(CommandManager.literal("unmute")
//                        .executes(ctx -> execute(ctx, ChatModeState.ChatMode.LOCAL))
//                        .then(CommandManager.argument("targets", EntityArgumentType.players()).requires(source -> source.hasPermissionLevel(2))
//                                .executes(ctx -> execute(ctx, ChatModeState.ChatMode.LOCAL, EntityArgumentType.getPlayers(ctx, "targets")))
//                        )
//                )
        );

    }

}

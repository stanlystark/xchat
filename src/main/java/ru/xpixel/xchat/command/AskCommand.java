package ru.xpixel.xchat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import ru.xpixel.xchat.Message;

public class AskCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("ask")
                .then(CommandManager.argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            if(context.getSource().getPlayer() == null) return 0;
                            Message.sendAskMessage(context.getSource().getPlayer(), Text.literal(StringArgumentType.getString(context, "message")));
                            return 1;
                        })
                )

        );
    }
}

package ru.xpixel.xchat;

import dev.ftb.mods.ftbteams.data.Team;
import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
import eu.pb4.placeholders.api.TextParserUtils;
import net.luckperms.api.util.Tristate;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import ru.xpixel.xchat.integration.FTBTeamsIntegration;
import ru.xpixel.xchat.integration.LuckPermsIntegration;

import java.util.concurrent.atomic.AtomicBoolean;

public class Message {

    public static boolean onChatMessage(SignedMessage message, ServerPlayerEntity sender, MessageType.Parameters params) {
        if (ExtraChatMod.config.enable && sender.getServer() != null) {

            if (message.getContent().getString().startsWith("!") && !ExtraChatMod.Integrations.LuckPerms || (
                    message.getContent().getString().startsWith("!") &&
                    ExtraChatMod.Integrations.LuckPerms &&
                    !LuckPermsIntegration.checkPermission(sender, "xchat.chat.global").equals(Tristate.FALSE))
            ) {
                Message.sendGlobalMessage(sender, message.getContent());
            } else if (message.getContent().getString().startsWith("#") && !ExtraChatMod.Integrations.LuckPerms || (
                    message.getContent().getString().startsWith("#") &&
                            ExtraChatMod.Integrations.LuckPerms &&
                            !LuckPermsIntegration.checkPermission(sender, "xchat.chat.world").equals(Tristate.FALSE))
            ) {
                Message.sendWorldMessage(sender, message.getContent());
            } else if (message.getContent().getString().startsWith("=") && ExtraChatMod.Integrations.FTBTeams) {
                Message.sendTeamMessage(sender, message.getContent());
            } else if (message.getContent().getString().startsWith("--") && sender.hasPermissionLevel(4) || (
                    message.getContent().getString().startsWith("--") &&
                            ExtraChatMod.Integrations.LuckPerms &&
                            LuckPermsIntegration.checkPermission(sender, "xchat.chat.admin").equals(Tristate.TRUE))
            ) {
                Message.sendAdminMessage(sender, message.getContent());
            } else {
                Message.sendLocalMessage(sender, message.getContent());
            }

            return false;
        }

        return true;
    }

    private static void sendLocalMessage(ServerPlayerEntity sender, Text content) {
        ServerWorld world = sender.getWorld();
        String message = ExtraChatMod.config.formats.local.replaceAll("%message%", Message.parse(sender, content.getString().trim()));

        AtomicBoolean messageSent = new AtomicBoolean(false);

        world.getPlayers().forEach(serverPlayer -> {
            Text resultMessage = Placeholders.parseText(TextParserUtils.formatText(message), PlaceholderContext.of(sender));
            if (sender.getUuid().equals(serverPlayer.getUuid())) {
                serverPlayer.sendMessage(resultMessage);
            } else {
                double distance = getDistance(sender.getBlockPos(), serverPlayer.getBlockPos());
                if (distance <= ExtraChatMod.config.local_chat_range) {
                    messageSent.set(true);
                    serverPlayer.sendMessage(resultMessage);
                }
                // todo: spy mode toggle
//                else if (ExtraChatMod.Integrations.LuckPerms && LuckPermsIntegration.checkPermission(serverPlayer, "xchat.chat.spy").equals(Tristate.TRUE)) {
//                    serverPlayer.sendMessage(resultMessage.copy().append(" [SPY]"));
//                }
            }
        });

        // todo: add symbol in message
//        Text senderMessage = Placeholders.parseText(TextParserUtils.formatText(message.trim()), PlaceholderContext.of(sender));
        if (!messageSent.get()) {
//            sender.sendMessage(senderMessage);
//        } else {
            if (ExtraChatMod.config.aloneMessage) {
                Text aloneMessage = Placeholders.parseText(TextParserUtils.formatText(Message.parse(sender, ExtraChatMod.config.messages.alone.trim())), PlaceholderContext.of(sender));
                sender.sendMessage(aloneMessage, true);
//            } else {
//                sender.sendMessage(senderMessage);
            }
        }
    }

    private static void sendGlobalMessage(ServerPlayerEntity sender, Text content) {
        ServerWorld world = sender.getWorld();
        String prepare = content.getString().substring(1);
        String message = ExtraChatMod.config.formats.global.replaceAll("%message%", Message.parse(sender, prepare.trim()));

        world.getPlayers().forEach(serverPlayer -> {
            Text resultMessage = Placeholders.parseText(TextParserUtils.formatText(message), PlaceholderContext.of(sender));
            serverPlayer.sendMessage(resultMessage);
        });
    }

    private static void sendWorldMessage(ServerPlayerEntity sender, Text content) {
        ServerWorld world = sender.getWorld();
        String prepare = content.getString().substring(1);
        String message = ExtraChatMod.config.formats.world.replaceAll("%message%", Message.parse(sender, prepare.trim()));

        world.getPlayers().forEach(serverPlayer -> {
            Text resultMessage = Placeholders.parseText(TextParserUtils.formatText(message), PlaceholderContext.of(sender));
            if (sender.getUuid().equals(serverPlayer.getUuid())) {
                serverPlayer.sendMessage(resultMessage);
            } else if (sender.getWorld().equals(serverPlayer.getWorld())) {
                serverPlayer.sendMessage(resultMessage);
            }
        });
    }

    private static void sendTeamMessage(ServerPlayerEntity sender, Text content) {
        ServerWorld world = sender.getWorld();
        String prepare = content.getString().substring(1);
        String message = ExtraChatMod.config.formats.ftb_team.replaceAll("%message%", Message.parse(sender, prepare.trim()));
        Team senderTeam = FTBTeamsIntegration.getTeam(sender);

        if (senderTeam != null) {
            world.getPlayers().forEach(serverPlayer -> {
                Text resultMessage = Placeholders.parseText(TextParserUtils.formatText(message), PlaceholderContext.of(sender));
                if (sender.getUuid().equals(serverPlayer.getUuid())) {
                    serverPlayer.sendMessage(resultMessage);
                } else if (senderTeam.equals(FTBTeamsIntegration.getTeam(serverPlayer))) {
                    serverPlayer.sendMessage(resultMessage);
                }
            });
        } else {
            sender.sendMessage(Text.literal(Message.parse(sender, ExtraChatMod.config.messages.no_team.trim())));
        }
    }

    private static void sendAdminMessage(ServerPlayerEntity sender, Text content) {
        ServerWorld world = sender.getWorld();
        String prepare = content.getString().substring(2);
        String message = ExtraChatMod.config.formats.admin.replaceAll("%message%", Message.parse(sender, prepare.trim()));

        world.getPlayers().forEach(serverPlayer -> {
            Text resultMessage = Placeholders.parseText(TextParserUtils.formatText(message), PlaceholderContext.of(sender));
            if (sender.getUuid().equals(serverPlayer.getUuid())) {
                serverPlayer.sendMessage(resultMessage);
            } else if (serverPlayer.hasPermissionLevel(4) || (ExtraChatMod.Integrations.LuckPerms &&
                    LuckPermsIntegration.checkPermission(sender, "xchat.chat.admin").equals(Tristate.TRUE))) {
                serverPlayer.sendMessage(resultMessage);
            }
        });
    }

    public static void sendAskMessage(ServerPlayerEntity sender, Text content) {
        ServerWorld world = sender.getWorld();
//        String prepare = content.getString().substring(2);
        String message = Message.parse(sender, ExtraChatMod.config.formats.ask).replaceAll("%message%", content.getString().trim());

        world.getPlayers().forEach(serverPlayer -> {
            Text resultMessage = Placeholders.parseText(TextParserUtils.formatText(message), PlaceholderContext.of(sender));
            if (sender.getUuid().equals(serverPlayer.getUuid())) {
                serverPlayer.sendMessage(Text.literal(Message.parse(sender, ExtraChatMod.config.messages.ask.trim())));
            }
            if (serverPlayer.hasPermissionLevel(4) || (ExtraChatMod.Integrations.LuckPerms &&
                    LuckPermsIntegration.checkPermission(sender, "xchat.chat.ask").equals(Tristate.TRUE))) {
                serverPlayer.sendMessage(resultMessage);
            }
        });
    }

    private static double getDistance(BlockPos pos1, BlockPos pos2) {
        BlockPos d = pos1.subtract(pos2);
        int x = d.getX();
        int y = d.getY();
        int z = d.getZ();
        return Math.sqrt(x * x + y * y + z * z);
    }

    private static String parse(ServerPlayerEntity player, String message) {
        return message.replaceAll("%answer_player_name%", player.getName().getString());
    }

    public static void sendHelpInformation(ServerPlayerEntity player) {
        player.sendMessage(Text.literal(Message.parse(player, ExtraChatMod.config.messages.help.trim())));
    }

    public static void sendAnswerMessage(ServerPlayerEntity sender, ServerPlayerEntity target, Text content) {
        String message = ExtraChatMod.config.formats.answer.replaceAll("%message%", Message.parse(sender, content.getString().trim()));
        Text resultMessage = Placeholders.parseText(TextParserUtils.formatText(message), PlaceholderContext.of(sender));
        sender.sendMessage(resultMessage);
        target.sendMessage(resultMessage);
    }
}
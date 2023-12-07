package ru.xpixel.xchat.integration;

import dev.ftb.mods.ftbteams.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.data.Team;
import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FTBTeamsIntegration {

    public static Team getTeam(ServerPlayerEntity player) {
        Team team = FTBTeamsAPI.getPlayerTeam(player.getUuid());

        if (team == null) return null;
        if (team.getType().isPlayer()) return null;

        return team;
    }

    public static void register() {
        Placeholders.register(new Identifier("player", "ftbteam"), (ctx, arg) -> {
            if (ctx.hasPlayer()) {
                if (FTBTeamsIntegration.getTeam(ctx.player()) != null) {
                    return PlaceholderResult.value(FTBTeamsIntegration.getTeam(ctx.player()).getDisplayName());
                } else {
                    return PlaceholderResult.invalid();
                }
            } else {
                return PlaceholderResult.invalid("No player!");
            }
        });
    }

}

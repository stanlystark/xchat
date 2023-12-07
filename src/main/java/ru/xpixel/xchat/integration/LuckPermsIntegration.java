package ru.xpixel.xchat.integration;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.minecraft.server.network.ServerPlayerEntity;

public class LuckPermsIntegration {
    public static Object checkPermission(ServerPlayerEntity player, String permission) {
        LuckPerms api = LuckPermsProvider.get();
        return api.getPlayerAdapter(ServerPlayerEntity.class).getPermissionData(player).checkPermission(permission);
    }
}

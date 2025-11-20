package io.github.aleksandarharalanov.chatguard.util.auth;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;

public final class AccessUtil {

    public static PermissionState senderHasPermission(CommandSender sender, Permission permission) {
        final String permissionName = permission.toString();
        final String noPermMessage = permission.noPermissionMessage();
        
        if (
            permission.requiresStaff() &&
            !senderHasPermission(sender, Permission.STAFF).result()
        ) return new PermissionState(false, true); // we want to send the help message here to the player to not alarm them

        return senderHasPermission(sender, permissionName, noPermMessage);
    }

    private static PermissionState senderHasPermission(CommandSender sender, String permission, String noPermissionMessage) {        
        if (!(sender instanceof Player))
            return PermissionState.TRUE;

        boolean hasPermission = sender.hasPermission(permission);
        boolean isOp = sender.isOp();

        if (hasPermission || isOp)
            return PermissionState.TRUE;

        if (!noPermissionMessage.isEmpty())
            sender.sendMessage(ColorUtil.translateColorCodes(String.format(
                "&c%s", noPermissionMessage
            )));

        return PermissionState.FALSE;
    }
}
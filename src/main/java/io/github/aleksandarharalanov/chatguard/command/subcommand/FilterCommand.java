package io.github.aleksandarharalanov.chatguard.command.subcommand;

import org.bukkit.command.CommandSender;

import io.github.aleksandarharalanov.chatguard.util.auth.AccessUtil;
import io.github.aleksandarharalanov.chatguard.util.auth.Permission;
import io.github.aleksandarharalanov.chatguard.util.auth.PermissionState;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;

public final class FilterCommand implements IChatGuardSubCommand {

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        PermissionState permission = AccessUtil.senderHasPermission(sender, Permission.FILTER);
        if (!permission.result())
            return !permission.hidden();

        if (args.length > 2) {
            sender.sendMessage(ColorUtil.translateColorCodes("&cUsage: /cg filter [true|false]"));
            return true;
        }

        sender.sendMessage("not implemented");

        return true;
    }
}

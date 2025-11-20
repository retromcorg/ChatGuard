package io.github.aleksandarharalanov.chatguard.command.subcommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.util.auth.AccessUtil;
import io.github.aleksandarharalanov.chatguard.util.auth.Permission;
import io.github.aleksandarharalanov.chatguard.util.auth.PermissionState;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;

public final class ReloadCommand implements IChatGuardSubCommand {

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        PermissionState permission = AccessUtil.senderHasPermission(sender, Permission.CONFIG);
        if (!permission.result())
            return !permission.hidden();

        if (sender instanceof Player)
            sender.sendMessage(ColorUtil.translateColorCodes("&a[ChatGuard] Configurations reloaded."));

        System.out.println("[ChatGuard] Configurations reloaded.");

        ChatGuard.reloadConfig();
        ChatGuard.getDiscord().loadAndLog();
        ChatGuard.getStrikes().loadAndLog();
        ChatGuard.getCaptchas().loadAndLog();

        return true;
    }
}

package io.github.aleksandarharalanov.chatguard.command.subcommand;

import io.github.aleksandarharalanov.chatguard.util.auth.AccessUtil;
import io.github.aleksandarharalanov.chatguard.util.auth.Permission;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class HelpCommand {

    private static final String[] normalMessages = {
        "&bChatGuard commands:",
        "&e/cg &7- Displays this content.",
        "&e/cg about &7- About ChatGuard.",
        "&e/cg filter [true|false] &7- Filters messages deemed inapropriate to RMC"
    };

    private static final String[] staffMessages = {
        "&e/cg reload &7- Reload ChatGuard config.",
        "&e/cg strike <username> [strikes] &7- View or set the strike count of a player."
    };

    public static void sendHelp(CommandSender sender) {
        List<String> messages = new ArrayList<String>();
        
        messages.addAll(Arrays.asList(normalMessages));

        if (AccessUtil.senderHasPermission(sender, Permission.STAFF).result())
            messages.addAll(Arrays.asList(staffMessages));

        for (String message : messages)
            if (sender instanceof Player)
                sender.sendMessage(ColorUtil.translateColorCodes(message));
            else
                System.out.println(message.replaceAll("&.", ""));
    }
}

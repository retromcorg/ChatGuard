package io.github.aleksandarharalanov.chatguard.command.subcommand;

import java.util.List;

import org.bukkit.command.CommandSender;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.util.misc.AboutUtil;

public final class AboutCommand implements IChatGuardSubCommand {

    private final ChatGuard plugin;

    public AboutCommand(ChatGuard plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        // Contributors: If you've contributed code, you can add your name here to be credited.
        List<String> contributors = List.of();

        AboutUtil.aboutPlugin(sender, plugin, contributors);
        return true;
    }
}

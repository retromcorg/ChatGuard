package io.github.aleksandarharalanov.chatguard;

import io.github.aleksandarharalanov.chatguard.command.ChatGuardCommand;
import io.github.aleksandarharalanov.chatguard.listener.PlayerChatListener;
import io.github.aleksandarharalanov.chatguard.listener.PlayerJoinListener;
import io.github.aleksandarharalanov.chatguard.util.ConfigUtil;
import io.github.aleksandarharalanov.chatguard.util.LoggerUtil;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import static io.github.aleksandarharalanov.chatguard.util.LoggerUtil.logInfo;
import static io.github.aleksandarharalanov.chatguard.util.UpdateUtil.checkForUpdates;

public class ChatGuard extends JavaPlugin {

    private static ChatGuard plugin;
    private static ConfigUtil config;
    private static ConfigUtil strikes;

    @Override
    public void onEnable() {
        checkForUpdates(this, "https://api.github.com/repos/AleksandarHaralanov/ChatGuard/releases/latest");

        plugin = this;

        config = new ConfigUtil(this, "config.yml");
        config.loadConfig();

        strikes = new ConfigUtil(this, "strikes.yml");
        strikes.loadConfig();

        final LoggerUtil log = new LoggerUtil(this, "log.txt");
        log.initializeLog();

        final PlayerChatListener playerChatListener = new PlayerChatListener();
        final PlayerJoinListener playerJoinListener = new PlayerJoinListener();
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvent(Type.PLAYER_CHAT, playerChatListener, Priority.Lowest, this);
        pluginManager.registerEvent(Type.PLAYER_JOIN, playerJoinListener, Priority.Normal, this);

        final ChatGuardCommand command = new ChatGuardCommand(this);
        getCommand("chatguard").setExecutor(command);

        logInfo(String.format("[%s] v%s Enabled.", getDescription().getName(), getDescription().getVersion()));
    }

    @Override
    public void onDisable() {
        logInfo(String.format("[%s] v%s Disabled.", getDescription().getName(), getDescription().getVersion()));
    }

    public static ChatGuard getInstance() {
        return plugin;
    }

    public static ConfigUtil getConfig() {
        return config;
    }

    public static ConfigUtil getStrikes() {
        return strikes;
    }
}
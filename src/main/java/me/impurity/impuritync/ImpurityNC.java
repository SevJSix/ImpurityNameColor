package me.impurity.impuritync;

import me.impurity.impuritync.command.NameColor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class ImpurityNC extends JavaPlugin {
    public File file = new File(getDataFolder(), "players.yml");
    PluginManager pl = getServer().getPluginManager();
    private FileConfiguration players;

    public static ImpurityNC getPlugin() {
        return getPlugin(ImpurityNC.class);
    }

    public static void sendMessage(Object player, String message) {
        if (player instanceof Player) {
            Player casted = (Player) player;
            casted.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        } else {
            if (player instanceof CommandSender) {
                CommandSender casted = (CommandSender) player;
                casted.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }
    }

    public FileConfiguration getPlayers() {
        return players;
    }

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        players = makeConfig();
        getCommand("nc").setExecutor(new NameColor());
        pl.registerEvents(new NameColor(), this);
        getLogger().info(ChatColor.GREEN + "ImpurityNC by SevJ6 enabled!");
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    private FileConfiguration makeConfig() {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            YamlConfiguration config = new YamlConfiguration();
            config.load(file);
            return config;
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveConfig() {
        try {
            players.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadPlayers() {
        makeConfig();
    }
}

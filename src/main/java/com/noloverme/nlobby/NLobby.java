package com.noloverme.nlobby;

import com.noloverme.nlobby.command.LobbyCommand;
import com.noloverme.nlobby.command.NHubCommand;
import com.noloverme.nlobby.config.ConfigManager;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Level;

public class NLobby extends Plugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Инициализация ConfigManager
        configManager = new ConfigManager(this);
        configManager.loadConfig();

        // Регистрация команд
        getProxy().getPluginManager().registerCommand(this, new LobbyCommand(this, "lobby"));
        getProxy().getPluginManager().registerCommand(this, new LobbyCommand(this, "hub"));
        getProxy().getPluginManager().registerCommand(this, new NHubCommand(this, "nhub"));

        getLogger().info("NLobby v" + getDescription().getVersion() + " включен!");
    }

    @Override
    public void onDisable() {
        getLogger().info("NLobby v" + getDescription().getVersion() + " выключен!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
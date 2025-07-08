package com.noloverme.nlobby.config;

import com.noloverme.nlobby.NLobby;
import com.noloverme.nlobby.util.MessageUtil;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class ConfigManager {

    private final NLobby plugin;
    private Configuration config;
    private List<String> lobbyServers;
    private List<String> disabledServers;
    private String noPermissionMessage;
    private String onlyPlayersMessage;
    private String playerNotFoundMessage;
    private String reloadSuccessMessage;
    private String successfullySentMessage;
    private String noLobbyServerMessage;
    private String alreadyOnLobbyMessage;
    private String disabledServerMessage;

    public ConfigManager(NLobby plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        try {
            // Создание папки плагина, если ее нет
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdir();
            }

            // Создание файла конфигурации, если его нет
            File configFile = new File(plugin.getDataFolder(), "config.yml");
            if (!configFile.exists()) {
                try (InputStream in = plugin.getResourceAsStream("config.yml")) {
                    Files.copy(in, configFile.toPath());
                } catch (IOException e) {
                    plugin.getLogger().log(Level.SEVERE, "Ошибка при создании файла конфигурации:", e);
                }
            }

            // Загрузка конфигурации
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);

            // Загрузка данных из конфигурации
            lobbyServers = config.getStringList("lobby_servers");
            disabledServers = config.getStringList("disabled_servers");
            noPermissionMessage = config.getString("messages.no_permission");
            onlyPlayersMessage = config.getString("messages.only_players");
            playerNotFoundMessage = config.getString("messages.player_not_found");
            reloadSuccessMessage = config.getString("messages.reload_success");
            successfullySentMessage = config.getString("messages.successfully_sent");
            noLobbyServerMessage = config.getString("messages.no_lobby_server");
            alreadyOnLobbyMessage = config.getString("messages.already_on_lobby");
            disabledServerMessage = config.getString("messages.disabled_server");


        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Ошибка при загрузке конфигурации:", e);
        }
    }

    public void reloadConfig() {
        try {
            File configFile = new File(plugin.getDataFolder(), "config.yml");
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);

            // Обновление данных из конфигурации
            lobbyServers = config.getStringList("lobby_servers");
            disabledServers = config.getStringList("disabled_servers");
            noPermissionMessage = config.getString("messages.no_permission");
            onlyPlayersMessage = config.getString("messages.only_players");
            playerNotFoundMessage = config.getString("messages.player_not_found");
            reloadSuccessMessage = config.getString("messages.reload_success");
            successfullySentMessage = config.getString("messages.successfully_sent");
            noLobbyServerMessage = config.getString("messages.no_lobby_server");
            alreadyOnLobbyMessage = config.getString("messages.already_on_lobby");
            disabledServerMessage = config.getString("messages.disabled_server");

            plugin.getLogger().info("Конфигурация перезагружена.");
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Ошибка при перезагрузке конфигурации:", e);
        }
    }

    public ServerInfo getRandomLobbyServer() {
        if (lobbyServers == null || lobbyServers.isEmpty()) {
            return null; // Список серверов пуст
        }

        // Фильтруем недоступные серверы
        List<String> availableServers = new ArrayList<>(lobbyServers);
        availableServers.removeIf(serverName -> plugin.getProxy().getServerInfo(serverName) == null);

        if (availableServers.isEmpty()) {
            return null; // Ни один сервер не доступен
        }


        Random random = new Random();
        String serverName = availableServers.get(random.nextInt(availableServers.size()));
        return plugin.getProxy().getServerInfo(serverName);
    }

    public void sendPlayerToLobby(ProxiedPlayer player) {
        ServerInfo lobbyServer = getRandomLobbyServer();

        if (lobbyServer == null) {
            player.sendMessage(MessageUtil.color(noLobbyServerMessage));
            return;
        }

        if (player.getServer().getInfo().equals(lobbyServer)){
            player.sendMessage(MessageUtil.color(alreadyOnLobbyMessage));
            return;
        }

        player.connect(lobbyServer);
        player.sendMessage(MessageUtil.color(successfullySentMessage));
    }

    // Геттеры для доступа к данным конфигурации
    public List<String> getLobbyServers() {
        return lobbyServers;
    }

    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }

    public String getOnlyPlayersMessage() {
        return onlyPlayersMessage;
    }

    public String getPlayerNotFoundMessage() {
        return playerNotFoundMessage;
    }

    public String getReloadSuccessMessage() {
        return reloadSuccessMessage;
    }

    public String getSuccessfullySentMessage() {
        return successfullySentMessage;
    }

    public String getNoLobbyServerMessage() {
        return noLobbyServerMessage;
    }

    public String getAlreadyOnLobbyMessage() {
        return alreadyOnLobbyMessage;
    }

    public List<String> getDisabledServers() {
        return disabledServers;
    }

    public String getDisabledServerMessage() {
        return disabledServerMessage;
    }
}
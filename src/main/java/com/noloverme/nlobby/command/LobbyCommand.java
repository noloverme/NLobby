package com.noloverme.nlobby.command;

import com.noloverme.nlobby.NLobby;
import com.noloverme.nlobby.config.ConfigManager;
import com.noloverme.nlobby.util.MessageUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LobbyCommand extends Command {

    private final NLobby plugin;
    private final ConfigManager configManager;

    public LobbyCommand(NLobby plugin, String name) {
        super(name);
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(MessageUtil.color(configManager.getOnlyPlayersMessage()));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        ServerInfo currentServer = player.getServer().getInfo();

        // Проверка, находится ли игрок на заблокированном сервере
        if (configManager.getDisabledServers().contains(currentServer.getName())) {
            player.sendMessage(MessageUtil.color(configManager.getDisabledServerMessage()));
            return;
        }

        // Проверка, находится ли игрок уже на лобби-сервере
        ServerInfo lobbyServer = plugin.getConfigManager().getRandomLobbyServer();

        if (lobbyServer != null && currentServer.equals(lobbyServer)) {
            player.sendMessage(MessageUtil.color(configManager.getAlreadyOnLobbyMessage()));
            return;
        }

        plugin.getConfigManager().sendPlayerToLobby(player);
    }
}
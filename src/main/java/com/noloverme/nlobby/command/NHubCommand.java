package com.noloverme.nlobby.command;

import com.noloverme.nlobby.NLobby;
import com.noloverme.nlobby.config.ConfigManager;
import com.noloverme.nlobby.util.MessageUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Collections;
import java.util.stream.Collectors;

public class NHubCommand extends Command implements TabExecutor {

    private final NLobby plugin;
    private final ConfigManager configManager;

    public NHubCommand(NLobby plugin, String name) {
        super(name, "nlobby.admin");
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("nlobby.admin")) {
            sender.sendMessage(MessageUtil.color(configManager.getNoPermissionMessage()));
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(MessageUtil.color("&cИспользование: /nhub <reload|all|<игрок>>"));
            return;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                configManager.reloadConfig();
                sender.sendMessage(MessageUtil.color(configManager.getReloadSuccessMessage()));
                break;
            case "all":
                // Отправка всех игроков на лобби-сервер
                for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
                    configManager.sendPlayerToLobby(player);
                }
                sender.sendMessage(MessageUtil.color(configManager.getSuccessfullySentMessage()));
                break;
            default:
                // Отправка конкретного игрока на лобби-сервер
                ProxiedPlayer target = plugin.getProxy().getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(MessageUtil.color(configManager.getPlayerNotFoundMessage()));
                    return;
                }
                configManager.sendPlayerToLobby(target);
                sender.sendMessage(MessageUtil.color(configManager.getSuccessfullySentMessage().replace("%player%", target.getName())));
                break;
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            // Возвращаем список имен игроков, которые начинаются с введенного текста
            String partialName = args[0].toLowerCase();
            return plugin.getProxy().getPlayers().stream()
                    .map(ProxiedPlayer::getName)
                    .filter(name -> name.toLowerCase().startsWith(partialName))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList(); // Нет автозавершения для других аргументов
    }
}
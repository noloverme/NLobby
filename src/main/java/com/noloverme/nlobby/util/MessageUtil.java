package com.noloverme.nlobby.util;

import net.md_5.bungee.api.ChatColor;

public class MessageUtil {

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
package core.luuh.aspergescore.utils;


import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class chatcolor {

    // method to replace & with the color codes
    public static String chat(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    // method to replace (ex: &#FF0000) into color codes
    public static String hex(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String papi(Player p, String message){

        return PlaceholderAPI.setPlaceholders(p, message);

    }

    public static String col(String message){

        return chat(hex(message));

    }

    public static String all(Player p, String message){

        return chat(hex(papi(p, message)));

    }

}





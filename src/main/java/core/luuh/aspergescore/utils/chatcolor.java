package core.luuh.aspergescore.utils;


import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class chatcolor {

    // method to replace & with the color codes
    public static String chat(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    // method to replace (ex: #FF0000) into color codes
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
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String papi(Player p, String message){

        return PlaceholderAPI.setPlaceholders(p, message);

    }

    public static String col(String message){

        return hex(chat(message));

    }

    public static String repLegacyToComp(String message){

        return message
                //colors
                .replaceAll("&0", "<black>")
                .replaceAll("&1", "<dark_blue>")
                .replaceAll("&2", "<dark_green>")
                .replaceAll("&3", "<dark_aqua>")
                .replaceAll("&4", "<dark_red>")
                .replaceAll("&5", "<dark_purple>")
                .replaceAll("&6", "<gold>")
                .replaceAll("&7", "<gray>")
                .replaceAll("&8", "<dark_gray>")
                .replaceAll("&9", "<blue>")
                .replaceAll("&a", "<green>")
                .replaceAll("&b", "<aqua>")
                .replaceAll("&c", "<red>")
                .replaceAll("&d", "<light_purple>")
                .replaceAll("&e", "<yellow>")
                .replaceAll("&f", "<white>")

                //decorations
                .replaceAll("&l", "<b>")
                .replaceAll("&o", "<i>")
                .replaceAll("&n", "<u>")
                .replaceAll("&m", "<st>")
                .replaceAll("&k", "<obf>")
                .replaceAll("&r", "<reset>");

    }

    public static String replaceHexTags(String input) {
        return input.replaceAll("<", "").replaceAll(">", "");
    }

    public static String repCompToLegacy(String message){

        message = message

                //colors
                .replaceAll("<black>", "&0")
                .replaceAll( "<dark_blue>", "&1")
                .replaceAll("<dark_green>", "&2")
                .replaceAll("<dark_aqua>", "&3")
                .replaceAll("<dark_red>", "&4")
                .replaceAll("<dark_purple>", "&5")
                .replaceAll("<gold>", "&6")
                .replaceAll("<gray>", "&7")
                .replaceAll("<dark_gray>", "&8")
                .replaceAll("<blue>", "&9")
                .replaceAll("<green>", "&a")
                .replaceAll("<aqua>", "&b")
                .replaceAll("<red>", "&c")
                .replaceAll("<light_purple>", "&d")
                .replaceAll("<yellow>", "&e")
                .replaceAll("<white>", "&f")

                //decorations
                .replaceAll("<b>", "&l")
                .replaceAll("<bold>", "&l")
                .replaceAll("<i>", "&o")
                .replaceAll("<italic>", "&o")
                .replaceAll("<u>", "&n")
                .replaceAll("<underlined>", "&n")
                .replaceAll("<st>", "&m")
                .replaceAll("<strikethrough>", "&m")
                .replaceAll("<obf>", "&k")
                .replaceAll("<obfuscated>", "&k")
                .replaceAll("<r>", "&r")
                .replaceAll("<reset>", "&r");

        return hex(replaceHexTags(message));
    }

    public static Component mm(String message){

        message = repLegacyToComp(message);
        return MiniMessage.miniMessage().deserialize(message);

    }

    public static Component mm(String message, Player player){

        message = papi(player, repLegacyToComp(message));
        return mm(message);

    }

    public static String all(Player p, String message){

        return chat(hex(papi(p, message)));

    }

}





package core.luuh.aspergescore.utils.files;

import core.luuh.aspergescore.AspergesCore;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class RCUtils {

    // Read from Config Utils

    private static final AspergesCore plugin = AspergesCore.getInstance();

    public static List<ConfigurationSection> readCS(String path){

        List<ConfigurationSection> csList = new ArrayList<>();

        ConfigurationSection cs = plugin.getConfig().getConfigurationSection(path);
        for (String s : cs.getKeys(false)) {

            csList.add(plugin.getConfig().getConfigurationSection(s));

        }

        return csList;
    }

    public static List<String> readList(String path) {
        return plugin.getConfig().getStringList(path);
    }

    public static Boolean readBool(String path){
        return plugin.getConfig().getBoolean(path);
    }

    public static String readString(String path){
        return plugin.getConfig().getString(path);
    }

    public static Integer readInt(String path){
        return plugin.getConfig().getInt(path);
    }

    public static Double readDouble(String path){
        return plugin.getConfig().getDouble(path);
    }

    public static void set(String path, Object value){
        plugin.getConfig().set(path, value);
        plugin.saveConfig();

    }
}

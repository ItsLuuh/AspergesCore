package core.luuh.aspergescore.utils;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.utils.scoreboard.SBFilesManager;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class RSUtils {

    // Read from Config Utils

    private static final SBFilesManager plugin = SBFilesManager.getInstance();

    public static List<ConfigurationSection> readCS(String path){

        List<ConfigurationSection> csList = new ArrayList<>();

        ConfigurationSection cs = plugin.getData().getConfigurationSection(path);
        for (String s : cs.getKeys(false)) {

            csList.add(plugin.getData().getConfigurationSection(s));

        }

        return csList;
    }

    public static List<String> readList(String path) {
        return plugin.getData().getStringList(path);
    }

    public static Boolean readBool(String path){
        return plugin.getData().getBoolean(path);
    }

    public static String readString(String path){
        return plugin.getData().getString(path);
    }

    public static Integer readInt(String path){
        return plugin.getData().getInt(path);
    }
}

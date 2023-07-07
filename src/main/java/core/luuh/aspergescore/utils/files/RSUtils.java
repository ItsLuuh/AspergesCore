package core.luuh.aspergescore.utils.files;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class RSUtils {

    // Read from Scoreboards Utils

    private static final SBFileManager plugin = SBFileManager.getInstance();

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

    public static void set(String path, Object value){
        plugin.getData().set(path, value);
        plugin.saveData();

    }
}

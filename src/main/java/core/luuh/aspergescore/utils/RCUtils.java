package core.luuh.aspergescore.utils;

import core.luuh.aspergescore.AspergesCore;

import java.util.List;

public class RCUtils {

    // Read from Config Utils

    private static final AspergesCore plugin = AspergesCore.getInstance();


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
}

package core.luuh.aspergescore.utils.files;

import core.luuh.aspergescore.AspergesCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class SBFileManager {

    private final AspergesCore plugin = AspergesCore.getInstance();

    static SBFileManager instance = new SBFileManager();


    public static SBFileManager getInstance() {
        return instance;
    }

    Plugin p;

    FileConfiguration dataconfig;
    File dfile;

    public void setup(Plugin p) {
        if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdir();
        }

        dfile = new File(p.getDataFolder(), "scoreboards.yml");

        if (!dfile.exists()) {
            try {
                dfile.createNewFile();
            } catch (IOException e) {
                Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create scoreboards.yml!");
            }
        }

        dataconfig = YamlConfiguration.loadConfiguration(dfile);
    }

    public FileConfiguration getData() {
        return dataconfig;
    }

    public void saveData() {
        try {
            dataconfig.save(dfile);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save scoreboards.yml");
        }
    }

    public void reloadData() {
        dataconfig = YamlConfiguration.loadConfiguration(dfile);
    }
}
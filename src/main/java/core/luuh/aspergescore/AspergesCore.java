// 
// Decompiled by Procyon v0.5.36
// 

package core.luuh.aspergescore;

import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Iterator;
import java.util.List;
import org.bukkit.scheduler.BukkitRunnable;
import core.luuh.aspergescore.utils.scoreboard.SBManager;
import org.bukkit.entity.Player;
import core.luuh.aspergescore.utils.scoreboard.SBFilesManager;
import java.sql.SQLException;
import core.luuh.verioncore.VerionAPIManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import core.luuh.aspergescore.scoreboard.ScoreboardPlayerQJEvent;
import org.bukkit.Bukkit;
import core.luuh.aspergescore.scoreboard.LoadSBCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;
import core.luuh.aspergescore.db.Database;
import org.bukkit.plugin.java.JavaPlugin;

public final class AspergesCore extends JavaPlugin
{
    String versionplugin;
    static AspergesCore instance;
    private Database database;
    
    public AspergesCore() {
        this.versionplugin = this.getDescription().getVersion();
    }
    
    public String getVersionPlugin() {
        return this.versionplugin;
    }
    
    public static AspergesCore getInstance() {
        return AspergesCore.instance;
    }
    
    private void registerCommands() {
        this.getCommand("aspergescore").setExecutor(new CoreCommand(this));
        this.getCommand("aspergescore").setTabCompleter(new CoreCommand(this));
        this.getCommand("loadsb").setExecutor(new LoadSBCommand(this));
        this.getCommand("loadsb").setTabCompleter(new LoadSBCommand(this));
    }
    
    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new ScoreboardPlayerQJEvent(this), this);
    }
    
    public Database getDatabase() {
        return this.database;
    }
    
    private void registerDB() {
        try {
            (database = new Database(this)).initializeDatabase();
        }
        catch (SQLException e) {
            VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + this.versionplugin + "&r &f»&r &cCan't connect to DB: &bplayer_stats&f!&r");
            VerionAPIManager.logConsole("&fFollowing the Stack Trace:");
            e.printStackTrace();
            VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + this.versionplugin + "&r &f»&r &cUnable to create TABLE: &bplayer_stats&f, into DB: &bplayer_stats&f!&r");
        }
    }
    
    private void registerConfigs() {
        final SBFilesManager sbF = SBFilesManager.getInstance();
        sbF.setup(this);
        this.saveDefaultConfig();
        sbF.getData().options().copyDefaults(true);
    }

    private void registerScoreUpdate() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (SBManager.hasScore(player)) {
                SBFilesManager sbF = SBFilesManager.getInstance();
                SBManager sbManager = SBManager.getByPlayer(player);
                String name = SBManager.getScoreNameOfPlayerByHM(player);
                ConfigurationSection sbN = sbF.getData().getConfigurationSection(name);

                Set<String> list = sbN.getKeys(false);
                int lines = sbN.getKeys(false).size() - 1;

                for (String s : list) {
                    if (!s.equalsIgnoreCase("title")) {
                        int finalLines = lines;

                        ConfigurationSection cs = sbN.getConfigurationSection(s);
                        List<String> textList = cs.getStringList("text");
                        long interval = cs.getInt("interval");
                        String text = cs.getStringList("text").get(0);

                        new BukkitRunnable() {
                            int i = 0;

                            @Override
                            public void run() {
                                if (textList.size() > 1) {
                                    String currentText = textList.get(i);
                                    sbManager.setSlot(finalLines, currentText, player);

                                    i++;
                                    if (i >= textList.size()) {
                                        i = 0;
                                    }
                                } else {
                                    sbManager.setSlot(finalLines, text, player);
                                }
                            }
                        }.runTaskTimer(this, 0L, interval);

                        lines--;
                    }
                }
            }
        }
    }
    
    private void registerAll() {
        registerCommands();
        registerEvents();
        registerConfigs();
    }
    
    public void onEnable() {
        instance = this;
        registerAll();
        registerScoreUpdate();

        VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + this.versionplugin + "&r &f»&r &aENABLED!&r");
    }
    
    public void onDisable() {
        SBFilesManager.getInstance().saveData();

        VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + this.versionplugin + "&r &f»&r &cDISABLED!&r");
    }
}

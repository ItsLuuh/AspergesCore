package core.luuh.aspergescore;

import core.luuh.aspergescore.itemlore.ItemLoreCommand;
import core.luuh.aspergescore.itemlore.LoreCommand;
import core.luuh.aspergescore.itemlore.RenameCommand;
import core.luuh.aspergescore.scoreboard.SwitchWorldEvent;
import core.luuh.aspergescore.utils.Ticker;
import org.bukkit.scheduler.BukkitRunnable;
import core.luuh.aspergescore.utils.scoreboard.SBManager;
import org.bukkit.entity.Player;
import core.luuh.aspergescore.utils.scoreboard.SBFilesManager;
import java.sql.SQLException;
import core.luuh.verioncore.VerionAPIManager;
import core.luuh.aspergescore.scoreboard.ScoreboardPlayerQJEvent;
import org.bukkit.Bukkit;
import core.luuh.aspergescore.scoreboard.LoadSBCommand;
import core.luuh.aspergescore.db.Database;
import org.bukkit.plugin.java.JavaPlugin;

public final class AspergesCore extends JavaPlugin {
    String versionplugin;
    static AspergesCore instance;
    private Database database;
    private Ticker ticker;
    
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
        this.getCommand("itemlore").setExecutor(new ItemLoreCommand(this));
        this.getCommand("itemlore").setTabCompleter(new ItemLoreCommand(this));
        this.getCommand("rename").setExecutor(new RenameCommand(this));
        this.getCommand("rename").setTabCompleter(new RenameCommand(this));
        this.getCommand("lore").setExecutor(new LoreCommand(this));
        this.getCommand("lore").setTabCompleter(new LoreCommand(this));
    }
    
    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new ScoreboardPlayerQJEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new SwitchWorldEvent(this), this);
    }
    
    public Database getDatabase() {
        return this.database;
    }

    public void registerDB(){
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
    
    private void registerAll() {
        registerCommands();
        registerEvents();
        registerConfigs();
    }

    public void onTick(){
        registerScoreUpdate();
    }


    public void registerScoreUpdate() {

        for (Player player : Bukkit.getOnlinePlayers()) {

            SBManager sbM = SBManager.getByPlayer(player);
            int i = 15;
            for (String s : sbM.getLines(player)) {

                if(i==0)break;

                SBManager.scoreUpdateTask(player, s, i);

                i--;

            }
        }

    }



        public void onEnable() {
        instance = this;
        registerAll();

        // Ticker
        ticker=new Ticker(this);
        ticker.setEnabled(true);

        VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + this.versionplugin + "&r &f»&r &aENABLED!&r");
    }
    
    public void onDisable() {
        SBFilesManager.getInstance().saveData();

        VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + this.versionplugin + "&r &f»&r &cDISABLED!&r");
    }
}

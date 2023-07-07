package core.luuh.aspergescore;

import core.luuh.aspergescore.mysql.db.SetStartingValuesDB;
import core.luuh.aspergescore.health.CustomHealthCommand;
import core.luuh.aspergescore.itemlore.ItemLoreCommand;
import core.luuh.aspergescore.itemlore.LoreCommand;
import core.luuh.aspergescore.itemlore.RenameCommand;
import core.luuh.aspergescore.mobhealth.MobHealthListener;
import core.luuh.aspergescore.pets.PetStandQJEvent;
import core.luuh.aspergescore.scoreboard.SwitchWorldEvent;
import core.luuh.aspergescore.utils.Ticker;
import core.luuh.aspergescore.utils.PSManager;
import core.luuh.aspergescore.utils.SBManager;
import core.luuh.aspergescore.utils.files.MexFileManager;
import core.luuh.aspergescore.utils.files.RCUtils;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import core.luuh.aspergescore.utils.files.SBFileManager;
import java.sql.SQLException;
import core.luuh.verioncore.VerionAPIManager;
import core.luuh.aspergescore.scoreboard.ScoreboardPlayerQJEvent;
import org.bukkit.Bukkit;
import core.luuh.aspergescore.scoreboard.LoadSBCommand;
import core.luuh.aspergescore.mysql.db.Database;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class AspergesCore extends JavaPlugin {
    String versionplugin;
    static AspergesCore instance;
    private Database database;
    private Ticker ticker;

    final SBFileManager sfM = SBFileManager.getInstance();

    final MexFileManager mfM = MexFileManager.getInstance();
    
    public AspergesCore() {
        versionplugin = getDescription().getVersion();
    }
    
    public String getVersionPlugin() {
        return versionplugin;
    }
    
    public static AspergesCore getInstance() {
        return AspergesCore.instance;
    }

    public static String replaceCustomPlaceHolders(String s){

        s = s
                .replaceAll("%version%", getInstance().getVersionPlugin())
                .replaceAll("%prefix%", RCUtils.readString("prefix"))
        ;

        return s;
    }

    private void registerCommands() {
        PluginCommand aspergeScoreCommand = getCommand("aspergescore");
        aspergeScoreCommand.setExecutor(new CoreCommand(this));
        aspergeScoreCommand.setTabCompleter(new CoreCommand(this));

        PluginCommand loadSbCommand = getCommand("loadsb");
        loadSbCommand.setExecutor(new LoadSBCommand(this));
        loadSbCommand.setTabCompleter(new LoadSBCommand(this));

        PluginCommand itemLoreCommand = getCommand("itemlore");
        itemLoreCommand.setExecutor(new ItemLoreCommand(this));
        itemLoreCommand.setTabCompleter(new ItemLoreCommand(this));

        PluginCommand renameCommand = getCommand("rename");
        renameCommand.setExecutor(new RenameCommand(this));
        renameCommand.setTabCompleter(new RenameCommand(this));

        PluginCommand loreCommand = getCommand("lore");
        loreCommand.setExecutor(new LoreCommand(this));
        loreCommand.setTabCompleter(new LoreCommand(this));

        PluginCommand healthCommand = getCommand("health");
        healthCommand.setExecutor(new CustomHealthCommand(this));
        healthCommand.setTabCompleter(new CustomHealthCommand(this));
    }

    private void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new SetStartingValuesDB(this), this);
        pluginManager.registerEvents(new ScoreboardPlayerQJEvent(this), this);
        pluginManager.registerEvents(new SwitchWorldEvent(this), this);
        pluginManager.registerEvents(new PetStandQJEvent(this), this);
        pluginManager.registerEvents(new MobHealthListener(this), this);
    }
    
    public Database getDatabase() {
        return database;
    }

    private void registerDB(){
        try {
            (database = new Database(this)).initializeDatabase();
        }
        catch (SQLException e) {
            VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + versionplugin + "&r &f»&r &cCan't connect to DB: &bplayer_stats&f!&r");
            VerionAPIManager.logConsole("&fFollowing the Stack Trace:");
            e.printStackTrace();
            VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + versionplugin + "&r &f»&r &cUnable to create TABLE: &bplayer_stats&f, into DB: &bplayer_stats&f!&r");
        }
    }
    
    private void registerConfigs() {
        sfM.setup(this);
        mfM.setup(this);
        saveDefaultConfig();
        sfM.getData().options().copyDefaults(true);
        mfM.getMessages().options().copyDefaults(true);
    }
    
    private void registerAll() {
        registerCommands();
        registerEvents();
        registerConfigs();

        registerDB();

        // Ticker
        ticker=new Ticker(this);
        ticker.setEnabled(true);

    }

    private void saveConfigs(){

        SBFileManager.getInstance().saveData();
        MexFileManager.getInstance().saveData();

    }

    private void unRegisterAll(){

        database.closeConnection();
        saveConfigs();
        PSManager.removeAllPS();

    }

    private double petHeight = 0.01;
    private boolean isAscending = true;

    public void onTick(){

        // Pet Height Calculator

        if (isAscending) {
            petHeight += 0.01;
            if (petHeight >= 0.35) {
                isAscending = false;
            }
        } else {
            petHeight -= 0.01;
            if (petHeight <= 0.0) {
                isAscending = true;
            }
        }

        // Players

        for(Player player : Bukkit.getOnlinePlayers()){

            SBManager.registerScoreUpdate(player);
            PSManager.updatePSPosOfPlayer(player, petHeight);

        }
    }



    public void onEnable() {
        instance = this;
        registerAll();

        VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + versionplugin + "&r &f»&r &aENABLED!&r");
    }
    
    public void onDisable() {
        unRegisterAll();

        VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + versionplugin + "&r &f»&r &cDISABLED!&r");
    }
}

package core.luuh.aspergescore;

import core.luuh.aspergescore.db.Database;
import core.luuh.aspergescore.scoreboard.LoadSBCommand;
import core.luuh.aspergescore.scoreboard.ScoreboardPlayerQJEvent;
import core.luuh.aspergescore.utils.scoreboard.SBFilesManager;
import core.luuh.verioncore.VerionAPIManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class AspergesCore extends JavaPlugin {

    String versionplugin = this.getDescription().getVersion();

    public String getVersionPlugin() {

        return versionplugin;

    }

    static AspergesCore instance;

    public static AspergesCore getInstance(){
        return instance;
    }

    private void registerCommands(){

        getCommand("aspergescore").setExecutor(new CoreCommand(this));
        getCommand("aspergescore").setTabCompleter(new CoreCommand(this));

        getCommand("loadsb").setExecutor(new LoadSBCommand(this));
        getCommand("loadsb").setTabCompleter(new LoadSBCommand(this));

    }

    private void registerEvents(){

        // Bukkit.getPluginManager().registerEvents(new SetStartingValuesDB(this), this);
        Bukkit.getPluginManager().registerEvents(new ScoreboardPlayerQJEvent(this), this);

    }

    private Database database;

    public Database getDatabase() {
        return database;
    }

    private void registerDB(){

        try {
            this.database = new Database(this);
            database.initializeDatabase();
        }catch (SQLException e){

            VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + versionplugin + "&r &f»&r &cCan't connect to DB: &bplayer_stats&f!&r");
            VerionAPIManager.logConsole("&fFollowing the Stack Trace:");
            e.printStackTrace();

            VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + versionplugin + "&r &f»&r &cUnable to create TABLE: &bplayer_stats&f, into DB: &bplayer_stats&f!&r");

        }
    }

    private void registerConfigs(){

        SBFilesManager sbF = SBFilesManager.getInstance();
        sbF.setup(this);
        saveDefaultConfig();
        sbF.getData().options().copyDefaults(true);

    }

    private void registerAll(){

        registerCommands();
        registerEvents();

        registerConfigs();


    }

    @Override
    public void onEnable() {
        instance = this;
        registerAll();

        VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + versionplugin + "&r &f»&r &aENABLED!&r");

    }

    @Override
    public void onDisable() {

        // database.closeConnection();
        SBFilesManager.getInstance().saveData();

        VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + versionplugin + "&r &f»&r &cDISABLED!&r");
    }
}

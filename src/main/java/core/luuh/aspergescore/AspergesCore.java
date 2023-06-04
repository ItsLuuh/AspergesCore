package core.luuh.aspergescore;

import core.luuh.aspergescore.db.Database;
import core.luuh.aspergescore.events.SetStartingValuesDB;
import core.luuh.verioncore.VerionAPIManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class AspergesCore extends JavaPlugin {

    String versionplugin = this.getDescription().getVersion();

    public String getVersionPlugin() {

        return versionplugin;

    }

    public void registerCommands(){

        // getCommand("coinflip").setExecutor(new CoinflipCommand(this));
        // getCommand("coinflip").setTabCompleter(new CoinflipCommand(this));

    }

    public void registerEvents(){

        Bukkit.getPluginManager().registerEvents(new SetStartingValuesDB(this), this);

    }

    private Database database;

    public Database getDatabase() {
        return database;
    }

    public void registerDB(){

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

    public void registerAll(){

        registerCommands();
        registerEvents();


    }

    @Override
    public void onEnable() {
        registerAll();

        VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + versionplugin + "&r &f»&r &aENABLED!&r");

    }

    @Override
    public void onDisable() {

        database.closeConnection();

        VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + versionplugin + "&r &f»&r &cDISABLED!&r");
    }
}

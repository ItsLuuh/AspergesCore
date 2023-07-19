package core.luuh.aspergescore;

import core.luuh.aspergescore.attributes.ItemHotBarAttEvent;
import core.luuh.aspergescore.economy.EconomyManagerCommand;
import core.luuh.aspergescore.mainmenu.mainpage.MainPageInventory;
import core.luuh.aspergescore.mysql.db.SetStartingValuesDB;
import core.luuh.aspergescore.health.CustomHealthCommand;
import core.luuh.aspergescore.itemlore.ItemLoreCommand;
import core.luuh.aspergescore.itemlore.LoreCommand;
import core.luuh.aspergescore.itemlore.RenameCommand;
import core.luuh.aspergescore.mobs.nametag.MobHealthListener;
import core.luuh.aspergescore.pets.PetStandQJEvent;
import core.luuh.aspergescore.privateisle.PIsleCommand;
import core.luuh.aspergescore.scoreboard.SwitchWorldEvent;
import core.luuh.aspergescore.utils.PlaceholderAPIHook;
import core.luuh.aspergescore.utils.ticker.TickerSeconds;
import core.luuh.aspergescore.utils.ticker.TickerTicks;
import core.luuh.aspergescore.utils.PSManager;
import core.luuh.aspergescore.utils.SBManager;
import core.luuh.aspergescore.utils.files.GUIFileManager;
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
    private TickerTicks tickerT;
    private TickerSeconds tickerS;
    private double petHeight = 0.01;
    private boolean isAscending = true;

    final SBFileManager sfM = SBFileManager.getInstance();

    final MexFileManager mfM = MexFileManager.getInstance();
    final GUIFileManager gfM = GUIFileManager.getInstance();
    
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

        PluginCommand secretcommands = getCommand("secretcommands");
        secretcommands.setExecutor(new MainPageInventory(this));

        PluginCommand economyManager = getCommand("economy");
        economyManager.setExecutor(new EconomyManagerCommand(this));
        economyManager.setTabCompleter(new EconomyManagerCommand(this));

        PluginCommand pisleCommand = getCommand("is");
        pisleCommand.setExecutor(new PIsleCommand(this));


    }

    private void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new SetStartingValuesDB(this), this);
        pluginManager.registerEvents(new ScoreboardPlayerQJEvent(this), this);
        pluginManager.registerEvents(new SwitchWorldEvent(this), this);
        pluginManager.registerEvents(new PetStandQJEvent(this), this);
        pluginManager.registerEvents(new MobHealthListener(this), this);
        pluginManager.registerEvents(new ItemHotBarAttEvent(this), this);
        pluginManager.registerEvents(new TimeBendSword(this), this);

    }
    
    public Database getDatabase() {
        return database;
    }

    private void registerDB(){
        try {
            (database = new Database()).initializeDatabase();
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
        gfM.setup(this);
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        sfM.getData().options().copyDefaults(true);
        sfM.saveData();
        mfM.getMessages().options().copyDefaults(true);
        mfM.saveData();
        gfM.getData().options().copyDefaults(true);
        mfM.saveData();
    }

    private void registerAll() {
        registerCommands();
        registerEvents();
        registerConfigs();

        registerDB();

        // PAPI
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPIHook(this).register();
        }

        // TickerTicks
        tickerT=new TickerTicks(this);
        tickerT.setEnabled(true);
        // TickerSeconds
        tickerS=new TickerSeconds(this);
        tickerS.setEnabled(true);
    }

    private void saveConfigs(){

        SBFileManager.getInstance().saveData();
        MexFileManager.getInstance().saveData();
        GUIFileManager.getInstance().saveData();

    }

    private void unRegisterAll(){

        database.closeConnection();
        saveConfigs();
        PSManager.removeAllPS();

    }

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

    public void onSecond(){

        TimeBendSword.verifyEntitiesTBend();

        // Players

        for(Player player : Bukkit.getOnlinePlayers()){

            TimeBendSword.sendActionBar(player);

        }

    }



    public void onEnable() {
        instance = this;
        registerAll();

        VerionAPIManager.logConsole("#FF0000  ___   ___________ ___________ _____  _____ _____ ");
        VerionAPIManager.logConsole("#FF0000 / _ \\ /  ___| ___ \\  ___| ___ \\  __ \\|  ___/  ___|");
        VerionAPIManager.logConsole("#FF0000/ /_\\ \\\\ `--.| |_/ / |__ | |_/ / |  \\/| |__ \\ `--. ");
        VerionAPIManager.logConsole("#FF0000|  _  | `--. \\  __/|  __||    /| | __ |  __| `--. \\");
        VerionAPIManager.logConsole("#FF0000| | | |/\\__/ / |   | |___| |\\ \\| |_\\ \\| |___/\\__/ /");
        VerionAPIManager.logConsole("#FF0000\\_| |_/\\____/\\_|   \\____/\\_| \\_|\\____/\\____/\\____/ ");
        VerionAPIManager.logConsole("");
        VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + versionplugin + "&r &f»&r &aENABLED!&r");
    }
    
    public void onDisable() {
        unRegisterAll();

        VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + versionplugin + "&r &f»&r &cDISABLED!&r");
    }
}

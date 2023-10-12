package core.luuh.aspergescore;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import core.luuh.aspergescore.attributes.ItemHotBarAttEvent;
import core.luuh.aspergescore.dungeon.DungeonIstanceManager;
import core.luuh.aspergescore.economy.EconomyManagerCommand;
import core.luuh.aspergescore.mainmenu.MainMenuItemEvent;
import core.luuh.aspergescore.mainmenu.mainpage.MainPageInventory;
import core.luuh.aspergescore.mysql.db.SetStartingValuesDB;
import core.luuh.aspergescore.health.CustomHealthCommand;
import core.luuh.aspergescore.itemlore.ItemLoreCommand;
import core.luuh.aspergescore.itemlore.LoreCommand;
import core.luuh.aspergescore.itemlore.RenameCommand;
import core.luuh.aspergescore.mobs.nametag.MobHealthListener;
import core.luuh.aspergescore.party.PartyCommand;
import core.luuh.aspergescore.party.PartyManager;
import core.luuh.aspergescore.pets.PetStandQJEvent;
import core.luuh.aspergescore.privateisle.PIsleCommand;
import core.luuh.aspergescore.scoreboard.SwitchWorldEvent;
import core.luuh.aspergescore.stats.levels.LevelCommand;
import core.luuh.aspergescore.utils.PlaceholderAPIHook;
import core.luuh.aspergescore.utils.ticker.TickerSeconds;
import core.luuh.aspergescore.utils.ticker.TickerSecondsAsync;
import core.luuh.aspergescore.utils.ticker.TickerTicks;
import core.luuh.aspergescore.utils.PSManager;
import core.luuh.aspergescore.utils.SBManager;
import core.luuh.aspergescore.utils.files.GUIFileManager;
import core.luuh.aspergescore.utils.files.MexFileManager;
import core.luuh.aspergescore.utils.files.RCUtils;
import core.luuh.aspergescore.utils.ticker.TickerTicksAsync;
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
    private TickerTicksAsync tickerTA;
    private TickerSecondsAsync tickerSA;

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

    public static String replacePlayerPHolders(String s, Player p){

        s = s
                .replaceAll("%player%", p.getName())
                .replaceAll("%player_name%", p.getName())
                .replaceAll("%player_uuid%", String.valueOf(p.getUniqueId()))
                .replaceAll("%player_health%", String.valueOf(p.getHealth()))
                .replaceAll("%player_maxhealth%", String.valueOf(p.getMaxHealth()))
                .replaceAll("%player_food%", String.valueOf(p.getFoodLevel()))
                .replaceAll("%player_xp%", String.valueOf(p.getExp()))
                .replaceAll("%player_level%", String.valueOf(p.getLevel()))
                .replaceAll("%player_gamemode%", String.valueOf(p.getGameMode()))
                .replaceAll("%player_world%", String.valueOf(p.getWorld()))
                .replaceAll("%player_location%", String.valueOf(p.getLocation()))
                .replaceAll("%player_location_x%", String.valueOf(p.getLocation().getX()))
                .replaceAll("%player_location_y%", String.valueOf(p.getLocation().getY()))
                .replaceAll("%player_location_z%", String.valueOf(p.getLocation().getZ()))
                .replaceAll("%player_ping%", String.valueOf(p.getPing()))
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

        PluginCommand dungeonCommand = getCommand("dungeon");
        dungeonCommand.setExecutor(new DungeonIstanceManager(this));

        PluginCommand partyCommand = getCommand("party");
        partyCommand.setExecutor(new PartyCommand(this));
        partyCommand.setTabCompleter(new PartyCommand(this));

        PluginCommand levelCommand = getCommand("level");
        levelCommand.setExecutor(new LevelCommand(this));
    }

    private void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new SetStartingValuesDB(this), this);
        pluginManager.registerEvents(new ScoreboardPlayerQJEvent(this), this);
        pluginManager.registerEvents(new SwitchWorldEvent(this), this);
        pluginManager.registerEvents(new PetStandQJEvent(this), this);
        pluginManager.registerEvents(new MobHealthListener(this), this);
        pluginManager.registerEvents(new ItemHotBarAttEvent(this), this);
        pluginManager.registerEvents(new MainMenuItemEvent(this), this);
        pluginManager.registerEvents(new MainPageInventory(this), this);

        // Swords
        pluginManager.registerEvents(new TimeBendSword(this), this);

    }
    
    public Database getDatabase() {
        return database;
    }

    private void registerDB(){
        String usingDB = null;
        if(RCUtils.readString("storage.type").equalsIgnoreCase("mysql"))usingDB="mysql";
        else if(RCUtils.readString("storage.type").equalsIgnoreCase("mongodb"))usingDB="mongodb";
        switch(usingDB) {
            case "mysql":
                try {
                    (database = new Database()).initializeDatabase();
                } catch (SQLException e) {
                    VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + versionplugin + "&r &f»&r &cCan't connect to DB: &bplayer_stats&f!&r");
                    VerionAPIManager.logConsole("&fFollowing the Stack Trace:");
                    e.printStackTrace();
                    VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + versionplugin + "&r &f»&r &cUnable to create TABLE: &bplayer_stats&f, into DB: &bplayer_stats&f!&r");
                }
                break;
            case "mongodb":

                break;
            default:
                VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + versionplugin + "&r &f»&r &cCouldn't connect to database type: &b"+RCUtils.readString("storage.type") + "&c!&r");
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
        // TickerTicksAsync
        tickerTA=new TickerTicksAsync(this);
        tickerTA.setEnabled(true);
        // TickerSeconds
        tickerS=new TickerSeconds(this);
        tickerS.setEnabled(true);
        // TickerSecondsAsync
        tickerSA=new TickerSecondsAsync(this);
        tickerSA.setEnabled(true);
    }

    private void saveConfigs(){

        SBFileManager.getInstance().saveData();
        MexFileManager.getInstance().saveData();
        GUIFileManager.getInstance().saveData();

    }

    private void unRegisterAll(){

        PSManager.removeAllPS();
        database.closeConnection();
        saveConfigs();

    }

    public void onTick(){

        // Players

        for(Player player : Bukkit.getOnlinePlayers()){

            SBManager.registerScoreUpdate(player);

        }
    }

    public void onTickAsync(){

        // Players

        for(Player player : Bukkit.getOnlinePlayers()){

            PSManager.checkArmorStandPosition(player);

        }
    }

    public void onSecond(){

        TimeBendSword.verifyEntitiesTBend();

        // Players

        for(Player player : Bukkit.getOnlinePlayers()){

            TimeBendSword.sendActionBar(player);

        }

    }

    public void onSecondAsync(){

        // Players

        for(Player player : Bukkit.getOnlinePlayers()){

            PartyManager.updateInvitedPlayerData(player);

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

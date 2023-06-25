package core.luuh.aspergescore.utils.scoreboard;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.utils.RCUtils;
import core.luuh.aspergescore.utils.chatcolor;
import core.luuh.verioncore.VerionAPIManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 *
 * @author crisdev333
 * @author ItsLuuh (made small changes)
 *
 */
public class SBManager {

    private static final AspergesCore plugin = AspergesCore.getInstance();

    private static final HashMap<Player, String> scoreName = new HashMap<>();

    // gets the name of the scoreboard of the player using HashMap
    public static String getScoreNameOfPlayerByHM(Player player) {
        return scoreName.get(player);
    }

    // binds the scoreboard name to player
    public static void setScoreNameOfPlayerInHM(Player player, String score) {
        if (RCUtils.readList("scoreboards").contains(score)) {
            scoreName.put(player, score);
        }
    }

    // unbinds the scoreboard name from player
    public static void removeScoreNameOfPlayerInHM(Player player) {
        scoreName.remove(player);
    }

    private static final HashMap<UUID, SBManager> players = new HashMap<>();

    // check if the player has already a scoreboard
    public static boolean hasScore(Player player) {
        return players.containsKey(player.getUniqueId());
    }

    // Method, creates a scoreboard for the player
    public static SBManager createScore(Player player) {
        return new SBManager(player);
    }

    // gets the scoreboard from the player
    public static SBManager getByPlayer(Player player) {
        return players.get(player.getUniqueId());
    }

    // removes a scoreboard from the player
    public static SBManager removeScore(Player player) {
        return players.remove(player.getUniqueId());
    }

    private final Scoreboard scoreboard;
    private final Objective sidebar;

    // Constructor, creates the scoreboard for the player
    private SBManager(Player player) {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        sidebar = scoreboard.registerNewObjective("sidebar", "dummy");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        // Create Teams
        for(int i=1; i<=15; i++) {
            Team team = scoreboard.registerNewTeam("SLOT_" + i);
            team.addEntry(genEntry(i));
        }
        player.setScoreboard(scoreboard);
        players.put(player.getUniqueId(), this);
    }

    // sets the title of the scoreboard, max 32 chars
    public void setTitle(String title) {

        Component component = chatcolor.mm(title);
        sidebar.displayName(component);

    }

    // sets the text of the slot (so what the player will see in the scoreboard)
    /*
     * Notes:
     * Don't go below 1 in the int slot, otherwise it will be seen as null
     *
     */
    public void setSlot(int slot, String text, Player player) {
        Team team = scoreboard.getTeam("SLOT_" + slot);
        String entry = genEntry(slot);
        if(!scoreboard.getEntries().contains(entry)) {
            sidebar.getScore(entry).setScore(slot);
        }

        Component component = chatcolor.mm(chatcolor.papi(player, text));
        team.prefix(component);
        team.suffix();
    }

    // removes the slot
    public void removeSlot(int slot) {
        String entry = genEntry(slot);
        if(scoreboard.getEntries().contains(entry)) {
            scoreboard.resetScores(entry);
        }
    }

    // sets slots from a created list descendent
    public void setSlotsFromList(List<String> list, Player player){
        int i = 15;
        for (String s : list) {

            if(i==0)break;

            setSlot(i, s, player);

            i--;

        }
    }

    // sets slots from a created list ascendent
    /*
    public void setSlotsFromList(List<String> list, Player player) {
        while(list.size()>15) {
            list.remove(list.size()-1);
        }

        int slot = list.size();

        if(slot<15) {
            for(int i=(slot +1); i<=15; i++) {
                removeSlot(i);
            }
        }

        for(String line : list) {
            setSlot(slot, line, player);
            slot--;
        }
    }
     */

    public static void scoreUpdateTask(Player player, String line, Integer slot){

        SBManager sbM = SBManager.getByPlayer(player);
        int i = 0;
        List<String> list = sbM.getTextsLine(player, line);
        if(list.size() > 1){

            String currentText = list.get(i);
            sbM.setSlot(slot, currentText, player);

        } else {

            sbM.setSlot(slot, list.get(0), player);

        }
    }

    // get interval of a line in the scoreboard
    public long getLineInterval(Player player, String line){

        if (SBManager.hasScore(player)) {
            SBFilesManager sbF = SBFilesManager.getInstance();
            String name = SBManager.getScoreNameOfPlayerByHM(player);
            ConfigurationSection sbN = sbF.getData().getConfigurationSection(name);
            ConfigurationSection cs = sbN.getConfigurationSection(line);
            return cs.getLong("interval");

        }

        return 0L;

    }

    // get texts of a line in the scoreboard
    public List<String> getTextsLine(Player player, String line){

        if (SBManager.hasScore(player)) {
            SBFilesManager sbF = SBFilesManager.getInstance();
            String name = SBManager.getScoreNameOfPlayerByHM(player);
            ConfigurationSection sbN = sbF.getData().getConfigurationSection(name);
            ConfigurationSection cs = sbN.getConfigurationSection(line);
            return cs.getStringList("text");

        }

        return null;

    }

    // get lines of a scoreboard
    public @NotNull Set<String> getLines(Player player){

        if (SBManager.hasScore(player)) {
            SBFilesManager sbF = SBFilesManager.getInstance();
            String name = SBManager.getScoreNameOfPlayerByHM(player);
            ConfigurationSection sbN = sbF.getData().getConfigurationSection(name);

            Set<String> list = sbN.getKeys(false);
            list.remove("title");

            return list;
        }

        return null;

    }

    // update score Line
    public void updateScoreLine(Player player, String line, Integer slot) {

        SBManager sbM = SBManager.getByPlayer(player);

        List<String> list = sbM.getTextsLine(player, line);
        if(list.size() > 1) {
            for (String s : list) {

                sbM.setSlot(slot, s, player);

            }
        } else {
            sbM.setSlot(slot, list.get(0), player);
        }

    }

    private String genEntry(int slot) {
        return ChatColor.values()[slot].toString();
    }

    private String getFirstSplit(String s) {
        return s.length()>64 ? s.substring(0, 64) : s;
    }

    private String getSecondSplit(String s) {
        if(s.length()>128) {
            s = s.substring(0, 128);
        }
        return s.length()>64 ? s.substring(64) : "";
    }

    // gets the scoreboard from list of scoreboards and if it exists, it creates the scoreboard for the player
    public static void createSBFromName(Player player, String name){

        List<String> list = RCUtils.readList("scoreboards");
        if(list.contains(name)){

            internalSBName(player, name);

        } else {
            VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&r &cDidn't find any scoreboard with such name. &7(/loadsb [SCOREBOARD-NAME] [PLAYER])&r");
        }

    }

    public static void createSBFromName(Player player, World world){

        internalSBName(player, world.getName());

    }

    private static void internalSBName(Player player, String s){

        // creates the scoreboard
        SBManager sb = SBManager.createScore(player);

        // gets the instance of SBFilesManager
        SBFilesManager sbF = SBFilesManager.getInstance();

        // gets the ConfigurationSection with the name of the scoreboard
        ConfigurationSection sbN = sbF.getData().getConfigurationSection(s);
        if(sbN != null) {
            String title = sbN.getStringList("title.text").get(0);

            // gets the lines, and then it adds them to the List<String> lines
            List<String> lines = new ArrayList<>();
            // sbN.getKeys(false) is used to get every line, since they are ConfigurationSection and not strings... they require this iteration
            for (String ss : sbN.getKeys(false)) {

                if (!ss.equalsIgnoreCase("title")) {
                    // gets the ConfigurationSection from the name of the lines using sbN
                    ConfigurationSection cs = sbN.getConfigurationSection(ss);
                    lines.add(cs.getStringList("text").get(0));
                }

            }


            // sets the title of the scoreboard
            sb.setTitle(title);

            // add the list "lines" to the scoreboard
            sb.setSlotsFromList(lines, player);

            // binds player to scoreboard name
            SBManager.setScoreNameOfPlayerInHM(player, s);
        } else {
            VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&r &cYou need to set valid arguments! &7(/loadsb [SCOREBOARD-NAME] [PLAYER])&r");
        }

    }


}

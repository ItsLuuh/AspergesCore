package core.luuh.aspergescore.utils.scoreboard;

import core.luuh.aspergescore.utils.chatcolor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author crisdev333
 * @author ItsLuuh (made small changes)
 *
 */
public class SBManager {

    private static HashMap<UUID, SBManager> players = new HashMap<>();

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

    private Scoreboard scoreboard;
    private Objective sidebar;

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
        title = chatcolor.col(title);
        sidebar.setDisplayName(title.length()>32 ? title.substring(0, 32) : title);
    }

    // sets the text of the slot (so what the player will see in the scoreboard)
    /*
     * Notes:
     * Don't go below 1 in the int slot, otherwise it will be seen as null
     *
     */
    public void setSlot(int slot, String text) {
        Team team = scoreboard.getTeam("SLOT_" + slot);
        String entry = genEntry(slot);
        if(!scoreboard.getEntries().contains(entry)) {
            sidebar.getScore(entry).setScore(slot);
        }

        text = chatcolor.col(text);
        String pre = getFirstSplit(text);
        String suf = getFirstSplit(ChatColor.getLastColors(pre) + getSecondSplit(text));
        team.setPrefix(pre);
        team.setSuffix(suf);
    }

    // removes the slot
    public void removeSlot(int slot) {
        String entry = genEntry(slot);
        if(scoreboard.getEntries().contains(entry)) {
            scoreboard.resetScores(entry);
        }
    }

    // sets slots from a created list
    public void setSlotsFromList(List<String> list) {
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
            setSlot(slot, line);
            slot--;
        }
    }

    private String genEntry(int slot) {
        return ChatColor.values()[slot].toString();
    }

    private String getFirstSplit(String s) {
        return s.length()>16 ? s.substring(0, 16) : s;
    }

    private String getSecondSplit(String s) {
        if(s.length()>32) {
            s = s.substring(0, 32);
        }
        return s.length()>16 ? s.substring(16) : "";
    }


}

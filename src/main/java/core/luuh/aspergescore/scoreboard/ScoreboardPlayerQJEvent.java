package core.luuh.aspergescore.scoreboard;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.utils.scoreboard.SBManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ScoreboardPlayerQJEvent implements Listener {

    // This class makes the scoreboard set/toggle on player join/quit

    private final AspergesCore plugin;

    public ScoreboardPlayerQJEvent(AspergesCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        Player player = e.getPlayer();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "loadsb default-scoreboard " + player.getName());


    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){

        Player player = e.getPlayer();
        if(SBManager.hasScore(player)){
            SBManager.removeScore(player);
        }
        if (SBManager.getScoreNameOfPlayerByHM(player) != null) {
            SBManager.removeScoreNameOfPlayerInHM(player);
        }

    }

}

package core.luuh.aspergescore.mysql.db;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.mysql.model.PlayerStats;
import core.luuh.verioncore.VerionAPIManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.util.Date;

public class SetStartingValuesDB implements Listener {

    private final AspergesCore plugin;

    public SetStartingValuesDB(AspergesCore plugin) {this.plugin = plugin;}


    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e){

        Player p = e.getPlayer();

        try {

            PlayerStats stats = getPlayerStatsFromDatabase(p);
            stats.setLastLogin(new Date());
            stats.setLastLogout(new Date());

            plugin.getDatabase().updatePlayerStats(stats);

        } catch (SQLException exception){

            VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&r &cCan't update stats of PLAYER: &b" + p.getName() + " &fto DB: &bplayers_stats&f!&r");
            VerionAPIManager.logConsole("&fFollowing the Stack Trace:");
            exception.printStackTrace();
        }

    }

    private PlayerStats getPlayerStatsFromDatabase(Player p) throws SQLException{

        PlayerStats stats = plugin.getDatabase().findPlayerStatsByUUID(p.getUniqueId().toString());

        if (stats == null) {

            stats = new PlayerStats(p.getUniqueId().toString(), 0, 0, 0, new Date(), new Date());

            plugin.getDatabase().setPlayerStats(stats);

            return stats;

        }

        return stats;

    }

}

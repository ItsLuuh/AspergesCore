package core.luuh.aspergescore.mysql.db;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.mysql.PUUID;
import core.luuh.aspergescore.mysql.model.Profile;
import core.luuh.aspergescore.mysql.model.User;
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
    public void onJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        try {
            if(Database.getUserFromDatabase(p) == null) {

                String puuid = PUUID.generatePUUID().getValue();

                User user = new User(p.getUniqueId().toString(), puuid, p.getName(), 0, new Date(), new Date());
                plugin.getDatabase().setUsers(user);

                Profile profile = new Profile(p.getUniqueId().toString(), p.getName(), puuid, "christianity", 0, 0L, 0L, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, 0, 0, 0, 0, 0, 0, 0, 0);
                plugin.getDatabase().setProfiles(profile);

            }

        } catch (SQLException exception) {
            VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&r &cCan't update stats of PLAYER: &b" + p.getName() + " &fto DB: &busers/profiles&f!&r");
            VerionAPIManager.logConsole("&fFollowing the Stack Trace:");
            exception.printStackTrace();
        }
    }

}

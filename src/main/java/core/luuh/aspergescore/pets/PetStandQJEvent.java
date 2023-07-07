package core.luuh.aspergescore.pets;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.utils.PSManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PetStandQJEvent implements Listener {

    private final AspergesCore plugin;

    public PetStandQJEvent(AspergesCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        PSManager.createPSofPlayer(e.getPlayer());

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){

        PSManager.unboundPSfromPlayer(e.getPlayer());

    }

}

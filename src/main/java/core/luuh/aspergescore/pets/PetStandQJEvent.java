package core.luuh.aspergescore.pets;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.utils.PSManager;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

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
    public void onPlayerRespawn(PlayerPostRespawnEvent e){

        PSManager.createPSofPlayer(e.getPlayer());

    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e){

        ArmorStand stand = PSManager.getPSbyPlayer(e.getPlayer());
        stand.remove();
        PSManager.unboundPSfromPlayer(e.getPlayer());
        PSManager.createPSofPlayer(e.getPlayer());

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){

        PSManager.unboundPSfromPlayer(e.getPlayer());

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){

        PSManager.unboundPSfromPlayer(e.getPlayer());

    }

}

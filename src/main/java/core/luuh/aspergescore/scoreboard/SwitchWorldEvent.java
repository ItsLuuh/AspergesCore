package core.luuh.aspergescore.scoreboard;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.utils.RSUtils;
import core.luuh.aspergescore.utils.scoreboard.SBManager;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SwitchWorldEvent implements Listener {

    private final AspergesCore plugin;

    public SwitchWorldEvent(AspergesCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void switchWorldScoreboardUpdate(PlayerChangedWorldEvent e){

        World world = e.getFrom();
        Player player = e.getPlayer();
        List<ConfigurationSection> csList = RSUtils.readCS("per-world");

        if(csList.contains(world.getName())){

            SBManager.removeScoreNameOfPlayerInHM(player);
            SBManager.removeScore(player);

            SBManager.createSBFromName(player, world);

        }

    }

}

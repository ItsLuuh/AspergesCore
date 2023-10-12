package core.luuh.aspergescore.privateisle;

import com.infernalsuite.aswm.api.SlimePlugin;
import com.infernalsuite.aswm.api.exceptions.*;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import com.infernalsuite.aswm.api.world.properties.SlimeProperties;
import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap;
import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.mysql.db.Database;
import core.luuh.aspergescore.mysql.model.Profile;
import core.luuh.aspergescore.utils.chatcolor;
import core.luuh.aspergescore.utils.files.CEM;
import core.luuh.aspergescore.utils.files.MTUtils;
import core.luuh.verioncore.VerionAPIManager;
import io.papermc.paper.event.world.border.WorldBorderCenterChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class PIsleCommand implements CommandExecutor {

    private final AspergesCore plugin;

    public PIsleCommand(AspergesCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {

            VerionAPIManager.logConsole(MTUtils.caseErrorMex(CEM.ERROR_CONSOLE_EXECUTE));

        } else {
            if (args.length == 0) {

                Player player = (Player) sender;
                SlimeWorld world = null;
                try {
                    world = createIs(player);
                } catch (IOException | UnknownWorldException | WorldAlreadyExistsException | WorldLockedException |
                         CorruptedWorldException | NewerFormatException e) {
                    e.printStackTrace();
                    player.sendMessage(chatcolor.col("not working, watch the errors in the console."));
                }

                if (world != null) {
                    Location loc = new Location(Bukkit.getWorld(world.getName()), 0.5, 100, 0.5);
                    player.teleportAsync(loc);
                    player.sendMessage(chatcolor.col(MTUtils.readString("island-teleporting-to")));
                }

            }

        }

        return true;
    }

    private SlimeWorld createIs(Player player) throws IOException, UnknownWorldException, WorldAlreadyExistsException, WorldLockedException, CorruptedWorldException, NewerFormatException {

        Profile profile = Database.getPlayerProfileFromPName(player, "current");
        String worldName = "PrivateIs-" + player.getName() + "-" + profile.getPuuid();
        final SlimePlugin slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        final SlimeLoader sLoader = slimePlugin.getLoader("file");
        SlimeWorld slimeWorld;

        if(sLoader.worldExists(worldName)) {
            if (Bukkit.getWorld(worldName) != null) {
                // the world is already on the server, just get the world
                slimeWorld = slimePlugin.getWorld(worldName);
            } else {
                // the world already exists in the data source
                // we need to load the world and generate it
                slimeWorld = slimePlugin.loadWorld(sLoader, worldName, false, new SlimePropertyMap());
                slimePlugin.loadWorld(slimeWorld);
            }
        } else {
            // the world does not exist in the data source
            // we have to create it
            slimeWorld = slimePlugin.loadWorld(slimePlugin.getWorld("PrivateIs").clone(worldName, sLoader));
        }
        return slimeWorld;
    }
}

package core.luuh.aspergescore.scoreboard;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.utils.RCUtils;
import core.luuh.aspergescore.utils.scoreboard.SBFilesManager;
import core.luuh.aspergescore.utils.scoreboard.SBManager;
import core.luuh.verioncore.VerionAPIManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LoadSBCommand implements CommandExecutor, TabCompleter {

    private final AspergesCore plugin;

    public LoadSBCommand(AspergesCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)){

            if(args.length == 0 || args.length == 1) {

                VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&r &cYou need to set valid arguments! &7(/loadsb [SCOREBOARD-NAME] [PLAYER])&r");

            } else if(args.length == 2){

                if(RCUtils.readList("scoreboards").contains(args[0])) {
                    if (Bukkit.getPlayer(args[1]) != null) {

                        Player player = Bukkit.getPlayer(args[1]);

                        if(SBManager.hasScore(player)){
                            SBManager.removeScore(player);
                        }

                        SBManager.createSBFromName(player, args[0]);

                    } else {
                        VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&r &cYou need to set a valid second argument! &7(OFFLINE_PLAYER)&r");
                    }
                } else {
                    VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&r &cYou need to set a valid first argument! &7(INVALID-SCOREBOARD)&r");
                }
            }

        } else {


            Player player = (Player) sender;

            if(player.hasPermission("asperges.loadsb")) {

                if(args.length == 0){

                    VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&r &cYou need to set valid arguments! &7(/loadsb [SCOREBOARD-NAME] [PLAYER])&r");

                } else if(args.length == 1) {
                    if (RCUtils.readList("scoreboards").contains(args[0])) {

                        if (SBManager.hasScore(player)) {
                            SBManager.removeScore(player);
                        }

                        SBManager.createSBFromName(player, args[0]);

                    } else {
                        VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&r &cYou need to set a valid first argument! &7(INVALID-SCOREBOARD)&r");
                    }
                } else if (args.length == 2) {

                    if (RCUtils.readList("scoreboards").contains(args[0])) {
                        if (Bukkit.getPlayer(args[1]) != null) {

                            if (SBManager.hasScore(player)) {
                                SBManager.removeScore(player);
                            }

                            SBManager.createSBFromName(Bukkit.getPlayer(args[1]), args[0]);

                        } else {
                            VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&r &cYou need to set a valid second argument! &7(OFFLINE_PLAYER)&r");
                        }

                    } else {
                        VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&r &cYou need to set a valid first argument! &7(INVALID-SCOREBOARD)&r");
                    }
                }
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){

        if(args.length == 1){

            return RCUtils.readList("scoreboards");

        } else if(args.length == 2){

            List<String> arguments = new ArrayList<>();
            for( Player player : plugin.getServer().getOnlinePlayers()) {
                arguments.add(player.getDisplayName());
            }
            return arguments;

        }

        return null;

    }
}

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

public class LoadSBCommand implements CommandExecutor, TabCompleter {

    private final AspergesCore plugin;

    public LoadSBCommand(AspergesCore plugin) {
        this.plugin = plugin;
    }

    // gets the scoreboard from list of scoreboards and if it exists, it creates the scoreboard for the player
    private void createSBFromName(Player player, String name){

        List<String> list = RCUtils.readList("scoreboards");
        if(list.contains(name)){
            // creates the scoreboard
            SBManager sb = SBManager.createScore(player);

            // gets the instance of SBFilesManager
            SBFilesManager sbF = SBFilesManager.getInstance();

            // gets the ConfigurationSection with the name of the scoreboard
            ConfigurationSection sbN = sbF.getData().getConfigurationSection(name);
            if(sbN != null) {
                String title = sbN.getStringList("title.text").get(0);

                // gets the lines, and then it adds them to the List<String> lines
                List<String> lines = new ArrayList<>();
                // sbN.getKeys(false) is used to get every line, since they are ConfigurationSection and not strings... they require this iteration
                for (String s : sbN.getKeys(false)) {

                    if (!s.equalsIgnoreCase("title")) {
                        // gets the ConfigurationSection from the name of the lines using sbN
                        ConfigurationSection cs = sbN.getConfigurationSection(s);
                        lines.add(cs.getStringList("text").get(0));
                    }

                }


                // sets the title of the scoreboard
                sb.setTitle(title);

                // add the list "lines" to the scoreboard
                sb.setSlotsFromList(lines, player);

                // binds player to scoreboard name
                SBManager.setScoreNameOfPlayerInHM(player, name);
            } else {
                VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&r &cYou need to set valid arguments! &7(/loadsb [SCOREBOARD-NAME] [PLAYER])&r");
            }
        } else {
            VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&r &cDidn't find any scoreboard with such name. &7(/loadsb [SCOREBOARD-NAME] [PLAYER])&r");
        }

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

                        createSBFromName(player, args[0]);

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

                        createSBFromName(player, args[0]);

                    } else {
                        VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&r &cYou need to set a valid first argument! &7(INVALID-SCOREBOARD)&r");
                    }
                } else if (args.length == 2) {

                    if (RCUtils.readList("scoreboards").contains(args[0])) {
                        if (Bukkit.getPlayer(args[1]) != null) {

                            if (SBManager.hasScore(player)) {
                                SBManager.removeScore(player);
                            }

                            createSBFromName(Bukkit.getPlayer(args[1]), args[0]);

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

package core.luuh.aspergescore;

import core.luuh.aspergescore.utils.chatcolor;
import core.luuh.aspergescore.utils.scoreboard.SBFilesManager;
import core.luuh.verioncore.VerionAPIManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CoreCommand implements CommandExecutor, TabCompleter {

    private final AspergesCore plugin;

    public CoreCommand(AspergesCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)){

            if(args.length == 0) {

                VerionAPIManager.logConsole("");
                VerionAPIManager.logConsole("&6[&e!&6] &lASPERGES&r &fCore &ov" + plugin.getVersionPlugin() + " &6[&e!&6]");
                VerionAPIManager.logConsole("&fBy ItsLuuh.");
                VerionAPIManager.logConsole("");

            } else if(args.length == 1) {

                SBFilesManager.getInstance().reloadData();
                plugin.reloadConfig();
                VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&a All configs reloaded successfully!&r");

            } else if(args.length == 2) {

                if(args[1].equalsIgnoreCase("all")){

                    SBFilesManager.getInstance().reloadData();
                    plugin.reloadConfig();
                    VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&a All configs reloaded successfully!&r");

                } else if(args[1].equalsIgnoreCase("scoreboards")) {

                    SBFilesManager.getInstance().reloadData();
                    plugin.reloadConfig();
                    VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&a Scoreboard Config reloaded successfully!&r");

                } else if(args[1].equalsIgnoreCase("config")){

                    SBFilesManager.getInstance().reloadData();
                    plugin.reloadConfig();
                    VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&a Main Config reloaded successfully!&r");

                }

            }

        } else {

            Player player = (Player) sender;

            if(player.hasPermission("asperges.core")) {

                if(args.length == 0) {

                    player.sendMessage(chatcolor.col(""));
                    player.sendMessage(chatcolor.col("&6[&e!&6] &lASPERGES&r &fCore &ov" + plugin.getVersionPlugin() + " &6[&e!&6]"));
                    player.sendMessage(chatcolor.col("&fBy ItsLuuh."));
                    player.sendMessage(chatcolor.col(""));

                } else if(args.length == 1) {

                    SBFilesManager.getInstance().reloadData();
                    plugin.reloadConfig();
                    player.sendMessage(chatcolor.col("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&a All configs reloaded successfully!&r"));

                } else if(args.length == 2) {

                    if(args[1].equalsIgnoreCase("all")){

                        SBFilesManager.getInstance().reloadData();
                        plugin.reloadConfig();
                        player.sendMessage(chatcolor.col("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&a All configs reloaded successfully!&r"));

                    } else if(args[1].equalsIgnoreCase("scoreboards")) {

                        SBFilesManager.getInstance().reloadData();
                        plugin.reloadConfig();
                        player.sendMessage(chatcolor.col("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&a Scoreboard Config reloaded successfully!&r"));

                    } else if(args[1].equalsIgnoreCase("config")){

                        SBFilesManager.getInstance().reloadData();
                        plugin.reloadConfig();
                        player.sendMessage(chatcolor.col("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&a Main Config reloaded successfully!&r"));

                    }

                }

            }

        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(args.length == 1) {

            List<String> arguments = new ArrayList<>();
            arguments.add("reload");
            return arguments;

        } else if (args.length == 2) {

            List<String> arguments = new ArrayList<>();
            arguments.add("all");
            arguments.add("config");
            arguments.add("scoreboards");
            return arguments;

        }

        return null;
    }
}

package core.luuh.aspergescore;

import core.luuh.aspergescore.utils.chatcolor;
import core.luuh.aspergescore.utils.files.GUIFileManager;
import core.luuh.aspergescore.utils.files.MTUtils;
import core.luuh.aspergescore.utils.files.MexFileManager;
import core.luuh.aspergescore.utils.files.SBFileManager;
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

                for(String s : MTUtils.readList("core-default-mex")){

                    VerionAPIManager.logConsole(AspergesCore.replaceCustomPlaceHolders(s));

                }

            } else if(args.length == 1) {

                SBFileManager.getInstance().reloadData();
                MexFileManager.getInstance().reloadData();
                GUIFileManager.getInstance().reloadData();
                plugin.reloadConfig();
                VerionAPIManager.logConsole(MTUtils.readString("core-reload-all"));

            } else if(args.length == 2) {

                if(args[1].equalsIgnoreCase("all")){

                    SBFileManager.getInstance().reloadData();
                    MexFileManager.getInstance().reloadData();
                    GUIFileManager.getInstance().reloadData();
                    plugin.reloadConfig();
                    VerionAPIManager.logConsole(MTUtils.readString("core-reload-all"));

                } else if(args[1].equalsIgnoreCase("config")){

                    plugin.reloadConfig();
                    VerionAPIManager.logConsole(MTUtils.readString("core-reload-config"));

                } else if(args[1].equalsIgnoreCase("scoreboards")) {

                    SBFileManager.getInstance().reloadData();
                    VerionAPIManager.logConsole(MTUtils.readString("core-reload-scoreboards"));

                } else if(args[1].equalsIgnoreCase("messages")){

                    MexFileManager.getInstance().reloadData();
                    VerionAPIManager.logConsole(MTUtils.readString("core-reload-messages"));

                } else if(args[1].equalsIgnoreCase("guis")){

                    GUIFileManager.getInstance().reloadData();
                    VerionAPIManager.logConsole(MTUtils.readString("core-reload-guis"));

                }

            }

        } else {

            Player player = (Player) sender;

            if(player.hasPermission("asperges.core")) {

                if(args.length == 0) {

                    for(String s : MTUtils.readList("core-default-mex")){

                        player.sendMessage(chatcolor.col(AspergesCore.replaceCustomPlaceHolders(s)));

                    }

                } else if(args.length == 1) {

                    MexFileManager.getInstance().reloadData();
                    SBFileManager.getInstance().reloadData();
                    GUIFileManager.getInstance().reloadData();
                    plugin.reloadConfig();
                    player.sendMessage(chatcolor.col(MTUtils.readString("core-reload-all")));

                } else if(args.length == 2) {

                    if(args[1].equalsIgnoreCase("all")){

                        SBFileManager.getInstance().reloadData();
                        MexFileManager.getInstance().reloadData();
                        GUIFileManager.getInstance().reloadData();
                        plugin.reloadConfig();
                        player.sendMessage(chatcolor.col(MTUtils.readString("core-reload-all")));

                    } else if(args[1].equalsIgnoreCase("config")){

                        plugin.reloadConfig();
                        player.sendMessage(chatcolor.col(MTUtils.readString("core-reload-config")));

                    } else if(args[1].equalsIgnoreCase("scoreboards")) {

                        SBFileManager.getInstance().reloadData();
                        player.sendMessage(chatcolor.col(MTUtils.readString("core-reload-scoreboards")));

                    } else if(args[1].equalsIgnoreCase("messages")){

                        MexFileManager.getInstance().reloadData();
                        player.sendMessage(chatcolor.col(MTUtils.readString("core-reload-messages")));

                    } else if(args[1].equalsIgnoreCase("guis")){

                        GUIFileManager.getInstance().reloadData();
                        player.sendMessage(chatcolor.col(MTUtils.readString("core-reload-guis")));

                    }

                }

            }

        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        List<String> arguments = new ArrayList<>();
        if(args.length == 1) {

            arguments.add("reload");

        } else if (args.length == 2) {

            arguments.add("all");
            arguments.add("config");
            arguments.add("scoreboards");
            arguments.add("messages");
            arguments.add("guis");

        }

        return arguments;
    }
}

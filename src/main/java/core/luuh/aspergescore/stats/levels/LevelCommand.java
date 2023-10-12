package core.luuh.aspergescore.stats.levels;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.mysql.db.Database;
import core.luuh.aspergescore.mysql.model.Profile;
import core.luuh.aspergescore.utils.NumberFormatter;
import core.luuh.aspergescore.utils.chatcolor;
import core.luuh.aspergescore.utils.files.CEM;
import core.luuh.aspergescore.utils.files.MTUtils;
import core.luuh.verioncore.VerionAPIManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class LevelCommand implements CommandExecutor {

    private final AspergesCore plugin;

    public LevelCommand(AspergesCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)){

            // level set <player> <level>
            // level add <player> <level>
            // level remove <player> <level>
            // level reset <player>

            if(args.length == 0 || args.length == 1) {

                VerionAPIManager.logConsole(chatcolor.col(MTUtils.caseErrorMex(CEM.NOT_ENOUGH_ARGS)));

            } else if(args.length == 2) {

                Player target = Bukkit.getPlayerExact(args[1]);
                Profile profile = Database.getPlayerProfileFromPUUID(target);

                if (args[0].equalsIgnoreCase("reset")) {

                    profile.setLvl(1);
                    try {
                        plugin.getDatabase().updateProfile(profile);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    VerionAPIManager.logConsole(MTUtils.readString("level-reset-console"));

                }

            } else if(args.length == 3){

                Player target = Bukkit.getPlayerExact(args[1]);
                Profile profile = Database.getPlayerProfileFromPUUID(target);

                if (args[0].equalsIgnoreCase("set")) {

                    profile.setLvl(Integer.parseInt(args[2]));
                    try {
                        plugin.getDatabase().updateProfile(profile);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    VerionAPIManager.logConsole(chatcolor.col(MTUtils.readString("level-set-console")));
                    target.sendMessage(chatcolor.col(MTUtils.readString("level-set-byothers")), target.getName(), "console");

                } else if (args[0].equalsIgnoreCase("add")) {

                    profile.setLvl(profile.getLvl() + Integer.parseInt(args[2]));
                    try {
                        plugin.getDatabase().updateProfile(profile);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    VerionAPIManager.logConsole(chatcolor.col(MTUtils.readString("level-add-console")));
                    target.sendMessage(chatcolor.col(MTUtils.readString("level-add-byothers")), target.getName(), "console");

                } else if (args[0].equalsIgnoreCase("remove")) {

                    profile.setLvl(profile.getLvl() - Integer.parseInt(args[2]));
                    if(profile.getLvl() < 1) profile.setLvl(1);
                    try {
                        plugin.getDatabase().updateProfile(profile);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    VerionAPIManager.logConsole(chatcolor.col(MTUtils.readString("level-remove-console")));
                    target.sendMessage(chatcolor.col(MTUtils.readString("level-remove-byothers")), target.getName(), "console");

                } else {

                    VerionAPIManager.logConsole(chatcolor.col(MTUtils.caseErrorMex(CEM.INVALID_ARGS)));

                }
            } else {

                VerionAPIManager.logConsole(chatcolor.col(MTUtils.caseErrorMex(CEM.INVALID_PLAYER)));

            }

        } else {

            Player player = (Player) sender;
            if(args.length == 0){

                // gui

            } else if(args.length > 1){

                if(player.hasPermission("*")){

                    // level set <player> <level>
                    // level add <player> <level>
                    // level remove <player> <level>
                    // level reset <player>

                    if(args.length == 2) {

                        if(NumberFormatter.isInteger(args[1])) {

                            Profile profile = Database.getPlayerProfileFromPUUID(player);

                            if (args[0].equalsIgnoreCase("set")) {

                                profile.setLvl(Integer.parseInt(args[1]));
                                try {
                                    plugin.getDatabase().updateProfile(profile);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                player.sendMessage(chatcolor.col(MTUtils.readString("level-set-yourself")));

                            } else if (args[0].equalsIgnoreCase("add")) {

                                profile.setLvl(profile.getLvl() + Integer.parseInt(args[1]));
                                try {
                                    plugin.getDatabase().updateProfile(profile);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                player.sendMessage(chatcolor.col(MTUtils.readString("level-add-yourself")));

                            } else if (args[0].equalsIgnoreCase("remove")) {

                                profile.setLvl(profile.getLvl() - Integer.parseInt(args[1]));
                                if(profile.getLvl() < 1) profile.setLvl(1);
                                try {
                                    plugin.getDatabase().updateProfile(profile);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                player.sendMessage(chatcolor.col(MTUtils.readString("level-remove-yourself")));

                            } else if (args[0].equalsIgnoreCase("reset")) {

                                profile.setLvl(1);
                                try {
                                    plugin.getDatabase().updateProfile(profile);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                player.sendMessage(chatcolor.col(MTUtils.readString("level-reset-yourself")));

                            } else {

                                player.sendMessage(chatcolor.col(MTUtils.caseErrorMex(CEM.INVALID_ARGS)));

                            }
                        } else {

                            player.sendMessage(chatcolor.col(MTUtils.caseErrorMex(CEM.INVALID_ARG)));

                        }

                    } else if(args.length == 3){

                        if(Bukkit.getPlayerExact(args[1]) != null) {

                            Player target = Bukkit.getPlayerExact(args[1]);
                            Profile profile = Database.getPlayerProfileFromPUUID(target);

                            if (args[0].equalsIgnoreCase("set")) {

                                profile.setLvl(Integer.parseInt(args[2]));
                                try {
                                    plugin.getDatabase().updateProfile(profile);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                player.sendMessage(chatcolor.col(MTUtils.readString("level-set-other")), player.getName(), target.getName());
                                target.sendMessage(chatcolor.col(MTUtils.readString("level-set-byothers")), target.getName(), player.getName());

                            } else if (args[0].equalsIgnoreCase("add")) {

                                profile.setLvl(profile.getLvl() + Integer.parseInt(args[2]));
                                try {
                                    plugin.getDatabase().updateProfile(profile);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                player.sendMessage(chatcolor.col(MTUtils.readString("level-add-other")), player.getName(), target.getName());
                                target.sendMessage(chatcolor.col(MTUtils.readString("level-add-byothers")), target.getName(), player.getName());

                            } else if (args[0].equalsIgnoreCase("remove")) {

                                profile.setLvl(profile.getLvl() - Integer.parseInt(args[2]));
                                if(profile.getLvl() < 1) profile.setLvl(1);
                                try {
                                    plugin.getDatabase().updateProfile(profile);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                player.sendMessage(chatcolor.col(MTUtils.readString("level-remove-other")), player.getName(), target.getName());
                                target.sendMessage(chatcolor.col(MTUtils.readString("level-remove-byothers")), target.getName(), player.getName());

                            } else {

                                player.sendMessage(chatcolor.col(MTUtils.caseErrorMex(CEM.INVALID_ARGS)));

                            }
                        } else {

                            player.sendMessage(chatcolor.col(MTUtils.caseErrorMex(CEM.INVALID_PLAYER)));

                        }

                    } else {

                        player.sendMessage(chatcolor.col(MTUtils.caseErrorMex(CEM.NOT_ENOUGH_ARGS)));
                    }

                }

            }

        }

        return true;
    }

}

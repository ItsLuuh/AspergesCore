package core.luuh.aspergescore.economy;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.mysql.db.Database;
import core.luuh.aspergescore.mysql.model.Profile;
import core.luuh.aspergescore.utils.NumberFormatter;
import core.luuh.aspergescore.utils.chatcolor;
import core.luuh.aspergescore.utils.files.MTUtils;
import core.luuh.verioncore.VerionAPIManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EconomyManagerCommand implements CommandExecutor, TabCompleter {

    private final AspergesCore plugin;

    public EconomyManagerCommand(AspergesCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)){

            VerionAPIManager.logConsole(MTUtils.caseErrorMex("error-console-execute"));

        } else {

            // eco set ItsLuuh 100
            // eco set 100
            // eco add ItsLuuh 100
            // eco add 100
            // eco remove ItsLuuh 100
            // eco remove 100
            // eco get ItsLuuh
            // eco get

            Player player = (Player) sender;

            if(player.hasPermission("*")) {

                Profile profile = Database.getPlayerProfileFromPUUID(player);
                long bal = profile.getPolusBalance();
                String sBal = Long.toString(bal);

                if(args.length == 0){

                    player.sendMessage(chatcolor.col(MTUtils.caseErrorMex("not-enough-args")));

                }else if (args.length == 1) {

                    if (args[0].equalsIgnoreCase("get")) {

                        player.sendMessage(chatcolor.col(MTUtils.readString("economy-get-yours", sBal)));

                    } else {

                        player.sendMessage(chatcolor.col(MTUtils.caseErrorMex("invalid-arg")));

                    }

                } else if (args.length == 2){
                    Long modifier = Long.parseLong(args[1]);
                    if(args[0].equalsIgnoreCase("get")) {
                        if (Bukkit.getPlayer(args[1]) != null) {
                            Player target = Bukkit.getPlayer(args[1]);
                            Profile targetProfile = Database.getPlayerProfileFromPUUID(target);
                            player.sendMessage(chatcolor.col(MTUtils.readString("economy-get-others", String.valueOf(targetProfile.getPolusBalance()), target.getName())));
                        }
                    } else if(args[0].equalsIgnoreCase("remove")) {
                        if(NumberFormatter.isLong(args[1])) {
                            bal = profile.getPolusBalance() - modifier;
                            player.sendMessage(chatcolor.col(MTUtils.readString("economy-remove-yours", NumberFormatter.formatNumberCommas(modifier))));
                        }
                    } else if(args[0].equalsIgnoreCase("add")) {
                        if(NumberFormatter.isLong(args[1])) {
                            bal = profile.getPolusBalance() + modifier;
                            player.sendMessage(chatcolor.col(MTUtils.readString("economy-add-yours", NumberFormatter.formatNumberCommas(modifier))));
                        }
                    } else if(args[0].equalsIgnoreCase("set")) {
                        if(NumberFormatter.isLong(args[1])) {
                            bal = modifier;
                            player.sendMessage(chatcolor.col(MTUtils.readString("economy-set-yours", NumberFormatter.formatNumberCommas(modifier))));
                        }
                    } else {
                        player.sendMessage(chatcolor.col(MTUtils.caseErrorMex("invalid-arg")));
                    }

                    try {
                        profile.setPolusBalance(bal);
                        plugin.getDatabase().updateProfile(profile);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                } else if (args.length == 3){

                    if(Bukkit.getPlayer(args[1]) == null) {

                        player.sendMessage(chatcolor.col(MTUtils.caseErrorMex("invalid-arg")));

                    } else {
                        Player target = Bukkit.getPlayer(args[1]);
                        Profile targetProfile = Database.getPlayerProfileFromPUUID(target);
                        Long modifier = Long.parseLong(args[2]);
                        if (args[0].equals("remove")) {
                            bal = profile.getPolusBalance() - modifier;
                            player.sendMessage(chatcolor.col(MTUtils.readString("economy-remove-others", NumberFormatter.formatNumberCommas(modifier), target.getName())));
                            target.sendMessage(chatcolor.col(MTUtils.readString("economy-others-remove", NumberFormatter.formatNumberCommas(modifier), player.getName())));
                        } else if (args[0].equals("add")) {
                            bal = profile.getPolusBalance() + modifier;
                            player.sendMessage(chatcolor.col(MTUtils.readString("economy-add-others", NumberFormatter.formatNumberCommas(modifier), target.getName())));
                            target.sendMessage(chatcolor.col(MTUtils.readString("economy-others-add", NumberFormatter.formatNumberCommas(modifier), player.getName())));
                        } else if (args[0].equals("set")) {
                            bal = modifier;
                            player.sendMessage(chatcolor.col(MTUtils.readString("economy-set-others", NumberFormatter.formatNumberCommas(modifier), target.getName())));
                            target.sendMessage(chatcolor.col(MTUtils.readString("economy-others-set", NumberFormatter.formatNumberCommas(modifier), player.getName())));
                        } else {
                            player.sendMessage(chatcolor.col(MTUtils.caseErrorMex("invalid-arg")));
                        }


                        try {
                            targetProfile.setPolusBalance(bal);
                            plugin.getDatabase().updateProfile(targetProfile);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }

                }

            }

        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        List<String> arguments = new ArrayList<>();
        if (args.length == 1){

            arguments.add("get");
            arguments.add("set");
            arguments.add("add");
            arguments.add("remove");

        } else if (args.length == 2){

            arguments.add("(Valore)");
            arguments.add("(Nome)");

        } else if (args.length == 3){

            arguments.add("(Valore)");

        }

        return arguments;
    }
}

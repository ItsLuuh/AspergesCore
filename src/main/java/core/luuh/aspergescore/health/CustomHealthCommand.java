package core.luuh.aspergescore.health;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.utils.chatcolor;
import core.luuh.aspergescore.utils.files.MTUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomHealthCommand implements CommandExecutor, TabCompleter {

    private final AspergesCore plugin;

    public CustomHealthCommand(AspergesCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Player player = (Player) sender;

        if(args[0].equalsIgnoreCase("setmax")){

            player.setMaxHealth(Double.parseDouble(args[1]));
            player.sendMessage(chatcolor.col(MTUtils.readString("health-maxhealth-set")));

        } else if(args[0].equalsIgnoreCase("setscale")) {

            player.setHealthScale(Double.parseDouble(args[1]));
            player.sendMessage(chatcolor.col(MTUtils.readString("health-scale-set")));

        } else if(args[0].equalsIgnoreCase("sethealth")) {

            player.setHealth(Double.parseDouble(args[1]));
            player.sendMessage(chatcolor.col(MTUtils.readString("health-health-set")));

        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        List<String> arguments = new ArrayList<>();
        if(args.length == 1){
            arguments.add("setmax");
            arguments.add("setscale");
            arguments.add("sethealth");
        } else if(args.length == 2) {
            arguments.add("(Health)");
        }

        return arguments;
    }
}

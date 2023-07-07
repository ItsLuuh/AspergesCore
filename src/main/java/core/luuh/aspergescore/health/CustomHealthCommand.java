package core.luuh.aspergescore.health;

import core.luuh.aspergescore.AspergesCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CustomHealthCommand implements CommandExecutor {

    private final AspergesCore plugin;

    public CustomHealthCommand(AspergesCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Player player = (Player) sender;

        if(args[0].equalsIgnoreCase("setmax")){

            player.setMaxHealth(Double.parseDouble(args[1]));

        } else if(args[0].equalsIgnoreCase("scale")) {

            player.setHealthScale(Double.parseDouble(args[1]));

        } else if(args[0].equalsIgnoreCase("sethealth")) {

            player.setHealth(Double.parseDouble(args[1]));

        }

        return true;
    }
}

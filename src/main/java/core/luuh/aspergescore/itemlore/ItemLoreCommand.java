package core.luuh.aspergescore.itemlore;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.verioncore.VerionAPIManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ItemLoreCommand implements CommandExecutor {

    private final AspergesCore plugin;

    public ItemLoreCommand(AspergesCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)){

            VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&r &cYou can't do this command on console!&r");

        } else {

            //itemlore rename (Name)
            //itemlore addlore (Text)
            //itemlore setloreline (Line) (Text)
            //itemlore removeloreline (Line)
            //itemlore addloreline (Line) (Text)
            //itemlore resetlore
            //itemlore resetname
            //itemlore glow
            //itemlore removeglow
            //itemlore hidenchants

            Player player = (Player) sender;

            if(args.length == 0){

                player.sendMessage("");

            }

        }

        return true;
    }
}

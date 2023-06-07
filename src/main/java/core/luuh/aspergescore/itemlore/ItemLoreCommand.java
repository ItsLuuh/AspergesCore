package core.luuh.aspergescore.itemlore;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.verioncore.VerionAPIManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
            //itemlore loreline set (Line) (Text)
            //itemlore loreline remove (Line)
            //itemlore loreline add (Line) (Text)
            //itemlore resetlore
            //itemlore glow
            //itemlore removeglow
            //itemlore hidenchants
            //itemlore nbt add (Key) (Value)
            //itemlore nbt remove (Value)
            //itemlore nbt get (Value)
            //itemlore nbt all

            Player player = (Player) sender;


            if(player.getInventory().getItemInMainHand().equals(Material.AIR)){

                VerionAPIManager.logConsole("test");

            } else {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (args.length == 0) {


                } else if (args.length == 1) {

                    switch (args[0]) {

                        case "resetlore":

                            item.getItemMeta().lore(null);

                            break;
                        case "glow":

                            VerionAPIManager.setEnchanted(item, true);

                            break;
                        case "removeglow":

                            VerionAPIManager.setEnchanted(item, false);

                            break;
                        case "hidenchants":
                        case "hideenchants":

                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            item.setItemMeta(itemMeta);

                            break;

                    }


                } else if (args.length == 2) {

                    if (args[0].equalsIgnoreCase("rename")) {

                        String itemName = args[0];
                        item.getItemMeta().setDisplayName(itemName);


                    } else if (args[0].equalsIgnoreCase("addlore")) {


                    } else if (args[0].equalsIgnoreCase("removeloreline")) {


                    } else if (args[0].equalsIgnoreCase("nbt")) {



                    }

                }
            }


        }

        return true;
    }
}

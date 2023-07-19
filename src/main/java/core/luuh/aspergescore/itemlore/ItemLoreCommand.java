package core.luuh.aspergescore.itemlore;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.utils.NBTUtils;
import core.luuh.aspergescore.utils.chatcolor;
import core.luuh.aspergescore.utils.files.MTUtils;
import core.luuh.verioncore.VerionAPIManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemLoreCommand implements CommandExecutor, TabCompleter {

    private final AspergesCore plugin;

    public ItemLoreCommand(AspergesCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)){

            VerionAPIManager.logConsole(MTUtils.caseErrorMex("error-console-execute"));

        } else {

            //itemlore rename (Name) [2]
            //itemlore addlore (Text) [2]
            //itemlore lore set (Line) (Text) [4]
            //itemlore lore add (Text) [3]
            //itemlore lore remove (Line) [3]
            //itemlore resetlore [1]
            //itemlore glow [1]
            //itemlore removeglow [1]
            //itemlore hidenchants [1]
            //itemlore nbt add (Key) (Value) [4]
            //itemlore nbt remove (Value) [3]
            //itemlore nbt get (Value) [3]
            //itemlore nbt all [2]

            Player player = (Player) sender;


            if(player.getInventory().getItemInMainHand().equals(Material.AIR)){

                player.sendMessage(chatcolor.col(MTUtils.readString("error-invalid-item")));

            } else {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (args.length == 0) {

                    player.sendMessage(chatcolor.col(MTUtils.caseErrorMex("not-enough-arguments")));

                } else if (args[0].equalsIgnoreCase("rename") && args.length >= 2){

                    ItemMeta itemMeta = item.getItemMeta();

                    StringBuilder message = new StringBuilder();

                    message.append(args[1]);
                    for (int i = 2; i < args.length; i++) {
                        message.append(" ");
                        message.append(args[i]);
                    }

                    String text = chatcolor.col(message.toString());
                    itemMeta.setDisplayName(text);
                    item.setItemMeta(itemMeta);
                    player.sendMessage(chatcolor.col(MTUtils.readString("item-rename")));

                } else if (args[0].equalsIgnoreCase("lore") && args.length >= 3) {

                    if (args[1].equalsIgnoreCase("add")) {

                        StringBuilder message = new StringBuilder();

                        message.append(args[2]);
                        for (int i = 3; i < args.length; i++) {
                            message.append(" ");
                            message.append(args[i]);
                        }

                        String text = message.toString();
                        LoreCommand.addLore(text, item);
                        player.sendMessage(chatcolor.col(MTUtils.readString("item-lore-add")));
                    } else if (args[1].equalsIgnoreCase("set") && args.length >= 4) {

                        StringBuilder message = new StringBuilder();

                        message.append(args[3]);
                        for (int i = 4; i < args.length; i++) {
                            message.append(" ");
                            message.append(args[i]);
                        }

                        LoreCommand.setLore(args, item, message, Integer.parseInt(args[2]));
                        player.sendMessage(chatcolor.col(MTUtils.readString("item-lore-set", args[2] )));
                    }
                } else if (args[0].equalsIgnoreCase("addlore") && args.length >= 2) {

                    StringBuilder message = new StringBuilder();

                    message.append(args[1]);
                    for (int i = 2; i < args.length; i++) {
                        message.append(" ");
                        message.append(args[i]);
                    }

                    String text = message.toString();
                    LoreCommand.addLore(text, item);
                    player.sendMessage(chatcolor.col(MTUtils.readString("item-lore-add")));

                } else if (args.length == 1) {

                    ItemMeta itemMeta = item.getItemMeta();
                    switch (args[0]) {


                        case "resetlore":

                            if(item.getItemMeta().hasLore()) {
                                for (String s : item.getItemMeta().getLore()) {
                                    item.getItemMeta().getLore().remove(s);
                                }
                            }

                            player.sendMessage(chatcolor.col(MTUtils.readString("item-lore-reset")));


                            break;
                        case "glow":

                            VerionAPIManager.setEnchanted(item, true);
                            player.sendMessage(chatcolor.col(MTUtils.readString("item-glow-add")));

                            break;
                        case "removeglow":

                            VerionAPIManager.setEnchanted(item, false);
                            player.sendMessage(chatcolor.col(MTUtils.readString("item-glow-remove")));

                            break;
                        case "hidenchants":
                        case "hideenchants":

                            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            item.setItemMeta(itemMeta);
                            player.sendMessage(chatcolor.col(MTUtils.readString("item-enchants-hide")));

                            break;
                        case "removenchants":
                        case "removeenchants":

                            for(Enchantment e : item.getEnchantments().keySet()) {
                                item.removeEnchantment(e);
                            }
                            player.sendMessage(chatcolor.col(MTUtils.readString("item-enchants-remove")));

                            break;
                        case "hideattributes":

                            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                            item.setItemMeta(itemMeta);

                            player.sendMessage(chatcolor.col(MTUtils.readString("item-attributes-hide")));


                            break;

                    }


                } else if (args.length == 2) {

                    if (args[0].equalsIgnoreCase("addlore")) {

                        LoreCommand.addLore(args, item);
                        player.sendMessage(chatcolor.col(MTUtils.readString("item-lore-add")));

                    } else if (args[0].equalsIgnoreCase("nbt")) {



                    }

                } else if(args.length == 3){

                    if(args[0].equalsIgnoreCase("lore")){

                        if(args[1].equalsIgnoreCase("remove")) {

                            if (item.getItemMeta().hasLore()) {
                                ItemMeta itemMeta = item.getItemMeta();
                                int i = Integer.parseInt(args[2]);
                                if (i < item.getItemMeta().getLore().size()) {
                                    List<String> lore = itemMeta.getLore();
                                    lore.remove(i - 1);
                                    itemMeta.setLore(lore);
                                    item.setItemMeta(itemMeta);
                                    player.sendMessage(chatcolor.col(MTUtils.readString("item-lore-remove", args[2])));
                                } else {
                                    player.sendMessage(chatcolor.col(MTUtils.readString("item-lore-remove-too-big-arg")));
                                }
                            } else {

                                player.sendMessage(chatcolor.col(MTUtils.readString("item-lore-no-lore")));

                            }
                        }

                    } else if(args[0].equalsIgnoreCase("nbt")) {

                        if (args[1].equalsIgnoreCase("remove")) {

                            NBTUtils.removeNBT(item, args[2]);

                        } else if (args[1].equalsIgnoreCase("get")) {

                            player.sendMessage(chatcolor.col(NBTUtils.getNBT(item, args[2])));

                        } else {

                        }

                    }


                } else if(args.length == 4){

                    if(args[0].equalsIgnoreCase("nbt") && args[1].equalsIgnoreCase("add")){

                        NBTUtils.addNBT(item, args[2], args[3]);

                    } else {

                    }

                } else {

                }
            }


        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){

        List<String> arguments = new ArrayList<>();

        if(args.length == 1){

            arguments.add("rename");
            arguments.add("lore");
            arguments.add("addlore");
            arguments.add("resetlore");
            arguments.add("nbt");
            arguments.add("glow");
            arguments.add("removeglow");
            arguments.add("hidenchants");
            arguments.add("hideattributes");

        } else if(args.length == 2){

            if(args[0].equalsIgnoreCase("rename"))arguments.add("(Name)");
            if(args[0].equalsIgnoreCase("addlore"))arguments.add("(Text)");
            if(args[0].equalsIgnoreCase("nbt")) {

                arguments.add("add");
                arguments.add("remove");
                arguments.add("get");
                arguments.add("all");

            } else if(args[0].equalsIgnoreCase("lore")){

                arguments.add("set");
                arguments.add("add");
                arguments.add("remove");

            }

        } else if(args.length == 3){
            if(args[0].equalsIgnoreCase("lore")){

                if(args[1].equalsIgnoreCase("set")){
                    arguments.add("(Line) (Text)");
                } else if(args[1].equalsIgnoreCase("add")){
                    arguments.add("(Text)");
                } else if(args[1].equalsIgnoreCase("remove")){
                    arguments.add("(Line)");
                }

            } else if(args[0].equalsIgnoreCase("nbt")){

                if(args[1].equalsIgnoreCase("add")){
                    arguments.add("(Key) (Value)");
                } else if(args[1].equalsIgnoreCase("remove")){
                    arguments.add("(Key)");
                } else if(args[1].equalsIgnoreCase("get")){
                    arguments.add("(Key)");
                }

            }
        }

        return arguments;

    }
}

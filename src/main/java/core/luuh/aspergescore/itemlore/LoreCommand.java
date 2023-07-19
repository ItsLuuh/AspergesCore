package core.luuh.aspergescore.itemlore;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.utils.chatcolor;
import core.luuh.aspergescore.utils.files.MTUtils;
import core.luuh.verioncore.VerionAPIManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LoreCommand implements CommandExecutor, TabCompleter {

    private final AspergesCore plugin;

    public LoreCommand(AspergesCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)){

            VerionAPIManager.logConsole(MTUtils.caseErrorMex("error-console-execute"));

        } else {

            Player player = (Player) sender;

            if(!player.getInventory().getItemInMainHand().equals(Material.AIR)) {

                ItemStack item = player.getInventory().getItemInMainHand();

                if(args.length >= 2) {

                    if (args[0].equalsIgnoreCase("add")) {

                        StringBuilder message = new StringBuilder();

                        message.append(args[1]);
                        for (int i = 2; i < args.length; i++) {
                            message.append(" ");
                            message.append(args[i]);
                        }

                        String text = message.toString();
                        addLore(text, item);
                        player.sendMessage(chatcolor.col(MTUtils.readString("item-lore-add")));
                    } else if (args[0].equalsIgnoreCase("set") && args.length >= 3) {

                        StringBuilder message = new StringBuilder();

                        message.append(args[2]);
                        for (int i = 3; i < args.length; i++) {
                            message.append(" ");
                            message.append(args[i]);
                        }
                        player.sendMessage(chatcolor.col(MTUtils.readString("item-lore-set", args[1])));

                        setLore(args, item, message, Integer.parseInt(args[1]));
                    } else if (args[0].equalsIgnoreCase("remove")) {

                        if (item.getItemMeta().hasLore()) {
                            ItemMeta itemMeta = item.getItemMeta();
                            int i = Integer.parseInt(args[1]);
                            if (i < item.getItemMeta().getLore().size()) {
                                List<String> lore = itemMeta.getLore();
                                lore.remove(i);
                                itemMeta.setLore(lore);
                                item.setItemMeta(itemMeta);
                                player.sendMessage(chatcolor.col(MTUtils.readString("item-lore-remove", args[1])));
                            } else {
                                player.sendMessage(chatcolor.col(MTUtils.readString("item-lore-remove-too-big-arg")));
                            }
                        } else {

                            player.sendMessage(chatcolor.col(MTUtils.readString("item-lore-no-lore")));

                        }
                    }

                } else {

                }

            } else {

            }

        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        List<String> arguments = new ArrayList<>();
        if(args.length == 1) {
            arguments.add("add");
            arguments.add("set");
            arguments.add("remove");
        } else if(args[0].equalsIgnoreCase("add")){arguments.add("(Text)");
        } else if(args[0].equalsIgnoreCase("set")){arguments.add("(Line) (Text)");
        }else if(args[0].equalsIgnoreCase("remove"))arguments.add("(Line)");

        return arguments;
    }

    static void setLore(@NotNull String[] args, ItemStack item, StringBuilder message, Integer n) {
        String text = chatcolor.col(chatcolor.repCompToLegacy(message.toString()));
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore;
        if (item.getItemMeta().hasLore()) {
            lore = itemMeta.getLore();
            if(n > lore.size()){
                for(int i = lore.size() + 1; i < n; i++){
                    lore.add(chatcolor.chat("&0"));
                }
                lore.add(text);
            } else {
                lore.set(n - 1, text);
            }
        } else {
            lore = new ArrayList<>();
            for(int i = 1; i < n; i++){
                lore.add(chatcolor.chat("&0"));
            }
            lore.add(text);
        }
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
    }

    public static void addLore(@NotNull String text, ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        if(item.getItemMeta().hasLore()) lore = itemMeta.getLore();
        lore.add(chatcolor.col(chatcolor.repCompToLegacy(text)));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
    }

    public static void addLore(@NotNull String[] args, ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        if(item.getItemMeta().hasLore()) lore = itemMeta.getLore();
        lore.add(chatcolor.col(chatcolor.repCompToLegacy(args[1])));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
    }
}

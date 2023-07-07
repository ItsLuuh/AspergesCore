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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RenameCommand implements CommandExecutor, TabCompleter {

    private final AspergesCore plugin;

    public RenameCommand(AspergesCore plugin) {
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
                ItemMeta itemMeta = item.getItemMeta();

                StringBuilder message = new StringBuilder();
                if (args.length > 0) {
                    message.append(args[0]);
                    for (int i = 1; i < args.length; i++) {
                        message.append(" ");
                        message.append(args[i]);
                    }
                }

                String text = chatcolor.col(message.toString());
                itemMeta.setDisplayName(text);
                item.setItemMeta(itemMeta);
                player.sendMessage(chatcolor.col(MTUtils.readString("item-rename")));

            } else {

                player.sendMessage(chatcolor.col(MTUtils.readString("error-invalid-item")));

            }

        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        List<String> arguments = new ArrayList<>();
        arguments.add("(Text)");

        return arguments;
    }
}

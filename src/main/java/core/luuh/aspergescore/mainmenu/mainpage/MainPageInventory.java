package core.luuh.aspergescore.mainmenu.mainpage;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.utils.chatcolor;
import core.luuh.aspergescore.utils.files.MTUtils;
import core.luuh.aspergescore.utils.files.RCUtils;
import core.luuh.verioncore.VerionAPIManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class MainPageInventory implements Listener, CommandExecutor {

    private final AspergesCore plugin;

    public MainPageInventory(AspergesCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e){

        if(e.getView().getTitle().equalsIgnoreCase(chatcolor.col(RCUtils.readString("guititles.mainmenu")))){

            Player player = (Player) e.getWhoClicked();

            if (e.getSlot() == 22) {
                player.closeInventory();
                player.performCommand("secretcommands open profilesmenu");
            } else {
                e.setCancelled(true);
            }

        } else if(e.getView().getTitle().equalsIgnoreCase(chatcolor.col(RCUtils.readString("guititles.profilesmenu")))){

            Player player = (Player) e.getWhoClicked();

            if (e.getSlot() == 22) {
                player.closeInventory();
                player.performCommand("secretcommands open profilesmenu");
            } else {
                e.setCancelled(true);
            }

        }

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //secretcommands open mainmenu
        //secretcommands open profilesmenu

        if(!(sender instanceof Player)){
            VerionAPIManager.logConsole(MTUtils.caseErrorMex("error-console-execute"));
        } else {
            Player player = (Player) sender;
            if(args.length == 0){

                player.sendMessage(chatcolor.col(MTUtils.caseErrorMex("not-enough-args")));

            } else if(args.length == 1){



            } else if(args.length == 2){

                if(args[0].equalsIgnoreCase("open")){

                    if(args[1].equalsIgnoreCase("mainmenu")){

                        Inventory gui = VerionAPIManager.createChestGUI(player, 5, RCUtils.readString("guititles.mainmenu"));
                        VerionAPIManager.fullPanes("5", false, gui, Material.BLACK_STAINED_GLASS_PANE);

                        ItemStack item = VerionAPIManager.createStartItem(Material.PLAYER_HEAD, 1);
                        ItemMeta itemMeta = item.getItemMeta();
                        itemMeta.displayName(chatcolor.mm("<gradient:#ff0000:#ff3d3d:#ff0000> Your Profiles </gradient>"));
                        item.setItemMeta(itemMeta);

                        gui.setItem(22, item);
                        player.openInventory(gui);

                    }

                }

            }
        }

        return true;
    }
}

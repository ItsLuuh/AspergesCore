package core.luuh.aspergescore.mainmenu;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.utils.chatcolor;
import core.luuh.aspergescore.utils.files.MTUtils;
import core.luuh.aspergescore.utils.files.RCUtils;
import core.luuh.verioncore.VerionAPIManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MainMenuItemEvent implements Listener {

    private final AspergesCore plugin;

    public MainMenuItemEvent(AspergesCore plugin) {
        this.plugin = plugin;
    }

    private String mmname = RCUtils.readString("guitems.mainmenu");

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        ItemStack itemStack = new ItemStack(Material.ENDER_EYE, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(chatcolor.col(mmname));
        itemStack.setItemMeta(itemMeta);
        VerionAPIManager.setEnchanted(itemStack, true);
        e.getPlayer().getInventory().setItem(8, itemStack);

    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent e){

        if(e.getItem() != null  && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(chatcolor.col(mmname))){

            e.setCancelled(true);
            e.getPlayer().performCommand("secretcommands open mainmenu");

        }

    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e){

        if(e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(chatcolor.col(mmname))){

            e.setCancelled(true);
            e.getPlayer().performCommand("secretcommands open mainmenu");

        }

    }

    @EventHandler
    public void onItemClickInventory(InventoryClickEvent e){

        if(e.getClickedInventory() != null && (e.getClickedInventory().getType().equals(InventoryType.PLAYER) || e.getClickedInventory().getType().equals(InventoryType.CREATIVE))) {
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chatcolor.col(mmname))) {

                Player player = (Player) e.getWhoClicked();
                e.setCancelled(true);
                player.closeInventory();
                player.performCommand("secretcommands open mainmenu");

            }
        }

    }


}

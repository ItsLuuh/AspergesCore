package core.luuh.aspergescore.mainmenu;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.utils.chatcolor;
import core.luuh.aspergescore.utils.files.RCUtils;
import core.luuh.verioncore.VerionAPIManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

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
        itemMeta.setCustomModelData(10000);
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

        if(e.getClickedInventory() != null) {
            if(e.getWhoClicked().getGameMode().equals(GameMode.SURVIVAL)) {
                if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chatcolor.col(mmname))) {

                    e.setCancelled(true);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            openMenu((Player) e.getWhoClicked());
                        }
                    }.runTaskLater(plugin, 2L);


                }
            }
        }

    }

    @EventHandler
    public void onCreativeItemClick(InventoryCreativeEvent e){

        if(e.getCursor().hasItemMeta() && e.getCursor().getItemMeta().getDisplayName().equalsIgnoreCase(chatcolor.col(mmname))){

            e.setCancelled(true);
            ItemStack item = new ItemStack(Material.ENDER_EYE);
            VerionAPIManager.setEnchanted(item, true);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(chatcolor.col(mmname));
            itemMeta.setCustomModelData(10000);
            item.setItemMeta(itemMeta);
            e.getClickedInventory().setItem(8, item);

        }

    }

    private void openMenu(Player player){

        player.performCommand("secretcommands open mainmenu");

    }


}

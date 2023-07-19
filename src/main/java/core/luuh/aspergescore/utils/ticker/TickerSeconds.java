package core.luuh.aspergescore.utils.ticker;

import core.luuh.aspergescore.AspergesCore;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class TickerSeconds {

    private boolean enabled;
    private final AspergesCore plugin;

    public TickerSeconds(AspergesCore plugin) {
        this.plugin = plugin;
    }

    private void Count() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, () -> {
            if(enabled) {
                plugin.onSecond();
                Count();
            }
        }, 20L);
    }


    public TickerSeconds toggle() {
        enabled=!enabled;
        if(enabled) {
            Count();
        }
        return this;
    }

    public TickerSeconds setEnabled(boolean bool) {
        if(enabled==bool) {
            return this;
        }
        enabled=bool;
        if(enabled) {
            Count();
        }
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

}

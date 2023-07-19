package core.luuh.aspergescore.utils.ticker;

import core.luuh.aspergescore.AspergesCore;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class TickerTicks {

    private boolean enabled;
    private final AspergesCore plugin;

    public TickerTicks(AspergesCore plugin) {
        this.plugin = plugin;
    }

    private void Count() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, () -> {
            if(enabled) {
                plugin.onTick();
                Count();
            }
        }, 1L);
    }


    public TickerTicks toggle() {
        enabled=!enabled;
        if(enabled) {
            Count();
        }
        return this;
    }

    public TickerTicks setEnabled(boolean bool) {
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

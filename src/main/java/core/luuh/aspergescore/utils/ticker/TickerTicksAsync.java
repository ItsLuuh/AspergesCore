package core.luuh.aspergescore.utils.ticker;

import core.luuh.aspergescore.AspergesCore;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class TickerTicksAsync {

    private boolean enabled;
    private final AspergesCore plugin;

    public TickerTicksAsync(AspergesCore plugin) {
        this.plugin = plugin;
    }

    private void Count() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleAsyncDelayedTask(plugin, () -> {
            if(enabled) {
                plugin.onTickAsync();
                Count();
            }
        }, 1L);
    }


    public TickerTicksAsync toggle() {
        enabled=!enabled;
        if(enabled) {
            Count();
        }
        return this;
    }

    public TickerTicksAsync setEnabled(boolean bool) {
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

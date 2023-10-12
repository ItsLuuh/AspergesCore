package core.luuh.aspergescore.utils.ticker;

import core.luuh.aspergescore.AspergesCore;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class TickerSecondsAsync {

    private boolean enabled;
    private final AspergesCore plugin;

    public TickerSecondsAsync(AspergesCore plugin) {
        this.plugin = plugin;
    }

    private void Count() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleAsyncDelayedTask(plugin, () -> {
            if(enabled) {
                plugin.onSecondAsync();
                Count();
            }
        }, 20L);
    }


    public TickerSecondsAsync toggle() {
        enabled=!enabled;
        if(enabled) {
            Count();
        }
        return this;
    }

    public TickerSecondsAsync setEnabled(boolean bool) {
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

package core.luuh.aspergescore.utils;

import core.luuh.aspergescore.AspergesCore;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class Ticker {

    private boolean enabled;
    private final AspergesCore plugin;

    public Ticker(AspergesCore plugin) {
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

    public Ticker toggle() {
        enabled=!enabled;
        if(enabled) {
            Count();
        }
        return this;
    }

    public Ticker setEnabled(boolean bool) {
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

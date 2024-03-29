package core.luuh.aspergescore.mobs.nametag;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.utils.NBTUtils;
import core.luuh.aspergescore.utils.NumberFormatter;
import core.luuh.aspergescore.utils.files.RCUtils;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MobHealthListener implements Listener {

    private final AspergesCore plugin;

    public MobHealthListener(AspergesCore plugin) {
        this.plugin = plugin;
    }

    private String getMobStatus(String name) {
        String type = "";

        if (name.contains(".Mob") || name.contains(".mob")) {
            type = "mob";
        } else if (name.contains(".Boss") || name.contains(".boss")) {
            type = "boss";
        }

        return type;
    }

    private String getMobName(String name) {
        name = name.replace(".Mob", "").replace(".Boss", "");

        return name;
    }

    private void spawnMobHealth(ActiveMob mob){

        String mobname = mob.getType().getDisplayName().get();
        String status = getMobStatus(mobname);
        String name;
        if(status.equals("boss")){
            name = RCUtils.readString("mythicmobs-boss-display-template");
        } else {
            name = RCUtils.readString("mythicmobs-mob-display-template");
        }

        double health = mob.getType().getHealth().get();
        String finalHealth = NumberFormatter.formatNumber(health);
        String finalName = name.replaceAll("%mob%", getMobName(mobname)).replaceAll("%mob_health%", finalHealth);

        mob.setDisplayName(finalName);

    }

    private void changeMobHealth(ActiveMob mob, double damage){

        String mobname = mob.getType().getDisplayName().get();
        String status = getMobStatus(mobname);
        String name;
        if(status.equals("boss")){
            name = RCUtils.readString("mythicmobs-boss-display-template");
        } else {
            name = RCUtils.readString("mythicmobs-mob-display-template");
        }

        double health = mob.getEntity().getHealth() - damage;
        if(health > 0) {
            String finalHealth = NumberFormatter.formatNumber(health);
            String finalName = name.replaceAll("%mob%", getMobName(mobname)).replaceAll("%mob_health%", finalHealth);

            mob.setDisplayName(finalName);
        }
    }

    @EventHandler
    public void onEntitySpawn(MythicMobSpawnEvent e) {

        spawnMobHealth(e.getMob());

    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {


        if(MythicBukkit.inst().getMobManager().getMythicMobInstance(e.getEntity()) != null) {

            ActiveMob mythicMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(e.getEntity());
            customDamageMobs(e, mythicMob, e.getFinalDamage());

        }
    }

    private void customDamageMobs(EntityDamageByEntityEvent e, ActiveMob mythicMob, double damage){

        if(e.getDamager() instanceof Player) {
            Player player = (Player) e.getDamager();
            if (mythicMob.getFaction().equalsIgnoreCase("Soul")) {
                if (!NBTUtils.hasNBT(player.getInventory().getItemInMainHand(), "ability-damage-ghost")) {

                    e.setCancelled(true);

                } else {

                    changeMobHealth(mythicMob, damage);

                }

            }
        }

    }

}





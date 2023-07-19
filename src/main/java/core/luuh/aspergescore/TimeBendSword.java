package core.luuh.aspergescore;

import core.luuh.aspergescore.utils.chatcolor;
import io.papermc.paper.event.entity.EntityMoveEvent;
import io.papermc.paper.event.entity.EntityPushedByEntityAttackEvent;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class TimeBendSword implements Listener {

    private final AspergesCore plugin;
    private static final HashMap<Player, Integer> playerRem = new HashMap<>();
    private static final HashMap<LivingEntity, Integer> TBendEntitiesTime = new HashMap<>();

    private static final int RADIUS = 10;
    private static final int WAVE_DURATION = 50; // In ticks

    public TimeBendSword(AspergesCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction().toString().contains("RIGHT") && player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_SWORD) {
            if(!playerRem.containsKey(player) || playerRem.get(player) == 0) {
                if(!playerRem.isEmpty() && playerRem.get(player) == 0)playerRem.remove(player);
                createCustomWave(player.getLocation(), 54, RADIUS);
                getNearbyLivingEntities(player, RADIUS);
                playerRem.put(player, 10);
            } else {
                player.sendMessage(String.valueOf(playerRem.get(player)));
                player.sendMessage(chatcolor.mm("<#7532DB>TimeBend not finished."));
            }
        }
    }

    private void getNearbyLivingEntities(Player player, double radius) {
        World world = player.getWorld();

        for (Entity entity : world.getEntities()) {
            if (entity instanceof LivingEntity && !(entity instanceof ArmorStand) && entity != player) {
                double distanceSquared = entity.getLocation().distanceSquared(player.getLocation());
                if (distanceSquared <= radius * radius) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    TBendEntitiesTime.put(livingEntity, 10);
                }
            }
        }
    }

    private void createCustomWave(Location center, int particleCount, double maxRadius) {
        new BukkitRunnable() {
            int ticks = 0;
            double heightOffset = 0;
            final double maxHeightOffset = 10;
            boolean isRising = false;
            boolean isReversing = false;

            @Override
            public void run() {
                ticks++;
                double radius;
                double finalY = center.getY(); // Posizione finale Y dell'onda d'urto

                if (ticks <= (2 * WAVE_DURATION / 3)) {
                    radius = maxRadius * (ticks / ((double) (2 * WAVE_DURATION / 3)));
                    wave(radius, finalY, particleCount);
                } else if (!isRising) {
                    double progress = (double) (ticks - (2 * WAVE_DURATION / 3)) / (WAVE_DURATION / 3);
                    heightOffset = maxHeightOffset * progress;
                    radius = maxRadius;
                    wave(radius, finalY + heightOffset, particleCount);
                } else if (!isReversing) {
                    isReversing = true;
                    radius = maxRadius;
                    wave(radius, finalY + heightOffset, particleCount);
                } else {
                    double progress = (double) (ticks - WAVE_DURATION) / (WAVE_DURATION / 3);
                    double reversedProgress = 1 - progress;
                    radius = maxRadius * reversedProgress;
                    wave(radius, finalY + heightOffset, particleCount);
                }

                if (ticks >= (2 * WAVE_DURATION / 3) + (WAVE_DURATION / 3)) {
                    this.cancel();
                }
            }

            private void wave(double radius, double y, int count) {
                Particle particle = Particle.REDSTONE;
                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.GRAY, 1);

                for (double angle = 0; angle < 360; angle += 10) {
                    double x = center.getX() + radius * Math.cos(Math.toRadians(angle));
                    double z = center.getZ() + radius * Math.sin(Math.toRadians(angle));
                    Location particleLocation = new Location(center.getWorld(), x, y, z);
                    particleLocation.add(0, 1, 0); // Aggiungi un offset verticale per evitare collisioni con il suolo
                    center.getWorld().spawnParticle(particle, particleLocation, count, dustOptions);
                }
            }

        }.runTaskTimer(plugin, 0, 1);

        center.getWorld().playSound(center, Sound.BLOCK_BEACON_ACTIVATE, 1, 1.5F);
    }

    public static void sendActionBar(Player player){

        if(playerRem.containsKey(player)){

            int rem = playerRem.get(player);
            player.sendActionBar(chatcolor.mm("<#7532DB>TimeBend: " + rem + "s"));
            if(rem == 0){
                playerRem.remove(player);
            } else {
                rem--;
                playerRem.put(player, rem);
            }
        }

    }

    @EventHandler
    public void onEntityMovement(EntityMoveEvent e){

        LivingEntity lv = e.getEntity();
        if(TBendEntitiesTime.containsKey(lv)){
            if(TBendEntitiesTime.get(lv) == 0)TBendEntitiesTime.remove(lv);
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onEntityPushByEntity(EntityPushedByEntityAttackEvent e){

        if(e.getEntity() instanceof LivingEntity) {
            LivingEntity lv = (LivingEntity) e.getEntity();
            if (TBendEntitiesTime.containsKey(lv)) {
                if(TBendEntitiesTime.get(lv) == 0)TBendEntitiesTime.remove(lv);
                e.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent e){

        LivingEntity lv = e.getEntity();
        if (TBendEntitiesTime.containsKey(lv)) {
            TBendEntitiesTime.remove(lv);
        }

    }

    public static void verifyEntitiesTBend(){

        Set<LivingEntity> lvEntities = TBendEntitiesTime.keySet();
        for(LivingEntity lvE : lvEntities){

            int i = TBendEntitiesTime.get(lvE);
            if(i == 0){
                TBendEntitiesTime.remove(lvE);
            } else {
                i--;
                TBendEntitiesTime.put(lvE, i);

            }

        }

    }
}

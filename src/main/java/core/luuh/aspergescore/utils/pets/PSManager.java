package core.luuh.aspergescore.utils.pets;

import core.luuh.aspergescore.utils.RCUtils;
import core.luuh.aspergescore.utils.chatcolor;
import core.luuh.aspergescore.utils.scoreboard.SBManager;
import core.luuh.verioncore.VerionAPIManager;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class PSManager {

    private static final HashMap<UUID, ArmorStand> petStand = new HashMap<>();

    public static void createPSofPlayer(Player player){

        Location playerLocation = player.getLocation();
        Vector playerDirection = playerLocation.getDirection();
        Vector oppositeDirection = playerDirection.multiply(-1.25);

        Location armorStandLocation = playerLocation.clone().add(oppositeDirection);
        armorStandLocation.setY(playerLocation.getY());

        ArmorStand armorStand = (ArmorStand) playerLocation.getWorld().spawnEntity(armorStandLocation, EntityType.ARMOR_STAND);

        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        armorStand.customName(chatcolor.mm("<#FF0000>" + player.getName()+"'s Pet &8[&cLv. 100&8]"));
        armorStand.setCustomNameVisible(true);

        armorStand.setDisabledSlots(EquipmentSlot.HEAD);
        armorStand.setDisabledSlots(EquipmentSlot.CHEST);
        armorStand.setDisabledSlots(EquipmentSlot.LEGS);
        armorStand.setDisabledSlots(EquipmentSlot.FEET);

        ItemStack item = VerionAPIManager.createStartItem(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta)item.getItemMeta();
        meta.setOwner(player.getName());
        item.setItemMeta(meta);
        armorStand.setItem(EquipmentSlot.HEAD, item);

        boundPStoPlayer(player, armorStand);

    }

    public static void updatePSPosOfPlayer(Player player) {
        double height = 0.0; // Default Height
        updatePSPosOfPlayer(player, height);
    }

    public static void updatePSPosOfPlayer(Player player, double height) {
        ArmorStand armorStand = getPSbyPlayer(player);
        Location playerLocation = player.getLocation();
        Vector playerDirection = playerLocation.getDirection().setY(0).normalize(); // Ignores the Y component of the player's direction

        double distance = RCUtils.readDouble("pet-distance"); // Distance from Player
        double offsetAmount = RCUtils.readDouble("pet-general-offset"); // Making the Armorstand to the right side of player, making it negative it goes to the left
        double yOffset = RCUtils.readDouble("pet-height-offset"); // Make the Armorstand lower height

        Location armorStandLocation = playerLocation.clone().subtract(playerDirection.multiply(distance));
        armorStandLocation.add(playerLocation.getDirection().crossProduct(new Vector(0, 1, 0)).multiply(offsetAmount)); // Calculates general offset
        armorStandLocation.setY(playerLocation.getY() + yOffset + height);
        armorStand.teleport(armorStandLocation);
        particleGenerator(armorStand);
    }

    private static void particleGenerator(ArmorStand armorStand){

        Location particleLocation = armorStand.getLocation().clone().subtract(0, RCUtils.readDouble("pet-particle-height"), 0);
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1);
        particleLocation.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 1, 0, 0, 0, 1, dustOptions);

    }

    public static void boundPStoPlayer(Player player, ArmorStand armorStand){

        petStand.put(player.getUniqueId(), armorStand);

    }

    public static void unboundPSfromPlayer(Player player){


        getPSbyPlayer(player).remove();
        petStand.remove(player.getUniqueId());

    }

    public static ArmorStand getPSbyPlayer(Player player){

        return petStand.get(player.getUniqueId());

    }

    public static void removeAllPS(){

        for (UUID key : petStand.keySet()) {
            ArmorStand armorStand = petStand.get(key);
            armorStand.remove();
            petStand.remove(key);
        }

    }

}

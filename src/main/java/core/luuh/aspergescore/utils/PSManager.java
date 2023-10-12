package core.luuh.aspergescore.utils;

import core.luuh.aspergescore.utils.files.RCUtils;
import core.luuh.aspergescore.utils.chatcolor;
import core.luuh.verioncore.VerionAPIManager;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
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
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class PSManager {

    private static final HashMap<UUID, ArmorStand> petStand = new HashMap<>();


    // creates the PetStand for Player
    public static void createPSofPlayer(Player player){

        ArmorStand armorStand = (ArmorStand) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);

        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        armorStand.setSmall(true);
        armorStand.customName(chatcolor.mm("<#FF0000>" + player.getName()+"'s Pet &8[&cLv. 100&8]"));
        armorStand.setCustomNameVisible(true);

        armorStand.addDisabledSlots(EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.LEGS,EquipmentSlot.FEET);

        ItemStack item = VerionAPIManager.createStartItem(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta)item.getItemMeta();
        meta.setOwner(player.getName());
        item.setItemMeta(meta);
        armorStand.setItem(EquipmentSlot.HEAD, item);

        boundPStoPlayer(player, armorStand);

    }

    private static boolean isAscending = true;
    private static float petHeight;

    public static void checkArmorStandPosition(Player player) {
        ArmorStand armorStand = getPSbyPlayer(player);

        if (armorStand != null) {
            Location armorStandLocation = armorStand.getLocation();
            Location playerLocation = player.getLocation();
            particleGenerator(armorStand);

            double distanceThreshold = RCUtils.readDouble("pet-distance");
            double currentDistance = armorStandLocation.distance(playerLocation);

            if (currentDistance > distanceThreshold) {
                // Calculate the direction towards the player's current position
                Vector petDirection = playerLocation.toVector().subtract(armorStandLocation.toVector()).normalize();

                // Calculate the speed based on the distance, using a logarithmic interpolation
                double maxSpeed = 0.3; // Adjust this value to control the maximum speed
                double minSpeed = 0.05; // Adjust this value to control the minimum speed
                double speed = maxSpeed * Math.log(currentDistance) / Math.log(distanceThreshold);
                speed = Math.max(speed, minSpeed); // Ensure the speed is at least minSpeed

                // Calculate the desired position for the pet at the distanceThreshold from the player
                Location desiredPetLocation = playerLocation.clone().add(petDirection.multiply(distanceThreshold));

                // Calculate the direction from the current armor stand position to the desired position
                Vector movement = desiredPetLocation.toVector().subtract(armorStandLocation.toVector()).normalize().multiply(speed);

                // Move the armor stand towards the desired position
                Location newArmorStandLocation = armorStandLocation.add(movement);

                // Check if the new location exceeds the distance threshold from the initial position
                Location initialArmorStandLocation = getPSbyPlayer(player).getLocation();
                double maxAllowedDistance = initialArmorStandLocation.distance(playerLocation) + distanceThreshold;
                if (newArmorStandLocation.distanceSquared(initialArmorStandLocation) <= maxAllowedDistance * maxAllowedDistance) {
                    // Only update the ArmorStand's location if it's within the allowed distance from the initial position
                    armorStand.teleportAsync(newArmorStandLocation);
                }

                // Set the ArmorStand's head pose to make it look at the player
                armorStandLocation.setDirection(petDirection);
                armorStand.teleportAsync(armorStandLocation);
            }

            // Updates the height of the armorstand
            if (isAscending) {
                petHeight += 0.01;
                if (petHeight >= 0.35) {
                    isAscending = false;
                }
            } else {
                petHeight -= 0.01;
                if (petHeight <= 0.0) {
                    isAscending = true;
                }
            }

            // Apply the height to the armorstand
            armorStandLocation.setY(player.getLocation().getY() + RCUtils.readDouble("pet-height-offset") + petHeight);
            armorStand.teleportAsync(armorStandLocation);
        }
    }

    /*
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
        armorStand.teleportAsync(armorStandLocation);
        particleGenerator(armorStand);
    }
     */

    private static Vector getPetDirection(Player player) {
        Vector playerDirection = player.getLocation().getDirection().setY(0).normalize();

        // Set the desired distance from the player
        double desiredDistance = 2.5;

        // Calculate the target location for the pet
        Location petLocation = player.getLocation().clone().subtract(playerDirection.multiply(desiredDistance));

        // Calculate the direction from the pet's location to the player's location
        Vector petDirection = player.getLocation().toVector().subtract(petLocation.toVector()).normalize();

        return petDirection;
    }

    // makes the particles under the pet
    private static void particleGenerator(ArmorStand armorStand){

        Location particleLocation = armorStand.getLocation().clone().subtract(0, RCUtils.readDouble("pet-particle-height"), 0);
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1);
        particleLocation.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 1, 0, 0, 0, 1, dustOptions);

    }

    // add PetStand to Player
    public static void boundPStoPlayer(Player player, ArmorStand armorStand){

        petStand.put(player.getUniqueId(), armorStand);

    }

    // removes PetStand from Player
    public static void unboundPSfromPlayer(Player player){


        getPSbyPlayer(player).remove();
        petStand.remove(player.getUniqueId());

    }

    // gets a PetStand from Player
    public static ArmorStand getPSbyPlayer(Player player){

        return petStand.get(player.getUniqueId());

    }

    // removes every ArmorStand in PetStand HashMap
    public static void removeAllPS(){

        for (UUID key : petStand.keySet()) {
            ArmorStand armorStand = petStand.get(key);
            armorStand.remove();
            petStand.remove(key);
        }

    }

}

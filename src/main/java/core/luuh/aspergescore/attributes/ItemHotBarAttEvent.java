package core.luuh.aspergescore.attributes;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.utils.NBTUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemHotBarAttEvent implements Listener {

    private final AspergesCore plugin;

    public ItemHotBarAttEvent(AspergesCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHotBarItemSwitch(PlayerItemHeldEvent event) {

        ItemStack newItem = event.getPlayer().getInventory().getItem(event.getNewSlot());
        Player player = event.getPlayer();
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);

        List<AttributeModifier> modifiersToRemove = new ArrayList<>();
        for (AttributeModifier modifier : attribute.getModifiers()) {
            if (modifier.getName().equals("item-strength")) {
                modifiersToRemove.add(modifier);
            }
        }
        for (AttributeModifier modifier : modifiersToRemove) {
            attribute.removeModifier(modifier);
        }

        if(event.getPlayer().getInventory().getItem(event.getNewSlot()) != null) {
            if (NBTUtils.hasNBT(newItem, "attribute-strength")) {
                double strength = Double.parseDouble(NBTUtils.getNBT(newItem, "attribute-strength"));
                AttributeModifier modifier = new AttributeModifier("item-strength", strength, AttributeModifier.Operation.ADD_NUMBER);
                attribute.addModifier(modifier);
            }
        }
    }

}

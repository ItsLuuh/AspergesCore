package core.luuh.aspergescore.utils;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.mysql.PUUID;
import core.luuh.aspergescore.mysql.db.Database;
import core.luuh.aspergescore.mysql.model.Profile;
import core.luuh.aspergescore.utils.files.RCUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private final AspergesCore plugin;

    public PlaceholderAPIHook(AspergesCore plugin) {this.plugin = plugin;}

        @Override
        public String getAuthor() {
            return "ItsLuuh";
        }

        @Override
        public String getIdentifier() {
            return "ascore";
        }

        @Override
        public String getVersion() {
            return "1.0.0";
        }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    public static String extractFirstPart(String input) {
        String[] parts = input.split("_");
        if (parts.length > 0) {
            return parts[0];
        }
        return null;  // Restituisce null se la stringa è vuota o non contiene il separatore "_"
    }

    public static String extractSecondPart(String input) {
        String[] parts = input.split("_");
        if (parts.length > 1) {
            return String.join("_", Arrays.copyOfRange(parts, 1, parts.length));
        }
        return null;  // Restituisce null se la stringa è vuota o non contiene il separatore "_" o contiene solo una parte
    }

    public String getPlayerWorld(Player player) {

        String worldName = player.getWorld().getName();

        if(worldName.contains("PrivateIs"))return RCUtils.readString("worlds.privateisland") + "Private Island";
        if(worldName.contains("Dungeon"))return RCUtils.readString("worlds.dungeon") + "Dungeon";

        String s;
        if(RCUtils.readString("worlds." + worldName) != null)s = RCUtils.readString("worlds." + worldName);
        else s = RCUtils.readString("worlds.default");

        return s + worldName;
    }

    public String getStageOfPlayer(int level) {
        String stage = "Unknown";

        ConfigurationSection stagesSection = plugin.getConfig().getConfigurationSection("stages");

        if (stagesSection != null) {
            for (String stageKey : stagesSection.getKeys(false)) {
                String[] levelRange = plugin.getConfig().getString("stages." + stageKey).split("-");

                if (levelRange.length == 2) {
                    int min = Integer.parseInt(levelRange[0]);
                    int max = Integer.parseInt(levelRange[1]);

                    if (level >= min && level <= max) {
                        stage = stageKey;
                        break;
                    }
                }
            }
        }

        return stage;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {

        if(params.equalsIgnoreCase("player_world")) return getPlayerWorld((Player) player);

        String sProfile = extractFirstPart(params.toLowerCase());
        String s = extractSecondPart(params.toLowerCase());
        Profile profile = Database.getPlayerProfileFromPName((Player) player, sProfile.equalsIgnoreCase("current") ? "current" : sProfile);

        switch (s) {
            case "balance":
                return String.valueOf(profile.getPolusBalance());
            case "balance_vault":
                return String.valueOf(profile.getPolusVault());
            case "balance_formatted":
                return NumberFormatter.formatNumberCommas(profile.getPolusBalance());
            case "balance_vault_formatted":
                return NumberFormatter.formatNumberCommas(profile.getPolusVault());
            case "balance_formatted_expanded":
                return NumberFormatter.formatNumber(profile.getPolusBalance());
            case "balance_vault_formatted_expanded":
                return NumberFormatter.formatNumber(profile.getPolusVault());
            case "lvl":
                return String.valueOf(profile.getLvl());
            case "stage":
                return getStageOfPlayer(profile.getLvl());
            case "stage_color":
                return chatcolor.col(RCUtils.readString("stages-color." + getStageOfPlayer(profile.getLvl())));
            case "base_health":
                return String.valueOf(profile.getBaseHealth());
            case "base_strength":
                return String.valueOf(profile.getBaseStrength());
            case "base_intelligence":
                return String.valueOf(profile.getBaseIntelligence());
            case "base_crit_dam":
                return String.valueOf(profile.getBaseCritDam());
            case "base_crit_chance":
                return String.valueOf(profile.getBaseCritChance());
            case "base_health_regen":
                return String.valueOf(profile.getBaseHealthRegen());
            case "base_speed":
                return String.valueOf(profile.getBaseSpeed());
            case "base_luck":
                return String.valueOf(profile.getBaseLuck());
            case "base_faith":
                return String.valueOf(profile.getBaseFaith());
            case "skill_lvl_average":
                return String.valueOf(profile.getSkillLvlAverage());
            case "skill_lvl_farming":
                return String.valueOf(profile.getSkillLvlFarming());
            case "skill_lvl_mining":
                return String.valueOf(profile.getSkillLvlMining());
            case "skill_lvl_combat":
                return String.valueOf(profile.getSkillLvlCombat());
            case "skill_lvl_lumberjack":
                return String.valueOf(profile.getSkillLvlLumberjack());
            case "skill_lvl_enchanting":
                return String.valueOf(profile.getSkillLvlEnchanting());
            case "skill_lvl_runesmith":
                return String.valueOf(profile.getSkillLvlRunesmith());
            case "skill_lvl_taming":
                return String.valueOf(profile.getSkillLvlTaming());
            case "skill_lvl_exorcism":
                return String.valueOf(profile.getSkillLvlExorcism());
            case "skill_xp_farming":
                return String.valueOf(profile.getSkillXpFarming());
            case "skill_xp_mining":
                return String.valueOf(profile.getSkillXpMining());
            case "skill_xp_combat":
                return String.valueOf(profile.getSkillXpCombat());
            case "skill_xp_lumberjack":
                return String.valueOf(profile.getSkillXpLumberjack());
            case "skill_xp_enchanting":
                return String.valueOf(profile.getSkillXpEnchanting());
            case "skill_xp_runesmith":
                return String.valueOf(profile.getSkillXpRunesmith());
            case "skill_xp_taming":
                return String.valueOf(profile.getSkillXpTaming());
            case "skill_xp_exorcism":
                return String.valueOf(profile.getSkillXpExorcism());
            default:
                return "";
        }
    }

}



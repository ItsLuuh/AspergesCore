package core.luuh.aspergescore.dungeon;

import com.infernalsuite.aswm.api.SlimePlugin;
import com.infernalsuite.aswm.api.exceptions.*;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import com.infernalsuite.aswm.api.world.properties.SlimeProperties;
import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap;
import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.party.PartyManager;
import core.luuh.aspergescore.utils.chatcolor;
import core.luuh.aspergescore.utils.files.CEM;
import core.luuh.aspergescore.utils.files.MTUtils;
import core.luuh.verioncore.VerionAPIManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DungeonIstanceManager implements CommandExecutor {

    private static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyz123456789";

    private static final int ID_LENGTH = 8;

    private static final SecureRandom random = new SecureRandom();

    private final PartyManager partyManager = new PartyManager();

    private static Map<Player, SlimeWorld> dungeonWorld = new HashMap<>();

    private final AspergesCore plugin;

    public DungeonIstanceManager(AspergesCore plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {

            VerionAPIManager.logConsole(MTUtils.caseErrorMex(CEM.ERROR_CONSOLE_EXECUTE));

        } else {
            if (args.length == 0) {

                Player player = (Player) sender;
                SlimeWorld world = null;
                try {
                    world = createDungeonInstance(player, "01", "01");
                } catch (IOException | UnknownWorldException | WorldAlreadyExistsException | WorldLockedException |
                         CorruptedWorldException | NewerFormatException e) {
                    e.printStackTrace();
                    player.sendMessage(chatcolor.col("not working, watch the errors in the console."));
                }

                if (world != null) {
                    Location loc = new Location(Bukkit.getWorld(world.getName()), 0.5, 100, 0.5);
                    player.teleportAsync(loc);
                    player.sendMessage(chatcolor.col(MTUtils.readString("dungeon-teleporting-to")));
                    if(partyManager.getPartyOfPlayer(player) != null) {
                        partyManager.getPartyOfPlayer(player).getMembers().forEach(member -> {
                            if(!member.equals(player)) {
                                member.teleportAsync(loc);
                                member.sendMessage(chatcolor.col(MTUtils.readString("dungeon-teleporting-to")));
                            }
                        });
                    }
                }

            }

        }

        return true;
    }

    private static String generateID() {
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public static String[] extractSubstrings(String input) {
        Pattern pattern = Pattern.compile("(\\d{2})");
        Matcher matcher = pattern.matcher(input);

        String[] substrings = new String[3];
        int index = 0;

        while (matcher.find() && index < 2) {
            substrings[index] = matcher.group(1);
            index++;
        }

        while (index < 2) {
            substrings[index] = "";
            index++;
        }

        if (matcher.find()) {
            substrings[2] = input.substring(matcher.end());
        } else {
            substrings[2] = "";
        }

        return substrings;
    }

    private void setSlimePropertyMap(SlimePropertyMap properties){
        properties.setValue(SlimeProperties.ALLOW_MONSTERS, true);
        properties.setValue(SlimeProperties.ALLOW_ANIMALS, true);
        properties.setValue(SlimeProperties.PVP, false);
    }

    private SlimeWorld createDungeonInstance(Player player, String number, String template) throws IOException, UnknownWorldException, WorldAlreadyExistsException, WorldLockedException, CorruptedWorldException, NewerFormatException {

        String worldName = "Dungeon-"+number+"-"+template+"-" + generateID();
        final SlimePlugin slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        SlimeWorld slimeWorld = slimePlugin.loadWorld(slimePlugin.getWorld("Dungeon-"+number+"-"+template).clone(worldName));

        dungeonWorld.put(player, slimeWorld);
        return slimeWorld;
    }

    private void deleteDungeonInstance(SlimeWorld world) throws UnknownWorldException, IOException {

        final SlimePlugin slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        final SlimeLoader sLoader = slimePlugin.getLoader("file");

        dungeonWorld.remove(world);
        sLoader.deleteWorld(world.getName());

    }

}

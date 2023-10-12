package core.luuh.aspergescore.party;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.utils.chatcolor;
import core.luuh.aspergescore.utils.files.CEM;
import core.luuh.aspergescore.utils.files.MTUtils;
import core.luuh.verioncore.VerionAPIManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PartyCommand implements CommandExecutor, TabCompleter {

    private final AspergesCore plugin;
    private final PartyManager partyManager = new PartyManager();

    public PartyCommand(AspergesCore plugin){
        this.plugin = plugin;
    }

    public boolean getPlayerIgnoreInvites(Player player) {
        if(!ignorePartyInvites.containsKey(player)){
            ignorePartyInvites.put(player, false);
        }
        return ignorePartyInvites.get(player);
    }

    public void setPlayerIgnoreInvites(Boolean bool, Player player) {
        ignorePartyInvites.put(player, bool);
    }

    public void removePlayerIgnoreInvites(Player player) {
        ignorePartyInvites.remove(player);
    }

    private Map<Player, Boolean> ignorePartyInvites;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // party list
        // party (player)
        // party decline (player)
        // party accept (player)
        // party leave
        // party leader (player)
        // party kick (player)
        // party disband
        // party ignoreinvites
        // party ignoreinvites (player)

        if(!(sender instanceof Player)){

            VerionAPIManager.logConsole(MTUtils.caseErrorMex(CEM.ERROR_CONSOLE_EXECUTE));

        } else {

            Player player = (Player) sender;
            if(args.length == 0){

                player.sendMessage(chatcolor.col(MTUtils.caseErrorMex(CEM.NOT_ENOUGH_ARGS)));

            } else if(args.length == 1) {

                if(Bukkit.getPlayerExact(args[0]) != null){

                    Player target = Bukkit.getPlayerExact(args[0]);

                    if(getPlayerIgnoreInvites(target)) {

                        player.sendMessage(chatcolor.col(MTUtils.readString("party-invite-target-ignore", target.getName())));

                    } else {

                        if (partyManager.getPartyOfPlayer(player) != null) {

                            if (partyManager.isLeader(player)) {
                                partyManager.getParty(player).addInvitedPlayer(target);

                                player.sendMessage(chatcolor.col(MTUtils.readString("party-invite-inviter", target.getName())));
                                target.sendMessage(chatcolor.col(MTUtils.readString("party-invite-target", player.getName())));
                                target.sendMessage(MTUtils.readComponent("party-invite-target-action", player.getName()));
                            } else {
                                player.sendMessage(chatcolor.col(MTUtils.readString("party-error-not-leader")));
                            }
                        } else {
                            partyManager.createParty(player);
                            player.sendMessage(chatcolor.col(MTUtils.readString("party-created", target.getName())));

                            partyManager.getParty(player).addInvitedPlayer(target);

                            player.sendMessage(chatcolor.col(MTUtils.readString("party-invite-inviter", target.getName())));
                            target.sendMessage(chatcolor.col(MTUtils.readString("party-invite-target", player.getName())));
                            target.sendMessage(MTUtils.readComponent("party-invite-target-action", player.getName()));
                        }
                    }
                } else {

                    switch (args[0]) {

                        case "list":

                            if(partyManager.getPartyOfPlayer(player) != null) {

                                List<Player> playerList = partyManager.getPartyOfPlayer(player).getMembers();
                                List<String> message = MTUtils.readList("party-list");
                                for(String s : message){

                                    if(s.contains("%party_members%")){

                                        for(Player p : playerList){

                                            player.sendMessage(chatcolor.col(s.replace("%party_members%", p.getName())));

                                        }

                                    } else {
                                        player.sendMessage(chatcolor.col(s));
                                    }

                                }

                            } else {
                                player.sendMessage(chatcolor.col(MTUtils.readString("party-error-not-in-party")));
                            }

                            break;

                        case "leave":

                            if(partyManager.getPartyOfPlayer(player) != null){

                                Party party = partyManager.getPartyOfPlayer(player);
                                party.removeMember(player);
                                player.sendMessage(chatcolor.col(MTUtils.readString("party-leave-player")));
                                for(Player p : party.getMembers()){
                                    p.sendMessage(chatcolor.col(MTUtils.readString("party-leave-members", player.getName())));
                                }

                            } else {
                                player.sendMessage(chatcolor.col(MTUtils.readString("party-error-not-in-party")));
                            }

                            break;

                        case "disband":

                            if(partyManager.getPartyOfPlayer(player) != null){

                                if(partyManager.isLeader(player)) {
                                    Party party = partyManager.getPartyOfPlayer(player);
                                    player.sendMessage(chatcolor.col(MTUtils.readString("party-disband-player")));
                                    for (Player p : party.getMembers()) {

                                        p.sendMessage(chatcolor.col(MTUtils.readString("party-disband-members", player.getName())));

                                    }
                                    party.disband();
                                } else {
                                    player.sendMessage(chatcolor.col(MTUtils.readString("party-error-not-leader")));
                                }
                            } else {
                                player.sendMessage(chatcolor.col(MTUtils.readString("party-not-in-party")));
                            }

                            break;

                        case "ignoreinvites":

                            if(getPlayerIgnoreInvites(player)) {

                                setPlayerIgnoreInvites(false, player);
                                player.sendMessage(chatcolor.col(MTUtils.readString("party-ignore-invites-off")));

                            } else {

                                setPlayerIgnoreInvites(true, player);
                                player.sendMessage(chatcolor.col(MTUtils.readString("party-ignore-invites-on")));

                            }
                            break;

                        default:

                            player.sendMessage(chatcolor.col(MTUtils.caseErrorMex(CEM.INVALID_ARG)));

                            break;

                    }
                }

            } else if(args.length == 2) {

                if(args[0].equalsIgnoreCase("kick")){

                    if(partyManager.getPartyOfPlayer(player) != null) {

                        if (partyManager.isLeader(player)) {

                            if (Bukkit.getPlayerExact(args[1]) != null) {

                                Player target = Bukkit.getPlayerExact(args[1]);

                                if(partyManager.getPartyOfPlayer(player) == partyManager.getPartyOfPlayer(target)) {
                                    Party party = partyManager.getPartyOfPlayer(player);
                                    party.removeMember(target);
                                    player.sendMessage(chatcolor.col(MTUtils.readString("party-kick-player", target.getName())));
                                    target.sendMessage(chatcolor.col(MTUtils.readString("party-kick-kicked", player.getName())));
                                    for (Player p : party.getMembers()) {
                                        p.sendMessage(chatcolor.col(MTUtils.readString("party-kick-members", player.getName(), target.getName())));
                                    }
                                } else {
                                    player.sendMessage(chatcolor.col(MTUtils.readString("party-error-not-in-party")));
                                }
                            } else {
                                player.sendMessage(chatcolor.col(MTUtils.caseErrorMex(CEM.INVALID_PLAYER)));
                            }

                        } else {
                            player.sendMessage(chatcolor.col(MTUtils.readString("party-error-not-leader")));
                        }
                    } else {
                        player.sendMessage(chatcolor.col(MTUtils.readString("party-error-not-in-party")));
                    }

                } else if (args[0].equalsIgnoreCase("decline")){

                    if(Bukkit.getPlayerExact(args[1]) != null){
                        Player target = Bukkit.getPlayerExact(args[1]);
                        if(partyManager.getPartyOfPlayer(player) == null) {
                            if (partyManager.getPartyOfPlayer(target) != null) {
                                if (partyManager.getPartyOfPlayer(target).getInvitedPlayers().contains(player)) {

                                    Party party = partyManager.getPartyOfPlayer(target);
                                    party.removeInvitedPlayer(player);
                                    player.sendMessage(chatcolor.col(MTUtils.readString("party-decline-player", target.getName())));
                                    target.sendMessage(chatcolor.col(MTUtils.readString("party-decline-inviter", player.getName())));

                                } else {
                                    player.sendMessage(chatcolor.col(MTUtils.readString("party-error-not-invited")));
                                }
                            } else {
                                player.sendMessage(chatcolor.col(MTUtils.readString("party-error-non-existent-party")));
                            }
                        } else {
                            player.sendMessage(chatcolor.col(MTUtils.readString("party-error-already-in-party")));
                        }
                    } else {
                        player.sendMessage(chatcolor.col(MTUtils.caseErrorMex(CEM.INVALID_PLAYER)));
                    }

                } else if(args[0].equalsIgnoreCase("accept")){

                    if(Bukkit.getPlayerExact(args[1]) != null){
                        Player target = Bukkit.getPlayerExact(args[1]);
                        if(partyManager.getPartyOfPlayer(player) == null) {
                            if (partyManager.getPartyOfPlayer(target) != null) {
                                if (partyManager.getPartyOfPlayer(target).getInvitedPlayers().contains(player)) {
                                    Party party = partyManager.getPartyOfPlayer(target);

                                    player.sendMessage(chatcolor.col(MTUtils.readString("party-accept-player", player.getPlayer().getName())));
                                    for (Player p : party.getMembers()) {
                                        p.sendMessage(chatcolor.col(MTUtils.readString("party-accept-members", player.getPlayer().getName())));
                                    }
                                    party.addMember(player);
                                    party.removeInvitedPlayer(player);

                                } else {
                                    player.sendMessage(chatcolor.col(MTUtils.readString("party-error-not-invited")));
                                }
                            } else {
                                player.sendMessage(chatcolor.col(MTUtils.readString("party-error-non-existent-party")));
                            }
                        } else {
                            player.sendMessage(chatcolor.col(MTUtils.readString("party-error-already-in-party")));
                        }

                    } else {
                        player.sendMessage(chatcolor.col(MTUtils.caseErrorMex(CEM.INVALID_PLAYER)));
                    }

                } else if (args[0].equalsIgnoreCase("leader")) {

                    if (partyManager.getPartyOfPlayer(player) != null) {

                        if (partyManager.isLeader(player)) {

                            if (Bukkit.getPlayerExact(args[1]) != null && partyManager.getPartyOfPlayer(player).getMembers().contains(Bukkit.getPlayerExact(args[1]))) {
                                Player target = Bukkit.getPlayerExact(args[1]);
                                Party party = partyManager.getPartyOfPlayer(player);
                                party.setLeader(target);
                                player.sendMessage(chatcolor.col(MTUtils.readString("party-promoted-player", target.getName())));
                                target.sendMessage(chatcolor.col(MTUtils.readString("party-promoted-target", player.getName())));
                                for (Player p : party.getMembers()) {
                                    p.sendMessage(chatcolor.col(MTUtils.readString("party-promoted-members", player.getName(), target.getName())));
                                }
                            } else {
                                player.sendMessage(chatcolor.col(MTUtils.caseErrorMex(CEM.INVALID_PLAYER)));
                            }

                        } else {
                            player.sendMessage(chatcolor.col(MTUtils.readString("party-error-not-leader")));
                        }
                    } else {
                        player.sendMessage(chatcolor.col(MTUtils.readString("party-error-not-in-party")));
                    }
                } else if(args[0].equalsIgnoreCase("ignoreinvites")) {

                    if(Bukkit.getPlayerExact(args[1]) != null) {

                        Player target = Bukkit.getPlayerExact(args[1]);
                        if (getPlayerIgnoreInvites(target)) {

                            setPlayerIgnoreInvites(false, target);
                            player.sendMessage(chatcolor.col(MTUtils.readString("party-ignore-invites-off-player", target.getName())));
                            target.sendMessage(chatcolor.col(MTUtils.readString("party-ignore-invites-off-target", player.getName())));

                        } else {

                            setPlayerIgnoreInvites(true, target);
                            player.sendMessage(chatcolor.col(MTUtils.readString("party-ignore-invites-on-player", target.getName())));
                            target.sendMessage(chatcolor.col(MTUtils.readString("party-ignore-invites-on-target", player.getName())));

                        }
                    } else {
                        player.sendMessage(chatcolor.col(MTUtils.caseErrorMex(CEM.INVALID_PLAYER)));
                    }

                } else {

                    player.sendMessage(chatcolor.col(MTUtils.caseErrorMex(CEM.INVALID_ARG)));

                }

            } else {

                player.sendMessage(chatcolor.col(MTUtils.caseErrorMex(CEM.INVALID_ARG)));

            }

        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> arguments = new ArrayList<>();

        // party list
        // party (player)
        // party decline (player)
        // party accept (player)
        // party leave
        // party leader (player)
        // party kick (player)
        // party disband
        // party ignoreinvites
        // party ignoreinvites (player)

        if(args.length == 1){

            arguments.add("list");
            arguments.add("leave");
            arguments.add("disband");
            arguments.add("ignoreinvites");
            arguments.add("decline");
            arguments.add("accept");
            arguments.add("leader");
            arguments.add("kick");
            Bukkit.getOnlinePlayers().forEach(player -> arguments.add(player.getName()));

        }

        return arguments;
    }
}

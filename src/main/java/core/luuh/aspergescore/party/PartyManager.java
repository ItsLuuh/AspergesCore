package core.luuh.aspergescore.party;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PartyManager {
    private static Map<Player, Party> parties;

    public PartyManager() {
        parties = new HashMap<>();
    }

    public void createParty(Player leader) {
        Party party = new Party(leader);
        parties.put(leader, party);
    }

    public Party getParty(Player leader) {
        return parties.get(leader);
    }

    public Party getPartyOfPlayer(Player player) {
        for (Party party : parties.values()) {
            if (party.containsPlayer(player)) {
                return party;
            }
        }
        return null;
    }

    public void removeFromParty(Player leader, Player playerToRemove) {
        Party party = parties.get(leader);
        if (party != null) {
            party.removeMember(playerToRemove);
        }
    }

    public boolean isLeader(Player player) {
        return parties.containsKey(player);
    }

    public Player getInvitedPlayer(Player invitedPlayer) {
        for (Party party : parties.values()) {
            if (party.getInvitedPlayers().contains(invitedPlayer)) {
                return invitedPlayer;
            }
        }
        return null;
    }

    public Party getPartyOfInvitedPlayer(Player invitedPlayer) {
        for (Party party : parties.values()) {
            if (party.getInvitedPlayers().contains(invitedPlayer)) {
                return party;
            }
        }
        return null;
    }

    public static void updateInvitedPlayerData(Player invitedPlayer) {
        for (Party party : parties.values()) {
            if (party.getInvitedPlayers().contains(invitedPlayer)) {
                int i = party.getInvitedPlayerData(invitedPlayer);

                if(i == 0)party.getInvitedPlayers().remove(invitedPlayer);
                else party.setInvitedPlayerData(invitedPlayer, i - 1);
            }
        }
    }
}

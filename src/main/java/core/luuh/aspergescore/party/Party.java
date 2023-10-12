package core.luuh.aspergescore.party;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Party {
    private Player leader;
    private List<Player> members;
    private ArrayList<Player> invitedPlayers;
    private Map<Player, Integer> invitedPlayerData;

    public Party(Player leader) {
        this.leader = leader;
        this.members = new ArrayList<>();
        this.members.add(leader);
        this.invitedPlayers = new ArrayList<>();
        this.invitedPlayerData = new HashMap<>();
    }

    public Player getLeader() {
        return leader;
    }

    public boolean isLeader(Player player) {
        return leader.equals(player);
    }

    public void setLeader(Player player) {
        leader = player;
    }

    public List<Player> getMembers() {
        return members;
    }

    public void addMember(Player player) {
        members.add(player);
    }

    public void removeMember(Player player) {
        members.remove(player);
    }

    public boolean containsPlayer(Player player) {
        return leader.equals(player) || members.contains(player);
    }

    public int getSize() {
        return members.size();
    }

    public List<Player> getInvitedPlayers() {
        return invitedPlayers;
    }

    public void removeInvitedPlayer(Player invitedPlayer) {
        invitedPlayers.remove(invitedPlayer);
    }

    public void addInvitedPlayer(Player invitedPlayer) {
        invitedPlayers.add(invitedPlayer);
        invitedPlayerData.put(invitedPlayer, 60);
    }

    public Map<Player, Integer> getInvitedPlayerDataHM() {
        return invitedPlayerData;
    }

    public Integer getInvitedPlayerData(Player player) {
        return invitedPlayerData.get(player);
    }

    public void setInvitedPlayerData(Player player, Integer data) {
        invitedPlayerData.put(player, data);
    }

    public void disband() {
        leader = null;
        members = null;
        invitedPlayers = null;
        invitedPlayerData = null;
    }
}

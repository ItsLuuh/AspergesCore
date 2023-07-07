package core.luuh.aspergescore.mysql.model;

import java.util.Date;

public class PlayerStats {

    private String uuid;
    private long balance;
    private int kills;
    private int deaths;
    private Date lastLogin;

    private Date lastLogout;

    public PlayerStats(String uuid, long balance, int kills, int deaths, Date lastLogin, Date lastLogout) {
        this.uuid = uuid;
        this.balance = balance;
        this.kills = kills;
        this.deaths = deaths;
        this.lastLogin = lastLogin;
        this.lastLogout = lastLogout;
    }

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getLastLogout() {
        return lastLogout;
    }

    public void setLastLogout(Date lastLogout) {
        this.lastLogout = lastLogout;
    }
}

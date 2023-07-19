package core.luuh.aspergescore.mysql.model;

import core.luuh.aspergescore.mysql.PUUID;

import java.util.Date;

public class User {

    private String uuid;
    private String last_puuid;
    private String username;
    private int wipes;
    private Date last_login;
    private Date last_logout;

    public User(String uuid, String last_puuid, String username, int wipes, Date last_login, Date last_logout) {

        this.uuid = uuid;
        this.last_puuid = last_puuid;
        this.username = username;
        this.wipes = wipes;
        this.last_login = last_login;
        this.last_logout = last_logout;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLastPUUID() {
        return last_puuid;
    }

    public void setLastPUUID(String puuid) {
        this.last_puuid = puuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getWipes() {
        return wipes;
    }

    public void setWipes(int wipes) {
        this.wipes = wipes;
    }

    public Date getLastLogin() {
        return last_login;
    }

    public void setLastLogin(Date last_login) {
        this.last_login = last_login;
    }

    public Date getLastLogout() {
        return last_logout;
    }

    public void setLastLogout(Date last_logout) {
        this.last_logout = last_logout;
    }

}

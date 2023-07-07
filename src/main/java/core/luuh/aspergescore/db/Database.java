package core.luuh.aspergescore.db;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.model.PlayerStats;
import core.luuh.verioncore.VerionAPIManager;

import java.sql.*;

public class Database {

    private final AspergesCore plugin;

    public Database(AspergesCore plugin) {this.plugin = plugin;}

    private Connection connection;

    public Connection getConnection()  throws SQLException{

        if(connection != null){
            return connection;
        }

        String url = "jdbc:mysql://localhost/players_stats";
        String user = "root";
        String password = "";

        this.connection = DriverManager.getConnection(url, user, password);

        VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&r &fConnected successfully to DB: &bplayers_stats&f!&r");


        return this.connection;

    }

    public void initializeDatabase() throws SQLException{

            Statement statement = getConnection().createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS player_stats(uuid varchar(36) primary key, balance long, kills int, deaths int, last_login DATE, last_logout DATE)";
            statement.execute(sql);

            statement.close();


            VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&r &fCreated TABLE: &bplayer_stats&f, into DB: &bplayer_stats&f!&r");


    }

    public PlayerStats findPlayerStatsByUUID(String uuid) throws SQLException{

        PreparedStatement statement = getConnection()
                .prepareStatement("SELECT * FROM player_stats WHERE uuid = ?");
        statement.setString(1, uuid);

        ResultSet results = statement.executeQuery();

        if(results.next()){

            long balance = results.getLong("balance");
            int kills = results.getInt("kills");
            int deaths = results.getInt("deaths");
            Date lastLogin = results.getDate("last_login");
            Date lastLogout = results.getDate("last_logout");

            PlayerStats playerStats = new PlayerStats(uuid, balance, kills, deaths, lastLogin, lastLogout);

            statement.close();

            return  playerStats;

        }

        statement.close();

        return  null;


    }

    public  void setPlayerStats(PlayerStats stats) throws SQLException{


        PreparedStatement statement = getConnection()
                .prepareStatement("INSERT INTO player_stats(uuid, balance, kills, deaths, last_login, last_logout) VALUES (?, ?, ?, ?, ?, ?)");

        statement.setString(1, stats.getUUID());
        statement.setLong(2, stats.getBalance());
        statement.setInt(3, stats.getKills());
        statement.setInt(4, stats.getDeaths());
        statement.setDate(5, new Date(stats.getLastLogin().getTime()));
        statement.setDate(6, new Date(stats.getLastLogout().getTime()));

        statement.executeUpdate();
        statement.close();


    }

    public  void updatePlayerStats(PlayerStats stats) throws SQLException{

        PreparedStatement statement = getConnection()
                .prepareStatement("UPDATE player_stats SET balance = ?,kills = ?,deaths = ?,last_login = ?,last_logout = ? WHERE uuid = ?");


        statement.setLong(1, stats.getBalance());
        statement.setInt(2, stats.getKills());
        statement.setInt(3, stats.getDeaths());
        statement.setDate(4, new Date(stats.getLastLogin().getTime()));
        statement.setDate(5, new Date(stats.getLastLogout().getTime()));
        statement.setString(6, stats.getUUID());

        statement.executeUpdate();
        statement.close();


    }

    public void deletePlayerStats(String uuid) throws SQLException{

        PreparedStatement statement = connection
                .prepareStatement("DELETE FROM player_stats WHERE uuid = ?");

        statement.setString(1, uuid);
        statement.executeUpdate();
        statement.close();

    }

    public void closeConnection(){

        try{

            if (this.connection != null){
                this.connection.close();
            }

        }catch (SQLException e){

        }

    }

}

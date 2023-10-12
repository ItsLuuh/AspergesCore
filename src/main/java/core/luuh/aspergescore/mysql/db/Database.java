package core.luuh.aspergescore.mysql.db;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.mysql.PUUID;
import core.luuh.aspergescore.mysql.model.Profile;
import core.luuh.aspergescore.mysql.model.User;
import core.luuh.aspergescore.utils.DBUtils;
import core.luuh.aspergescore.utils.files.RCUtils;
import core.luuh.verioncore.VerionAPIManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.*;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Database {

    private static final AspergesCore plugin = AspergesCore.getInstance();

    private Connection connection;

    public Connection getConnection() throws SQLException {

        if(connection != null){
            return connection;
        }

        String user = RCUtils.readString("storage.username");
        String password = RCUtils.readString("storage.password");
        String address = DBUtils.createAddress(RCUtils.readString("storage.address"));
        String database = RCUtils.readString("storage.database");
        String url = "jdbc:mysql://"+ DBUtils.urlEncode(user)+":"+DBUtils.urlEncode(password)+"@"+address+"/"+database;


        this.connection = DriverManager.getConnection(url, user, password);

        VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&r &fConnected successfully to DB: &bplayers_stats&f!&r");


        return this.connection;

    }

    public void initializeDatabase() throws SQLException{

        Statement statement = getConnection().createStatement();
        String sqlUsers = "CREATE TABLE IF NOT EXISTS " +
                "users(" +
                "uuid varchar(36) primary key," +
                "last_puuid varchar(14)," +
                "username varchar(16)," +
                "wipes int," +
                "last_login DATE," +
                "last_logout DATE" +
                ")";

        String sqlProfiles = "CREATE TABLE IF NOT EXISTS " +
                "profiles(" +
                "uuid varchar(36), " +
                "username varchar(16)," +
                "puuid varchar(14) primary key, " +
                "profile varchar(36), " +
                "lvl smallint, " +
                "polus_balance bigint, " +
                "polus_vault bigint, " +
                "base_health smallint, " +
                "base_strength smallint, " +
                "base_intelligence smallint, " +
                "base_crit_dam smallint, " +
                "base_crit_chance smallint, " +
                "base_health_regen smallint, " +
                "base_speed smallint, " +
                "base_luck smallint, " +
                "base_faith smallint, " +
                "skill_lvl_average tinyint, " +
                "skill_lvl_farming tinyint, " +
                "skill_lvl_mining tinyint, " +
                "skill_lvl_combat tinyint, " +
                "skill_lvl_lumberjack tinyint, " +
                "skill_lvl_enchanting tinyint, " +
                "skill_lvl_runesmith tinyint, " +
                "skill_lvl_taming tinyint, " +
                "skill_lvl_exorcism tinyint, " +
                "skill_xp_farming int, " +
                "skill_xp_mining int, " +
                "skill_xp_combat int, " +
                "skill_xp_lumberjack int, " +
                "skill_xp_enchanting int, " +
                "skill_xp_runesmith int, " +
                "skill_xp_taming int, " +
                "skill_xp_exorcism int" +
                ")";

        statement.execute(sqlUsers);
        statement.execute(sqlProfiles);

        statement.close();


        VerionAPIManager.logConsole("#D60000[#FF0000!#D60000]&r &6ASPERGES-Core&r " + plugin.getVersionPlugin() + "&r &f»&r &fCreated TABLE: &bplayer_stats&f, into DB: &bplayer_stats&f!&r");


    }

    public static Profile getPlayerProfileFromPUUID(Player player) {

        try {
            return plugin.getDatabase().findProfileByPUUID(getLastUsedPUUID(player).getValue());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Profile getPlayerProfileFromPName(Player player, String pname) {

        try {
            if(pname.equalsIgnoreCase("current"))return getPlayerProfileFromPUUID(player);
            return plugin.getDatabase().findProfileByPName(player.getUniqueId(), pname);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PUUID getLastUsedPUUID(Player player){

        return new PUUID(getUserFromDatabase(player).getLastPUUID());

    }

    public static User getUserFromDatabase(Player p) {

        try {
            return plugin.getDatabase().findUserByUUID(p.getUniqueId().toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public Profile findProfileByPUUID(String puuid) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("SELECT * FROM profiles WHERE puuid = ?");
        statement.setString(1, puuid);

        ResultSet results = statement.executeQuery();

        if (results.next()) {
            String profile = results.getString("profile");
            return getProfile(statement, results, profile, puuid);
        }

        statement.close();

        return null;
    }

    public Profile findProfileByPName(UUID uuid, String pname) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("SELECT * FROM profiles WHERE uuid = ? AND profile = ?");
        statement.setString(1, uuid.toString());
        statement.setString(2, pname);

        ResultSet results = statement.executeQuery();

        if (results.next()) {
            String profile = results.getString("profile");
            String puuid = results.getString("puuid");
            return getProfile(statement, results, profile, puuid);
        }

        statement.close();

        return null;
    }

    @NotNull
    private Profile getProfile(PreparedStatement statement, ResultSet results, String profile, String puuid) throws SQLException {
        String uuid = results.getString("uuid");
        String username = results.getString("username");
        int lvl = results.getInt("lvl");
        long polus_balance = results.getLong("polus_balance");
        long polus_vault = results.getLong("polus_vault");
        short base_health = results.getShort("base_health");
        short base_strength = results.getShort("base_strength");
        short base_intelligence = results.getShort("base_intelligence");
        short base_crit_dam = results.getShort("base_crit_dam");
        short base_crit_chance = results.getShort("base_crit_chance");
        short base_health_regen = results.getShort("base_health_regen");
        short base_speed = results.getShort("base_speed");
        short base_luck = results.getShort("base_luck");
        short base_faith = results.getShort("base_faith");
        byte skill_lvl_average = results.getByte("skill_lvl_average");
        byte skill_lvl_farming = results.getByte("skill_lvl_farming");
        byte skill_lvl_mining = results.getByte("skill_lvl_mining");
        byte skill_lvl_combat = results.getByte("skill_lvl_combat");
        byte skill_lvl_lumberjack = results.getByte("skill_lvl_lumberjack");
        byte skill_lvl_enchanting = results.getByte("skill_lvl_enchanting");
        byte skill_lvl_runesmith = results.getByte("skill_lvl_runesmith");
        byte skill_lvl_taming = results.getByte("skill_lvl_taming");
        byte skill_lvl_exorcism = results.getByte("skill_lvl_exorcism");
        int skill_xp_farming = results.getInt("skill_xp_farming");
        int skill_xp_mining = results.getInt("skill_xp_mining");
        int skill_xp_combat = results.getInt("skill_xp_combat");
        int skill_xp_lumberjack = results.getInt("skill_xp_lumberjack");
        int skill_xp_enchanting = results.getInt("skill_xp_enchanting");
        int skill_xp_runesmith = results.getInt("skill_xp_runesmith");
        int skill_xp_taming = results.getInt("skill_xp_taming");
        int skill_xp_exorcism = results.getInt("skill_xp_exorcism");

        Profile profilez = new Profile(uuid, username, puuid, profile, lvl, polus_balance, polus_vault, base_health, base_strength,
                base_intelligence, base_crit_dam, base_crit_chance, base_health_regen, base_speed, base_luck,
                base_faith, skill_lvl_average, skill_lvl_farming, skill_lvl_mining, skill_lvl_combat,
                skill_lvl_lumberjack, skill_lvl_enchanting, skill_lvl_runesmith, skill_lvl_taming,
                skill_lvl_exorcism, skill_xp_farming, skill_xp_mining, skill_xp_combat, skill_xp_lumberjack,
                skill_xp_enchanting, skill_xp_runesmith, skill_xp_taming, skill_xp_exorcism);

        statement.close();

        return profilez;
    }

    public void setProfiles(Profile profile) throws SQLException {
        String query = "INSERT INTO profiles(uuid, username, puuid, profile, lvl, polus_balance, polus_vault, " +
                "base_health, base_strength, base_intelligence, base_crit_dam, base_crit_chance, " +
                "base_health_regen, base_speed, base_luck, base_faith, skill_lvl_average, " +
                "skill_lvl_farming, skill_lvl_mining, skill_lvl_combat, skill_lvl_lumberjack, " +
                "skill_lvl_enchanting, skill_lvl_runesmith, skill_lvl_taming, skill_lvl_exorcism, " +
                "skill_xp_farming, skill_xp_mining, skill_xp_combat, skill_xp_lumberjack, " +
                "skill_xp_enchanting, skill_xp_runesmith, skill_xp_taming, skill_xp_exorcism) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = getConnection().prepareStatement(query);

        statement.setString(1, profile.getUuid());
        statement.setString(2, profile.getUsername());
        statement.setString(3, profile.getPuuid());
        statement.setString(4, profile.getProfile());
        statement.setInt(5, profile.getLvl());
        statement.setLong(6, profile.getPolusBalance());
        statement.setLong(7, profile.getPolusVault());
        statement.setShort(8, profile.getBaseHealth());
        statement.setShort(9, profile.getBaseStrength());
        statement.setShort(10, profile.getBaseIntelligence());
        statement.setShort(11, profile.getBaseCritDam());
        statement.setShort(12, profile.getBaseCritChance());
        statement.setShort(13, profile.getBaseHealthRegen());
        statement.setShort(14, profile.getBaseSpeed());
        statement.setShort(15, profile.getBaseLuck());
        statement.setShort(16, profile.getBaseFaith());
        statement.setByte(17, profile.getSkillLvlAverage());
        statement.setByte(18, profile.getSkillLvlFarming());
        statement.setByte(19, profile.getSkillLvlMining());
        statement.setByte(20, profile.getSkillLvlCombat());
        statement.setByte(21, profile.getSkillLvlLumberjack());
        statement.setByte(22, profile.getSkillLvlEnchanting());
        statement.setByte(23, profile.getSkillLvlRunesmith());
        statement.setByte(24, profile.getSkillLvlTaming());
        statement.setByte(25, profile.getSkillLvlExorcism());
        statement.setInt(26, profile.getSkillXpFarming());
        statement.setInt(27, profile.getSkillXpMining());
        statement.setInt(28, profile.getSkillXpCombat());
        statement.setInt(29, profile.getSkillXpLumberjack());
        statement.setInt(30, profile.getSkillXpEnchanting());
        statement.setInt(31, profile.getSkillXpRunesmith());
        statement.setInt(32, profile.getSkillXpTaming());
        statement.setInt(33, profile.getSkillXpExorcism());

        statement.executeUpdate();
        statement.close();
    }

    public void updateProfile(Profile profile) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("UPDATE profiles SET uuid = ?, username = ?, profile = ?, lvl = ?, polus_balance = ?, polus_vault = ?, " +
                        "base_health = ?, base_strength = ?, base_intelligence = ?, base_crit_dam = ?, " +
                        "base_crit_chance = ?, base_health_regen = ?, base_speed = ?, base_luck = ?, " +
                        "base_faith = ?, skill_lvl_average = ?, skill_lvl_farming = ?, skill_lvl_mining = ?, " +
                        "skill_lvl_combat = ?, skill_lvl_lumberjack = ?, skill_lvl_enchanting = ?, skill_lvl_runesmith = ?, " +
                        "skill_lvl_taming = ?, skill_lvl_exorcism = ?, skill_xp_farming = ?, skill_xp_mining = ?, " +
                        "skill_xp_combat = ?, skill_xp_lumberjack = ?, skill_xp_enchanting = ?, skill_xp_runesmith = ?, " +
                        "skill_xp_taming = ?, skill_xp_exorcism = ? " +
                        "WHERE puuid = ?");

        statement.setString(1, profile.getUuid());
        statement.setString(2, profile.getUsername());
        statement.setString(3, profile.getProfile());
        statement.setInt(4, profile.getLvl());
        statement.setLong(5, profile.getPolusBalance());
        statement.setLong(6, profile.getPolusVault());
        statement.setShort(7, profile.getBaseHealth());
        statement.setShort(8, profile.getBaseStrength());
        statement.setShort(9, profile.getBaseIntelligence());
        statement.setShort(10, profile.getBaseCritDam());
        statement.setShort(11, profile.getBaseCritChance());
        statement.setShort(12, profile.getBaseHealthRegen());
        statement.setShort(13, profile.getBaseSpeed());
        statement.setShort(14, profile.getBaseLuck());
        statement.setShort(15, profile.getBaseFaith());
        statement.setByte(16, profile.getSkillLvlAverage());
        statement.setByte(17, profile.getSkillLvlFarming());
        statement.setByte(18, profile.getSkillLvlMining());
        statement.setByte(19, profile.getSkillLvlCombat());
        statement.setByte(20, profile.getSkillLvlLumberjack());
        statement.setByte(21, profile.getSkillLvlEnchanting());
        statement.setByte(22, profile.getSkillLvlRunesmith());
        statement.setByte(23, profile.getSkillLvlTaming());
        statement.setByte(24, profile.getSkillLvlExorcism());
        statement.setInt(25, profile.getSkillXpFarming());
        statement.setInt(26, profile.getSkillXpMining());
        statement.setInt(27, profile.getSkillXpCombat());
        statement.setInt(28, profile.getSkillXpLumberjack());
        statement.setInt(29, profile.getSkillXpEnchanting());
        statement.setInt(30, profile.getSkillXpRunesmith());
        statement.setInt(31, profile.getSkillXpTaming());
        statement.setInt(32, profile.getSkillXpExorcism());
        statement.setString(33, profile.getPuuid());

        statement.executeUpdate();
        statement.close();
    }

    public void setUsers(User user) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("INSERT INTO users(uuid, last_puuid, username, wipes, last_login, last_logout) " +
                        "VALUES (?, ?, ?, ?, ?, ?)");

        statement.setString(1, user.getUuid());
        statement.setString(2, user.getLastPUUID() == null ? PUUID.generatePUUID().getValue() : user.getLastPUUID());
        statement.setString(3, user.getUsername());
        statement.setInt(4, user.getWipes());
        statement.setDate(5, new Date(user.getLastLogin().getTime()));
        statement.setDate(6, new Date(user.getLastLogout().getTime()));

        statement.executeUpdate();
        statement.close();
    }

    public void updateUsers(User user) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("UPDATE users SET username = ?, last_puuid = ?, wipes = ?, last_login = ?, last_logout = ? " +
                        "WHERE uuid = ?");

        statement.setString(1, user.getUuid());
        statement.setString(2, user.getUsername());
        statement.setInt(3, user.getWipes());
        statement.setDate(4, new Date(user.getLastLogin().getTime()));
        statement.setDate(5, new Date(user.getLastLogout().getTime()));
        statement.setString(6, user.getLastPUUID() == null ? PUUID.generatePUUID().getValue() : user.getLastPUUID());


        statement.executeUpdate();
        statement.close();
    }

    public User findUserByUUID(String uuid) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("SELECT * FROM users WHERE uuid = ?");
        statement.setString(1, uuid);

        ResultSet results = statement.executeQuery();

        if (results.next()) {
            String username = results.getString("username");
            String last_puuid = results.getString("last_puuid");
            Date last_login = results.getDate("last_login");
            Date last_logout = results.getDate("last_logout");

            User user = new User(uuid, last_puuid, username, 0, last_login, last_logout);

            statement.close();

            return user;
        }

        statement.close();

        return null;
    }

    public void deleteProfiles(String uuid) throws SQLException{

        PreparedStatement statement = connection
                .prepareStatement("DELETE FROM profiles WHERE uuid = ?");

        statement.setString(1, uuid);
        statement.executeUpdate();
        statement.close();

    }

    public void deleteUsers(String uuid) throws SQLException{

        PreparedStatement statement = connection
                .prepareStatement("DELETE FROM users WHERE uuid = ?");

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

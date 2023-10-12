package core.luuh.aspergescore.mongodb.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import core.luuh.aspergescore.mysql.model.Profile;
import core.luuh.aspergescore.mysql.model.User;
import core.luuh.aspergescore.utils.DBUtils;
import core.luuh.aspergescore.utils.files.RCUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bukkit.entity.Player;

public class Database {

    private static MongoClient mongoClient;
    private static MongoDatabase database;

    private static synchronized void initConnection() {
        if (mongoClient == null) {
            String username = DBUtils.urlEncode(RCUtils.readString("storage.username"));
            String password = DBUtils.urlEncode(RCUtils.readString("storage.password"));
            String address = DBUtils.createAddress(RCUtils.readString("storage.address"));
            String dbname = RCUtils.readString("storage.database");

            String connectionString = "mongodb://" + username + ":" + password + "@" + address + "/" + dbname;
            mongoClient = MongoClients.create(connectionString);
            database = mongoClient.getDatabase(dbname);
        }
    }

    public static MongoCollection<Document> getStatsCollection(String collectionName) {
        initConnection();
        return database.getCollection(collectionName);
    }

    private static void insertDocument(MongoCollection<Document> collection, Document document) {
        collection.insertOne(document);
    }

    public static void createUserDocument(User user) {
        MongoCollection<Document> collection = getStatsCollection("user_stats");

        Document doc = new Document(
                "_uuid", user.getUuid())
                .append("last_puuid", user.getLastPUUID())
                .append("username", user.getUsername())
                .append("wipes", user.getWipes())
                .append("last_login", user.getLastLogin())
                .append("last_logout", user.getLastLogout());

        insertDocument(collection, doc);
    }

    public static void createProfileDocument(Profile profile) {
        MongoCollection<Document> collection = getStatsCollection("profile_stats");

        Document profileDocument = new Document(
                "uuid", profile.getUuid())
                .append("username", profile.getUsername())
                .append("_puuid", profile.getPuuid())
                .append("profile", profile.getProfile())
                .append("lvl", profile.getLvl())
                .append("polus_balance", profile.getPolusBalance())
                .append("polus_vault", profile.getPolusVault())
                .append("base_health", profile.getBaseHealth())
                .append("base_strength", profile.getBaseStrength())
                .append("base_intelligence", profile.getBaseIntelligence())
                .append("base_crit_dam", profile.getBaseCritDam())
                .append("base_crit_chance", profile.getBaseCritChance())
                .append("base_health_regen", profile.getBaseHealthRegen())
                .append("base_speed", profile.getBaseSpeed())
                .append("base_luck", profile.getBaseLuck())
                .append("base_faith", profile.getBaseFaith())
                .append("skill_lvl_average", profile.getSkillLvlAverage())
                .append("skill_lvl_farming", profile.getSkillLvlFarming())
                .append("skill_lvl_mining", profile.getSkillLvlMining())
                .append("skill_lvl_combat", profile.getSkillLvlCombat())
                .append("skill_lvl_lumberjack", profile.getSkillLvlLumberjack())
                .append("skill_lvl_enchanting", profile.getSkillLvlEnchanting())
                .append("skill_lvl_runesmith", profile.getSkillLvlRunesmith())
                .append("skill_lvl_taming", profile.getSkillLvlTaming())
                .append("skill_lvl_exorcism", profile.getSkillLvlExorcism())
                .append("skill_xp_farming", profile.getSkillXpFarming())
                .append("skill_xp_mining", profile.getSkillXpMining())
                .append("skill_xp_combat", profile.getSkillXpCombat())
                .append("skill_xp_lumberjack", profile.getSkillXpLumberjack())
                .append("skill_xp_enchanting", profile.getSkillXpEnchanting())
                .append("skill_xp_runesmith", profile.getSkillXpRunesmith())
                .append("skill_xp_taming", profile.getSkillXpTaming())
                .append("skill_xp_exorcism", profile.getSkillXpExorcism());

        insertDocument(collection, profileDocument);
    }

    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
        }
    }

}

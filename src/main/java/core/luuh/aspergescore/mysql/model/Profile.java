package core.luuh.aspergescore.mysql.model;

import core.luuh.aspergescore.mysql.PUUID;

public class Profile {

    private String uuid;
    private String username;
    private String puuid;
    private String profile;
    private int lvl;
    private long polus_balance;
    private long polus_vault;
    private short base_health;
    private short base_strength;
    private short base_intelligence;
    private short base_crit_dam;
    private short base_crit_chance;
    private short base_health_regen;
    private short base_speed;
    private short base_luck;
    private short base_faith;
    private byte skill_lvl_average;
    private byte skill_lvl_farming;
    private byte skill_lvl_mining;
    private byte skill_lvl_combat;
    private byte skill_lvl_lumberjack;
    private byte skill_lvl_enchanting;
    private byte skill_lvl_runesmith;
    private byte skill_lvl_taming;
    private byte skill_lvl_exorcism;
    private int skill_xp_farming;
    private int skill_xp_mining;
    private int skill_xp_combat;
    private int skill_xp_lumberjack;
    private int skill_xp_enchanting;
    private int skill_xp_runesmith;
    private int skill_xp_taming;
    private int skill_xp_exorcism;

    public Profile(String uuid, String username, String puuid, String profile, int lvl, long polus_balance, long polus_vault, short base_health,
                   short base_strength, short base_intelligence, short base_crit_dam, short base_crit_chance,
                   short base_health_regen, short base_speed, short base_luck, short base_faith,
                   byte skill_lvl_average, byte skill_lvl_farming, byte skill_lvl_mining, byte skill_lvl_combat,
                   byte skill_lvl_lumberjack, byte skill_lvl_enchanting, byte skill_lvl_runesmith,
                   byte skill_lvl_taming, byte skill_lvl_exorcism, int skill_xp_farming, int skill_xp_mining,
                   int skill_xp_combat, int skill_xp_lumberjack, int skill_xp_enchanting, int skill_xp_runesmith,
                   int skill_xp_taming, int skill_xp_exorcism) {
        this.uuid = uuid;
        this.username = username;
        this.puuid = puuid;
        this.profile = profile;
        this.lvl = lvl;
        this.polus_balance = polus_balance;
        this.polus_vault = polus_vault;
        this.base_health = base_health;
        this.base_strength = base_strength;
        this.base_intelligence = base_intelligence;
        this.base_crit_dam = base_crit_dam;
        this.base_crit_chance = base_crit_chance;
        this.base_health_regen = base_health_regen;
        this.base_speed = base_speed;
        this.base_luck = base_luck;
        this.base_faith = base_faith;
        this.skill_lvl_average = skill_lvl_average;
        this.skill_lvl_farming = skill_lvl_farming;
        this.skill_lvl_mining = skill_lvl_mining;
        this.skill_lvl_combat = skill_lvl_combat;
        this.skill_lvl_lumberjack = skill_lvl_lumberjack;
        this.skill_lvl_enchanting = skill_lvl_enchanting;
        this.skill_lvl_runesmith = skill_lvl_runesmith;
        this.skill_lvl_taming = skill_lvl_taming;
        this.skill_lvl_exorcism = skill_lvl_exorcism;
        this.skill_xp_farming = skill_xp_farming;
        this.skill_xp_mining = skill_xp_mining;
        this.skill_xp_combat = skill_xp_combat;
        this.skill_xp_lumberjack = skill_xp_lumberjack;
        this.skill_xp_enchanting = skill_xp_enchanting;
        this.skill_xp_runesmith = skill_xp_runesmith;
        this.skill_xp_taming = skill_xp_taming;
        this.skill_xp_exorcism = skill_xp_exorcism;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPuuid() {
        return puuid;
    }

    public void setPuuid(String uuid) {
        this.puuid = puuid;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public long getPolusBalance() {
        return polus_balance;
    }

    public void setPolusBalance(long polus_balance) {
        this.polus_balance = polus_balance;
    }

    public long getPolusVault() {
        return polus_vault;
    }

    public void setPolusVault(long polus_vault) {
        this.polus_vault = polus_vault;
    }

    public short getBaseHealth() {
        return base_health;
    }

    public void setBaseHealth(short base_health) {
        this.base_health = base_health;
    }

    public short getBaseStrength() {
        return base_strength;
    }

    public void setBaseStrength(short base_strength) {
        this.base_strength = base_strength;
    }

    public short getBaseIntelligence() {
        return base_intelligence;
    }

    public void setBaseIntelligence(short base_intelligence) {
        this.base_intelligence = base_intelligence;
    }

    public short getBaseCritDam() {
        return base_crit_dam;
    }

    public void setBaseCritDam(short base_crit_dam) {
        this.base_crit_dam = base_crit_dam;
    }

    public short getBaseCritChance() {
        return base_crit_chance;
    }

    public void setBaseCritChance(short base_crit_chance) {
        this.base_crit_chance = base_crit_chance;
    }

    public short getBaseHealthRegen() {
        return base_health_regen;
    }

    public void setBaseHealthRegen(short base_health_regen) {
        this.base_health_regen = base_health_regen;
    }

    public short getBaseSpeed() {
        return base_speed;
    }

    public void setBaseSpeed(short base_speed) {
        this.base_speed = base_speed;
    }

    public short getBaseLuck() {
        return base_luck;
    }

    public void setBaseLuck(short base_luck) {
        this.base_luck = base_luck;
    }

    public short getBaseFaith() {
        return base_faith;
    }

    public void setBaseFaith(short base_faith) {
        this.base_faith = base_faith;
    }

    public byte getSkillLvlAverage() {
        return skill_lvl_average;
    }

    public void setSkillLvlAverage(byte skill_lvl_average) {
        this.skill_lvl_average = skill_lvl_average;
    }

    public byte getSkillLvlFarming() {
        return skill_lvl_farming;
    }

    public void setSkillLvlFarming(byte skill_lvl_farming) {
        this.skill_lvl_farming = skill_lvl_farming;
    }

    public byte getSkillLvlMining() {
        return skill_lvl_mining;
    }

    public void setSkillLvlMining(byte skill_lvl_mining) {
        this.skill_lvl_mining = skill_lvl_mining;
    }

    public byte getSkillLvlCombat() {
        return skill_lvl_combat;
    }

    public void setSkillLvlCombat(byte skill_lvl_combat) {
        this.skill_lvl_combat = skill_lvl_combat;
    }

    public byte getSkillLvlLumberjack() {
        return skill_lvl_lumberjack;
    }

    public void setSkillLvlLumberjack(byte skill_lvl_lumberjack) {
        this.skill_lvl_lumberjack = skill_lvl_lumberjack;
    }

    public byte getSkillLvlEnchanting() {
        return skill_lvl_enchanting;
    }

    public void setSkillLvlEnchanting(byte skill_lvl_enchanting) {
        this.skill_lvl_enchanting = skill_lvl_enchanting;
    }

    public byte getSkillLvlRunesmith() {
        return skill_lvl_runesmith;
    }

    public void setSkillLvlRunesmith(byte skill_lvl_runesmith) {
        this.skill_lvl_runesmith = skill_lvl_runesmith;
    }

    public byte getSkillLvlTaming() {
        return skill_lvl_taming;
    }

    public void setSkillLvlTaming(byte skill_lvl_taming) {
        this.skill_lvl_taming = skill_lvl_taming;
    }

    public byte getSkillLvlExorcism() {
        return skill_lvl_exorcism;
    }

    public void setSkillLvlExorcism(byte skill_lvl_exorcism) {
        this.skill_lvl_exorcism = skill_lvl_exorcism;
    }

    public int getSkillXpFarming() {
        return skill_xp_farming;
    }

    public void setSkillXpFarming(int skill_xp_farming) {
        this.skill_xp_farming = skill_xp_farming;
    }

    public int getSkillXpMining() {
        return skill_xp_mining;
    }

    public void setSkillXpMining(int skill_xp_mining) {
        this.skill_xp_mining = skill_xp_mining;
    }

    public int getSkillXpCombat() {
        return skill_xp_combat;
    }

    public void setSkillXpCombat(int skill_xp_combat) {
        this.skill_xp_combat = skill_xp_combat;
    }

    public int getSkillXpLumberjack() {
        return skill_xp_lumberjack;
    }

    public void setSkillXpLumberjack(int skill_xp_lumberjack) {
        this.skill_xp_lumberjack = skill_xp_lumberjack;
    }

    public int getSkillXpEnchanting() {
        return skill_xp_enchanting;
    }

    public void setSkillXpEnchanting(int skill_xp_enchanting) {
        this.skill_xp_enchanting = skill_xp_enchanting;
    }

    public int getSkillXpRunesmith() {
        return skill_xp_runesmith;
    }

    public void setSkillXpRunesmith(int skill_xp_runesmith) {
        this.skill_xp_runesmith = skill_xp_runesmith;
    }

    public int getSkillXpTaming() {
        return skill_xp_taming;
    }

    public void setSkillXpTaming(int skill_xp_taming) {
        this.skill_xp_taming = skill_xp_taming;
    }

    public int getSkillXpExorcism() {
        return skill_xp_exorcism;
    }

    public void setSkillXpExorcism(int skill_xp_exorcism) {
        this.skill_xp_exorcism = skill_xp_exorcism;
    }

}

package core.luuh.aspergescore.utils.files;

import core.luuh.aspergescore.AspergesCore;
import core.luuh.aspergescore.utils.chatcolor;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MTUtils {

    // Messages Translate Utils

    private static final MexFileManager mfM = MexFileManager.getInstance();

    @NotNull
    public static String caseErrorMex(CEM caseError){

        switch (caseError){

            case ERROR_CONSOLE_EXECUTE:
                return AspergesCore.replaceCustomPlaceHolders(readString("error-console-execute"));
            case NOT_ENOUGH_ARGUMENTS:
            case NOT_ENOUGH_ARGS:
                return AspergesCore.replaceCustomPlaceHolders(readString("not-enough-arguments"));
            case NOT_ENOUGH_PERMS:
            case NOT_ENOUGH_PERMISSIONS:
                return AspergesCore.replaceCustomPlaceHolders(readString("not-enough-perms"));
            case INVALID_PLAYER:
                return AspergesCore.replaceCustomPlaceHolders(readString("invalid-player"));
            case INVALID_ARG:
            case INVALID_ARGS:
                return AspergesCore.replaceCustomPlaceHolders(readString("invalid-arg"));

        }

        return null;
    }

    public static List<ConfigurationSection> readCS(String path){

        List<ConfigurationSection> csList = new ArrayList<>();

        ConfigurationSection cs = mfM.getMessages().getConfigurationSection(path);
        for (String s : cs.getKeys(false)) {

            csList.add(mfM.getMessages().getConfigurationSection(s));

        }

        return csList;
    }

    public static List<String> readList(String path) {
        return mfM.getMessages().getStringList(path);
    }

    public static Boolean readBool(String path){
        return mfM.getMessages().getBoolean(path);
    }

    public static String readString(String path){
        return AspergesCore.replaceCustomPlaceHolders(mfM.getMessages().getString(path));
    }

    public static Component readComponent(String path){
        return chatcolor.mm(AspergesCore.replaceCustomPlaceHolders(mfM.getMessages().getString(path)));
    }

    public static String readString(String path, Player player){
        return AspergesCore.replacePlayerPHolders(AspergesCore.replaceCustomPlaceHolders(mfM.getMessages().getString(path)), player);
    }

    public static Component readComponent(String path, Player player){
        return chatcolor.mm(AspergesCore.replacePlayerPHolders(AspergesCore.replaceCustomPlaceHolders(mfM.getMessages().getString(path)), player));
    }

    public static String readString(String path, String arg_one){
        return AspergesCore.replaceCustomPlaceHolders(mfM.getMessages().getString(path).replaceAll("%arg1%", arg_one));
    }

    public static Component readComponent(String path, String arg_one){
        return chatcolor.mm(AspergesCore.replaceCustomPlaceHolders(mfM.getMessages().getString(path)));
    }


    public static String readString(String path, String arg_one, String arg_two){
        return AspergesCore.replaceCustomPlaceHolders(mfM.getMessages().getString(path).replaceAll("%arg1%", arg_one).replaceAll("%arg2%", arg_two));
    }

    public static Component readComponent(String path, String arg_one, String arg_two){
        return chatcolor.mm(AspergesCore.replaceCustomPlaceHolders(mfM.getMessages().getString(path)));
    }


    public static Integer readInt(String path){
        return mfM.getMessages().getInt(path);
    }

    public static void set(String path, Object value){
        mfM.getMessages().set(path, value);
        mfM.saveData();

    }
    
}

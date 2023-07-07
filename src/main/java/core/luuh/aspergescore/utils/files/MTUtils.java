package core.luuh.aspergescore.utils.files;

import core.luuh.aspergescore.AspergesCore;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class MTUtils {


//    .replaceAll("%version%", mfM.getVersionmfM()
    // Messages Translate Utils

    private static final MexFileManager mfM = MexFileManager.getInstance();

    public static String caseErrorMex(String caseError){

        caseError = AspergesCore.replaceCustomPlaceHolders(caseError);

        switch (caseError){

            case "error-console-execute":
                return readString("error-console-execute");
            case "not-enough-arguments":
            case "not-enough-args":
                return readString("not-enough-arguments");
            case "not-enough-perms":
            case "not-enough-permissions":
                return readString("not-enough-perms");
            case "invalid-player":
                return readString("invalid-player");
            case "invalid-arg":
            case "invalid-args":
                return readString("invalid-arg");

        }

        return caseError;
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

    public static Integer readInt(String path){
        return mfM.getMessages().getInt(path);
    }

    public static void set(String path, Object value){
        mfM.getMessages().set(path, value);
        mfM.saveData();

    }
    
}

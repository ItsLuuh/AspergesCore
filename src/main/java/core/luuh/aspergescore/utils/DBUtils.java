package core.luuh.aspergescore.utils;

import core.luuh.aspergescore.utils.files.RCUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBUtils {

    public static String urlEncode(String input) {
        try{
            return URLEncoder.encode(input, "UTF-8");
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean hasPort(String input) {
        Pattern pattern = Pattern.compile("127\\.0\\.0\\.1:\\d+");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static String extractPort(String input) {
        Pattern pattern = Pattern.compile("127\\.0\\.0\\.1:(\\d+)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String createAddress(String input) {

        if(hasPort(input)){
            return input;
        } else {
            switch (RCUtils.readString("storage.type")){
                case "mysql":
                    return input+":3306";
                case "mongodb":
                    return input+":27017";
                default:
                    return input;
            }
        }

    }

}

package core.luuh.aspergescore.mysql;

import core.luuh.aspergescore.AspergesCore;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class PUUID {

    private final AspergesCore plugin = AspergesCore.getInstance();

    private String value;

    public PUUID(String value) {
        this.value = value;
    }

    public static PUUID generatePUUID() {
        StringBuilder sb = new StringBuilder();
        String allowedChars = "123456789abcdefghijklmnopqrstuvwxyz";

        for (int i = 0; i < 12; i++) {
            int randomIndex = (int) (Math.random() * allowedChars.length());
            char randomChar = allowedChars.charAt(randomIndex);
            sb.append(randomChar);
        }

        String puuid = sb.toString();
        StringBuilder formattedPuuid = new StringBuilder();

        for (int i = 0; i < puuid.length(); i++) {
            formattedPuuid.append(puuid.charAt(i));
            if ((i + 1) % 4 == 0 && i != puuid.length() - 1) {
                formattedPuuid.append("-");
            }
        }

        return new PUUID(formattedPuuid.toString());
    }

    public String getValue() {
        return value;
    }

}

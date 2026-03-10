package host.plas.cmiaddons.papi;

import host.plas.bou.compat.CompatManager;
import host.plas.bou.compat.papi.PAPICompat;
import org.bukkit.OfflinePlayer;

public class PapiManager {
    public static boolean isEnabled() {
        return CompatManager.isPAPIEnabled();
    }

    public static String parse(String from) {
        if (! isEnabled()) return from;

        return PAPICompat.replace(from);
    }

    public static String parse(OfflinePlayer player, String from) {
        if (! isEnabled()) return from;

        return PAPICompat.replace(player, from);
    }
}

package host.plas.cmiaddons.utils;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import com.comphenix.protocol.wrappers.*;
import gg.drak.thebase.async.AsyncUtils;
import host.plas.bou.scheduling.TaskManager;
import host.plas.bou.utils.ColorUtils;
import host.plas.bou.utils.PlayerUtils;
import host.plas.bou.utils.UUIDFetcher;
import host.plas.bou.utils.UuidUtils;
import host.plas.cmiaddons.CMIAddons;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

public class PlayerUtil {
    public static void onUpdatePlayerName(CMIUser user, String changeTo) {
        CMIAddons.getInstance().logInfo("Updating player name for " + user.getName() + " to " + changeTo);

        OfflinePlayer player = user.getOfflinePlayer();
        if (player == null || !player.isOnline()) return;

        Player p = (Player) player;
        String colorized = colorize(changeTo);
        String noColor = ColorUtils.stripFormatting(changeTo);

        // Update tab list name
        if (CMIAddons.getMainConfig().isUpdateTabListOnNickChange()) {
            p.setPlayerListName(colorized);
        }

        // Update display name and nameplate
        if (CMIAddons.getMainConfig().isUpdateDisplayNameOnNickChange()) {
            p.setDisplayName(colorized); // For chat and other displays
            p.setCustomName(noColor); // For nameplate (plain text)
            p.setCustomNameVisible(true); // Ensure nameplate is visible
            user.setNickName(colorized, true); // Sync with CMI nickname system

            try {
                PlayerUtils.updatePlayerName(p, changeTo, true, CMIAddons.getMainConfig().isUpdateTabListOnNickChange());
            } catch (Exception e) {
                CMIAddons.getInstance().logWarningWithInfo("Failed to change player name via BOU: ", e);
            }

            user.updateDisplayName(true); // Force CMI to update internal state
        }

        AsyncUtils.runAsync(() -> updateSkin(user, changeTo), 20);
        updateSkin(user, changeTo);

        // Force update for all clients, including the player
        updatePlayerEntity(p);

        updateSkin(user, changeTo);
    }

    public static void updateSkin(CMIUser user, String changeTo) {
        OfflinePlayer player = user.getOfflinePlayer();
        if (player == null || !player.isOnline()) return;

        Player p = (Player) player;
        String noColor = ColorUtils.stripFormatting(changeTo);

        // Update skin (if applicable)
        if (CMIAddons.getMainConfig().isUpdateSkinOnNickChange()) {
            AsyncUtils.executeAsync(() -> {
                if (! UuidUtils.isValidPlayerName(noColor)) {
                    CMIAddons.getInstance().logWarning("Cannot update skin for " + user.getName() + " to invalid name: " + noColor);
                } else {
                    try {
                        UUID uuid = UUIDFetcher.getUUID(noColor);
                        PlayerUtils.getProfile(p).ifPresent(profile -> {
                            if (! CMI.getInstance().getSkinManager().setSkin(profile, uuid)) {
                                CMIAddons.getInstance().logWarning("Failed to update skin for " + user.getName() + " to " + noColor);
                            }
                        });
                    } catch (Throwable e) {
                        CMIAddons.getInstance().logWarningWithInfo("Error while updating skin for " + user.getName() + " to " + noColor + ": ", e);
                    }
                }
            });
        }
    }

    // Force update the player entity to refresh nameplate for all clients
    public static void updatePlayerEntity(Player player) {
        TaskManager.runTaskLater(player, () -> {
            // Hide and show the player to refresh entity data
            forAllPlayers(onlinePlayer -> {
                onlinePlayer.hidePlayer(CMIAddons.getInstance(), player);
                onlinePlayer.showPlayer(CMIAddons.getInstance(), player);
            });
        }, 3);
    }

    public static void checkAndUpdate(Player player) {
        CMIUser user = CMI.getInstance().getPlayerManager().getUser(player);
        if (user == null) return;

        String changeTo = user.getNickName();
        if (changeTo == null || changeTo.isEmpty()) {
            changeTo = player.getName(); // Fallback to original name if nickname is unset
        }
        String colorized = colorize(changeTo);
        String noColor = ColorUtils.stripFormatting(changeTo);
        boolean needsUpdate = false;

        if (CMIAddons.getMainConfig().isUpdateDisplayNameOnNickChange()) {
            if (! colorized.equals(player.getDisplayName()) || !noColor.equals(player.getCustomName())) {
                needsUpdate = true;
            }
        }

        if (CMIAddons.getMainConfig().isUpdateTabListOnNickChange()) {
            if (! colorized.equals(player.getPlayerListName())) {
                needsUpdate = true;
            }
        }

        if (CMIAddons.getMainConfig().isUpdateSkinOnNickChange()) {
            if (! noColor.equals(user.getSkin())) {
                needsUpdate = true;
            }
        }

        if (needsUpdate) {
            onUpdatePlayerName(user, changeTo);
        }
    }

    public static String colorize(String value) {
        return ColorUtils.colorizeHard(value);
    }

    public static void forAllPlayers(Consumer<Player> consumer) {
        Bukkit.getOnlinePlayers().forEach(consumer);
    }

    public static void changePlayerName(Player player, String newName) {
    }
}
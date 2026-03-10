package host.plas.cmiaddons.events;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import com.Zrips.CMI.events.CMIPlayerNickNameChangeEvent;
import host.plas.bou.scheduling.TaskManager;
import host.plas.cmiaddons.CMIAddons;
import host.plas.cmiaddons.papi.PapiManager;
import host.plas.cmiaddons.utils.PlayerUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class MainListener extends AbstractConglomerate {
    @EventHandler
    public void onNick(CMIPlayerNickNameChangeEvent event) {
        CMIUser user = event.getUser();
        String nick = event.getNickName();

        PlayerUtil.onUpdatePlayerName(user, nick);
    }

    @EventHandler
    public void onQuit(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerUtil.checkAndUpdate(player);
    }
}

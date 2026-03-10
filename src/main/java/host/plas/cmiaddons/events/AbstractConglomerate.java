package host.plas.cmiaddons.events;

import gg.drak.thebase.events.BaseEventHandler;
import host.plas.bou.events.ListenerConglomerate;
import host.plas.cmiaddons.CMIAddons;
import org.bukkit.Bukkit;

public class AbstractConglomerate implements ListenerConglomerate {
    public AbstractConglomerate() {
        register();
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, CMIAddons.getInstance());
        BaseEventHandler.bake(this, CMIAddons.getInstance());
        CMIAddons.getInstance().logInfo("Registered listeners for: &c" + this.getClass().getSimpleName());
    }
}

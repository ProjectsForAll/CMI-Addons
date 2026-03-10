package host.plas.cmiaddons.events.own;

import gg.drak.thebase.events.components.BaseEvent;
import host.plas.bou.BukkitOfUtils;
import host.plas.cmiaddons.CMIAddons;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OwnEvent extends BaseEvent {
    public OwnEvent() {
        super();
    }

    public CMIAddons getPlugin() {
        return CMIAddons.getInstance();
    }

    public BukkitOfUtils getBou() {
        return BukkitOfUtils.getInstance();
    }
}

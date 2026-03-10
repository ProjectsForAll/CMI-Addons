package host.plas.cmiaddons.config;

import gg.drak.thebase.storage.resources.flat.simple.SimpleConfiguration;
import host.plas.cmiaddons.CMIAddons;

public class MainConfig extends SimpleConfiguration {
    public MainConfig() {
        super("config.yml", CMIAddons.getInstance(), true);
    }

    @Override
    public void init() {
        isUpdateTabListOnNickChange();
        isUpdateDisplayNameOnNickChange();
        isUpdateSkinOnNickChange();
        getChangeToOnNickChange();
    }

    public boolean isUpdateTabListOnNickChange() {
        reloadResource();

        return getOrSetDefault("on-nick-change.update-tablist", true);
    }

    public boolean isUpdateDisplayNameOnNickChange() {
        reloadResource();

        return getOrSetDefault("on-nick-change.update-displayname", true);
    }

    public boolean isUpdateSkinOnNickChange() {
        reloadResource();

        return getOrSetDefault("on-nick-change.update-skin", true);
    }

    public String getChangeToOnNickChange() {
        reloadResource();

        return getOrSetDefault("on-nick-change.change-to", "%passed%");
    }
}

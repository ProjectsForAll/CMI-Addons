package host.plas.cmiaddons;

import host.plas.bou.BetterPlugin;
import host.plas.cmiaddons.config.MainConfig;
import host.plas.cmiaddons.events.MainListener;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class CMIAddons extends BetterPlugin {
    @Getter @Setter
    private static CMIAddons instance;
    @Getter @Setter
    private static MainConfig mainConfig;

    @Getter @Setter
    private static MainListener mainListener;

    public CMIAddons() {
        super();
    }

    @Override
    public void onBaseEnabled() {
        // Plugin startup logic
        setInstance(this); // Set the instance of the plugin. // For use in other classes.

        setMainConfig(new MainConfig()); // Instantiate the main config and set it.

        setMainListener(new MainListener()); // Instantiate the main listener and set it.
    }

    @Override
    public void onBaseDisable() {
        // Plugin shutdown logic
    }
}

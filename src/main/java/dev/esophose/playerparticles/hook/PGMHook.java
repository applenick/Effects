package dev.esophose.playerparticles.hook;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PGMHook {

    private static Boolean enabled;

    /**
     * @return true if PGM is enabled, otherwise false
     */
    private static boolean enabled() {
        if (enabled == null) {
            enabled = Bukkit.getPluginManager().getPlugin("PGM") != null;
        }
        return enabled;
    }

    public static boolean isParticipating(Player player) {
        if(!enabled()) return false;
        return player.hasMetadata("isPlaying");
    }

}

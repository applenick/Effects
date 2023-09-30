package dev.esophose.playerparticles.hook;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import tc.oc.occ.afk.AFKPlugin;

public class AFKHook {
    
    private static Boolean enabled;

    /**
     * @return true if AFK is enabled, otherwise false
     */
    private static boolean enabled() {
        if (enabled == null) {
            enabled = Bukkit.getPluginManager().getPlugin("AFK") != null && AFKPlugin.get() != null;
        }
        return enabled;
    }

    
    public static boolean isAFK(Player player) {
        if(!enabled()) return false;        
        AFKPlugin afk = AFKPlugin.get();
        return afk.getManager().isAFK(player);
    }

}

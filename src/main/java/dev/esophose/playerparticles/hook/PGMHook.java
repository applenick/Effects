package dev.esophose.playerparticles.hook;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import tc.oc.pgm.api.PGM;
import tc.oc.pgm.api.match.Match;
import tc.oc.pgm.api.match.MatchManager;

public class PGMHook {

    private static Boolean enabled;

    /**
     * @return true if PGM is enabled, otherwise false
     */
    private static boolean enabled() {
        if (enabled == null) {
            enabled = Bukkit.getPluginManager().getPlugin("PGM") != null && PGM.get() != null;
        }
        return enabled;
    }

    public static boolean isParticipating(Player player) {
        if(!enabled()) return false;
        
        Match match = getMatch(player);
        
        if(match != null) {
            return !match.isFinished() && match.getParticipant(player) != null;
        }
        
        return false;
    }

    @Nullable
    private static Match getMatch(Player player) {
        if(player == null || !player.isOnline()) return null;
        
        if(PGM.get().getMatchManager() != null) {
            MatchManager mm = PGM.get().getMatchManager();
            Match match = mm.getMatch(player);
            
            if(match != null) {
                return match;
            }
        }
        return null;
    }

}

package dev.esophose.playerparticles.hook;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import tc.oc.pgm.api.PGM;
import tc.oc.pgm.api.match.Match;

public class PGMHook {
	
	private static Boolean enabled;
	
	public static boolean enabled() {
		if(enabled != null) {
			return enabled;
		}
		return enabled = Bukkit.getPluginManager().getPlugin("PGM") != null;
	}
	
	public static boolean isParticipating(Player player) {
		Match match = PGM.get().getMatchManager().getMatch(player);
		if(match != null) {
			return match.getParticipant(player) != null;
		}
		return false;
	}

}

package com.esophose.playerparticles.command;

import java.util.List;

import org.bukkit.entity.Player;

import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.particles.PPlayer;

public class StylesCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        Player p = pplayer.getPlayer();

        List<String> styleNames = PermissionManager.getStylesUserHasPermissionFor(p);
        String toSend = "";
        for (String name : styleNames) {
            toSend += name + ", ";
        }
        
        if (toSend.endsWith(", ")) {
            toSend = toSend.substring(0, toSend.length() - 2);
        }

        LangManager.sendMessage(p, Lang.STYLE_LIST, toSend);
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return null;
    }

    public String getName() {
        return "styles";
    }

    public Lang getDescription() {
        return Lang.COMMAND_DESCRIPTION_STYLES;
    }

    public String getArguments() {
        return "";
    }

    public boolean requiresEffects() {
        return false;
    }

}

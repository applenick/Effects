package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.CommandManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.particles.PPlayer;
import java.util.ArrayList;
import java.util.List;

public class HelpCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        boolean isConsole = pplayer.getPlayer() == null;

        localeManager.sendMessage(pplayer, "command-descriptions");
        List<CommandModule> cmds = PlayerParticles.getInstance().getManager(CommandManager.class).getCommands();
        for (CommandModule cmd : cmds)
            if (!(cmd instanceof DefaultCommandModule) && (!isConsole || cmd.canConsoleExecute()))
                CommandModule.printUsageWithDescription(pplayer, cmd);

        localeManager.sendSimpleMessage(pplayer, "command-descriptions-help-other");
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<>();
    }

    public String getName() {
        return "help";
    }

    public String getDescriptionKey() {
        return "command-description-help";
    }

    public String getArguments() {
        return "";
    }

    public boolean requiresEffectsAndStyles() {
        return true;
    }

    public boolean canConsoleExecute() {
        return true;
    }

}

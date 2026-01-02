package org.example.artyom.opactabs.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import xaero.pac.common.server.api.OpenPACServerAPI;

public class TabUtil {

    private static final String TEAM_PREFIX = "opac_party_";

    public static void updatePlayer(ServerPlayer player, OpenPACServerAPI api) {
        Scoreboard scoreboard = player.getScoreboard();
        String playerName = player.getScoreboardName();

        var party = api.getPartyManager().getPartyByMember(player.getUUID());

        String teamName;
        Component prefix;

        if (party != null) {
            teamName = TEAM_PREFIX + party.getId();
            prefix = Component.literal("§7[" + party.getDefaultName() + "] ");
        } else {
            teamName = TEAM_PREFIX + "solo";
            prefix = Component.literal("§7[Solo] ");
        }

        // Удаляем из старых команд (из старой партии)
        removeFromOurTeams(scoreboard, playerName);

        PlayerTeam team = scoreboard.getPlayerTeam(teamName);
        if (team == null) {
            team = scoreboard.addPlayerTeam(teamName);
            team.setPlayerPrefix(prefix);
        }

        scoreboard.addPlayerToTeam(playerName, team);
    }

    public static void cleanupPlayer(ServerPlayer player, OpenPACServerAPI api) {
        removeFromOurTeams(player.getScoreboard(), player.getScoreboardName());
    }

    private static void removeFromOurTeams(Scoreboard scoreboard, String playerName) {
        for (PlayerTeam team : scoreboard.getPlayerTeams()) {
            if (!team.getName().startsWith(TEAM_PREFIX)) continue;

            if (team.getPlayers().contains(playerName)) {
                scoreboard.removePlayerFromTeam(playerName, team);
            }
        }
    }
}
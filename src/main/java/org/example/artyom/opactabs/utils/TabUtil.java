package org.example.artyom.opactabs.utils;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import dev.ftb.mods.ftbteams.api.Team;
import org.example.artyom.customwars.saveddata.PartyData;
import xaero.pac.common.server.api.OpenPACServerAPI;

import java.util.Optional;
import java.util.UUID;

public class TabUtil {

    private static final String TEAM_PREFIX = "opac_party_";

    public static void updatePlayer(ServerPlayer player, MinecraftServer server, PartyData partyData) {
        ServerScoreboard scoreboard = server.getScoreboard();
        String playerName = player.getScoreboardName();

        // Получаем команду игрока
        Optional<Team> partyOpt = FTBTeamsAPI.api()
                .getManager()
                .getTeamForPlayerID(player.getUUID());



        String scoreboardTeamName;
        Component prefix;
        if (partyOpt.isPresent() && partyOpt.get().isPartyTeam()) {
            Team party = partyOpt.get();

            // UUID команды удобно использовать как уникальный ключ
            UUID id = party.getId(); // если в твоей версии метод называется иначе — скажи, подстрою
            scoreboardTeamName = TEAM_PREFIX + id;
            String score = partyData.getPoints(party.getId()) + "";
            // Отображаемое имя партии
            prefix = Component.literal("[")
                    .append(party.getColoredName())   // сохраняет цвет/стиль
                    .append(Component.literal("]"))
                    .append(Component.literal("["))
                    .append(Component.literal(score))
                    .append(Component.literal("] "));
            // возможно getDisplayName() в твоей версии

        } else {
            scoreboardTeamName = TEAM_PREFIX + "solo";
            prefix = Component.literal("");
        }

        // 3) Удаляем игрока из старых наших scoreboard-команд
        removeFromOurTeams(scoreboard, playerName);

        // 4) Создаём/обновляем scoreboard-team и добавляем игрока
        PlayerTeam sbTeam = scoreboard.getPlayerTeam(scoreboardTeamName);
        if (sbTeam == null) {
            sbTeam = scoreboard.addPlayerTeam(scoreboardTeamName);
        }
        sbTeam.setPlayerPrefix(prefix);

        scoreboard.addPlayerToTeam(playerName, sbTeam);
    }

    public static void cleanupPlayer(ServerPlayer player, OpenPACServerAPI api) {
        removeFromOurTeams(player.getScoreboard(), player.getScoreboardName());
    }

    public static Optional<Team> getTeam(ServerPlayer player) {
        Optional<Team> teamOpt = FTBTeamsAPI.api().getManager().getTeamForPlayer(player);
        return teamOpt;
    }

    public static Optional<Team> getTeam(UUID playerId) {
        return FTBTeamsAPI.api()
                .getManager()
                .getTeamForPlayerID(playerId); // Optional<Team>
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
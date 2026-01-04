package org.example.artyom.opactabs.events;

import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.event.TeamEvent;
import dev.ftb.mods.ftbteams.api.event.TeamPropertiesChangedEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.example.artyom.opactabs.utils.TabUtil;
import xaero.pac.common.server.api.OpenPACServerAPI;

import java.util.UUID;

public class MyFTBTeamsHooks {
    public static void init() {

        TeamEvent.PLAYER_JOINED_PARTY.register(event -> {
            var player = event.getPlayer();

            MinecraftServer server = player.getServer();
            if (server == null) return;

            OpenPACServerAPI api = OpenPACServerAPI.get(server);
            TabUtil.updatePlayer(player, api);
        });

        TeamEvent.PLAYER_LEFT_PARTY.register(event -> {
            var player = event.getPlayer();

            MinecraftServer server = player.getServer();
            if (server == null) return;

            OpenPACServerAPI api = OpenPACServerAPI.get(server);
            TabUtil.updatePlayer(player, api);
        });

        TeamEvent.PROPERTIES_CHANGED.register(MyFTBTeamsHooks::onTeamPropsChanged);
        };

    private static void onTeamPropsChanged(TeamPropertiesChangedEvent e) {
        Team team = e.getTeam();

        // 1) Получаем сервер (в разных версиях по-разному: из player, из event, или из team)
        // Если в событии есть getPlayer()/getServer() — используй их.
        // Тут самый универсальный вариант: взять сервер из любого онлайн игрока команды (если есть).
        ServerPlayer anyOnline = null;

        // 2) Пройтись по участникам команды (названия методов у Team зависят от версии)
        // Попробуй в IDE: team.getMembers() / team.getMembersRaw() / team.getPlayerIds() / team.getMembers().keySet()
        for (UUID id : team.getMembers()) {               // <-- подставь реальный метод
            ServerPlayer p = net.minecraftforge.server.ServerLifecycleHooks
                    .getCurrentServer()
                    .getPlayerList()
                    .getPlayer(id);
            MinecraftServer server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
            if (p != null) {
                anyOnline = p;
                // тут делай свою логику для каждого онлайн-игрока
                OpenPACServerAPI api = OpenPACServerAPI.get(server);
                 TabUtil.updatePlayer(p, api);
            }
        }

        // Если никого онлайн нет — просто выходим
        if (anyOnline == null) return;
    }

}

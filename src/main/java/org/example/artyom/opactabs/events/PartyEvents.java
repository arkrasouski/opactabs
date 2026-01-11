package org.example.artyom.opactabs.events;

import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import org.example.artyom.customwars.events.custom.WarScoreChangeEvent;
import org.example.artyom.customwars.saveddata.PartyData;

import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.common.server.api.OpenPACServerAPI;

import org.example.artyom.opactabs.utils.TabUtil;

import java.util.Set;
import java.util.UUID;

public class PartyEvents {

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        MinecraftServer server = player.getServer();
        if (server == null) return;


        PartyData partyData = PartyData.get(player.server.overworld());

        TabUtil.updatePlayer(player,server, partyData);
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        MinecraftServer server = player.getServer();
        if (server == null) return;

        OpenPACServerAPI api = OpenPACServerAPI.get(server);
        TabUtil.cleanupPlayer(player, api);
    }

    @SubscribeEvent
    public void onWarScoreChange(WarScoreChangeEvent event) {

        OpenPartiesAndClaims.LOGGER.info("[DEBUG] opactabs");
        Team party = FTBTeamsAPI.api().getManager().getTeamByID(event.getClanId()).get();
        Set<UUID> memberIds = party.getMembers();
        MinecraftServer server = event.getServer();
        //server.getPlayerList().broadcastSystemMessage(Component.literal("Война между партиями "), false);
        PartyData partyData = PartyData.get(server.overworld());
        for (UUID memberId : memberIds) {
            ServerPlayer player = server.getPlayerList().getPlayer(memberId);
            if (player != null) {
                TabUtil.updatePlayer(player,server, partyData);
            }


        }

        // пример:
        // выдать ачивку
        // обновить статистику
        // отправить пакет клиенту
    }

}
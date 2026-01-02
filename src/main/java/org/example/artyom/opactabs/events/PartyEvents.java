package org.example.artyom.opactabs.events;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import xaero.pac.common.server.api.OpenPACServerAPI;

import org.example.artyom.opactabs.utils.TabUtil;

public class PartyEvents {

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        MinecraftServer server = player.getServer();
        if (server == null) return;

        OpenPACServerAPI api = OpenPACServerAPI.get(server);
        TabUtil.updatePlayer(player, api);
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        MinecraftServer server = player.getServer();
        if (server == null) return;

        OpenPACServerAPI api = OpenPACServerAPI.get(server);
        TabUtil.cleanupPlayer(player, api);
    }
}
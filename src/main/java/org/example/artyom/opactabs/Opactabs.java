package org.example.artyom.opactabs;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.example.artyom.opactabs.events.MyFTBTeamsHooks;
import org.example.artyom.opactabs.events.PartyEvents;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(Opactabs.MOD_ID)
public class Opactabs {

    public static final String MOD_ID = "opactabs";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Opactabs() {
        MinecraftForge.EVENT_BUS.register(new PartyEvents());
        MyFTBTeamsHooks.init();
        LOGGER.info("OPAC Tabs (Server-side) загружен");
    }
}
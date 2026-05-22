package com.chinaex123.fintastic_supreme;

import com.chinaex123.fintastic_supreme.config.FSConfig;
import com.chinaex123.fintastic_supreme.data.FSDataComponents;
import com.chinaex123.fintastic_supreme.init.FSItems;
import com.chinaex123.fintastic_supreme.network.FishFinderDataPacket;
import com.chinaex123.fintastic_supreme.network.FishFinderPacketHandler;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

@Mod(FintasticSupreme.MOD_ID)
public class FintasticSupreme {
    public static final String MOD_ID = "fintastic_supreme";
    public static final Logger LOGGER = LogUtils.getLogger();

    public FintasticSupreme(IEventBus modEventBus, ModContainer modContainer) {
        FSItems.ITEMS_REGISTER.register(modEventBus);
        FSDataComponents.DATA_COMPONENTS.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, FSConfig.SPEC);
        modEventBus.addListener(this::registerPackets);
    }

    private void registerPackets(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(MOD_ID);
        registrar.playToClient(
                FishFinderDataPacket.TYPE,
                FishFinderDataPacket.STREAM_CODEC,
                FishFinderPacketHandler::handle
        );
    }
}

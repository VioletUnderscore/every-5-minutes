package net.violetunderscore.every5minutes;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.violetunderscore.every5minutes.commands.Every5MinutesBaseCommands;
import net.violetunderscore.every5minutes.vars.TickDataClient;
import net.violetunderscore.every5minutes.vars.TickDataSync;

public class Every5MinutesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PayloadTypeRegistry.playS2C().register(TickDataSync.ID, TickDataSync.CODEC);
        TickDataClient.init();

        Every5MinutesBaseCommands.registerCommands();
    }
}

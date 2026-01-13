package net.violetunderscore.every5minutes;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.violetunderscore.every5minutes.commands.Every5MinutesBaseCommands;
import net.violetunderscore.every5minutes.gui.ChallengesGui;
import net.violetunderscore.every5minutes.gui.DevGui;
import net.violetunderscore.every5minutes.gui.FiveMinutesGui;
import net.violetunderscore.every5minutes.gui.IntervalGui;
import net.violetunderscore.every5minutes.network.OpenGuiPayload;
import net.violetunderscore.every5minutes.vars.TickDataClient;
import net.violetunderscore.every5minutes.vars.TickDataSync;

public class Every5MinutesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TickDataClient.init();

        ClientPlayNetworking.registerGlobalReceiver(OpenGuiPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                switch (payload.menu()) {
                    case 1:
                        context.client().setScreen(new FiveMinutesGui());
                        break;
                    case 2:
                        context.client().setScreen(new ChallengesGui());
                        break;
                    case 3:
                        context.client().setScreen(new IntervalGui());
                        break;
                    default:
                        context.client().setScreen(new DevGui());
                        break;
                }
            });
        });
    }
}

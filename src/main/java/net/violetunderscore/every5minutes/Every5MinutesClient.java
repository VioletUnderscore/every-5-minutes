package net.violetunderscore.every5minutes;

import net.fabricmc.api.ClientModInitializer;
import net.violetunderscore.every5minutes.commands.Every5MinutesBaseCommands;

public class Every5MinutesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Every5MinutesBaseCommands.registerCommands();
    }
}

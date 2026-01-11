package net.violetunderscore.every5minutes.vars;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class TickDataClient {
    private static int ticks = 0;
    private static int counter = 0;
    private static int interval = 6000;
    private static int challenge = 1;
    private static boolean started = false;
    private static boolean active = false;

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(
                TickDataSync.ID,
                (payload, context) -> {
                    // Update cached values
                    ticks = payload.ticks();
                    counter = payload.counter();
                    interval = payload.interval();
                    challenge = payload.challenge();
                    started = payload.started();
                    active = payload.active();
                }
        );
    }

    public static int getTicks() {
        return ticks;
    }

    public static int getCounter() {
        return counter;
    }

    public static int getInterval() {
        return interval;
    }

    public static int getChallenge() {
        return challenge;
    }

    public static boolean isStarted() {
        return started;
    }

    public static boolean isActive() {
        return active;
    }



    public static void setStarted(boolean b) {
        started = b;
    }
    public static void setActive(boolean b) {
        active = b;
    }
}
package net.violetunderscore.every5minutes.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.violetunderscore.every5minutes.vars.TickData;

public class Every5MinutesBaseCommands {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
            if (environment.dedicated || environment.integrated) {
                register(dispatcher);
            }
        }));
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        /*start/stop*/
        {
            dispatcher.register(CommandManager.literal("e5m")
                    .then(CommandManager.literal("pause")
                            .executes(context -> {
                                TickData data = context.getSource().getServer().getOverworld()
                                        .getPersistentStateManager()
                                        .getOrCreate(TickData.TYPE, "e5m_tick_data");
                                data.active = false;
                                data.markDirty();

                                context.getSource().sendFeedback(() -> Text.literal("Pausing the game..."), true);
                                return 1;
                            }))
            );
            dispatcher.register(CommandManager.literal("e5m")
                    .then(CommandManager.literal("resume")
                            .executes(context -> {
                                TickData data = context.getSource().getServer().getOverworld()
                                        .getPersistentStateManager()
                                        .getOrCreate(TickData.TYPE, "e5m_tick_data");
                                data.active = true;
                                data.markDirty();

                                context.getSource().sendFeedback(() -> Text.literal("Resuming the game..."), true);
                                return 1;
                            }))
            );
            dispatcher.register(CommandManager.literal("e5m")
                    .then(CommandManager.literal("stop")
                            .executes(context -> {
                                TickData data = context.getSource().getServer().getOverworld()
                                        .getPersistentStateManager()
                                        .getOrCreate(TickData.TYPE, "e5m_tick_data");
                                data.active = false;
                                data.counter = 0;
                                data.ticks = 0;
                                context.getSource().getServer().getTickManager().setTickRate(20);
                                data.markDirty();

                                context.getSource().sendFeedback(() -> Text.literal("Stopping the game..."), true);
                                return 1;
                            }))
            );
            dispatcher.register(CommandManager.literal("e5m")
                    .then(CommandManager.literal("start")
                            .executes(context -> {
                                TickData data = context.getSource().getServer().getOverworld()
                                        .getPersistentStateManager()
                                        .getOrCreate(TickData.TYPE, "e5m_tick_data");
                                data.active = true;
                                data.counter = 0;
                                data.ticks = 0;
                                context.getSource().getServer().getTickManager().setTickRate(20);
                                data.markDirty();

                                context.getSource().sendFeedback(() -> Text.literal("Starting the game..."), true);
                                return 1;
                            }))
            );
        }

        /*change time interval*/
        {
            dispatcher.register(CommandManager.literal("e5m")
                    .then(CommandManager.literal("interval")
                            .then(CommandManager.argument("interval", IntegerArgumentType.integer())
                                    .then(CommandManager.literal("ticks")
                                            .executes(context -> {
                                                TickData data = context.getSource().getServer().getOverworld()
                                                        .getPersistentStateManager()
                                                        .getOrCreate(TickData.TYPE, "e5m_tick_data");
                                                data.interval = IntegerArgumentType.getInteger(context, "interval");
                                                data.markDirty();

                                                context.getSource().sendFeedback(() -> Text.literal(("Interval set to " + IntegerArgumentType.getInteger(context, "interval") + " ticks")), true);
                                                return 1;
                                            }))
                            )
                    )
            );
            dispatcher.register(CommandManager.literal("e5m")
                    .then(CommandManager.literal("interval")
                            .then(CommandManager.argument("interval", IntegerArgumentType.integer())
                                    .then(CommandManager.literal("seconds")
                                            .executes(context -> {
                                                TickData data = context.getSource().getServer().getOverworld()
                                                        .getPersistentStateManager()
                                                        .getOrCreate(TickData.TYPE, "e5m_tick_data");
                                                data.interval = IntegerArgumentType.getInteger(context, "interval") * 20;
                                                data.markDirty();

                                                context.getSource().sendFeedback(() -> Text.literal(("Interval set to " + IntegerArgumentType.getInteger(context, "interval") + " seconds")), true);
                                                return 1;
                                            }))
                            )
                    )
            );
            dispatcher.register(CommandManager.literal("e5m")
                    .then(CommandManager.literal("interval")
                            .then(CommandManager.argument("interval", IntegerArgumentType.integer())
                                    .then(CommandManager.literal("minutes")
                                            .executes(context -> {
                                                TickData data = context.getSource().getServer().getOverworld()
                                                        .getPersistentStateManager()
                                                        .getOrCreate(TickData.TYPE, "e5m_tick_data");
                                                data.interval = IntegerArgumentType.getInteger(context, "interval") * 20 * 60;
                                                data.markDirty();

                                                context.getSource().sendFeedback(() -> Text.literal(("Interval set to " + IntegerArgumentType.getInteger(context, "interval") + " minutes")), true);
                                                return 1;
                                            }))
                            )
                    )
            );
        }

        /*change the challenge*/
        {
            dispatcher.register(CommandManager.literal("e5m")
                    .then(CommandManager.literal("challenge")
                            .then(CommandManager.literal("1_TeleportEveryEntity")
                                    .executes(context -> {
                                        TickData data = context.getSource().getServer().getOverworld()
                                                .getPersistentStateManager()
                                                .getOrCreate(TickData.TYPE, "e5m_tick_data");
                                        data.challenge = 1;
                                        data.markDirty();

                                        context.getSource().sendFeedback(() -> Text.literal("Current Challenge: Every Entity Gets Teleported To You"), true);
                                        return 1;
                                    })
                            )
                    )
            );
            dispatcher.register(CommandManager.literal("e5m")
                    .then(CommandManager.literal("challenge")
                            .then(CommandManager.literal("2_RaiseTickRate")
                                    .executes(context -> {
                                        TickData data = context.getSource().getServer().getOverworld()
                                                .getPersistentStateManager()
                                                .getOrCreate(TickData.TYPE, "e5m_tick_data");
                                        data.challenge = 2;
                                        data.markDirty();

                                        context.getSource().sendFeedback(() -> Text.literal("Current Challenge: The Tick Rate Increases By 10%"), true);
                                        return 1;
                                    })
                            )
                    )
            );
            dispatcher.register(CommandManager.literal("e5m")
                    .then(CommandManager.literal("challenge")
                            .then(CommandManager.literal("3_RandomPotionEffect")
                                    .executes(context -> {
                                        TickData data = context.getSource().getServer().getOverworld()
                                                .getPersistentStateManager()
                                                .getOrCreate(TickData.TYPE, "e5m_tick_data");
                                        data.challenge = 3;
                                        data.markDirty();

                                        context.getSource().sendFeedback(() -> Text.literal("Current Challenge: Every Player Gets A Random Effect"), true);
                                        return 1;
                                    })
                            )
                    )
            );
            dispatcher.register(CommandManager.literal("e5m")
                    .then(CommandManager.literal("challenge")
                            .then(CommandManager.literal("4_RandomSwarmOfMobs")
                                    .executes(context -> {
                                        TickData data = context.getSource().getServer().getOverworld()
                                                .getPersistentStateManager()
                                                .getOrCreate(TickData.TYPE, "e5m_tick_data");
                                        data.challenge = 4;
                                        data.markDirty();

                                        context.getSource().sendFeedback(() -> Text.literal("Current Challenge: A Swarm Of A Random Mob Appears"), true);
                                        return 1;
                                    })
                            )
                    )
            );
            dispatcher.register(CommandManager.literal("e5m")
                    .then(CommandManager.literal("challenge")
                            .then(CommandManager.literal("5_LoseHealth")
                                    .executes(context -> {
                                        TickData data = context.getSource().getServer().getOverworld()
                                                .getPersistentStateManager()
                                                .getOrCreate(TickData.TYPE, "e5m_tick_data");
                                        data.challenge = 5;
                                        data.markDirty();

                                        context.getSource().sendFeedback(() -> Text.literal("Current Challenge: You Permanently Lose Half A Heart"), true);
                                        return 1;
                                    })
                            )
                    )
            );
            dispatcher.register(CommandManager.literal("e5m")
                    .then(CommandManager.literal("challenge")
                            .then(CommandManager.literal("6_Mitosis")
                                    .executes(context -> {
                                        TickData data = context.getSource().getServer().getOverworld()
                                                .getPersistentStateManager()
                                                .getOrCreate(TickData.TYPE, "e5m_tick_data");
                                        data.challenge = 6;
                                        data.markDirty();

                                        context.getSource().sendFeedback(() -> Text.literal("Current Challenge: Mobs Experience Mitosis"), true);
                                        return 1;
                                    })
                            )
                    )
            );
        }
    }
}


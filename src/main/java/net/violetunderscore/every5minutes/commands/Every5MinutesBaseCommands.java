package net.violetunderscore.every5minutes.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionCheck;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.violetunderscore.every5minutes.Every5Minutes;
import net.violetunderscore.every5minutes.network.OpenGuiPayload;
import net.violetunderscore.every5minutes.vars.TickData;
import net.violetunderscore.every5minutes.vars.TickDataSync;

public class Every5MinutesBaseCommands {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
            if (environment.dedicated || environment.integrated) {
                register(dispatcher);
            }
        }));
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        {
            dispatcher.register(CommandManager.literal("d/e/v/g/u/i")
                    .executes(context -> {
                        if (context.getSource().isExecutedByPlayer()) {
                            TickData data = TickData.retrieve(context);
                            ServerPlayerEntity p = context.getSource().getPlayer();
                            TickDataSync.sendToPlayer(p, data);
                            ServerPlayNetworking.send(p, new OpenGuiPayload(0));
                        } else {
                            context.getSource().sendFeedback(() -> Text.literal("Only players can open menus"), true);
                        }
                        return 1;
                    })
            );
        } // Five Minutes Dev Menu
        {
            dispatcher.register(CommandManager.literal("FiveMinutes")
                    .requires(CommandManager.requirePermissionLevel(CommandManager.MODERATORS_CHECK))
                    .executes(context -> {
                        if (context.getSource().isExecutedByPlayer()) {
                            TickData data = TickData.retrieve(context);
                            ServerPlayerEntity p = context.getSource().getPlayer();
                            TickDataSync.sendToPlayer(p, data);
                            ServerPlayNetworking.send(p, new OpenGuiPayload(1));
                        } else {
                            context.getSource().sendFeedback(() -> Text.literal("Only players can open menus"), true);
                        }
                        return 1;
                    })
            );
        } // Five Minutes Menu
        {
            dispatcher.register(CommandManager.literal("FMChallenges")
                    .requires(CommandManager.requirePermissionLevel(CommandManager.MODERATORS_CHECK))
                    .executes(context -> {
                        if (context.getSource().isExecutedByPlayer()) {
                            TickData data = TickData.retrieve(context);
                            ServerPlayerEntity p = context.getSource().getPlayer();
                            TickDataSync.sendToPlayer(p, data);
                            ServerPlayNetworking.send(p, new OpenGuiPayload(2));
                        } else {
                            context.getSource().sendFeedback(() -> Text.literal("Only players can open menus"), true);
                        }
                        return 1;
                    })
            );
        } // Five Minutes Challenges Menu
        {
            dispatcher.register(CommandManager.literal("FMInterval")
                    .requires(CommandManager.requirePermissionLevel(CommandManager.MODERATORS_CHECK))
                    .executes(context -> {
                        if (context.getSource().isExecutedByPlayer()) {
                            TickData data = TickData.retrieve(context);
                            ServerPlayerEntity p = context.getSource().getPlayer();
                            TickDataSync.sendToPlayer(p, data);
                            ServerPlayNetworking.send(p, new OpenGuiPayload(3));
                        } else {
                            context.getSource().sendFeedback(() -> Text.literal("Only players can open menus"), true);
                        }
                        return 1;
                    })
            );
        } // Five Minutes Interval Menu

        /*start/stop*/
        {
            dispatcher.register(CommandManager.literal("e5m")
                    .requires(CommandManager.requirePermissionLevel(CommandManager.MODERATORS_CHECK))
                    .then(CommandManager.literal("pause")
                            .executes(context -> {
                                TickData data = TickData.retrieve(context);
                                if (data.started) {
                                    data.active = false;
                                    data.markDirty();

                                    context.getSource().sendFeedback(() -> Text.translatable("cmd.e5m.pause"), true);
                                } else {
                                    context.getSource().sendFeedback(() -> Text.translatable("cmd.e5m.game_inactive").withColor(0xFF8888), true);
                                }
                                return 1;
                            }))
            );
            dispatcher.register(CommandManager.literal("e5m")
                    .requires(CommandManager.requirePermissionLevel(CommandManager.MODERATORS_CHECK))
                    .then(CommandManager.literal("resume")
                            .executes(context -> {
                                TickData data = TickData.retrieve(context);
                                if (data.started) {
                                    data.active = true;
                                    data.markDirty();

                                    context.getSource().sendFeedback(() -> Text.translatable("cmd.e5m.resume"), true);
                                } else {
                                    context.getSource().sendFeedback(() -> Text.translatable("cmd.e5m.game_inactive").withColor(0xFF8888), true);
                                }
                                return 1;
                            }))
            );
            dispatcher.register(CommandManager.literal("e5m")
                    .requires(CommandManager.requirePermissionLevel(CommandManager.MODERATORS_CHECK))
                    .then(CommandManager.literal("stop")
                            .executes(context -> {
                                TickData data = TickData.retrieve(context);
                                if (data.started) {
                                    data.active = false;
                                    data.started = false;
                                    data.counter = 0;
                                    data.ticks = 0;
                                    context.getSource().getServer().getTickManager().setTickRate(20);
                                    data.markDirty();

                                    context.getSource().sendFeedback(() -> Text.translatable("cmd.e5m.stop"), true);
                                } else {
                                    context.getSource().sendFeedback(() -> Text.translatable("cmd.e5m.game_inactive").withColor(0xFF8888), true);
                                }
                                return 1;
                            }))
            );
            dispatcher.register(CommandManager.literal("e5m")
                    .requires(CommandManager.requirePermissionLevel(CommandManager.MODERATORS_CHECK))
                    .then(CommandManager.literal("start")
                            .executes(context -> {
                                TickData data = TickData.retrieve(context);
                                if (!data.started) {
                                    data.active = true;
                                    data.started = true;
                                    data.counter = 0;
                                    data.ticks = 0;
                                    context.getSource().getServer().getTickManager().setTickRate(20);
                                    data.markDirty();

                                    context.getSource().sendFeedback(() -> Text.translatable("cmd.e5m.start"), true);
                                } else {
                                    context.getSource().sendFeedback(() -> Text.translatable("cmd.e5m.game_active").withColor(0xFF8888), true);
                                }
                                return 1;
                            }))
            );
        }

        /*change time interval*/
        {
            dispatcher.register(CommandManager.literal("e5m")
                    .requires(CommandManager.requirePermissionLevel(CommandManager.MODERATORS_CHECK))
                    .then(CommandManager.literal("interval")
                            .then(CommandManager.argument("interval", IntegerArgumentType.integer())
                                    .then(CommandManager.literal("ticks")
                                            .executes(context -> {
                                                TickData data = TickData.retrieve(context);
                                                data.interval = IntegerArgumentType.getInteger(context, "interval");
                                                data.markDirty();

                                                context.getSource().sendFeedback(() -> Text.translatable("cmd.e5m.interval_set", readableTime(data.interval)), true);
                                                return 1;
                                            }))
                            )
                    )
            );
            dispatcher.register(CommandManager.literal("e5m")
                    .requires(CommandManager.requirePermissionLevel(CommandManager.MODERATORS_CHECK))
                    .then(CommandManager.literal("interval")
                            .then(CommandManager.argument("interval", IntegerArgumentType.integer())
                                    .then(CommandManager.literal("seconds")
                                            .executes(context -> {
                                                TickData data = TickData.retrieve(context);
                                                data.interval = IntegerArgumentType.getInteger(context, "interval") * 20;
                                                data.markDirty();

                                                context.getSource().sendFeedback(() -> Text.translatable("cmd.e5m.interval_set", readableTime(data.interval)), true);
                                                return 1;
                                            }))
                            )
                    )
            );
            dispatcher.register(CommandManager.literal("e5m")
                    .requires(CommandManager.requirePermissionLevel(CommandManager.MODERATORS_CHECK))
                    .then(CommandManager.literal("interval")
                            .then(CommandManager.argument("interval", IntegerArgumentType.integer())
                                    .then(CommandManager.literal("minutes")
                                            .executes(context -> {
                                                TickData data = TickData.retrieve(context);
                                                data.interval = IntegerArgumentType.getInteger(context, "interval") * 20 * 60;
                                                data.markDirty();

                                                context.getSource().sendFeedback(() -> Text.translatable("cmd.e5m.interval_set", readableTime(data.interval)), true);
                                                return 1;
                                            }))
                            )
                    )
            );
        }

        /*change the challenge*/
        {
            dispatcher.register(CommandManager.literal("e5m")
                    .requires(CommandManager.requirePermissionLevel(CommandManager.MODERATORS_CHECK))
                    .then(CommandManager.literal("challenge")
                            .then(CommandManager.argument("challenge", IntegerArgumentType.integer())
                                    .executes(context -> {
                                        int c = IntegerArgumentType.getInteger(context, "challenge");
                                        if (c > Every5Minutes.challengeCount || c < 1) {
                                            context.getSource().sendFeedback(() -> Text.translatable("cmd.e5m.invalid_challenge", Every5Minutes.challengeCount).withColor(0xFF8888), true);
                                            return 0;
                                        } else {
                                            TickData data = TickData.retrieve(context);
                                            data.challenge = c;
                                            data.markDirty();

                                            context.getSource().sendFeedback(() ->
                                                    Text.translatable("ui.e5m.challenge.current",
                                                            Text.translatable("ui.e5m.challenge.intro",
                                                                    readableTime(data.interval),
                                                                    Text.translatable("ui.e5m.challenge." + data.challenge)
                                                            )
                                                    ), true);
                                            return 1;
                                        }
                                    })
                            )
                    )
            );
        }
    }

    public static String readableTime(int i) {
        String s = "";
        int t = 60 * 20;
        if (i > t) {
            s = s + (int)Math.floor((double)i / t) + " " +
                    Text.translatable("time.e5m.3").getString();
            if (i % t == 0) {
                return s;
            } else { s = s + ", "; }
        }
        i -= (int)Math.floor((double)i / t) * t;

        t = 20;
        if (i > t) {
            s = s + (int)Math.floor((double)i / t) + " " +
                    Text.translatable("time.e5m.2").getString();
            if (i % t == 0) {
                return s;
            } else { s = s + ", "; }
        }
        i -= (int)Math.floor((double)i / t) * t;

        s = s + i + " " +
                Text.translatable("time.e5m.1").getString();
        return s;
    }
}


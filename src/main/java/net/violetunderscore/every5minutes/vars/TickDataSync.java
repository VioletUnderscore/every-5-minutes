package net.violetunderscore.every5minutes.vars;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public record TickDataSync(
        int ticks,
        int counter,
        int interval,
        int challenge,
        boolean started,
        boolean active
) implements CustomPayload {

    public static final Id<TickDataSync> ID =
            new Id<>(Identifier.of("every5minutes", "tickdata_sync"));

    public static final PacketCodec<RegistryByteBuf, TickDataSync> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.INTEGER, TickDataSync::ticks,
                    PacketCodecs.INTEGER, TickDataSync::counter,
                    PacketCodecs.INTEGER, TickDataSync::interval,
                    PacketCodecs.INTEGER, TickDataSync::challenge,
                    PacketCodecs.BOOLEAN, TickDataSync::started,
                    PacketCodecs.BOOLEAN, TickDataSync::active,
                    TickDataSync::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void sendToPlayer(ServerPlayerEntity player, TickData data) {
        TickDataSync payload = new TickDataSync(
                data.ticks,
                data.counter,
                data.interval,
                data.challenge,
                data.started,
                data.active
        );
        ServerPlayNetworking.send(player, payload);
    }
}
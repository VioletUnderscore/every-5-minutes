package net.violetunderscore.every5minutes.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record OpenGuiPayload(int menu) implements CustomPayload {

    public static final Id<OpenGuiPayload> ID =
            new Id<>(Identifier.of("every5minutes", "open_gui"));

    public static final PacketCodec<RegistryByteBuf, OpenGuiPayload> CODEC =
            PacketCodec.tuple(PacketCodecs.INTEGER, OpenGuiPayload::menu,
                    OpenGuiPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
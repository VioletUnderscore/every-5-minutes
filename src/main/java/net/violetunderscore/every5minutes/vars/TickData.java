package net.violetunderscore.every5minutes.vars;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;

public class TickData extends PersistentState {
    public int ticks = 0;
    public int counter = 0;
    public int interval = 6000;
    public int challenge = 1;
    public boolean active = false;

    public static final Codec<TickData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("ticks").forGetter(data -> data.ticks),
                    Codec.INT.fieldOf("counter").forGetter(data -> data.counter),
                    Codec.INT.fieldOf("interval").forGetter(data -> data.interval),
                    Codec.INT.fieldOf("challenge").forGetter(data -> data.challenge),
                    Codec.BOOL.fieldOf("active").forGetter(data -> data.active)
            ).apply(instance, TickData::create)
    );

    public static final PersistentStateType<TickData> TYPE = new PersistentStateType<>(
            "every5minutes_tick_data",
            TickData::new,
            CODEC,
            null
    );

    public TickData() {
        super();
    }

    private static TickData create(Integer ticks, Integer counter, Integer interval, Integer challenge, Boolean active) {
        TickData data = new TickData();
        data.ticks = ticks;
        data.counter = counter;
        data.interval = interval;
        data.challenge = challenge;
        data.active = active;
        return data;
    }

    private static TickData fromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup lookup) {
        TickData data = new TickData();
        data.ticks = tag.getInt("ticks", 0);
        data.counter = tag.getInt("counter", 0);
        data.interval = tag.getInt("interval", 6000);
        data.challenge = tag.getInt("challenge", 1);
        data.active = tag.getBoolean("active", false);
        return data;
    }

    public NbtCompound writeNbt(NbtCompound tag, RegistryWrapper.WrapperLookup lookup) {
        tag.putInt("ticks",   this.ticks);
        tag.putInt("counter", this.counter);
        tag.putInt("interval", this.interval);
        tag.putInt("challenge", this.challenge);
        tag.putBoolean("active", this.active);
        return tag;
    }
}

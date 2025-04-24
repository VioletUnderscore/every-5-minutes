package net.violetunderscore.every5minutes.vars;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.PersistentState;

public class TickData extends PersistentState {
    public int ticks = 0;
    public int counter = 0;
    public int interval = 6000;
    public int challenge = 1;
    public boolean active = false;

    public static final Type<TickData> TYPE = new Type<>(
            TickData::new,
            TickData::fromNbt,
            null
    );

    public TickData() { super(); }

    private static TickData fromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup lookup) {
        TickData data = new TickData();
        data.ticks   = tag.getInt("ticks");
        data.counter = tag.getInt("counter");
        data.interval = tag.getInt("interval");
        data.challenge = tag.getInt("challenge");
        data.active = tag.getBoolean("active");
        return data;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag, RegistryWrapper.WrapperLookup lookup) {
        tag.putInt("ticks",   this.ticks);
        tag.putInt("counter", this.counter);
        tag.putInt("interval", this.interval);
        tag.putInt("challenge", this.challenge);
        tag.putBoolean("active", this.active);
        return tag;
    }
}

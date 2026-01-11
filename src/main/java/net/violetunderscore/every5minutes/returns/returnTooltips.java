package net.violetunderscore.every5minutes.returns;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class returnTooltips {
    public static Tooltip lagWarn() {
        return Tooltip.of(Text.translatable("tooltip.e5m.lag_warn").withColor(0xff3333).formatted(Formatting.BOLD));
    }

    public static Tooltip comingSoon() {
        return Tooltip.of(Text.translatable("tooltip.e5m.coming_soon").withColor(0x777777).formatted(Formatting.ITALIC));
    }
}

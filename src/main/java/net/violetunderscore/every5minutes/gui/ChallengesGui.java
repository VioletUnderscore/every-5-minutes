package net.violetunderscore.every5minutes.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.violetunderscore.every5minutes.Every5Minutes;
import net.violetunderscore.every5minutes.returns.returnTooltips;
import net.violetunderscore.every5minutes.vars.TickDataClient;

public class ChallengesGui extends Screen {
    public ChallengesGui() {
        super(Text.literal("Challenge Select"));
    }

    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(Every5Minutes.versionNumberWidget(this));

        int startHeight = ((-Every5Minutes.challengeCount + 1) * 25 / 2) - 10;
        this.addDrawableChild(new TextWidget(
                this.width / 2 - 100,
                this.height / 2 + startHeight - 30,
                200,
                10,
                Text.translatable("ui.e5m.challenge_menu"),
                this.textRenderer
        ));
        for (int i = 1; i < Every5Minutes.challengeCount + 1; i++) {
            final int chal = i;
            this.addDrawableChild(ButtonWidget.builder(Text.translatable("ui.e5m.challenge." + chal), button -> {
                        if (this.client != null) {
                            this.client.setScreen(null);
                        }
                        if (this.client != null && this.client.player != null) {
                            this.client.player.networkHandler.sendChatCommand("e5m challenge " + chal);
                            this.client.player.networkHandler.sendChatCommand("FMChallenges");
                        }
                    })
                    .dimensions(this.width / 2 - 100, this.height / 2 + startHeight, 200, 20)
                    .tooltip(i==6?returnTooltips.lagWarn():null)
                    .build());
            startHeight += 25;
        }
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.back"), button -> {
                    if (this.client != null) {
                        this.client.setScreen(null);
                    }
                    if (this.client != null && this.client.player != null) {
                        this.client.player.networkHandler.sendChatCommand("FiveMinutes");
                    }
                })
                .dimensions(this.width / 2 - 100, this.height / 2 + startHeight + 10, 200, 20)
                .build());

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(
                this.textRenderer,
                this.title,
                this.width / 2,
                20,
                0xFFFFFF
        );
    }

    @Override
    public boolean shouldPause() {
        return false; // Set to true if you want the game to pause when GUI is open
    }
}
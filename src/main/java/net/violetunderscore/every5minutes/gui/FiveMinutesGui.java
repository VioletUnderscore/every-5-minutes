package net.violetunderscore.every5minutes.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.violetunderscore.every5minutes.Every5Minutes;
import net.violetunderscore.every5minutes.returns.returnTooltips;
import net.violetunderscore.every5minutes.vars.TickDataClient;

public class FiveMinutesGui extends Screen {

    private ButtonWidget challengeSelectButton;
    private ButtonWidget challengeSettingsButton;
    private ButtonWidget intervalSelectButton;
    private ButtonWidget startButton;
    private ButtonWidget stopButton;
    private ButtonWidget pauseButton;
    private ButtonWidget resumeButton;
    private ButtonWidget closeButton;

    public FiveMinutesGui() {
        super(Text.literal("Every 5 Minutes"));
    }

    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(Every5Minutes.versionNumberWidget(this));

        {
            challengeSelectButton = ButtonWidget.builder(Text.literal("Challenge Select"), button -> {
                        if (this.client != null) {
                            this.client.setScreen(new ChallengesGui());
                        }
                    })
                    .dimensions(this.width / 2 - 100, this.height / 2 - 70, 200, 20)
                    .build();
        } // Challenge Select
        {
            challengeSettingsButton = ButtonWidget.builder(Text.literal("Challenge Settings"), button -> {
                        if (this.client != null && this.client.player != null) {
                            this.client.player.sendMessage(Text.literal("Challenge Select"), false);
                        }
                    })
                    .dimensions(this.width / 2 - 100, this.height / 2 - 40, 200, 20)
                    .tooltip(returnTooltips.comingSoon())
                    .build();
        } // Challenge Settings
        {
            intervalSelectButton = ButtonWidget.builder(Text.literal("Interval Select"), button -> {
                        if (this.client != null) {
                            this.client.setScreen(new IntervalGui());
                        }
                    })
                    .dimensions(this.width / 2 - 100, this.height / 2 - 10, 200, 20)
                    .build();
        } // Interval Select
        {
            startButton = ButtonWidget.builder(Text.translatable("ui.e5m.start"), button -> {
                        if (this.client != null) {
                            this.client.setScreen(null);
                        }
                        if (this.client != null && this.client.player != null) {
                            this.client.player.networkHandler.sendChatCommand("e5m start");
                            this.client.player.networkHandler.sendChatCommand("FiveMinutes");
                        }
                    })
                    .dimensions(this.width / 2 - 100, this.height / 2 + 20, 200, 20)
                    .build();
        } // Start Game
        {
            stopButton = ButtonWidget.builder(Text.translatable("ui.e5m.stop"), button -> {
                        if (this.client != null) {
                            this.client.setScreen(null);
                        }
                        if (this.client != null && this.client.player != null) {
                            this.client.player.networkHandler.sendChatCommand("e5m stop");
                            this.client.player.networkHandler.sendChatCommand("FiveMinutes");
                        }
                    })
                    .dimensions(this.width / 2 + 5, this.height / 2 + 20, 95, 20)
                    .build();
        } // Stop Game
        {
            resumeButton = ButtonWidget.builder(Text.translatable("ui.e5m.resume"), button -> {
                        if (this.client != null) {
                            this.client.setScreen(null);
                        }
                        if (this.client != null && this.client.player != null) {
                            this.client.player.networkHandler.sendChatCommand("e5m resume");
                            this.client.player.networkHandler.sendChatCommand("FiveMinutes");
                        }
                    })
                    .dimensions(this.width / 2 - 100, this.height / 2 + 20, 95, 20)
                    .build();
        } // Resume Game
        {
            pauseButton = ButtonWidget.builder(Text.translatable("ui.e5m.pause"), button -> {
                        if (this.client != null) {
                            this.client.setScreen(null);
                        }
                        if (this.client != null && this.client.player != null) {
                            this.client.player.networkHandler.sendChatCommand("e5m pause");
                            this.client.player.networkHandler.sendChatCommand("FiveMinutes");
                        }
                    })
                    .dimensions(this.width / 2 - 100, this.height / 2 + 20, 95, 20)
                    .build();
        } // Pause Game
        {
            closeButton = ButtonWidget.builder(Text.translatable("menu.returnToGame"), button -> {
                        if (this.client != null) {
                            this.client.setScreen(null);
                        }
                    })
                    .dimensions(this.width / 2 - 100, this.height / 2 + 50, 200, 20)
                    .build();
        } // Close Menu

        challengeSettingsButton.active = false;

        this.addDrawableChild(new TextWidget(
                this.width / 2 - 100,
                30,
                200,
                10,
                Text.translatable("ui.e5m.e5m_menu"),
                this.textRenderer
        ));

        if (!TickDataClient.isStarted()) {
            this.addDrawableChild(startButton);
        } else {
            intervalSelectButton.active = false;
            challengeSelectButton.active = false;
            this.addDrawableChild(stopButton);
            if (TickDataClient.isActive()) {
                this.addDrawableChild(pauseButton);
            } else {
                this.addDrawableChild(resumeButton);
            }
        }
        this.addDrawableChild(challengeSelectButton);
        this.addDrawableChild(challengeSettingsButton);
        this.addDrawableChild(intervalSelectButton);
        this.addDrawableChild(closeButton);
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
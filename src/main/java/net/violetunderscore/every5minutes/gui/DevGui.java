package net.violetunderscore.every5minutes.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.violetunderscore.every5minutes.Every5Minutes;
import net.violetunderscore.every5minutes.returns.returnTooltips;
import net.violetunderscore.every5minutes.vars.TickDataClient;

public class DevGui extends Screen {

    private ButtonWidget challengeSelectButton;
    private ButtonWidget challengeSettingsButton;
    private ButtonWidget intervalSelectButton;
    private ButtonWidget startButton;
    private ButtonWidget stopButton;
    private ButtonWidget pauseButton;
    private ButtonWidget resumeButton;
    private ButtonWidget closeButton;

    public DevGui() {
        super(Text.literal("Every 5 Minutes"));
    }

    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(Every5Minutes.versionNumberWidget(this));

        this.addDrawableChild(new TextWidget(
                this.width / 2 - 100,
                30,
                200,
                10,
                Text.literal("gui 0"),
                this.textRenderer
        ));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false; // Set to true if you want the game to pause when GUI is open
    }
}
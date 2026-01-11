package net.violetunderscore.every5minutes.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.violetunderscore.every5minutes.Every5Minutes;
import net.violetunderscore.every5minutes.vars.TickDataClient;

public class IntervalGui extends Screen {
    public IntervalGui() {
        super(Text.literal("Interval Select"));
    }

    private TextFieldWidget minutesField;
    private TextFieldWidget secondsField;
    private TextFieldWidget ticksField;

    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(Every5Minutes.versionNumberWidget(this));

        this.addDrawableChild(new TextWidget(
                this.width / 2 - 100,
                30,
                200,
                10,
                Text.translatable("ui.e5m.interval_menu"),
                this.textRenderer
        ));



        int startHeight = -35;
        this.addDrawableChild(new TextWidget(
                this.width / 2 - 100,
                this.height / 2 + startHeight - 15,
                200,
                10,
                Text.translatable("ui.e5m.every1", Text.translatable("ui.e5m.challenge." + TickDataClient.getChallenge())),
                this.textRenderer
        ));
        {
            minutesField = new TextFieldWidget(
                    this.textRenderer,
                    this.width / 2 - 100,
                    this.height / 2 + startHeight,
                    40,
                    20,
                    Text.literal("Enter number")
            );
            minutesField.setPlaceholder(Text.translatable("time.e5m.short.3"));
            minutesField.setMaxLength(5);
            minutesField.setTextPredicate(text -> text.matches("\\d*"));
            minutesField.setText("5");
            this.addDrawableChild(minutesField);
            startHeight += 25;
            this.addDrawableChild(new TextWidget(
                    this.width / 2 - 55,
                    this.height / 2 + startHeight - 19,
                    200,
                    10,
                    Text.translatable("time.e5m.3"),
                    this.textRenderer
            ));
        } // minutes
        {
            secondsField = new TextFieldWidget(
                    this.textRenderer,
                    this.width / 2 - 100,
                    this.height / 2 + startHeight,
                    40,
                    20,
                    Text.literal("Enter number")
            );
            secondsField.setPlaceholder(Text.translatable("time.e5m.short.2"));
            secondsField.setMaxLength(5);
            secondsField.setTextPredicate(text -> text.matches("\\d*"));
            secondsField.setText("0");
            this.addDrawableChild(secondsField);
            startHeight += 25;
            this.addDrawableChild(new TextWidget(
                    this.width / 2 - 55,
                    this.height / 2 + startHeight - 19,
                    200,
                    10,
                    Text.translatable("time.e5m.2"),
                    this.textRenderer
            ));
        } // seconds
        {
            ticksField = new TextFieldWidget(
                    this.textRenderer,
                    this.width / 2 - 100,
                    this.height / 2 + startHeight,
                    40,
                    20,
                    Text.literal("Enter number")
            );
            ticksField.setPlaceholder(Text.translatable("time.e5m.short.1"));
            ticksField.setMaxLength(5);
            ticksField.setTextPredicate(text -> text.matches("\\d*"));
            ticksField.setText("0");
            this.addDrawableChild(ticksField);
            startHeight += 25;
            this.addDrawableChild(new TextWidget(
                    this.width / 2 - 55,
                    this.height / 2 + startHeight - 19,
                    200,
                    10,
                    Text.translatable("time.e5m.1"),
                    this.textRenderer
            ));
        } // ticks
        this.addDrawableChild(new TextWidget(
                this.width / 2 - 100,
                this.height / 2 + startHeight + 1,
                200,
                10,
                Text.translatable("ui.e5m.every2", Text.translatable("ui.e5m.challenge." + TickDataClient.getChallenge())),
                this.textRenderer
        ));
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.done"), button -> {
                    if (this.client != null) {
                        this.client.setScreen(null);
                    }
                    if (this.client != null && this.client.player != null) {
                        this.client.player.networkHandler.sendChatCommand("e5m interval " + (
                                (Integer.parseInt(minutesField.getText()) * 20 * 60)
                                        + (Integer.parseInt(secondsField.getText()) * 20)
                                        + (Integer.parseInt(ticksField.getText()))
                        ) + " ticks");
                        this.client.player.networkHandler.sendChatCommand("FiveMinutes");
                    }
                })
                .dimensions(this.width / 2 + 5, this.height - 25, 95, 20)
                .build());
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.cancel"), button -> {
                    if (this.client != null) {
                        this.client.setScreen(null);
                    }
                    if (this.client != null && this.client.player != null) {
                        this.client.player.networkHandler.sendChatCommand("FiveMinutes");
                    }
                })
                .dimensions(this.width / 2 - 100, this.height - 25, 95, 20)
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
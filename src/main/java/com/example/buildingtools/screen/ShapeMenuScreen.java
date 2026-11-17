package com.example.buildingtools.screen;

import com.example.buildingtools.util.BuildingState;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ShapeMenuScreen extends Screen {
    public ShapeMenuScreen() {
        super(Text.literal("Shape & Mode Selection"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int y = 40;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Mode: " + BuildingState.currentMode.displayName), b -> {
            BuildingState.Mode[] modes = BuildingState.Mode.values();
            int next = (BuildingState.currentMode.ordinal() + 1) % modes.length;
            BuildingState.currentMode = modes[next];
            b.setMessage(Text.literal("Mode: " + BuildingState.currentMode.displayName));
        }).dimensions(centerX - 100, y, 200, 20).build());

        y += 30;

        int col = 0;
        for (BuildingState.Shape shape : BuildingState.Shape.values()) {
            int bx = centerX - 160 + (col % 3) * 110;
            int by = y + (col / 3) * 24;

            this.addDrawableChild(ButtonWidget.builder(Text.literal(shape.displayName), b -> {
                BuildingState.currentShape = shape;
                if (this.client != null && this.client.player != null) {
                    this.client.player.sendMessage(Text.literal("§bShape: " + shape.displayName), true);
                }
                this.close();
            }).dimensions(bx, by, 100, 20).build());

            col++;
        }

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Close"), b -> close())
                .dimensions(centerX - 40, this.height - 30, 80, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);

        String info = "Current: §a" + BuildingState.currentShape.displayName + " §7| §e" + BuildingState.currentMode.displayName;
        if (BuildingState.selectedBlock != null) {
            info += " §7| §f" + BuildingState.selectedBlock.getName().getString();
        }
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(info), this.width / 2, this.height - 50, 0xAAAAAA);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}

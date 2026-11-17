package com.example.buildingtools.screen;

import com.example.buildingtools.util.BuildingState;
import net.minecraft.block.Block;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class BlockPickerScreen extends Screen {
    private TextFieldWidget searchField;
    private List<Block> allBlocks = new ArrayList<>();
    private List<Block> filteredBlocks = new ArrayList<>();
    private int scrollOffset = 0;
    private static final int VISIBLE_ROWS = 12;

    public BlockPickerScreen() {
        super(Text.literal("Block Picker – All Blocks (including modded)"));
    }

    @Override
    protected void init() {
        allBlocks = Registries.BLOCK.stream().collect(Collectors.toList());
        filteredBlocks = new ArrayList<>(allBlocks);

        searchField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 20, 200, 20, Text.literal("Search..."));
        searchField.setChangedListener(this::onSearchChanged);
        this.addSelectableChild(searchField);
        this.setInitialFocus(searchField);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Close"), b -> close())
                .dimensions(this.width / 2 - 40, this.height - 30, 80, 20).build());
    }

    private void onSearchChanged(String query) {
        String q = query.toLowerCase(Locale.ROOT).trim();
        if (q.isEmpty()) {
            filteredBlocks = new ArrayList<>(allBlocks);
        } else {
            filteredBlocks = allBlocks.stream()
                    .filter(b -> {
                        Identifier id = Registries.BLOCK.getId(b);
                        String name = b.getName().getString().toLowerCase(Locale.ROOT);
                        return id.getPath().contains(q) || id.toString().contains(q) || name.contains(q);
                    })
                    .collect(Collectors.toList());
        }
        scrollOffset = 0;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 5, 0xFFFFFF);
        searchField.render(context, mouseX, mouseY, delta);

        int startY = 50;
        int end = Math.min(scrollOffset + VISIBLE_ROWS, filteredBlocks.size());

        for (int i = scrollOffset; i < end; i++) {
            Block block = filteredBlocks.get(i);
            int y = startY + (i - scrollOffset) * 18;
            boolean hovered = mouseX >= this.width / 2 - 120 && mouseX <= this.width / 2 + 120
                    && mouseY >= y && mouseY <= y + 16;

            Identifier id = Registries.BLOCK.getId(block);
            String name = block.getName().getString() + " §8(" + id.getPath() + ")";

            int color = hovered ? 0xFFFF55 : 0xFFFFFF;
            if (block == BuildingState.selectedBlock) color = 0x55FF55;

            context.drawTextWithShadow(this.textRenderer, name, this.width / 2 - 110, y + 4, color);
        }

        if (filteredBlocks.size() > VISIBLE_ROWS) {
            context.drawCenteredTextWithShadow(this.textRenderer,
                    Text.literal("§7Mouse wheel to scroll • " + filteredBlocks.size() + " blocks"),
                    this.width / 2, this.height - 50, 0xAAAAAA);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int startY = 50;
            int end = Math.min(scrollOffset + VISIBLE_ROWS, filteredBlocks.size());
            for (int i = scrollOffset; i < end; i++) {
                int y = startY + (i - scrollOffset) * 18;
                if (mouseX >= this.width / 2 - 120 && mouseX <= this.width / 2 + 120
                        && mouseY >= y && mouseY <= y + 16) {
                    BuildingState.selectedBlock = filteredBlocks.get(i);
                    if (this.client != null && this.client.player != null) {
                        this.client.player.sendMessage(Text.literal("§aSelected: " + BuildingState.selectedBlock.getName().getString()), true);
                    }
                    this.close();
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount > 0 && scrollOffset > 0) {
            scrollOffset--;
        } else if (verticalAmount < 0 && scrollOffset + VISIBLE_ROWS < filteredBlocks.size()) {
            scrollOffset++;
        }
        return true;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}

package com.example.buildingtools.client;

import com.example.buildingtools.item.ModItems;
import com.example.buildingtools.screen.BlockPickerScreen;
import com.example.buildingtools.screen.ShapeMenuScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class BuildingToolsClient implements ClientModInitializer {
    public static KeyBinding openBlockPickerKey;
    public static KeyBinding openShapeMenuKey;

    @Override
    public void onInitializeClient() {
        openBlockPickerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.buildingtools.block_picker",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F,
                "category.buildingtools"
        ));

        openShapeMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.buildingtools.shape_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.buildingtools"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openBlockPickerKey.wasPressed()) {
                if (client.player != null && client.player.getMainHandStack().isOf(ModItems.BUILDING_WAND)) {
                    openBlockPicker();
                }
            }
            while (openShapeMenuKey.wasPressed()) {
                if (client.player != null && client.player.getMainHandStack().isOf(ModItems.BUILDING_WAND)) {
                    openShapeMenu();
                }
            }
        });
    }

    public static void openBlockPicker() {
        MinecraftClient.getInstance().setScreen(new BlockPickerScreen());
    }

    public static void openShapeMenu() {
        MinecraftClient.getInstance().setScreen(new ShapeMenuScreen());
    }
}

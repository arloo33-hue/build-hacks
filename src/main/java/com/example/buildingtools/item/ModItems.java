package com.example.buildingtools.item;

import com.example.buildingtools.BuildingTools;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item BUILDING_WAND = register("building_wand",
            new BuildingWandItem(new FabricItemSettings().maxCount(1)));

    public static final Item GHOST_TOOL = register("ghost_tool",
            new GhostToolItem(new FabricItemSettings().maxCount(1)));

    public static final Item MEASURING_TAPE = register("measuring_tape",
            new MeasuringTapeItem(new FabricItemSettings().maxCount(1)));

    public static final Item COPY_PASTE_TOOL = register("copy_paste_tool",
            new CopyPasteToolItem(new FabricItemSettings().maxCount(1)));

    public static final Item TRAP_LAUNCHER = register("trap_launcher",
            new TrapLauncherItem(new FabricItemSettings().maxCount(1)));

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(BuildingTools.MOD_ID, name), item);
    }

    public static void register() {
        // Forces class loading / static initialization
    }
}

package com.example.buildingtools;

import com.example.buildingtools.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildingTools implements ModInitializer {
    public static final String MOD_ID = "buildingtools";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final RegistryKey<ItemGroup> BUILDING_TOOLS_GROUP_KEY = RegistryKey.of(
            RegistryKeys.ITEM_GROUP, new Identifier(MOD_ID, "main"));

    public static final ItemGroup BUILDING_TOOLS_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.BUILDING_WAND))
            .displayName(Text.translatable("itemGroup.buildingtools.main"))
            .entries((displayContext, entries) -> {
                entries.add(ModItems.BUILDING_WAND);
                entries.add(ModItems.GHOST_TOOL);
                entries.add(ModItems.MEASURING_TAPE);
                entries.add(ModItems.COPY_PASTE_TOOL);
                entries.add(ModItems.TRAP_LAUNCHER);
            })
            .build();

    @Override
    public void onInitialize() {
        ModItems.register();

        Registry.register(Registries.ITEM_GROUP, BUILDING_TOOLS_GROUP_KEY, BUILDING_TOOLS_GROUP);

        // Also add to vanilla tabs for convenience
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(ModItems.BUILDING_WAND);
            entries.add(ModItems.GHOST_TOOL);
            entries.add(ModItems.MEASURING_TAPE);
            entries.add(ModItems.COPY_PASTE_TOOL);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(ModItems.TRAP_LAUNCHER);
        });

        LOGGER.info("Advanced Building Tools (1.20.1) loaded!");
    }
}

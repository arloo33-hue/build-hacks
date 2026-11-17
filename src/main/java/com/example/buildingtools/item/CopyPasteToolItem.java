package com.example.buildingtools.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public class CopyPasteToolItem extends Item {
    public CopyPasteToolItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player != null && !context.getWorld().isClient) {
            player.sendMessage(Text.literal("§7Copy/Paste – select two corners then paste (full version coming)"), true);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.literal("§aCopy & Paste Tool");
    }
}

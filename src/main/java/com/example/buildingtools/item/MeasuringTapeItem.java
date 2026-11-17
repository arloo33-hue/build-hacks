package com.example.buildingtools.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MeasuringTapeItem extends Item {
    private static BlockPos firstPos = null;

    public MeasuringTapeItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getBlockPos();

        if (player == null || world.isClient) return ActionResult.SUCCESS;

        if (firstPos == null) {
            firstPos = pos.toImmutable();
            player.sendMessage(Text.literal("§aFirst corner: " + pos.toShortString()), true);
        } else {
            int dx = Math.abs(pos.getX() - firstPos.getX()) + 1;
            int dy = Math.abs(pos.getY() - firstPos.getY()) + 1;
            int dz = Math.abs(pos.getZ() - firstPos.getZ()) + 1;
            int volume = dx * dy * dz;

            player.sendMessage(Text.literal(String.format(
                    "§bSize: §f%d × %d × %d §7(Volume: %d)", dx, dy, dz, volume)), false);
            firstPos = null;
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.literal("§eMeasuring Tape");
    }
}

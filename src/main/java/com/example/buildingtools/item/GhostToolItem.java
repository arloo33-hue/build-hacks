package com.example.buildingtools.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GhostToolItem extends Item {
    public GhostToolItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        BlockState state = world.getBlockState(pos);

        if (player == null || world.isClient) return ActionResult.SUCCESS;

        // Simple ghost: replace with barrier. Full version would use a custom translucent block.
        if (!state.isAir() && state.getBlock() != Blocks.BARRIER) {
            world.setBlockState(pos, Blocks.BARRIER.getDefaultState());
            player.sendMessage(Text.literal("§7Turned into ghost block (Barrier). Right-click again to remove."), true);
            return ActionResult.SUCCESS;
        }

        if (state.getBlock() == Blocks.BARRIER) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            player.sendMessage(Text.literal("§7Ghost block removed."), true);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.literal("§dGhost Tool");
    }
}

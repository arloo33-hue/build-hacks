package com.example.buildingtools.item;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TrapLauncherItem extends Item {
    public TrapLauncherItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getBlockPos().up();

        if (player == null || world.isClient) return ActionResult.SUCCESS;

        world.setBlockState(pos, Blocks.OAK_PRESSURE_PLATE.getDefaultState());
        player.sendMessage(Text.literal("§cPressure plate trap placed."), true);
        return ActionResult.SUCCESS;
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.literal("§cTrap Launcher");
    }
}

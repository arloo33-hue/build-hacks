package com.example.buildingtools.item;

import com.example.buildingtools.client.BuildingToolsClient;
import com.example.buildingtools.util.BuildingState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BuildingWandItem extends Item {
    public BuildingWandItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (world.isClient) {
            if (BuildingState.selectedBlock != null) {
                BuildingToolsClient.openShapeMenu();
            } else {
                BuildingToolsClient.openBlockPicker();
            }
        }
        return TypedActionResult.success(stack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getBlockPos();

        if (player == null) return ActionResult.PASS;

        if (world.isClient) {
            BuildingState.handleClick(pos, player.isSneaking());
            return ActionResult.SUCCESS;
        }

        // Server-side placement
        if (BuildingState.hasValidSelection() && BuildingState.selectedBlock != null) {
            BuildingState.placeShape(world, player);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.literal("§bBuilding Wand §7(F = Picker)");
    }
}

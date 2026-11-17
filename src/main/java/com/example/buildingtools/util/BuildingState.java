package com.example.buildingtools.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class BuildingState {
    public enum Shape {
        CUBE("Cube"),
        HOLLOW_CUBE("Hollow Cube"),
        SPHERE("Sphere"),
        HOLLOW_SPHERE("Hollow Sphere"),
        CYLINDER("Cylinder"),
        HOLLOW_CYLINDER("Hollow Cylinder"),
        PYRAMID("Pyramid"),
        LINE("Line"),
        WALL("Wall"),
        FLOOR("Floor / Platform"),
        CIRCLE("Circle (Flat)"),
        DOME("Dome"),
        CONE("Cone"),
        CROSS("Cross / Plus"),
        FRAME("Frame (Edges only)"),
        DIAGONAL_WALL("Diagonal Wall"),
        STAIRS("Stairs"),
        ARCH("Arch"),
        TUNNEL("Tunnel"),
        RANDOM_SCATTER("Random Scatter");

        public final String displayName;
        Shape(String displayName) {
            this.displayName = displayName;
        }
    }

    public enum Mode {
        SOLID("Solid (Fill)"),
        HOLLOW("Hollow"),
        AIR("Air / Clear");

        public final String displayName;
        Mode(String displayName) {
            this.displayName = displayName;
        }
    }

    public static Block selectedBlock = Blocks.STONE;
    public static Shape currentShape = Shape.CUBE;
    public static Mode currentMode = Mode.SOLID;

    public static BlockPos pos1 = null;
    public static BlockPos pos2 = null;

    public static void handleClick(BlockPos pos, boolean sneaking) {
        if (sneaking || pos1 == null) {
            pos1 = pos.toImmutable();
            pos2 = null;
        } else {
            pos2 = pos.toImmutable();
        }
    }

    public static boolean hasValidSelection() {
        return pos1 != null && pos2 != null;
    }

    public static void clearSelection() {
        pos1 = null;
        pos2 = null;
    }

    public static void placeShape(World world, PlayerEntity player) {
        if (!hasValidSelection() || selectedBlock == null) return;

        Set<BlockPos> positions = calculatePositions(pos1, pos2, currentShape, currentMode);
        BlockState state = currentMode == Mode.AIR ? Blocks.AIR.getDefaultState() : selectedBlock.getDefaultState();

        int placed = 0;
        for (BlockPos p : positions) {
            if (world.isInBuildLimit(p)) {
                world.setBlockState(p, state, 3);
                placed++;
            }
        }

        player.sendMessage(net.minecraft.text.Text.literal("§aPlaced " + placed + " blocks (" + currentShape.displayName + ")"), true);
        clearSelection();
    }

    private static Set<BlockPos> calculatePositions(BlockPos a, BlockPos b, Shape shape, Mode mode) {
        Set<BlockPos> result = new HashSet<>();

        int minX = Math.min(a.getX(), b.getX());
        int minY = Math.min(a.getY(), b.getY());
        int minZ = Math.min(a.getZ(), b.getZ());
        int maxX = Math.max(a.getX(), b.getX());
        int maxY = Math.max(a.getY(), b.getY());
        int maxZ = Math.max(a.getZ(), b.getZ());

        int cx = (minX + maxX) / 2;
        int cy = (minY + maxY) / 2;
        int cz = (minZ + maxZ) / 2;
        double rx = (maxX - minX) / 2.0 + 0.5;
        double ry = (maxY - minY) / 2.0 + 0.5;
        double rz = (maxZ - minZ) / 2.0 + 0.5;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    boolean include = false;

                    switch (shape) {
                        case CUBE, HOLLOW_CUBE -> {
                            include = true;
                            if (shape == Shape.HOLLOW_CUBE || mode == Mode.HOLLOW) {
                                include = x == minX || x == maxX || y == minY || y == maxY || z == minZ || z == maxZ;
                            }
                        }
                        case SPHERE, HOLLOW_SPHERE -> {
                            double dx = (x - cx) / rx;
                            double dy = (y - cy) / ry;
                            double dz = (z - cz) / rz;
                            double dist = dx * dx + dy * dy + dz * dz;
                            include = dist <= 1.0;
                            if (shape == Shape.HOLLOW_SPHERE || mode == Mode.HOLLOW) {
                                include = dist <= 1.0 && dist >= 0.7;
                            }
                        }
                        case CYLINDER, HOLLOW_CYLINDER -> {
                            double dx = (x - cx) / rx;
                            double dz = (z - cz) / rz;
                            double dist = dx * dx + dz * dz;
                            include = dist <= 1.0;
                            if (shape == Shape.HOLLOW_CYLINDER || mode == Mode.HOLLOW) {
                                include = dist <= 1.0 && dist >= 0.75;
                            }
                        }
                        case PYRAMID -> {
                            double progress = (double) (y - minY) / Math.max(1, maxY - minY);
                            double currentRx = rx * (1.0 - progress);
                            double currentRz = rz * (1.0 - progress);
                            double dx = Math.abs(x - cx);
                            double dz = Math.abs(z - cz);
                            include = dx <= currentRx && dz <= currentRz;
                        }
                        case LINE -> include = isOnLine(a, b, new BlockPos(x, y, z));
                        case WALL -> {
                            if (maxX - minX >= maxZ - minZ) {
                                include = Math.abs(z - cz) <= 0;
                            } else {
                                include = Math.abs(x - cx) <= 0;
                            }
                        }
                        case FLOOR -> include = y == minY || y == maxY;
                        case CIRCLE -> {
                            double dx = (x - cx) / rx;
                            double dz = (z - cz) / rz;
                            double dist = dx * dx + dz * dz;
                            include = dist <= 1.0 && Math.abs(y - cy) <= 0;
                        }
                        case DOME -> {
                            double dx = (x - cx) / rx;
                            double dz = (z - cz) / rz;
                            double dist = dx * dx + dz * dz;
                            include = dist <= 1.0 && y >= cy;
                        }
                        case FRAME -> {
                            int edges = 0;
                            if (x == minX || x == maxX) edges++;
                            if (y == minY || y == maxY) edges++;
                            if (z == minZ || z == maxZ) edges++;
                            include = edges >= 2;
                        }
                        case CROSS -> include = (x == cx || z == cz);
                        default -> include = true;
                    }

                    if (include) {
                        result.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        return result;
    }

    private static boolean isOnLine(BlockPos a, BlockPos b, BlockPos p) {
        int dx = b.getX() - a.getX();
        int dy = b.getY() - a.getY();
        int dz = b.getZ() - a.getZ();
        int steps = Math.max(Math.max(Math.abs(dx), Math.abs(dy)), Math.abs(dz));
        if (steps == 0) return p.equals(a);

        for (int i = 0; i <= steps; i++) {
            int x = a.getX() + (dx * i) / steps;
            int y = a.getY() + (dy * i) / steps;
            int z = a.getZ() + (dz * i) / steps;
            if (p.getX() == x && p.getY() == y && p.getZ() == z) return true;
        }
        return false;
    }
}

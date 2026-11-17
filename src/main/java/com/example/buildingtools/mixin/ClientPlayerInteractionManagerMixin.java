package com.example.buildingtools.mixin;

import com.example.buildingtools.item.ModItems;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Shadow
    private ClientPlayerEntity player;

    /**
     * Extends block reach while holding the Building Wand or Ghost Tool.
     * Method name may be getReachDistance or similar depending on exact mappings.
     * After ./gradlew genSources check the exact name in the interaction manager.
     */
    @Inject(method = "getReachDistance", at = @At("RETURN"), cancellable = true)
    private void extendReach(CallbackInfoReturnable<Float> cir) {
        if (player == null) return;
        ItemStack main = player.getMainHandStack();
        ItemStack off = player.getOffHandStack();

        if (main.isOf(ModItems.BUILDING_WAND) || off.isOf(ModItems.BUILDING_WAND)
                || main.isOf(ModItems.GHOST_TOOL) || off.isOf(ModItems.GHOST_TOOL)) {
            cir.setReturnValue(64.0f);
        }
    }
}

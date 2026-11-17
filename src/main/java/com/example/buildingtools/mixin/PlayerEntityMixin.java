package com.example.buildingtools.mixin;

import com.example.buildingtools.item.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    /**
     * In 1.20.1 the main reach methods are getBlockInteractionRange / getEntityInteractionRange
     * (added in 1.20.5 actually in some mappings). For 1.20.1 we also patch the interaction manager.
     * This is a safe fallback.
     */
    @Inject(method = "isCreativeLevelTwoOp", at = @At("HEAD"), cancellable = true)
    private void dummy(CallbackInfoReturnable<Boolean> cir) {
        // Placeholder – real reach extension is done in ClientPlayerInteractionManagerMixin
    }
}

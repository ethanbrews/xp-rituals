package me.ethanbrews.xprituals.mixin;

import me.ethanbrews.xprituals.extensions.PlayerEntityExtensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class EntityPlayerMixin {
    @Inject(method = "attack", at = @At("HEAD"))
    public void onAttack(Entity target, CallbackInfo cb_info) {
        // Casting to Object and then to PlayerEntity is a sneaky trick to cast to a PlayerEntity without
        // compile time errors. This class is mixed in to PlayerEntity, so it cannot extend it. (This would
        // result in class PlayerEntity extends PlayerEntity).
        PlayerEntityExtensions.INSTANCE.onPlayerAttack((PlayerEntity)(Object)this, target);
    }
}

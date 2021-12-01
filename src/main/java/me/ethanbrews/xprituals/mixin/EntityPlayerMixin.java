package me.ethanbrews.xprituals.mixin;

import com.mojang.authlib.GameProfile;
import me.ethanbrews.xprituals.common.extensions.PlayerEntityExtensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class EntityPlayerMixin {
    @Inject(method = "attack", at = @At("HEAD"))
    public void onAttack(Entity target, CallbackInfo cb_info) {
        PlayerEntityExtensions.INSTANCE.onPlayerAttack((PlayerEntity)(Object)this, target);
    }
}

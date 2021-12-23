package me.ethanbrews.xprituals.mixin;

import me.ethanbrews.xprituals.IMixedLivingEntity;
import me.ethanbrews.xprituals.extensions.PlayerEntityExtensions;
import me.ethanbrews.xprituals.persistance.DeadMobPersistentState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
import java.util.function.Function;

import static kotlin.reflect.jvm.internal.impl.builtins.functions.FunctionClassKind.Function;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IMixedLivingEntity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    public boolean isBoundToLens = false;

    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource source, CallbackInfo ci) {
        if (isBoundToLens && !world.isClient) {
            // This entity is bound to a respawn lens
            ((ServerWorld)world).getPersistentStateManager().set(
                    this.getUuidAsString(),
                    DeadMobPersistentState.Companion.fromEntity((LivingEntity)(Object)(this))
            );
        }
    }
}

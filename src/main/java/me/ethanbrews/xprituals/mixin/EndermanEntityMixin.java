package me.ethanbrews.xprituals.mixin;


import me.ethanbrews.xprituals.IMixedEndermanEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin implements IMixedEndermanEntity {
    boolean canTeleport;

    public boolean getCanTeleport() { return canTeleport; }
    public void setCanTeleport(boolean value) { canTeleport = value; }

    @Shadow
    World world;

    @Shadow
    abstract boolean teleport(double x, double y, double z, boolean canTeleport);

    @Shadow
    abstract boolean isSilent();

    @Shadow
    double prevX;

    @Shadow
    double prevY;

    @Shadow
    double prevZ;

    @Shadow
    abstract SoundCategory getSoundCategory();

    @Shadow
    abstract void playSound(SoundEvent event, float a, float b);

    @Inject(method = "<init>", at = @At("HEAD"))
    public void init() {
        canTeleport = true;
    }

    private boolean teleportTo(double x, double y, double z) {
        if (!canTeleport)
            return false;

        BlockPos.Mutable mutable = new BlockPos.Mutable(x, y, z);

        while(mutable.getY() > this.world.getBottomY() && !this.world.getBlockState(mutable).getMaterial().blocksMovement()) {
            mutable.move(Direction.DOWN);
        }

        BlockState blockState = this.world.getBlockState(mutable);
        boolean bl = blockState.getMaterial().blocksMovement();
        boolean bl2 = blockState.getFluidState().isIn(FluidTags.WATER);
        if (bl && !bl2) {
            boolean bl3 = this.teleport(x, y, z, true);
            if (bl3 && !this.isSilent()) {
                this.world.playSound((PlayerEntity)null, this.prevX, this.prevY, this.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
                this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }

            return bl3;
        } else {
            return false;
        }

    }
}
package me.ethanbrews.xprituals.mixin;

import me.ethanbrews.xprituals.IMixedMobEntity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin implements IMixedMobEntity {
    @Shadow
    protected int experiencePoints;

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public void setExperiencePoints(int xp) {
        experiencePoints = xp;
    }


}

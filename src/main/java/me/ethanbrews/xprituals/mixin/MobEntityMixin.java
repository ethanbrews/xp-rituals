package me.ethanbrews.xprituals.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {
    @Shadow
    protected int experiencePoints;

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public void setExperiencePoints(int xp) {
        experiencePoints = xp;
    }


}

package me.ethanbrews.xprituals.mixin;

import me.ethanbrews.xprituals.IMixedMobEntity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

// The class must implement an interface, so it can be cast to in the rest of the program.
@Mixin(MobEntity.class)
public class MobEntityMixin implements IMixedMobEntity {
    @Shadow
    protected int experiencePoints;

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public void setExperiencePoints(int xp) {
        experiencePoints = xp;
    }


}

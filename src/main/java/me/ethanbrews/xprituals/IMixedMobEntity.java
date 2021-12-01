package me.ethanbrews.xprituals;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;

public interface IMixedMobEntity {
    int getExperiencePoints();
    void setExperiencePoints(int xp);
}

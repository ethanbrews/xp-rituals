package me.ethanbrews.xprituals.common.extensions

import me.ethanbrews.xprituals.mixin.MobEntityMixin
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld

object PlayerEntityExtensions {
    fun onPlayerAttack(player: PlayerEntity, target: Entity) {
        if (target is MobEntity) {
            val exp = (target as MobEntityMixin).experiencePoints;
            val toDrop = if (exp < 0.5) {
                exp
            } else {
                exp / 1.8
            }

            // make target drop xp now
            EntityType.EXPERIENCE_ORB.spawn(
                player.world as ServerWorld?,
                null,
                null,
                null,
                target.blockPos,
                SpawnReason.EVENT,
                true,
                false
            )

        }
    }
}
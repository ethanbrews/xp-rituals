package me.ethanbrews.xprituals.common.extensions

import me.ethanbrews.xprituals.IMixedMobEntity
import me.ethanbrews.xprituals.common.item.ModItems
import net.minecraft.entity.Entity
import net.minecraft.entity.ExperienceOrbEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.GameRules
import org.apache.logging.log4j.LogManager

object PlayerEntityExtensions {
    val logger = LogManager.getLogger()

    fun onPlayerAttack(player: PlayerEntity, target: Entity) {
        if (player.isHolding(ModItems.xp_stick) && target is MobEntity) {
            val exp: Int = (target as IMixedMobEntity).experiencePoints;
            val toDrop = if (exp < 5) {
                exp
            } else {
                exp / 2
            }
            logger.info("Will drop $exp xp.")
            val world = player.world;
            if (world is ServerWorld && world.gameRules.getBoolean(GameRules.DO_MOB_LOOT)) {
                //TODO: toDrop multiplier should be loaded from config.
                ExperienceOrbEntity.spawn(world, target.pos, (toDrop*2) as Int)
                target.experiencePoints -= toDrop
            }
        }
    }
}
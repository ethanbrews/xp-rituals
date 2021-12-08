package me.ethanbrews.xprituals.enchantments

import me.ethanbrews.xprituals.IMixedEndermanEntity
import me.ethanbrews.xprituals.IMixedMobEntity
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.ExperienceOrbEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.EndermanEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.GameRules

class XpBoostEnchantment : Enchantment(
    Rarity.RARE,
    EnchantmentTarget.WEAPON,
    arrayOf(EquipmentSlot.MAINHAND)
) {

    override fun getMinPower(level: Int): Int {
        return level*10
    }

    override fun canAccept(other: Enchantment?): Boolean = true

    override fun onTargetDamaged(user: LivingEntity?, target: Entity?, level: Int) {
        if (target is MobEntity) {
            if (target.world is ServerWorld && target.world.gameRules.getBoolean(GameRules.DO_MOB_LOOT)) {
                if (target.isDead) {
                    ExperienceOrbEntity.spawn(target.world as ServerWorld, target.pos, (target as IMixedMobEntity).experiencePoints*level)
                }
            }
        }

        super.onTargetDamaged(user, target, level)
    }
}
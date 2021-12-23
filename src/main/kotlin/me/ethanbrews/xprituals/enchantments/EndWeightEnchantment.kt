package me.ethanbrews.xprituals.enchantments

import me.ethanbrews.xprituals.IMixedEndermanEntity
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.EndermanEntity

/**
 *  TODO: Give players the 'End Weight' effect to prevent ender pearls from working whilst effected.
 */
class EndWeightEnchantment : Enchantment(
    Rarity.VERY_RARE,
    EnchantmentTarget.WEAPON,
    arrayOf(EquipmentSlot.MAINHAND)
) {

    override fun getMinPower(level: Int): Int {
        return level*10
    }

    override fun canAccept(other: Enchantment?): Boolean = true

    override fun onTargetDamaged(user: LivingEntity?, target: Entity?, level: Int) {
        if (target is IMixedEndermanEntity) {
            target.canTeleport = false
        } else {
            //TODO: Effect [target] with the as-yet unimplemented [EndWeightEffect]
        }

        super.onTargetDamaged(user, target, level)
    }
}
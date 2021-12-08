package me.ethanbrews.xprituals.registry

import me.ethanbrews.fabric.RegistryHelper.id
import me.ethanbrews.xprituals.enchantments.EndWeightEnchantment
import me.ethanbrews.xprituals.enchantments.XpBoostEnchantment
import net.minecraft.util.registry.Registry

object ModEnchantments {

    val endWeightEnchantment = EndWeightEnchantment()
    val xpBoostEnchantment = XpBoostEnchantment()

    fun registerEnchantments() {
        Registry.register(Registry.ENCHANTMENT, id("end_weight"), endWeightEnchantment)
        Registry.register(Registry.ENCHANTMENT, id("xp_boost"), xpBoostEnchantment)
    }

}
package me.ethanbrews.xprituals.common.item

import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object ModItems {
    private fun id(name: String): Identifier = Identifier("xp-rituals:${name}")

    fun registerItems() {
        Registry.register(Registry.ITEM, id("xp-stick"), XpStick())
    }
}
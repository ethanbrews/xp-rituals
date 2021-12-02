package me.ethanbrews.xprituals.common.item

import me.ethanbrews.xprituals.XpRituals
import me.ethanbrews.xprituals.util.RegistryHelper.id
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object ModItems {
    val xp_stick = XpStick()
    //val guide_book = GuideBook()

    fun registerItems() {
        Registry.register(Registry.ITEM, id("xp_stick"), xp_stick)
        //Registry.register(Registry.ITEM, id("guide-book"), guide_book)
    }
}
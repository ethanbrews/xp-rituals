package me.ethanbrews.xprituals.item

import me.ethanbrews.fabric.RegistryHelper.id
import me.ethanbrews.xprituals.registry.ModBlocks
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.registry.Registry


object ModItems {
    val xp_stick = XpStick()
    val zombie_brains = Item(Item.Settings().group(ItemGroup.MISC))
    //val guide_book = GuideBook()

    fun registerItems() {
        Registry.register(Registry.ITEM, id("xp_stick"), xp_stick)
        Registry.register(Registry.ITEM, id("zombie_brain"), zombie_brains)
        Registry.register(Registry.ITEM, id("enchant_pedestal"), BlockItem(ModBlocks.enchant_pedestal_block, Item.Settings().group(ItemGroup.MISC)))
        //Registry.register(Registry.ITEM, id("guide-book"), guide_book)
    }
}
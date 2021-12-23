package me.ethanbrews.xprituals.registry

import me.ethanbrews.fabric.RegistryHelper.id
import me.ethanbrews.xprituals.item.Defaults.defaultItemSettings
import me.ethanbrews.xprituals.item.RespawnLens
import me.ethanbrews.xprituals.item.StaffOfDebugging
import me.ethanbrews.xprituals.item.StaffOfKnowledge
import me.ethanbrews.xprituals.item.StaffOfKnowledge2
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.registry.Registry


object ModItems {

    val item_group: ItemGroup = FabricItemGroupBuilder.create(id("general"))
        .icon { ItemStack(enchant_pedestal_item) }
        .build()

    val tier1Staff = StaffOfKnowledge()
    val tier2Staff = StaffOfKnowledge2()
    val zombie_brains = Item(defaultItemSettings)
    private val debug_stick = StaffOfDebugging()
    private val enchant_pedestal_item = BlockItem(ModBlocks.enchant_pedestal_block, defaultItemSettings)
    private val respawn_lens = RespawnLens()
    //val guide_book = GuideBook()

    fun registerItems() {
        Registry.register(Registry.ITEM, id("staff_of_knowledge"), tier1Staff)
        Registry.register(Registry.ITEM, id("staff_of_greater_knowledge"), tier2Staff)
        Registry.register(Registry.ITEM, id("staff_of_debugging"), debug_stick)
        Registry.register(Registry.ITEM, id("zombie_brain"), zombie_brains)
        Registry.register(Registry.ITEM, id("enchant_pedestal"), enchant_pedestal_item)
        Registry.register(Registry.ITEM, id("respawn_lens"), respawn_lens)
        //Registry.register(Registry.ITEM, id("guide-book"), guide_book)
    }
}
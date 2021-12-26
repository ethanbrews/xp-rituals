package me.ethanbrews.xprituals.registry

import me.ethanbrews.fabric.RegistryHelper.id
import me.ethanbrews.xprituals.item.*
import me.ethanbrews.xprituals.item.Defaults.defaultItemSettings
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.LogManager


object ModItems {
    private val logger = LogManager.getLogger()

    val item_group: ItemGroup = FabricItemGroupBuilder.create(id("general"))
        .icon { ItemStack(enchant_pedestal_item) }
        .build()

    val tier1Staff = StaffOfKnowledge()
    val tier2Staff = StaffOfKnowledge2()
    val zombie_brains = Item(defaultItemSettings)
    private val debug_stick = StaffOfDebugging()
    private val enchant_pedestal_item = BlockItem(ModBlocks.enchant_pedestal_block, defaultItemSettings)
    //private val respawn_lens = RespawnLens()
    private val guide_book = GuideBook()

    fun registerItems() {
        Registry.register(Registry.ITEM, id("staff_of_knowledge"), tier1Staff)
        Registry.register(Registry.ITEM, id("staff_of_greater_knowledge"), tier2Staff)
        Registry.register(Registry.ITEM, id("staff_of_debugging"), debug_stick)
        Registry.register(Registry.ITEM, id("zombie_brain"), zombie_brains)
        Registry.register(Registry.ITEM, id("enchant_pedestal"), enchant_pedestal_item)
        //Registry.register(Registry.ITEM, id("respawn_lens"), respawn_lens)
        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("patchouli")) {
            logger.info("Loading guide book - patchouli is loaded.")
            logger.info("Guide book is disabled in this build")
            //Registry.register(Registry.ITEM, id("guide_book"), guide_book)
        } else {
            logger.info("Patchouli is not loaded. Skipping guide book registration.")
        }

    }
}
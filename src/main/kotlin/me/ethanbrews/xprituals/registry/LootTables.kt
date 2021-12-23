package me.ethanbrews.xprituals.registry

import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback.LootTableSetter
import net.minecraft.item.Items
import net.minecraft.loot.LootManager
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager


object LootTables {
    private val logger = LogManager.getLogger()

    fun registerLoot(resourceManager: ResourceManager?, lootManager: LootManager?, identifier: Identifier?, supplier: FabricLootSupplierBuilder, lootTableSetter: LootTableSetter?) {
        if (listOf("zombie", "drowned", "husk", "zombie_villager").map { Identifier("minecraft", "entities/$it") }.contains(identifier)) {
            logger.info("Registering loot for $identifier");
            val poolBuilder = FabricLootPoolBuilder.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(ModItems.zombie_brains))
            for(i in 0..6) {
                poolBuilder.with(ItemEntry.builder(Items.AIR))
            }
            supplier.pool(poolBuilder)
        }
    }
}
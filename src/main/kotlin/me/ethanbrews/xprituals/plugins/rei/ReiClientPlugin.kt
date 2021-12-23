package me.ethanbrews.xprituals.plugins.rei

import me.ethanbrews.xprituals.registry.ModItems
import me.shedaniel.rei.api.client.plugins.REIClientPlugin
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry
import me.shedaniel.rei.api.common.util.EntryStacks
import me.shedaniel.rei.plugin.common.BuiltinPlugin.CRAFTING
import net.minecraft.item.Items

class ReiClientPlugin : REIClientPlugin {
    override fun registerCategories(registry: CategoryRegistry?) {
        //registry?.addWorkstations(CRAFTING, EntryStacks.of(ModItems.ench))
    }
}
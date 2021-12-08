package me.ethanbrews.xprituals.registry

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry
import me.ethanbrews.xprituals.block.enchantPedestal.EnchantPedestalRenderer

object BlockRenderers {
    fun initBlockRenderers() {
        BlockEntityRendererRegistry.register(ModBlocks.enchant_pedestal_entity) { EnchantPedestalRenderer() }
    }
}
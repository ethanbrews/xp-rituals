package me.ethanbrews.xprituals.client.blockrenderer

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry
import me.ethanbrews.xprituals.common.block.ModBlocks
import me.ethanbrews.xprituals.common.item.ModItems

object BlockRenderers {
    fun initBlockRenderers() {
        BlockEntityRendererRegistry.register(ModBlocks.enchant_pedestal_entity) { EnchantPedestalRenderer() }
    }
}
package me.ethanbrews.xprituals.registry

import me.ethanbrews.xprituals.block.enchantPedestal.EnchantPedestalEntity
import me.ethanbrews.xprituals.block.enchantPedestal.EnchantPedestal
import me.ethanbrews.fabric.RegistryHelper.id
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry

object ModBlocks {

    val enchant_pedestal_block = EnchantPedestal()
    lateinit var enchant_pedestal_entity: BlockEntityType<EnchantPedestalEntity>

    fun registerBlocks() {
        Registry.register(Registry.BLOCK, id("enchant_pedestal"), enchant_pedestal_block)

        enchant_pedestal_entity = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("enchant_pedestal_entity"), FabricBlockEntityTypeBuilder.create({ x: BlockPos, y: BlockState -> EnchantPedestalEntity(x, y)}, enchant_pedestal_block).build())
    }
}
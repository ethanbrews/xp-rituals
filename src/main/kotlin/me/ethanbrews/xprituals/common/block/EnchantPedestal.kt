package me.ethanbrews.xprituals.common.block

import me.ethanbrews.xprituals.common.blockentity.EnchantPedestalEntity
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World


class EnchantPedestal : BlockWithEntity(Settings.of(Material.STONE)) {
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return EnchantPedestalEntity(pos, state)
    }

    override fun getRenderType(state: BlockState?): BlockRenderType = BlockRenderType.MODEL

    override fun <T : BlockEntity?> getTicker(
        world: World?,
        state: BlockState?,
        type: BlockEntityType<T>?
    ): BlockEntityTicker<T>? {
        return checkType(
            type, ModBlocks.enchant_pedestal_entity
        ) { world1: World, pos: BlockPos, state1: BlockState, be1: BlockEntity ->
            EnchantPedestalEntity.tick(
                world1,
                pos,
                state1,
                be1 as EnchantPedestalEntity
            )
        }
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult?
    ): ActionResult {
        if(hand == Hand.OFF_HAND) {
            return ActionResult.PASS
        }
        if (!world.isClient) {
            val e = world.getBlockEntity(pos) as EnchantPedestalEntity
            val res = e.use(player)
            e.sendUpdatePacket()
            return res
        }
        return ActionResult.PASS
    }
}
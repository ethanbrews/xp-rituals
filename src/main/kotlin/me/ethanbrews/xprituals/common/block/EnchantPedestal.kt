package me.ethanbrews.xprituals.common.block

import me.ethanbrews.xprituals.common.blockentity.EnchantPedestalEntity
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class EnchantPedestal : Block(Settings.of(Material.STONE)), BlockEntityProvider {
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return EnchantPedestalEntity(pos, state)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult?
    ): ActionResult {
        if (!world.isClient) {
            val e = world.getBlockEntity(pos) as EnchantPedestalEntity
            return e.use(player)
        }
        return ActionResult.PASS
    }
}
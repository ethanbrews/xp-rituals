package me.ethanbrews.xprituals.block.enchantPedestal

import me.ethanbrews.xprituals.registry.ModBlocks
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World


class EnchantPedestal : BlockWithEntity(Settings.of(Material.STONE).hardness(4.0F)) {
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

    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = VoxelShapes.union(
        VoxelShapes.cuboid(0.375, 0.0, 0.375, 0.625, 0.63, 0.625),
        VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0),
        VoxelShapes.cuboid(0.1875, 0.6925, 0.1875, 0.8125, 0.9425, 0.8125)
    )

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

    override fun onPlaced(
        world: World?,
        pos: BlockPos?,
        state: BlockState?,
        placer: LivingEntity?,
        itemStack: ItemStack?
    ) {
        super.onPlaced(world, pos, state, placer, itemStack)
    }
}
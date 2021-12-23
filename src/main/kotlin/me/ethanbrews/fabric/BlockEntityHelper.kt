package me.ethanbrews.fabric

import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object BlockEntityHelper {
    inline fun <reified T: BlockEntity?> readNbt(nbt: NbtCompound, world: World, key: String): T? = nbt.getIntArray(key).let {
        world.getBlockEntity(BlockPos(it[0], it[1], it[2])) as? T
    }

    fun writeNbt(nbt: NbtCompound, entity: BlockEntity, key: String): NbtCompound {
        nbt.putIntArray(key, listOf(entity.pos.x, entity.pos.y, entity.pos.z))
        return nbt
    }
}
package me.ethanbrews.fabric.blockentity

import me.ethanbrews.xprituals.block.enchantPedestal.EnchantPedestalEntity
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class MultiBlockEntity<T : BlockEntity>(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : EBlockEntity(type, pos, state) {
    protected var othersPos: List<BlockPos> = listOf()
    protected var masterPos: BlockPos? = null

    var isLoaded: Boolean = false
    var isValid: Boolean = false
    val isMaster: Boolean get() = isLoaded && isValid && masterPos == pos

    var master: T?
        get() = if (isLoaded && isValid && hasWorld()) world!!.getBlockEntity(masterPos) as T? else null
        set(value) { masterPos = value?.pos }

    var others: List<T>
        get() = if (hasWorld()) othersPos.map { world!!.getBlockEntity(it) as T } else listOf()
        set(value) { othersPos = value.map { it.pos } }

    override fun setWorld(world: World?) {
        super.setWorld(world)
        init()
    }

    protected abstract fun init()
}
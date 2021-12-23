package me.ethanbrews.fabric.blockentity

import me.ethanbrews.xprituals.block.enchantPedestal.EnchantPedestalEvent
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class MultiBlockEntityWithEvent<BT : BlockEntity, ET : IBlockEntityEvent>(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : MultiBlockEntity<BT>(type, pos, state) {
    var _event: ET? = null

    val event: ET? get() {
        return if (isValid && isLoaded) {
            if (isMaster) _event
            else (master as? MultiBlockEntityWithEvent<*, *>)?.event as ET?
        } else null
    }

    abstract fun serverTick()
    abstract fun clientTick()
    abstract fun eventCancelled()

    fun doTick(world: World) {
        if (_event?.isCancelled == true) {
            if (!world.isClient)
                sendUpdatePacket()
            eventCancelled()
            _event = null
        }
        if (world.isClient)
            clientTick()
        else
            serverTick()
    }

    // Is the current event blocking new events from being invoked? Always false if there is no current event
    val isBlocking: Boolean get() = event != null
}
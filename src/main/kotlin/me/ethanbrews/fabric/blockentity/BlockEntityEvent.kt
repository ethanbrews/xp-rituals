package me.ethanbrews.fabric.blockentity

import net.minecraft.nbt.NbtCompound

abstract class BlockEntityEvent : IBlockEntityEvent {
    override var isCancelled = false

    override fun cancel() {
        isCancelled = true
    }
}
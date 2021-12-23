package me.ethanbrews.fabric.blockentity

import net.minecraft.nbt.NbtCompound

interface IBlockEntityEvent {
    fun toNbt(): NbtCompound
    fun cancel()
    val isCancelled: Boolean
}
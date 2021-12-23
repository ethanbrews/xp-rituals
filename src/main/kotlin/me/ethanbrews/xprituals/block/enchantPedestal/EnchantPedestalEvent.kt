package me.ethanbrews.xprituals.block.enchantPedestal

import me.ethanbrews.fabric.BlockPosHelper
import me.ethanbrews.fabric.blockentity.BlockEntityEvent
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos

class EnchantPedestalEvent(
    var player: PlayerEntity?,
    var masterPos: BlockPos,
    var recipe: EnchantPedestalRecipe?,
    var expToRemove: Int,
    var tickCounter: Int,
) : BlockEntityEvent() {
    val expToRemoveEachTick: Int = expToRemove / (20*4)
    val totalExpToRemove = expToRemove

    override fun toNbt(): NbtCompound {
        val nbt = NbtCompound()
        nbt.putIntArray("masterPos", BlockPosHelper.toList(masterPos))
        nbt.putInt("exp", expToRemove)
        nbt.putInt("tickCounter", tickCounter)
        if (isCancelled)
            nbt.putBoolean("isCancelled", true)
        return nbt
    }

    companion object {
        fun fromNbt(nbt: NbtCompound): EnchantPedestalEvent {
            val event = EnchantPedestalEvent(
                null,
                BlockPosHelper.fromArray(nbt.getIntArray("masterPos")),
                null,
                nbt.getInt("exp"),
                nbt.getInt("tickCounter")
            )
            event.isCancelled = nbt.contains("isCancelled")
            return event
        }
    }
}
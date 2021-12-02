package me.ethanbrews.xprituals.common.blockentity

import me.ethanbrews.xprituals.common.block.ModBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import vazkii.patchouli.client.book.template.variable.ItemStackVariableSerializer

class EnchantPedestalEntity(pos: BlockPos, state: BlockState) : BlockEntity(ModBlocks.enchant_pedestal_entity, pos, state), Inventory {

    var stack: ItemStack = ItemStack.EMPTY

    fun use(player: PlayerEntity): ActionResult {
        val holding = player.inventory.getStack(player.inventory.selectedSlot)
        if (stack.isEmpty) {
            if (holding.isEmpty) {
                return ActionResult.PASS
            }
            player.inventory.setStack(player.inventory.selectedSlot, if (holding.count == 1) ItemStack.EMPTY else ItemStack(holding.item, holding.count - 1))
            setStack(0, ItemStack(holding.item, 1))
            markDirty()
            return ActionResult.CONSUME
        } else {
            if (!holding.isEmpty) {
                return ActionResult.PASS
            }
            player.inventory.setStack(player.inventory.selectedSlot, stack)
            setStack(0, ItemStack.EMPTY)
            markDirty()
            return ActionResult.SUCCESS
        }
    }

    override fun clear() {
        stack = ItemStack.EMPTY
    }

    override fun size() = 1

    override fun getMaxCountPerStack() = 1

    override fun isEmpty() = stack == ItemStack.EMPTY

    override fun getStack(slot: Int) = stack

    override fun removeStack(slot: Int, amount: Int): ItemStack {
        assert(slot == 0 && amount <= 1)
        if(amount == 0) {
            return ItemStack.EMPTY
        }
        val res = stack
        stack = ItemStack.EMPTY
        return res
    }

    override fun removeStack(slot: Int): ItemStack {
        return removeStack(slot, 1)
    }

    override fun setStack(slot: Int, stack: ItemStack?) {
        assert(slot == 0)
        this.stack = stack ?: ItemStack.EMPTY
    }

    override fun canPlayerUse(player: PlayerEntity?) = true

}
package me.ethanbrews.xprituals.common.blockentity

import me.ethanbrews.xprituals.common.block.ModBlocks
import me.ethanbrews.xprituals.common.network.BlockEntityUpdatePacketID
import me.ethanbrews.xprituals.common.network.Packets
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.apache.logging.log4j.LogManager
import kotlin.math.max


class EnchantPedestalEntity(pos: BlockPos, state: BlockState) : BlockEntity(ModBlocks.enchant_pedestal_entity, pos, state) {
    val logger = LogManager.getLogger()
    var inventory = SimpleInventory(1)
    var tick_counter = 0

    override fun writeNbt(nbt: NbtCompound): NbtCompound {
        logger.info("Writing NBT list...${inventory.getStack(0).item}")
        nbt.put("inventory", inventory.toNbtList())
        return super.writeNbt(nbt)
    }

    override fun readNbt(nbt: NbtCompound) {
        logger.info("Reading NBT list...${inventory.getStack(0).item}")
        super.readNbt(nbt)
        inventory = SimpleInventory(1)
        this.inventory.readNbtList(nbt.getList("inventory", 10))
        logger.info("Read NBT list...${inventory.getStack(0).item}")
    }

    fun use(player: PlayerEntity): ActionResult {
        logger.info("${player.name.asString()} is using EnchantPedestalEntity at $pos.")
        val playerItem: ItemStack = player.inventory.getStack(player.inventory.selectedSlot)
        val blockItem: ItemStack = inventory.getStack(0)

        if (playerItem == ItemStack.EMPTY && blockItem != ItemStack.EMPTY) {
            player.inventory.setStack(player.inventory.selectedSlot, blockItem)
            inventory.setStack(0, ItemStack.EMPTY)
            return ActionResult.SUCCESS
        } else if (playerItem != ItemStack.EMPTY && blockItem == ItemStack.EMPTY) {
            player.inventory.setStack(player.inventory.selectedSlot, ItemStack.EMPTY)
            inventory.setStack(0, playerItem)
            return ActionResult.CONSUME
        }
        return ActionResult.PASS
    }



    override fun toUpdatePacket(): BlockEntityUpdateS2CPacket? {
        Packets.sendBlockEntityUpdate(this, BlockEntityUpdatePacketID.ENCHANT_PEDESTAL)
        return null
    }

    fun beginCrafting() {
        // Read recipe from surrounding blocks

        // Search for pedestals in a 9x9 grid around the center block (1 block gap)
        val enchantPedestals = listOf(
            pos.add(-2, 0, -2),
            pos.add(-2, 0, 0),
            pos.add(-2, 0, 2),
            pos.add(0, 0, -2),
            pos.add(0, 0, 2),
            pos.add(2, 0, -2),
            pos.add(2, 0, 0),
            pos.add(2, 0, 2)
        ).mapNotNull {
            val entity = world!!.getBlockEntity(it)
            // Will this cause null pointer exception? Should result in either an EnchantPedestalEntity or null
            entity as EnchantPedestalEntity
        }

        // Begin the crafting ticker... IT COUNTS DOWN TO 0!
        // Every ticksToConsumeComponent should consume an item from an outer pedestal then wait that time again to complete crafting...
    }

    fun sendUpdatePacket() {
        toUpdatePacket()
    }

    companion object {
        const val ticksToConsumeComponent = 3000

        fun tick(world: World, pos: BlockPos, state: BlockState, be: EnchantPedestalEntity) {
            be.tick_counter = max(0, be.tick_counter-1)
        }
    }
}
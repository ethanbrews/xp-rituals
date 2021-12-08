package me.ethanbrews.xprituals.block.enchantPedestal

import me.ethanbrews.xprituals.registry.ModEnchantments
import me.ethanbrews.xprituals.item.ModItems
import me.ethanbrews.xprituals.item.XpStick
import me.ethanbrews.xprituals.network.BlockEntityUpdatePacketID
import me.ethanbrews.xprituals.network.Packets
import me.ethanbrews.xprituals.recipe.EnchantPedestalRecipe
import me.ethanbrews.fabric.BlockPosHelper
import me.ethanbrews.xprituals.registry.ModBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.apache.logging.log4j.LogManager
import kotlin.math.max
import kotlin.random.Random


class EnchantPedestalEntity(pos: BlockPos, state: BlockState) : BlockEntity(ModBlocks.enchant_pedestal_entity, pos, state) {
    private val logger = LogManager.getLogger()
    var inventory = SimpleInventory(1)
    var tickCounter = 0
    var targetPedestal: EnchantPedestalEntity? = null

    var serverProviderEnchantPedestals: List<EnchantPedestalEntity>? = null
    var serverCurrentRecipe: EnchantPedestalRecipe? = null

    override fun writeNbt(nbt: NbtCompound): NbtCompound {
        super.writeNbt(nbt)
        logger.info("Writing NBT list...${inventory.getStack(0).item}")
        nbt.putInt("tickCounter", tickCounter)
        val targetPedestal2 = targetPedestal
        if (targetPedestal2 != null)
            nbt.putIntArray("targetPedestalPos", listOf(targetPedestal2.pos.x, targetPedestal2.pos.y, targetPedestal2.pos.z))
        nbt.put("inventory", inventory.toNbtList())
        return nbt
    }

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        logger.info("Reading NBT list...${inventory.getStack(0).item}")
        inventory = SimpleInventory(1)
        this.inventory.readNbtList(nbt.getList("inventory", 10))
        this.tickCounter = nbt.getInt("tickCounter")
        if (nbt.contains("targetPedestalPos")) {
            val pos = nbt.getIntArray("targetPedestalPos")
            this.targetPedestal = world?.getBlockEntity(BlockPos(pos[0], pos[1], pos[2])) as? EnchantPedestalEntity
        }
        logger.info("Read NBT list...${inventory.getStack(0).item}")
    }

    fun use(player: PlayerEntity): ActionResult {
        logger.info("${player.name.asString()} is using EnchantPedestalEntity at $pos.")

        if (tickCounter > 0 || targetPedestal != null)
            return ActionResult.FAIL

        val playerItem: ItemStack = player.inventory.getStack(player.inventory.selectedSlot)
        val blockItem: ItemStack = inventory.getStack(0)

        if (playerItem.item is XpStick) {
            beginCrafting()
            return ActionResult.SUCCESS
        }

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

    private fun beginCrafting() {
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
            world!!.getBlockEntity(it) as? EnchantPedestalEntity
        }

        enchantPedestals.forEach {
            logger.info("\t- ${it.inventory.getStack(0).item}")
        }

        //TODO: For some reason this is null when it.isValid(...) is true???????
        //It is too late for this nonsense
        val recipe = recipes.firstOrNull { it.isValid(inventory.getStack(0), enchantPedestals.map { other -> other.inventory.getStack(0).item }) }

        println("$recipe")
        if (recipe == null)
            return

        enchantPedestals.forEach {
            it.targetPedestal = this
        }

        serverCurrentRecipe = recipe

        tickCounter = enchantPedestals.fold(ticksToConsumeComponent) { acc, enchantPedestalEntity ->
            acc + (if (enchantPedestalEntity.inventory.getStack(0).isEmpty) 0 else ticksToConsumeComponent)
        }
        logger.info("Enchant pedestal crafting will take $tickCounter ticks")

        logger.info("Beginning enchant pedestal crafting on ${inventory.getStack(0).item} using:")
        enchantPedestals.forEach {
            logger.info("\t- ${it.inventory.getStack(0).item}")
            it.sendUpdatePacket()
        }

        serverProviderEnchantPedestals = enchantPedestals
    }

    fun serverTick() {
        val prev = tickCounter
        tickCounter = max(0, tickCounter-1)

        if (prev == 1) {
            this.inventory.setStack(0, serverCurrentRecipe?.output?.let { it(this.inventory.getStack(0)) })
            this.sendUpdatePacket()
            serverProviderEnchantPedestals?.forEach {
                if (serverCurrentRecipe?.consumesIngredients == true) {
                    it.inventory.setStack(0, ItemStack.EMPTY)
                }
                it.targetPedestal = null
                it.sendUpdatePacket()
            }

            serverProviderEnchantPedestals = null
        }


    }

    fun clientTick() {
        if ((tickCounter == 0 && (targetPedestal?.tickCounter ?: 0) == 0) || inventory.getStack(0).isEmpty)
            return
        // Local copy of target as it may be changed by another thread.
        val target = targetPedestal
        if (target == null) {
            world?.addParticle(ParticleTypes.PORTAL, true, pos.x.toDouble()+0.5, pos.y.toDouble()+1.5, pos.z.toDouble()+0.5, Random.nextDouble(-1.0, 1.0), Random.nextDouble(0.0, 1.0), Random.nextDouble(-1.0, 1.0))
            world?.addParticle(ParticleTypes.ENCHANT, true, pos.x.toDouble()+0.5, pos.y.toDouble()+1.5, pos.z.toDouble()+0.5, Random.nextDouble(-1.0, 1.0), Random.nextDouble(0.0, 1.0), Random.nextDouble(-1.0, 1.0))
        } else {
            val vector = BlockPosHelper.unitVector(pos, target.pos)
            listOf(0.6, 1.0, 1.4).forEach {
                world?.addParticle(ParticleTypes.ENCHANT, true, pos.x.toDouble()+(it*vector.x)+0.5, pos.y.toDouble()+1.5, pos.z.toDouble()+(it*vector.z)+0.5, vector.x*2, vector.y, vector.z*2)
                world?.addParticle(ParticleTypes.REVERSE_PORTAL, true, pos.x.toDouble()+(it*vector.x)+0.5, pos.y.toDouble()+1.5, pos.z.toDouble()+(it*vector.z)+0.5, (vector.x*0.02)+Random.nextDouble(-0.01, 0.01), vector.y+Random.nextDouble(0.0, 0.01), (vector.z*0.02)+Random.nextDouble(-0.01, 0.01))
            }

        }


    }

    fun sendUpdatePacket() {
        toUpdatePacket()
    }

    companion object {
        const val ticksToConsumeComponent = 25

        val recipes = listOf(
            EnchantPedestalRecipe.getRecipe(
                Enchantments.FORTUNE,
                "minecraft:fortune",
                listOf(Items.EMERALD, Items.EMERALD, Items.EMERALD_BLOCK, Items.EMERALD_BLOCK),
                50
            ),
            EnchantPedestalRecipe.getRecipe(
                ModEnchantments.endWeightEnchantment,
                "xp_rituals:end_weight",
                listOf(Items.ANVIL, Items.ENDER_PEARL, Items.ENDER_PEARL, Items.ENDER_PEARL),
                50
            ),
            EnchantPedestalRecipe.getRecipe(
                ModEnchantments.xpBoostEnchantment,
                "xp_rituals:xp_boost",
                listOf(ModItems.zombie_brains, ModItems.zombie_brains, ModItems.zombie_brains, ModItems.zombie_brains, Items.GOLD_INGOT, Items.GOLD_INGOT, Items.GOLD_INGOT, Items.GOLD_INGOT),
                150
            )
        )

        fun tick(world: World, pos: BlockPos, state: BlockState, be: EnchantPedestalEntity) {
            if (world.isClient) {
                be.clientTick()
            } else {
                be.serverTick()
            }
        }
    }
}
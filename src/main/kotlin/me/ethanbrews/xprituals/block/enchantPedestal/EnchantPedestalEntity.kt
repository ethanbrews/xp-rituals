package me.ethanbrews.xprituals.block.enchantPedestal

import me.ethanbrews.xprituals.item.StaffOfKnowledge
import me.ethanbrews.xprituals.network.BlockEntityUpdatePacketID
import me.ethanbrews.xprituals.network.Packets
import me.ethanbrews.fabric.BlockPosHelper
import me.ethanbrews.fabric.ExperienceHelper
import me.ethanbrews.fabric.blockentity.MultiBlockEntityWithEvent
import me.ethanbrews.xprituals.item.IStaff
import me.ethanbrews.xprituals.item.StaffOfDebugging
import me.ethanbrews.xprituals.recipe.EnchantPedestalRecipes
import me.ethanbrews.xprituals.registry.ModBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.ExperienceOrbEntity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.LiteralText
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.apache.logging.log4j.LogManager
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.sin
import kotlin.random.Random

class EnchantPedestalEntity(pos: BlockPos, state: BlockState) : MultiBlockEntityWithEvent<EnchantPedestalEntity, EnchantPedestalEvent>(ModBlocks.enchant_pedestal_entity, pos, state) {
    private val logger = LogManager.getLogger()
    private var _inventory = SimpleInventory(1)

    // The stack in the inventory. It's only got one slot
    var stack: ItemStack
        get() = _inventory.getStack(0)
        set(value) { _inventory.setStack(0, value) }

    override fun setWorld(world: World?) {
        super.setWorld(world)
        init()
    }

    override fun markRemoved() {
        super.markRemoved()
        isLoaded = false
        isValid = false
        if (world?.isClient == false) {
            master?.event?.cancel()

            if (!stack.isEmpty)
                world?.let { w -> w.spawnEntity(ItemEntity(w, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), stack)) }

            if (!isMaster)
                master?.init()
        }

        others.forEach { other ->
            other.othersPos = other.othersPos.filter { it != pos }
            other.masterPos = if (isMaster) null else other.masterPos
        }
    }

    override fun init() {
        if (hasWorld() && world?.isClient == false && !isLoaded) {
            isLoaded = true
            val allNearby = detectNearby()
            isValid = false
            if (isValidStructure(allNearby)) {
                logger.info("Final block placed in valid EnchantPedestal structure!")
                isValid = true
                val center = findCenterBlockOrNull(allNearby)!!
                for(other in allNearby) {
                    other.master = center
                    other.others = allNearby.filter { it.pos != other.pos }
                    other.isValid = isLoaded // Can only be true if it has loaded!
                    other.sendUpdatePacket()
                }
                center.others = allNearby.filter { it.pos != center.pos }
                center.master = center
                center.sendUpdatePacket()
            } else {
                logger.info("Structure is not yet valid")
            }
        }
    }

    private fun detectNearby(): List<EnchantPedestalEntity> {
        val result = mutableListOf(this)
        for (i in -4..4 step 2) {
            for (j in -4..4 step 2) {
                if (i == 0 && j == 0)
                    continue
                val pos = BlockPos(pos.x+i, pos.y, pos.z+j)
                val ent =  world!!.getBlockEntity(pos)
                if (ent is EnchantPedestalEntity) {
                    result.add(ent)
                }
            }
        }
        return result
    }

    private fun isValidStructure(): Boolean = isValidStructure(detectNearby())

    private fun isValidStructure(nearby: List<EnchantPedestalEntity>): Boolean {
        val centered = nearby.filter {
            listOf(
                BlockPosHelper.add(it.pos, -2, 0, 0),
                BlockPosHelper.add(it.pos, 2, 0, 0),
                BlockPosHelper.add(it.pos, 0, 0, -2),
                BlockPosHelper.add(it.pos, 0, 0, 2)
            ).all { pos ->
                nearby.any { near -> near.pos == pos }
            }
        }

        return(
            (centered.size == 1) && (
                (nearby.size == 5) ||
                (
                    (nearby.size == 9) &&
                    listOf(
                        BlockPosHelper.add(centered[0].pos, -2, 0, -2),
                        BlockPosHelper.add(centered[0].pos, -2, 0, 2),
                        BlockPosHelper.add(centered[0].pos, 2, 0, -2),
                        BlockPosHelper.add(centered[0].pos, 2, 0, 2)
                    ).all { pos ->
                        nearby.any { near -> near.pos == pos }
                    }
                )
            )
        )
    }

    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(nbt)
        //inventory
        nbt.put("inventory", _inventory.toNbtList())
        //isLoaded
        nbt.putBoolean("isLoaded", isLoaded)
        //isValid
        nbt.putBoolean("isValid", isValid)
        //masterPos
        masterPos?.let { nbt.putIntArray("masterPos", BlockPosHelper.toList(it)) }
        //othersPos
        val others = NbtCompound()
        others.putInt("size", othersPos.size)
        for (i in othersPos.indices) {
            others.putIntArray("$i", BlockPosHelper.toList(othersPos[i]))
        }
        nbt.put("others", others)
        _event?.let { nbt.put("event", it.toNbt()) }
    }

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        //inventory
        _inventory = SimpleInventory(1)
        _inventory.readNbtList(nbt.getList("inventory", 10))
        //isLoaded
        isLoaded = nbt.getBoolean("isLoaded")
        //isValid
        isValid = nbt.getBoolean("isValid")
        //masterPos
        if (nbt.contains("masterPos"))
            masterPos = BlockPosHelper.fromArray(nbt.getIntArray("masterPos"))
        //othersPos
        val others = nbt.get("others") as NbtCompound?
        val newOthers = mutableListOf<BlockPos>()
        others?.let {
            for (i in 0 until it.getInt("size")) {
                newOthers.add(BlockPosHelper.fromArray(it.getIntArray("$i")))
            }
        }
        othersPos = newOthers

        //event
        _event = if (nbt.contains("event"))
            EnchantPedestalEvent.fromNbt(nbt.getCompound("event"))
        else
            null
    }

    private fun doEnchant(sender: PlayerEntity, staff: ItemStack) {
        val enchantTarget: ItemStack = stack
        val ingredients: List<ItemStack> = others.map { it.stack }.subtract(setOf(ItemStack.EMPTY)).toList()

        // Check enchant target and ingredients
        val ingredientItems = ingredients.map { it.item }
        val recipe: EnchantPedestalRecipe
        try {
            recipe = recipes.first {
                it.ingredients.size == ingredients.size &&
                        it.ingredients.containsAll(ingredientItems) &&
                        ingredientItems.containsAll(it.ingredients) &&
                        it.input(enchantTarget, staff)
            }
        } catch (ex: NoSuchElementException) {
            logger.info("The player, ${sender.displayName.asString()}, attempted to use an enchant pedestal but there was no matching recipe.")
            return
        }

        if (sender.experienceLevel < recipe.xpAmount && !sender.isCreative) {
            logger.info("The player, ${sender.displayName.asString()}, attempted to use an enchant pedestal but didn't have the xp to trigger a ritual.")
            return
        }

        _event = EnchantPedestalEvent(sender, master!!.pos, recipe, if (sender.isCreative) 0 else ExperienceHelper.experienceToTake(sender.experienceLevel, recipe.xpAmount), ticksToConsumeComponent*(ingredients.size+1))
        logger.info("The player, ${sender.displayName.asString()}, has begun an enchantment ritual. It will cost ${_event?.expToRemove} exp take 80 ticks to begin and ${ticksToConsumeComponent*(1+recipe.ingredients.size)} ticks to complete.")
        sendUpdatePacket()
    }

    override fun outputDebugInfo(player: PlayerEntity) {
        player.sendMessage(LiteralText("Debug: Enchant pedestal entity at $pos"), false)
        player.sendMessage(LiteralText("isMaster: $isMaster"), false)
        if (!isMaster)
            player.sendMessage(LiteralText("Master: Enchant pedestal entity at ${master?.pos}"), false)
        player.sendMessage(LiteralText("Is a valid structure: ${isValidStructure()}"), false)
        player.sendMessage(LiteralText("Is blocking events: $isBlocking"), false)
        player.sendMessage(LiteralText("Connected block entities: ${othersPos.size}"), false)
    }

    fun use(player: PlayerEntity): ActionResult {
        val playerItem: ItemStack = player.inventory.getStack(player.inventory.selectedSlot)
        val blockItem: ItemStack = stack

        if (playerItem.item is StaffOfDebugging) {
            outputDebugInfo(player)
            return ActionResult.SUCCESS
        }

        if (isBlocking) {
            return ActionResult.FAIL
        }

        if (playerItem.item is IStaff && blockItem != ItemStack.EMPTY) {
            if (isMaster) {
                doEnchant(player, playerItem)
                return ActionResult.SUCCESS
            }
            return ActionResult.FAIL
        }

        if (blockItem == ItemStack.EMPTY) {
            if (playerItem != ItemStack.EMPTY) {
                if (playerItem.count > 1) {
                    playerItem.count -= 1
                    player.inventory.setStack(player.inventory.selectedSlot, playerItem)
                    stack = playerItem.copy()
                    stack.count = 1
                } else {
                    stack = playerItem
                    player.inventory.setStack(player.inventory.selectedSlot, ItemStack.EMPTY)
                }
                return ActionResult.SUCCESS
            }
        } else {
            //prefer the current slot if eligible
            val eligibleSlot: Int = if (playerItem.isEmpty || (playerItem.count < playerItem.item.maxCount && playerItem.item == stack.item)) {
                player.inventory.selectedSlot
            } else {
                val firstSlotWithStack = player.inventory.getSlotWithStack(stack)
                val firstEmptySlot = player.inventory.emptySlot
                if (firstSlotWithStack >= 0 && player.inventory.getStack(firstSlotWithStack).count < stack.maxCount) {
                    firstSlotWithStack
                } else if (firstEmptySlot >= 0) {
                    firstEmptySlot
                } else {
                    return ActionResult.FAIL
                }
            }
            val eligibleStack = player.inventory.getStack(eligibleSlot)
            if (eligibleStack.isEmpty) {
                player.inventory.setStack(eligibleSlot, stack)
            } else {
                eligibleStack.count += 1
                player.inventory.setStack(eligibleSlot, eligibleStack)
            }
            stack = ItemStack.EMPTY
        }

        if (playerItem == ItemStack.EMPTY && blockItem != ItemStack.EMPTY) {
            player.inventory.setStack(player.inventory.selectedSlot, blockItem)
            stack = ItemStack.EMPTY
            return ActionResult.SUCCESS
        } else if (playerItem != ItemStack.EMPTY && blockItem == ItemStack.EMPTY) {
            player.inventory.setStack(player.inventory.selectedSlot, ItemStack.EMPTY)
            stack = playerItem
            return ActionResult.CONSUME
        }
        return ActionResult.PASS
    }

    override fun toUpdatePacket(): BlockEntityUpdateS2CPacket? {
        Packets.sendBlockEntityUpdate(this, BlockEntityUpdatePacketID.ENCHANT_PEDESTAL)
        return null
    }

    override fun serverTick() {
        if ((!isValid) || event == null)
            return
        val e = event!!
        if (isMaster) {

            if (e.expToRemove > 0) {
                e.expToRemove -= e.expToRemoveEachTick
                e.player!!.addExperience(-e.expToRemoveEachTick)

                if (e.expToRemove < 10)
                    sendUpdatePacket()
                return
            }
            e.tickCounter = max(0, e.tickCounter-1)
            if (e.tickCounter == 1) {
                stack = e.recipe!!.output(stack)
                _event = null
                this.sendUpdatePacket()
                others.forEach {
                    if (e.recipe!!.consumesIngredients)
                        it.stack = ItemStack.EMPTY
                    it.sendUpdatePacket()
                }
                sendUpdatePacket()
            }

            if (e.tickCounter == floor(ticksToConsumeComponent / 2.0).toInt()) {
                sendUpdatePacket()
            }
        }
    }

    override fun clientTick() {
        if ((!isValid) || event == null)
            return

        val e = event!!

        if (stack == ItemStack.EMPTY)
            return

        val offset = sin(((world?.time ?: 0)).toDouble() / 8.0) / 4.0
        val randomX = Random.nextDouble(-0.25, 0.25)
        val randomY = Random.nextDouble(-0.2, 0.2)
        val randomZ = Random.nextDouble(-0.25, 0.25)

        if (e.expToRemove > 20) {
            world?.addParticle(ParticleTypes.ENCHANT, true, pos.x.toDouble()+0.5, pos.y.toDouble()+1.5, pos.z.toDouble()+0.5, 2*randomX, 1.5*randomY, 2*randomZ)
            return
        }

        if (isMaster) {
            world?.addParticle(ParticleTypes.PORTAL, true, pos.x.toDouble()+0.5, pos.y.toDouble()+1.0, pos.z.toDouble()+0.5, Random.nextDouble(-1.0, 1.0), Random.nextDouble(0.0, 1.0)+offset, Random.nextDouble(-1.0, 1.0))
            world?.addParticle(ParticleTypes.ENCHANT, true, pos.x.toDouble()+0.5, pos.y.toDouble()+1.5, pos.z.toDouble()+0.5, 3*randomX, 2*randomY, 3*randomZ)
        } else {
            val mp = master?.pos ?: pos
            val vector = BlockPosHelper.unitVector(mp, pos)
            val randomOffset = Random.nextDouble(0.0, 1.0)
            listOf(0.6, 1.0, 1.4).forEach { _ ->
                world?.addParticle(ParticleTypes.ENCHANT, true, pos.x.toDouble()+0.5-(randomOffset*vector.x), pos.y.toDouble()+1.5, pos.z.toDouble()+0.5-(randomOffset*vector.z), 3*randomX, 2*randomY, 3*randomZ)
                world?.addParticle(ParticleTypes.PORTAL, true, mp.x.toDouble()+0.5, mp.y.toDouble()+1.5+offset, mp.z.toDouble()+0.5, (vector.x*2)+randomX, randomY-1+offset, (vector.z*2)+randomZ)
            }
            if ((event?.tickCounter ?: 1000) < 30)
                world?.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true, pos.x.toDouble()+0.5, pos.y.toDouble()+1.5, pos.z.toDouble()+0.5, 0.001*randomX, 0.001*abs(randomY), 0.001*randomZ)
        }
    }

    override fun eventCancelled() {
        fun fireParticleAt(it: BlockEntity) {
            val randomX = Random.nextDouble(-0.02, 0.02)
            val randomY = Random.nextDouble(-0.05, 0.05)
            val randomZ = Random.nextDouble(-0.02, 0.02)
            world?.addParticle(ParticleTypes.FLAME, true, it.pos.x.toDouble()+0.5, it.pos.y.toDouble()+1.5, it.pos.z.toDouble()+0.5, 2*randomX, 1.5*randomY, 2*randomZ)
        }

        if (world?.isClient == true) {
            for (i in 0..10) {
                others.forEach {
                    fireParticleAt(it)
                }
                fireParticleAt(this)
            }
        } else {
            if (isMaster) {
                world?.let { ExperienceOrbEntity.spawn(it as ServerWorld, BlockPosHelper.toVec3d(pos), (event?.let { e -> e.totalExpToRemove - e.expToRemove } ?: 0)) }
            }
        }
    }

    override fun sendUpdatePacket() { toUpdatePacket() }

    companion object {
        const val ticksToConsumeComponent = 25

        private fun findCenterBlockOrNull(nearby: List<EnchantPedestalEntity>): EnchantPedestalEntity? {
            return nearby.firstOrNull {
                listOf(
                    BlockPosHelper.add(it.pos, -2, 0, 0),
                    BlockPosHelper.add(it.pos, 2, 0, 0),
                    BlockPosHelper.add(it.pos, 0, 0, -2),
                    BlockPosHelper.add(it.pos, 0, 0, 2)
                ).all { pos ->
                    nearby.any { near -> near.pos == pos }
                }
            }
        }

        val recipes = EnchantPedestalRecipes.allRecipes

        fun tick(world: World, pos: BlockPos, state: BlockState, be: EnchantPedestalEntity) {
            be.doTick(world)
        }
    }
}
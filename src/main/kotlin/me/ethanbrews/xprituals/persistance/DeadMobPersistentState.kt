package me.ethanbrews.xprituals.persistance

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.Registry
import net.minecraft.world.PersistentState
import net.minecraft.world.World

class DeadMobPersistentState(nbtCompound: NbtCompound) : PersistentState() {
    var nbt: NbtCompound = NbtCompound()

    override fun writeNbt(nbt: NbtCompound): NbtCompound {
        nbt.put("entity", this.nbt.getCompound("entity"))
        nbt.putString("type", this.nbt.getString("type"))
        return nbt
    }

    fun spawnEntity(world: World, pos: Vec3d) {
        respawn(nbt, world, pos)
    }

    companion object {
        fun readNbt(nbt: NbtCompound): DeadMobPersistentState = DeadMobPersistentState(nbt)

        fun fromEntity(entity: LivingEntity): DeadMobPersistentState {
            val entityData = entity.writeNbt(NbtCompound())
            val newNbt = NbtCompound()
            newNbt.put("entity", entityData)
            newNbt.putString("type", Registry.ENTITY_TYPE.getId(entity.type).toString())
            return DeadMobPersistentState(newNbt)
        }

        private fun getType(nbt: NbtCompound) = Registry.ENTITY_TYPE.get(Identifier(nbt.getString("type")))

        fun respawn(nbt: NbtCompound, world: World, pos: Vec3d) {
            if (nbt.contains("entity")) {
                val type = getType(nbt)
                val entity = type.create(world)
                entity?.let {
                    it.readNbt(nbt.getCompound("entity"))
                    it.setPosition(pos)
                }
            }
        }
    }
}
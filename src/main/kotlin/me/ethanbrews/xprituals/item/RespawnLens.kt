package me.ethanbrews.xprituals.item

import net.minecraft.client.MinecraftClient
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.MinecraftServer
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand

class RespawnLens : AmethystLens() {
    var associatedEntity: Entity? = null

    override fun hasGlint(stack: ItemStack?): Boolean = associatedEntity?.isAlive == false

    override fun useOnEntity(stack: ItemStack?, user: PlayerEntity?, entity: LivingEntity?, hand: Hand?): ActionResult {
        if (associatedEntity == null) {
            associatedEntity = entity
            return ActionResult.SUCCESS
        }
        return ActionResult.PASS
    }
}
package me.ethanbrews.xprituals.item

import me.ethanbrews.fabric.RegistryHelper.id
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import vazkii.patchouli.api.PatchouliAPI

class GuideBook() : Item(Settings()) {
    override fun use(world: World, user: PlayerEntity, hand: Hand?): TypedActionResult<ItemStack> {
        if (!world.isClient) {
            PatchouliAPI.get().openBookGUI(user as ServerPlayerEntity, id("xp-rituals"))
            return TypedActionResult.success(user.getStackInHand(hand))
        }
        return TypedActionResult.consume(user.getStackInHand(hand))
    }
}
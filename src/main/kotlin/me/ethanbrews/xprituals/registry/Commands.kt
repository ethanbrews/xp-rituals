package me.ethanbrews.xprituals.registry

import com.mojang.brigadier.CommandDispatcher
import me.ethanbrews.xprituals.command.EnchantPedestalsRecipes
import net.minecraft.server.command.ServerCommandSource

object Commands {
    fun registerCommands(dispatcher: CommandDispatcher<ServerCommandSource?>?, isDedicated: Boolean) {
        EnchantPedestalsRecipes.register(dispatcher, isDedicated)
    }
}
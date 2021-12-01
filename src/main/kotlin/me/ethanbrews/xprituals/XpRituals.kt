package me.ethanbrews.xprituals;

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import me.ethanbrews.xprituals.common.item.ModItems
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.LogManager
import org.dimdev.matrix.Matrix


object XpRituals {
	private val logger = LogManager.getLogger();

	public fun common() {
		logger.info("Loading XP Rituals");
		ModItems.registerItems()
		CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback {
			dispatcher: CommandDispatcher<ServerCommandSource?>?, dedicated: Boolean ->
			run {
				dispatcher!!.register(literal<ServerCommandSource?>("xpl").executes {
					logger.info("Hello");
					1
				});
			}
		})
	}

	public fun client() {
		logger.info("Loading XP Rituals (Client)");
	}

	public fun server() {
		logger.info("Loading XP Rituals (Server)");
	}
}

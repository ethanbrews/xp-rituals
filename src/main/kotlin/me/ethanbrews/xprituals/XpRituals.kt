package me.ethanbrews.xprituals;

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import me.ethanbrews.xprituals.client.blockrenderer.BlockRenderers
import me.ethanbrews.xprituals.common.block.ModBlocks
import me.ethanbrews.xprituals.common.item.ModItems
import me.ethanbrews.xprituals.common.network.Packets
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.LogManager


object XpRituals {
	private val logger = LogManager.getLogger();
	public const val modid = "xp_rituals"

	public fun common() {
		logger.info("Loading XP Rituals");
		ModItems.registerItems()
		ModBlocks.registerBlocks()
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
		BlockRenderers.initBlockRenderers()
		Packets.registerClient()
	}

	public fun server() {
		logger.info("Loading XP Rituals (Server)");
	}
}

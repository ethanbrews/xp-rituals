package me.ethanbrews.xprituals;

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import me.ethanbrews.xprituals.item.ModItems
import me.ethanbrews.xprituals.network.Packets
import me.ethanbrews.xprituals.registry.BlockRenderers
import me.ethanbrews.xprituals.registry.ModBlocks
import me.ethanbrews.xprituals.registry.ModEnchantments
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.command.ServerCommandSource
import org.apache.logging.log4j.LogManager


object XpRituals {
	private val logger = LogManager.getLogger();
	public const val modid = "xp_rituals"

	public fun common() {
		logger.info("Loading XP Rituals");
		ModItems.registerItems()
		ModBlocks.registerBlocks()
		ModEnchantments.registerEnchantments()
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

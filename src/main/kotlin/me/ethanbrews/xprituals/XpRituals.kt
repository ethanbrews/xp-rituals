package me.ethanbrews.xprituals;

import me.ethanbrews.xprituals.network.Packets
import me.ethanbrews.xprituals.registry.*
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback
import org.apache.logging.log4j.LogManager

@Suppress("unused")
object XpRituals {
	private val logger = LogManager.getLogger();
	const val modid = "xp_rituals"

	fun common() {
		logger.info("Loading XP Rituals")
		ModConfig.loadConfig()
		ModItems.registerItems()
		ModBlocks.registerBlocks()
		ModEnchantments.registerEnchantments()
		LootTableLoadingCallback.EVENT.register(LootTableLoadingCallback {
				resourceManager, lootManager, identifier, supplier, setter -> LootTables.registerLoot(resourceManager, lootManager, identifier, supplier, setter)
		})
		CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, isDedicated ->
			Commands.registerCommands(dispatcher, isDedicated)
		})
	}

	fun client() {
		logger.info("Loading XP Rituals (Client)");
		BlockRenderers.initBlockRenderers()
		Packets.registerClient()
	}

	fun server() {
		logger.info("Loading XP Rituals (Server)");
	}
}

package me.ethanbrews.xprituals.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.ethanbrews.xprituals.block.enchantPedestal.EnchantPedestalRecipe
import me.ethanbrews.xprituals.recipe.EnchantPedestalRecipes
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText

object EnchantPedestalsRecipes {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource?>?, isDedicated: Boolean) {
        dispatcher?.register(
            CommandManager.literal("ritualslist").then(
                RequiredArgumentBuilder.argument<ServerCommandSource?, String?>("enchantment", StringArgumentType.string())
                    .executes { listCommands(it) }
            ).executes { listRecipes(it, "") }
        )
    }

    private fun feedbackSender(ctx: CommandContext<ServerCommandSource>, translatable: Boolean): (String) -> Unit {
        return { s: String ->
            ctx.source.sendFeedback(if (translatable) TranslatableText(s) else LiteralText(s), false)
        }
    }

    private fun listRecipes(ctx: CommandContext<ServerCommandSource>, filter: String): Int {
        val matching = EnchantPedestalRecipes.allRecipes.filter { it.outputRecipeName?.contains(filter) == true }
        val fb = feedbackSender(ctx, false)
        fb("Found ${matching.size} recipes.")
        for(recipe in matching) {
            fb(" - ${recipe.outputRecipeName}")
        }
        return 0
    }

    private fun listCommands(ctx: CommandContext<ServerCommandSource>): Int {
        val filter = ctx.getArgument("enchantment", String::class.java)
        val fb = feedbackSender(ctx, false)

        val matching = EnchantPedestalRecipes.allRecipes.filter { it.outputRecipeName?.contains(filter) == true }

        if (matching.size > 3) {
            fb("Provide a narrower query to view the full recipe.")
            return listRecipes(ctx, filter)
        }

        for(recipe in matching) {
            fb(" - ${recipe.outputRecipeName ?: "Unknown"}")
            for(item in recipe.ingredients) {
                fb("   - ${item.name.string}")
            }
        }
        return 0
    }
}
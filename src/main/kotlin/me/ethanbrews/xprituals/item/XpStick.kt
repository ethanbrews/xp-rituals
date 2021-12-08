package me.ethanbrews.xprituals.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.item.ToolMaterials
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.world.World

class XpStick : SwordItem(
    ToolMaterials.WOOD,
    2,
    2.0F,
    Settings().maxCount(1)
) {
    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext?
    ) {
        super.appendTooltip(stack, world, tooltip, context)
        tooltip.add(TranslatableText("item.xp_rituals.xp_stick.tooltip1"))
        tooltip.add(TranslatableText("item.xp_rituals.xp_stick.tooltip2"))
    }
}



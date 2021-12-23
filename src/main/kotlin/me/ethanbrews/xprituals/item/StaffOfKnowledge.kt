package me.ethanbrews.xprituals.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.item.ToolMaterials
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.world.World

class StaffOfKnowledge : SwordItem(
    ToolMaterials.WOOD,
    2,
    2.0F,
    Defaults.defaultItemSettings
), IStaff {
    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext?
    ) {
        super.appendTooltip(stack, world, tooltip, context)
        tooltip.add(TranslatableText("item.xp_rituals.staff_of_knowledge.tooltip1"))
        tooltip.add(TranslatableText("item.xp_rituals.staff_of_knowledge.tooltip2"))
    }
}



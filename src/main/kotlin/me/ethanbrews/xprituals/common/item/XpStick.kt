package me.ethanbrews.xprituals.common.item

import net.minecraft.item.SwordItem
import net.minecraft.item.ToolMaterial
import net.minecraft.item.ToolMaterials

class XpStick : SwordItem(
    ToolMaterials.WOOD,
    2,
    2.0F,
    Settings().maxCount(1)
)



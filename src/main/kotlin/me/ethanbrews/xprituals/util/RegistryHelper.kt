package me.ethanbrews.xprituals.util

import me.ethanbrews.xprituals.XpRituals
import net.minecraft.util.Identifier

object RegistryHelper {
    fun id(name: String): Identifier = Identifier(XpRituals.modid, name)
}
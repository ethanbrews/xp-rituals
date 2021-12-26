package me.ethanbrews.fabric

import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import net.minecraft.text.TranslatableText

class TextBuilder {

    init {
        throw NotImplementedError()
    }

    var items = mutableListOf<Any>()

    fun literal(string: String) {
        items.add(LiteralText(string))
    }

    fun translation(string: String) {
        items.add(TranslatableText(string))
    }

    fun colour(string: String) {
        items.add(TextColor.parse(string)!!)
    }

    fun colour(rgb: Int) {
        items.add(TextColor.fromRgb(rgb))
    }

    fun build() {
        var text = LiteralText("")
        for (item in items) {
            if (item is LiteralText) {
                text.append(item)
            } else if (item is TranslatableText) {
            }
        }
    }
}
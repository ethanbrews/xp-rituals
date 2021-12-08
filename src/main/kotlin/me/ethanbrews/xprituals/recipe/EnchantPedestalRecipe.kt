package me.ethanbrews.xprituals.recipe

import me.ethanbrews.fabric.ListHelper
import net.minecraft.enchantment.Enchantment
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import java.lang.Integer.min

data class EnchantPedestalRecipe(
    val input: (ItemStack) -> Boolean,
    val output: (ItemStack) -> ItemStack,
    val ingredients: List<Item>,
    val xpAmount: Int,
    val consumesIngredients: Boolean = true
) {
    fun isValid(target: ItemStack, ingredients: List<Item>): Boolean =
        input(target) && ListHelper.equal(this.ingredients, ingredients)

    companion object {

        fun canGiveOrUpgradeEnchantment(enchantment: Enchantment, key: String, stack: ItemStack): Boolean {
            val index = stack.enchantments.indexOfFirst { (it as NbtCompound).getString("id") == key }
            val correctItem = enchantment.isAcceptableItem(stack)
            val isMaxEnchant = (index > 0 && (stack.enchantments[index] as NbtCompound).getInt("lvl") >= enchantment.maxLevel)
            return correctItem && !isMaxEnchant
        }

        fun getRecipe(enchantment: Enchantment, enchantmentKey: String, ingredients: List<Item>, xpAmount: Int, consumesIngredients: Boolean = false) = EnchantPedestalRecipe(
            { stack -> canGiveOrUpgradeEnchantment(enchantment, enchantmentKey, stack) },
            {stack ->
                stack.enchantments.indexOfFirst {
                    (it as? NbtCompound)?.getString("id") == enchantmentKey
                }.also {
                    if (it >= 0)
                        (stack.enchantments[it] as NbtCompound).putInt("lvl", min((stack.enchantments[it] as NbtCompound).getInt("lvl") + 1, enchantment.maxLevel))
                    else
                        stack.addEnchantment(enchantment, 1)
                }
                stack
            },
            ingredients,
            xpAmount,
            consumesIngredients
        )
    }
}
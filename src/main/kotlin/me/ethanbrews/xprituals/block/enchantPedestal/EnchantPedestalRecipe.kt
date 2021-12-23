package me.ethanbrews.xprituals.block.enchantPedestal

import me.ethanbrews.xprituals.registry.ModEnchantments
import me.ethanbrews.xprituals.registry.ModItems
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.registry.Registry
import java.lang.Integer.min

data class EnchantPedestalRecipe(
    val input: (ItemStack, ItemStack) -> Boolean,
    val output: (ItemStack) -> ItemStack,
    val ingredients: List<Item>,
    val xpAmount: Int,
    val consumesIngredients: Boolean = true
) {
    companion object {

        fun canGiveOrUpgradeEnchantment(enchantment: Enchantment, stack: ItemStack): Boolean {
            val index = stack.enchantments.indexOfFirst { (it as NbtCompound).getString("id") == Registry.ENCHANTMENT.getId(enchantment).toString() }
            val correctItem = enchantment.isAcceptableItem(stack)
            val isMaxEnchant = (index >= 0 && (stack.enchantments[index] as NbtCompound).getInt("lvl") >= enchantment.maxLevel)
            return correctItem && !isMaxEnchant
        }

        fun getRecipe(enchantment: Enchantment, ingredients: List<Item>, xpAmount: Int, consumesIngredients: Boolean = true) = EnchantPedestalRecipe(
            { stack, _ -> canGiveOrUpgradeEnchantment(enchantment, stack) },
            {stack ->
                stack.enchantments.indexOfFirst {
                    (it as? NbtCompound)?.getString("id") == Registry.ENCHANTMENT.getId(enchantment).toString()
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
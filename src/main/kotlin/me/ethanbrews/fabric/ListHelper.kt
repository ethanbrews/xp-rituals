package me.ethanbrews.fabric

object ListHelper {
    fun <E> equal(l1: List<E>, l2: List<E>) = l1.containsAll(l2) && l2.containsAll(l1)
}
package me.ethanbrews.fabric

import com.google.common.math.IntMath.pow
import kotlin.math.ceil
import kotlin.math.pow

object ExperienceHelper {
    fun experienceToNextLevel(currentLevel: Int): Int {
        return if (currentLevel < 16)
            2 * currentLevel + 7
        else if (currentLevel < 31)
            5 * currentLevel - 38
        else
            9 * currentLevel - 158
    }

    /**
     * Calculate how much experience to take from [currentLevel] so that [numberToTake] levels are taken.
     */
    fun experienceToTake(currentLevel: Int, numberToTake: Int): Int {
        return levelToExperience(currentLevel) - levelToExperience(currentLevel-numberToTake)
    }

    fun levelToExperience(level: Int): Int {
        return if (level in 1..16) {
            (level.toDouble().pow(2.0) + 6 * level).toInt()
        } else if (level in 17..31) {
            (2.5 * level.toDouble().pow(2.0) - 40.5 * level + 360).toInt()
        } else if (level >= 32) {
            (4.5 * level.toDouble().pow(2.0) - 162.5 * level + 2220).toInt()
        } else {
            0
        }
    }
}
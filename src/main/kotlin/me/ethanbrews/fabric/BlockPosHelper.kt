package me.ethanbrews.fabric

import net.minecraft.client.util.math.Vector3d
import net.minecraft.util.math.BlockPos
import kotlin.math.pow
import kotlin.math.sqrt

object BlockPosHelper {
    // Returns the magnitude of the vector joining p1 and p2
    fun magnitude(p1: BlockPos, p2: BlockPos) =
        sqrt(((p1.x*p2.x).toDouble()).pow(2) + ((p1.y*p2.y).toDouble()).pow(2) + ((p1.z*p2.z).toDouble()).pow(2))

    fun magnitude(p: BlockPos) = sqrt(p.x.toDouble().pow(2) + p.y.toDouble().pow(2) + p.z.toDouble().pow(2))

    fun magnitude(p: Vector3d) = sqrt(p.x.toDouble().pow(2) + p.y.toDouble().pow(2) + p.z.toDouble().pow(2))

    // Returns a vector, [BlockPos], that joins p1 to p2.
    fun join(p1: BlockPos, p2: BlockPos) = Vector3d((p2.x-p1.x).toDouble(), (p2.y-p1.y).toDouble(), (p2.z-p1.z).toDouble())

    fun unitVector(p1: BlockPos, p2: BlockPos): Vector3d {
        val j = join(p1, p2)
        val m = magnitude(j)
        return Vector3d(j.x/m, j.y/m, j.z/m)
    }
}
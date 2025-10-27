package dev.losterixx.sapi.utils.numutils

import kotlin.random.Random

object RandomNums {

    fun getRandomInt(min: Int, max: Int): Int {
        require(min < max) { "Min must be less than max" }
        return Random.nextInt(min, max + 1)
    }

    fun getRandomLong(min: Long, max: Long): Long {
        require(min < max) { "Min must be less than max" }
        return Random.nextLong(min, max + 1)
    }

    fun getRandomFloat(min: Float, max: Float): Float {
        require(min < max) { "Min must be less than max" }
        return Random.nextFloat() * (max - min) + min
    }

    fun getRandomDouble(min: Double, max: Double): Double {
        require(min < max) { "Min must be less than max" }
        return Random.nextDouble(min, max)
    }

    fun getRandomByte(min: Byte, max: Byte): Byte {
        require(min < max) { "Min must be less than max" }
        return getRandomInt(min.toInt(), max.toInt()).toByte()
    }

    fun getRandomShort(min: Short, max: Short): Short {
        require(min < max) { "Min must be less than max" }
        return getRandomInt(min.toInt(), max.toInt()).toShort()
    }

}

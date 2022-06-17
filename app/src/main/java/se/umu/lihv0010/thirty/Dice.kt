package se.umu.lihv0010.thirty

import java.util.*

class Dice(
        var rnd: Random,
        var value: Int = rnd.nextInt(5) + 1
        ) {

    fun roll() {
        value = rnd.nextInt(5) + 1
    }
}
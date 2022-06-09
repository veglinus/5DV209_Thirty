package se.umu.lihv0010.thirty

class Dice {
    var value: Number = (1..6).random()

    fun roll() {
        value = (1..6).random()
    }
}
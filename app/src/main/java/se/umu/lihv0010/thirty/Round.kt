package se.umu.lihv0010.thirty

import android.widget.Toast

// TODO: Separate into round file

class Round {
    private var diceViews = intArrayOf(R.id.dice1, R.id.dice2, R.id.dice3, R.id.dice4, R.id.dice5, R.id.dice6)
    var dices = Array(6) { Dice() }
    var selected: MutableList<Int> = mutableListOf()
    var rolls = 0

    fun roll() {
        if (rolls < 2) {
            if (selected.isEmpty()) {
                rollAll()
            } else {
                rollSelected()
            }
            afterRoll()

        } else {
            clearSelected()
            //Toast.makeText(this, "Already rolled 2 times.", Toast.LENGTH_SHORT).show() // TODO: Implement toast
            println("Already rolled 2 times.")
        }
    }

    private fun rollAll() {
        for (dice in dices) {
            dice.roll()
        }
    }

    private fun rollSelected() {
        for (id in selected) {
            dices[id].roll()
        }
    }

    private fun afterRoll() {
        clearSelected()
        rolls++
        println("Rolls are now: $rolls")
    }

    private fun clearSelected() {
        selected = mutableListOf() // Clears selected list
    }
}
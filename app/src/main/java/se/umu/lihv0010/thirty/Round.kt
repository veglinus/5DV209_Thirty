package se.umu.lihv0010.thirty

class Round {
    var dices = Array(6) { Dice() } // 6 dice objects, rollable
    var selected: MutableList<Int> = mutableListOf() // Index list of selected dice
    var rolls = 0 // How many rolls have been made, max of 2 after initial allowed

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
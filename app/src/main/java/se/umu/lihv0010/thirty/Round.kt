package se.umu.lihv0010.thirty

// TODO: Separate into round file

class Round {
    private var diceViews = intArrayOf(R.id.dice1, R.id.dice2, R.id.dice3, R.id.dice4, R.id.dice5, R.id.dice6)
    var dices = Array(6) { Dice() }
    var selected: MutableList<Int> = mutableListOf()

    fun roll() {
        if (selected.isEmpty()) {
            rollAll()
        } else {
            rollAllButSelected()
        }
    }

    private fun rollAll() {
        for (dice in dices) {
            dice.roll()
        }
        // TODO: Refresh view
    }

    fun clearSelected() {
        selected = mutableListOf()
    }

    private fun rollAllButSelected() {
        // TODO
    }
}
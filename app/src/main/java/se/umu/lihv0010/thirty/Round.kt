package se.umu.lihv0010.thirty

import android.util.Log
import androidx.lifecycle.SavedStateHandle

/**
class Round(private val handle: SavedStateHandle, var dices: Array<Dice>, var selected: MutableList<Int>, var rolls: Int) {
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

        } else { // This is never reached due to disabling button in the UI
            clearSelected()
            println("Already rolled 2 times.")
        }
    }

    fun selectedDiceValues(): MutableList<Int> {
        val values: MutableList<Int> = mutableListOf()
        for (id in selected) {
            values.add(dices[id].value)
        }
        return values
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

        handle.set("dices", dices)
        handle.set("selected", selected)
        handle.set("rolls", rolls)
        //println("Rolls are now: $rolls")
    }

    private fun clearSelected() {
        this.selected = mutableListOf() // Clears selected list
        //println("Selected dice are $selected")
    }
}
        */
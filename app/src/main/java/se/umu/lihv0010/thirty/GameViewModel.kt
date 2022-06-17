package se.umu.lihv0010.thirty

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import java.lang.Exception

private const val TAG = "MainViewModel"

class GameViewModel(private val handle: SavedStateHandle) : ViewModel() {
    var currentRound = Round(handle)

    val maxRounds = 10

    var score: Int = 0
    var roundsPlayed: Int = 0
    var results: MutableList<Int> = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    var selectors: MutableList<Int> = mutableListOf(3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

    init {
        Log.d(TAG, "ViewModel instance created")

        if (handle.contains("score")) score = handle.get<Int>("score") ?: 0
        if (handle.contains("roundsPlayed")) roundsPlayed = handle.get<Int>("roundsPlayed") ?: 0
        if (handle.contains("results")) results = handle.get<ArrayList<Int>>("results")?.toMutableList() ?:
                mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        if (handle.contains("selectors")) selectors = handle.get<ArrayList<Int>>("selectors")?.toMutableList() ?:
                    mutableListOf(3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    }

    override fun onCleared() {
        handle.set("dices", currentRound.dices)
        super.onCleared()

        Log.d(TAG, "ViewModel instance about to be destroyed")
    }

    fun newRound() {
        // TODO: Refactor this into Round class
        currentRound.dices = arrayListOf(Dice(), Dice(), Dice(), Dice(), Dice(), Dice()) // 6 dice objects, rollable
        currentRound.selected = mutableListOf() // Index list of selected dice
        currentRound.rolls = 0 // How many rolls have been made, max of 2 after initial allowed

        handle.set("dices", currentRound.getDiceIntArray())
        handle.set("selected", currentRound.selected)
        handle.set("rolls", currentRound.rolls)
    }

    fun validateSubmit(selected: Int): Boolean { // When submitting answer, validate it's correct
        try {
            val diceNumbers: MutableList<Int> = currentRound.selectedDiceValues()
            //println("Spinner is: $selected and dice values are: $diceNumbers")

            var sum = 0
            for (number in diceNumbers) {
                sum += number
            }

            return if (selected == 3) {
                lowCalculation(diceNumbers, selected, sum)
            } else {
                otherCalculation(sum, selected)
            }

        } catch (e: Exception) {
            throw e
        }
    }

    private fun lowCalculation(diceNumbers: MutableList<Int>, selected: Int, sum: Int): Boolean {
        var lowCheck = true
        for (number in diceNumbers) {
            if (number > 3) {
                lowCheck = false
            }
        }
        return if (lowCheck) {
            finishRound(selected, sum)
            true
        } else {
            println("One or more dice has a value higher than 3.")
            false
        }
    }

    private fun otherCalculation(sum: Int, selected: Int): Boolean {
        val isDivisible: Boolean = (sum % selected == 0)

        return if (isDivisible) {
            finishRound(selected, sum)
            true
        } else {
            println("That combination is not possible.")
            false
        }
    }

    private fun finishRound(selected: Int, points: Int) {
        println("Round finished! Score added: $points")
        selectors.remove(selected)
        score += points
        results[selected - 3] = points
        println(results)
        roundsPlayed++

        handle.set("score", score)
        handle.set("roundsPlayed", roundsPlayed)
        handle.set("results", results)
        handle.set("selectors", selectors)

        println("$roundsPlayed out of $maxRounds")
    }
}
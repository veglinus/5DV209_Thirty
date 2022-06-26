package se.umu.lihv0010.thirty

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import java.util.*
import java.lang.Exception

private const val TAG = "GameViewModel"

class GameViewModel(private val handle: SavedStateHandle) : ViewModel() {
    private var rnd: Random = Random(System.currentTimeMillis())
    var currentRound = Round(handle, rnd)

    val maxRounds = 10
    var score: Int = 0 // Total score (points)
    var roundsPlayed: Int = 0
    var results: MutableList<Int> = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0) // Position in array corresponds to what selector was chosen, low to 12
    var selectors: MutableList<Int> = mutableListOf(3, 4, 5, 6, 7, 8, 9, 10, 11, 12) // Different choices, 3 being "low"

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
        Log.d(TAG, "ViewModel onCleared")
    }

    fun validateSubmit(selected: Int): Boolean { // When submitting answer, validate it's correct
        try {
            val diceNumbers: MutableList<Int> = currentRound.selectedDiceValues()
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
        var tempArr = currentRound.selectedDiceValues().toMutableList() // To duplicate

        for (dice in currentRound.selectedDiceValues()) {
            println(tempArr)
            if (dice == selected) {
                tempArr.remove(dice)
            } else {
                val leftover: Int = selected - dice
                //println("Leftover exists: $leftover")
                if (tempArr.contains(leftover)) {
                    //println("Found a leftover, removing both")
                    tempArr.remove(dice)
                    tempArr.remove(leftover)
                } else {
                    println("That combination is not possible.")
                }
            }
        }

        return if (isDivisible && tempArr.isEmpty()) {
            finishRound(selected, sum)
            true
        } else {
            println("That combination is not possible.")
            false
        }
    }

    private fun finishRound(selected: Int, points: Int) {
        //println("Round finished! Score added: $points")
        selectors.remove(selected)
        score += points
        results[selected - 3] = points
        roundsPlayed++
        setRoundHandles()

        //println("$roundsPlayed out of $maxRounds")
    }

    fun newRound() { // Only called if a new round is actually needed
        // TODO: Refactor this into Round class
        currentRound.dices = arrayListOf(Dice(rnd), Dice(rnd), Dice(rnd), Dice(rnd), Dice(rnd), Dice(rnd)) // 6 dice objects, rollable
        currentRound.selected = mutableListOf() // Index list of selected dice
        currentRound.rolls = 0 // How many rolls have been made, max of 2 after initial allowed

        handle.set("dices", currentRound.getDiceIntArray())
        handle.set("selected", currentRound.selected)
        handle.set("rolls", currentRound.rolls)
    }

    fun initNewGame() {
        score = 0
        roundsPlayed = 0
        results = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        selectors = mutableListOf(3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        setRoundHandles()

        newRound()
        currentRound.roll()
    }

    private fun setRoundHandles() {
        handle.set("score", score)
        handle.set("roundsPlayed", roundsPlayed)
        handle.set("results", results)
        handle.set("selectors", selectors)
    }
}
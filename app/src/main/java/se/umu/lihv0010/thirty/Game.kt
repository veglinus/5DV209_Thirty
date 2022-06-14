package se.umu.lihv0010.thirty

import android.content.Context
import android.widget.Toast
import java.lang.Exception

class Game {
    lateinit var mainContext: Context
    var currentRound = Round()
    var score: Int = 0
    var results: MutableList<Int> = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    var roundsPlayed: Int = 0
    var selectors: MutableList<Int> = mutableListOf(3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

    val maxRounds = 10

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
            invalidCombo("One or more dice has a value higher than 3.")
            false
        }
    }

    private fun otherCalculation(sum: Int, selected: Int): Boolean {
        val isDivisible: Boolean = (sum % selected == 0)

        return if (isDivisible) {
            finishRound(selected, sum)
            true
        } else {
            invalidCombo("That combination is not possible.")
            false
        }
    }

    private fun invalidCombo(error: String) {
        Toast.makeText(mainContext, "Invalid combination.", Toast.LENGTH_SHORT).show()
        println(error)
    }

    private fun finishRound(selected: Int, points: Int) {
        println("Round finished! Score added: $points")
        selectors.remove(selected)
        score += points
        results[selected - 3] = points
        println(results)
        roundsPlayed++

        println("$roundsPlayed out of $maxRounds")
    }
}
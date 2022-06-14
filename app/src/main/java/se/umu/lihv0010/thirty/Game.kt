package se.umu.lihv0010.thirty

import java.lang.Exception

class Game {
    var currentRound = Round()
    var score: Int = 0
    var roundsPlayed: Int = 0
    var selectors: MutableList<Int> = mutableListOf(3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

    val maxRounds = 10

    fun validateSubmit(selected: Int): Boolean {
        try {
            var diceNumbers: MutableList<Number> = currentRound.selectedDiceValues()
            //println("Spinner is: $selected and dice values are: $diceNumbers")

            var sum: Int = 0
            for (number in diceNumbers) {
                sum += number.toInt()
            }

            if (selected == 3) {
                return lowCalculation(diceNumbers, selected, sum)
            } else {
                return otherCalculation(sum, selected)
            }

        } catch (e: Exception) {
            throw e
        }
    }

    private fun lowCalculation(diceNumbers: MutableList<Number>, selected: Any, sum: Int): Boolean {
        var lowCheck = true
        for (number in diceNumbers) {
            if (number.toInt() > 3) {
                lowCheck = false
            }
        }
        if (lowCheck) {
            finishRound(selected, sum)
            return true
        } else {
            invalidCombo("One or more dice has a value higher than 3.")
            return false
        }
    }

    private fun otherCalculation(sum: Int, selected: Int): Boolean {
        val isDivisible: Boolean = (sum % selected == 0)

        if (isDivisible) {
            finishRound(selected, sum)
            return true
        } else {
            invalidCombo("That combination is not possible.")
            return false
        }
    }

    private fun invalidCombo(error: String) {
        // TODO: Implement
        println(error)
    }

    private fun finishRound(selected: Any, points: Int) {
        println("Round finished! Score added: $points")
        selectors.remove(selected) // TODO: this could bring a problem
        score += points
        roundsPlayed++

        if (roundsPlayed >= maxRounds) {
            endGame()
        } else {
            currentRound = Round()
        }
    }

    private fun endGame() {
        // TODO: Implement
    }
}
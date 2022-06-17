package se.umu.lihv0010.thirty

import android.content.Context
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import java.lang.Exception

private const val TAG = "MainViewModel"

class MainViewModel(private val handle: SavedStateHandle) : ViewModel() {
    lateinit var mainContext: Context
    lateinit var dices: ArrayList<Dice>

    var selected: MutableList<Int> = mutableListOf() // Index list of selected dice
    var rolls = 0 // How many rolls have been made, max of 2 after initial allowed
    var currentRound = this

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

        if (handle.contains("selected")) selected = handle.get<ArrayList<Int>>("selected")?.toMutableList() ?:
                mutableListOf()
        if (handle.contains("rolls")) rolls = handle.get<Int>("rolls") ?: 0


        if (handle.contains("dices")) {
            val savedDice = handle.get<ArrayList<Int>>("dices")

            if (savedDice != null) {

                Log.d(TAG, "SavedDice are: $savedDice")


                var newDices: ArrayList<Dice> = arrayListOf()

                for (dice in savedDice) {
                    newDices.add(Dice(dice))
                }

                dices = newDices

            }
            Log.d(TAG, "Recovered dice!")
        } else {
            dices = arrayListOf(Dice(), Dice(), Dice(), Dice(), Dice(), Dice())
            Log.d(TAG, "Saved new dice")
            handle.set("dices", getDiceIntArray())
        }

    }

    override fun onCleared() {
        handle.set("dices", currentRound.dices)
        super.onCleared()

        Log.d(TAG, "ViewModel instance about to be destroyed")
    }

    fun newRound() {
        dices = arrayListOf(Dice(), Dice(), Dice(), Dice(), Dice(), Dice()) // 6 dice objects, rollable
        selected = mutableListOf() // Index list of selected dice
        rolls = 0 // How many rolls have been made, max of 2 after initial allowed

        handle.set("dices", getDiceIntArray())
        handle.set("selected", selected)
        handle.set("rolls", rolls)
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

        handle.set("score", score)
        handle.set("roundsPlayed", roundsPlayed)
        handle.set("results", results)
        handle.set("selectors", selectors)

        println("$roundsPlayed out of $maxRounds")
    }




    // ROUND DATA

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

    fun saveSelected() {
        handle.set("selected", selected)
    }

    fun selectedDiceValues(): MutableList<Int> {
        val values: MutableList<Int> = mutableListOf()
        for (id in selected) {
            values.add(dices[id].value!!)
        }
        return values
    }

    private fun getDiceIntArray(): ArrayList<Int> {
        var arr = mutableListOf<Int>()

        for (dice in dices) {
            arr.add(dice.value!!)
        }

        return arr as ArrayList<Int>
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

        handle.set("dices", getDiceIntArray())
        handle.set("selected", selected)
        handle.set("rolls", rolls)
        //println("Rolls are now: $rolls")
    }

    private fun clearSelected() {
        this.selected = mutableListOf() // Clears selected list
        //println("Selected dice are $selected")
    }






}
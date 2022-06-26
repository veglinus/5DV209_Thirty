package se.umu.lihv0010.thirty

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "Round"

class Round(private val handle: SavedStateHandle,
            var random: Random,
            var dices: ArrayList<Dice> = arrayListOf(Dice(random), Dice(random), Dice(random), Dice(random), Dice(random), Dice(random)),
            var selected: MutableList<Int> = mutableListOf(),
            var rolls: Int = 0) {

    init {
        if (handle.contains("selected")) selected = handle.get<ArrayList<Int>>("selected")?.toMutableList() ?:
                mutableListOf()
        if (handle.contains("rolls")) rolls = handle.get<Int>("rolls") ?: 0
        if (handle.contains("dices")) {
            val savedDice = handle.get<ArrayList<Int>>("dices")
            if (savedDice != null) {
                Log.d(TAG, "Recovering saved dice: $savedDice")
                val newDices: ArrayList<Dice> = arrayListOf()
                for (dice in savedDice) {
                    newDices.add(Dice(random, dice))
                }
                dices = newDices
            }
        } else {
            dices = arrayListOf(Dice(random), Dice(random), Dice(random), Dice(random), Dice(random), Dice(random))
            Log.d(TAG, "Saved new dice")
            handle.set("dices", getDiceIntArray())
        }
    }

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
            values.add(dices[id].getDiceValue())
        }
        return values
    }

    fun getDiceIntArray(): ArrayList<Int> {
        val arr = mutableListOf<Int>()

        for (dice in dices) {
            arr.add(dice.getDiceValue())
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
    }

    private fun clearSelected() {
        this.selected = mutableListOf() // Clears selected list
    }
}
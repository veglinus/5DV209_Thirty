package se.umu.lihv0010.thirty

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var reroll: Button

    private var dices = Array(6) { Dice() }
    private var diceViews = intArrayOf(R.id.dice1, R.id.dice2, R.id.dice3, R.id.dice4, R.id.dice5, R.id.dice6)
    private var selected: MutableList<Int> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        reroll = findViewById(R.id.reroll) // Reroll button
        reroll.setOnClickListener {
            rollAll()
        }

        setupOnClickDiceLogic()
        refreshDiceView()
    }

    private fun rollAll() {
        for (dice in dices) {
            dice.roll()
        }
        refreshDiceView()
    }

    private fun refreshDiceView() { // Draws dice values to view
        for ((index, id) in diceViews.withIndex()) {
            var currentView: TextView = findViewById(id)
            currentView.setText(dices[index].value.toString())
        }
    }

    private fun setupOnClickDiceLogic() {
        for ((index, id) in diceViews.withIndex()) { // Sets eventlisteners for all dice
            val currView: TextView = findViewById(id)
            currView.setOnClickListener {
                //println("You pressed dice: $index")

                if (!selected.contains(index)) { // Adds to selected list if not already in there
                    selected.add(index)
                } else { // If already in list, remove from selected list
                    selected.remove(index)
                }

                println("Selected dice are: $selected")
            }
        }
    }

    private fun clearSelected() {
        selected = mutableListOf()
    }
}
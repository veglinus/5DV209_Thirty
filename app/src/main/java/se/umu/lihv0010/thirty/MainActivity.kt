package se.umu.lihv0010.thirty

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private var diceViews = intArrayOf(R.id.dice1, R.id.dice2, R.id.dice3, R.id.dice4, R.id.dice5, R.id.dice6)
    private var game = Game() // Initialize new game
    private var selected = game.currentRound.selected

    // TODO: Display rolls left, also like "no rolls left, choose dice to submit"
    // TODO: Submit button beside spinner
    // TODO: Dice image logic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupListeners()
        refreshDiceView()
    }

    private fun refreshDiceView() { // Draws dice values to view
        var dices = game.currentRound.dices
        for ((index, id) in diceViews.withIndex()) {
            var currentView: TextView = findViewById(id)
            currentView.text = dices[index].value.toString()
        }
    }

    private fun setupListeners() {
        setupOnClickDiceLogic()
        setupReroll()
        setupSpinner()
    }

    private fun setupOnClickDiceLogic() {
        for ((index, id) in diceViews.withIndex()) { // Sets eventlisteners for all dice
            val currView: TextView = findViewById(id)
            currView.setOnClickListener {
                //println("You pressed dice: $index")
                if (!selected.contains(index)) { // Adds to selected list if not already in there
                    selected.add(index)
                    drawSelected(currView)
                } else { // If already in list, remove from selected list
                    selected.remove(index)
                    unDrawSelected(currView)
                }
                println("Selected dice are: $selected")
                checkRollButton()
            }
        }
    }

    private fun setupReroll() {
        var reroll: Button = findViewById(R.id.reroll) // Reroll button
        reroll.setOnClickListener {
            game.currentRound.roll()
            refreshDiceView()
        }
    }

    private fun setupSpinner() {
        val spinner: Spinner = findViewById(R.id.spinner1)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, game.selectors)
        spinner.adapter = spinnerAdapter
        // TODO: Does this refresh after a submit has occurred?
    }

    private fun drawSelected(view: TextView) {
        view.setBackgroundResource(R.drawable.border_selected)
    }
    private fun unDrawSelected(view: TextView) {
        view.setBackgroundResource(R.drawable.border)
    }

    private fun checkRollButton() {
        var reroll: Button = findViewById(R.id.reroll) // Reroll button
        if (selected.isEmpty()) {
            reroll.text = "Reroll all"
        } else {
            reroll.text = "Roll selected"
        }
    }
}
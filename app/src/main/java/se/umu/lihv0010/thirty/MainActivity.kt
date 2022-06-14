package se.umu.lihv0010.thirty

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private val diceViews = intArrayOf(R.id.dice1, R.id.dice2, R.id.dice3, R.id.dice4, R.id.dice5, R.id.dice6)
    private var game = Game() // Initialize new game

    // TODO: Dice image logic
    // TODO: Handle state change

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupListeners()
        refreshDiceView(true)
    }

    private fun refreshDiceView(unDraw: Boolean) { // Draws dice values to view
        val dices = game.currentRound.dices
        for ((index, id) in diceViews.withIndex()) {
            val currentView: TextView = findViewById(id)
            currentView.text = dices[index].value.toString()

            if (unDraw) {
                unDrawSelected(currentView)
            }
            checkRollButton()
        }
    }

    private fun setupListeners() {
        setupOnClickDiceLogic()
        setupReroll()
        setupSpinner()
        setupSubmit()
    }

    private fun setupOnClickDiceLogic() {
        for ((index, id) in diceViews.withIndex()) { // Sets eventlisteners for all dice
            val currView: TextView = findViewById(id)
            currView.setOnClickListener {
                //println("You pressed dice: $index")
                if (!game.currentRound.selected.contains(index)) { // Adds to selected list if not already in there
                    game.currentRound.selected.add(index)
                    drawSelected(currView)
                } else { // If already in list, remove from selected list
                    game.currentRound.selected.remove(index)
                    unDrawSelected(currView)
                }
                println("Selected dice are: ${game.currentRound.selected}")
                checkRollButton()
            }
        }
    }

    private fun setupReroll() {
        val reroll: Button = findViewById(R.id.reroll) // Reroll button
        reroll.setOnClickListener {
            game.currentRound.roll()
            refreshDiceView(true)
        }
    }

    private fun setupSubmit() {
        val submit: Button = findViewById(R.id.submit)
        var spinner: Spinner = findViewById(R.id.spinner)

        submit.setOnClickListener {
            var selectedScoring = spinner.selectedItem.toString().toInt()
            var completion: Boolean = game.validateSubmit(selectedScoring)

            refreshDiceView(completion)

            if (completion) {
                setupSpinner()
            }
            // TODO: Submit button logic
            // TODO: Update view
        }
    }

    private fun setupSpinner() {
        val spinner: Spinner = findViewById(R.id.spinner)
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
        val reroll: Button = findViewById(R.id.reroll) // Reroll button
        reroll.isEnabled = game.currentRound.rolls < 2

        if (game.currentRound.selected.isEmpty()) {
            reroll.text = resources.getString(R.string.reroll_all)
        } else {
            reroll.text = resources.getString(R.string.reroll_selected)
        }
    }
}
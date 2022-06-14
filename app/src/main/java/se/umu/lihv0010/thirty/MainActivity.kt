package se.umu.lihv0010.thirty

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private val diceViews = intArrayOf(R.id.dice1, R.id.dice2, R.id.dice3, R.id.dice4, R.id.dice5, R.id.dice6)
    private val whiteDices = intArrayOf(R.drawable.white1, R.drawable.white2, R.drawable.white3, R.drawable.white4, R.drawable.white5, R.drawable.white6)
    private val redDices = intArrayOf(R.drawable.red1, R.drawable.red2, R.drawable.red3, R.drawable.red4, R.drawable.red5, R.drawable.red6)

    private var game = Game() // Initialize new game

    // TODO: Handle state change
    // TODO: Show score and rounds played

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupListeners()
        refreshDiceView(true)
        game.mainContext = this@MainActivity
    }

    private fun refreshDiceView(unDraw: Boolean) { // Draws dice values to view
        val dices = game.currentRound.dices
        for ((index, id) in diceViews.withIndex()) {
            val currentView: ImageButton = findViewById(id)

            var id = dices[index].value - 1 // For array offset
            currentView.setImageResource(whiteDices[id])

            if (unDraw) {
                unDrawSelected(currentView, dices[index].value)
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
            val currView: ImageButton = findViewById(id)
            currView.setOnClickListener {
                //println("You pressed dice: $index")
                if (!game.currentRound.selected.contains(index)) { // Adds to selected list if not already in there, if so removes it
                    game.currentRound.selected.add(index)
                    drawSelected(currView, game.currentRound.dices[index].value)
                } else { // If already in list, remove from selected list
                    game.currentRound.selected.remove(index)
                    unDrawSelected(currView, game.currentRound.dices[index].value)
                }
                println("Selected dice are: ${game.currentRound.selected}")
                checkRollButton()
            }
        }
    }

    private fun setupReroll() {
        val reroll: Button = findViewById(R.id.reroll) // Reroll button
        reroll.setOnClickListener {
            if (game.currentRound.rolls > 2) {
                Toast.makeText(this, "Already rolled 2 times.", Toast.LENGTH_SHORT).show()
            }
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
        }
    }

    private fun setupSpinner() {
        val spinner: Spinner = findViewById(R.id.spinner)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, game.selectors)
        spinner.adapter = spinnerAdapter
    }

    private fun drawSelected(view: ImageButton, value: Int) {
        var id = value - 1
        view.setImageResource(redDices[id])
    }
    private fun unDrawSelected(view: ImageButton, value: Int) {
        var id = value - 1
        view.setImageResource(whiteDices[id])
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
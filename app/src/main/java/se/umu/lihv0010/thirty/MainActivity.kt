package se.umu.lihv0010.thirty

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import se.umu.lihv0010.thirty.databinding.ActivityMainBinding
import kotlin.system.exitProcess

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private val diceViews = intArrayOf(R.id.dice1, R.id.dice2, R.id.dice3, R.id.dice4, R.id.dice5, R.id.dice6)
    private val whiteDices = intArrayOf(R.drawable.white1, R.drawable.white2, R.drawable.white3, R.drawable.white4, R.drawable.white5, R.drawable.white6)
    private val redDices = intArrayOf(R.drawable.red1, R.drawable.red2, R.drawable.red3, R.drawable.red4, R.drawable.red5, R.drawable.red6)
    private lateinit var binding: ActivityMainBinding
    private lateinit var game: GameViewModel

    // TODO: Landscape layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        game = ViewModelProvider(this)[GameViewModel::class.java]
        setupListeners()
    }

    private fun setupListeners() { // Sets up all the onclicklisteners and logic needed
        Log.d(TAG, "Setting up ALL listeners!")
        setupOnClickDiceLogic()
        setupReroll()
        setupSpinner()
        setupSubmit()
        updateScoreAndRounds()
        refreshDiceView(false)
    }

    private fun refreshDiceView(unDrawSelected: Boolean) { // Draws dice values to view
        //Log.d(TAG, "Refreshing diceview")
        val dices = game.currentRound.dices
        for ((index, id) in diceViews.withIndex()) {
            val currentView: ImageButton = findViewById(id)

            val idOffset = dices[index].value - 1 // For array offset
            currentView.setImageResource(whiteDices[idOffset])
            if (unDrawSelected) {
                unDrawSelected(currentView, dices[index].value)
            }
            checkRollButton()
        }
    }

    private fun setupOnClickDiceLogic() { // Sets up logic for selection of dice
        for ((index, id) in diceViews.withIndex()) { // Sets listeners for all dice
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
                game.currentRound.saveSelected() // Saves selected dice in savedstate

                println("Selected dice are: ${game.currentRound.selected}")
                checkRollButton()
            }
        }
    }

    private fun setupReroll() { // Setups reroll button logic
        binding.reroll.setOnClickListener {
            if (game.currentRound.rolls > 2) {
                Toast.makeText(this, "Already rolled 2 times.", Toast.LENGTH_SHORT).show()
            }
            game.currentRound.roll()
            refreshDiceView(true)
        }
    }

    private fun checkRollButton() { // Changes text of roll button dynamically
        binding.reroll.isEnabled = game.currentRound.rolls < 2
        if (game.currentRound.selected.isEmpty()) {
            binding.reroll.text = resources.getString(R.string.reroll_all)
        } else {
            binding.reroll.text = resources.getString(R.string.reroll_selected)
        }
    }

    private fun setupSubmit() { // Setups and handles the submit button logic
        binding.submit.setOnClickListener {
            val selectedScoring = binding.spinner.selectedItem

            val validationSuccess: Boolean = if (selectedScoring == "Low") {
                game.validateSubmit(3)
            } else {
                game.validateSubmit(selectedScoring.toString().toInt())
            }

            if (validationSuccess) { // If calculation was done and added to score causing a new round
                onSubmitValidationSuccess()
            } else {
                Toast.makeText(this, "Invalid combination.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onSubmitValidationSuccess() {
        setupSpinner() // Updates spinner because selection just made has been deleted from list
        updateScoreAndRounds()
        if (game.roundsPlayed >= game.maxRounds) { // CHeck for win
            endGame()
        } else {
            game.newRound()
            refreshDiceView(true)
        }
    }

    private fun updateScoreAndRounds() { // Updates how many rounds are played and score
        binding.rounds.text = getString(R.string.rounds, game.roundsPlayed.toString())
        binding.score.text = getString(R.string.score, game.score.toString())
    }

    private fun setupSpinner() { // Handles dropdown menu for selection of points
        val newArray: MutableList<Any> = game.selectors.toMutableList()

        if (newArray.contains(3)) { // Sets the value "3" to low in the dropdown
            newArray[newArray.indexOf(3)] = "Low"
        }

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, newArray)
        binding.spinner.adapter = spinnerAdapter
    }

    /* For drawing and redrawing selected dices */
    private fun reDrawAllSelectedDice() { // Draw all selected dice
        for (id in game.currentRound.selected) {
            val faceValue = game.currentRound.dices[id].value - 1
            val myView = findViewById<ImageButton>(diceViews[id])
            myView.setImageResource(redDices[faceValue])
        }
    }
    private fun drawSelected(view: ImageButton, value: Int) { // Draw a single selected dice
        val id = value - 1
        view.setImageResource(redDices[id])
    }
    private fun unDrawSelected(view: ImageButton, value: Int) { // Undraw a single selected dice
        val id = value - 1
        view.setImageResource(whiteDices[id])
    }

    private fun endGame() { // Ending the game and displaying results
        println("Ending game!")
        val results = game.results
        val score = game.score
        // Source: https://developer.android.com/guide/topics/ui/dialogs
        this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage(
                    "Low: " + results[0].toString() + "\n" +
                            "4: " + results[1].toString() + "\n" +
                            "5: " + results[2].toString() + "\n" +
                            "6: " + results[3].toString() + "\n" +
                            "7: " + results[4].toString() + "\n" +
                            "8: " + results[5].toString() + "\n" +
                            "9: " + results[6].toString() + "\n" +
                            "10: " + results[7].toString() + "\n" +
                            "11: " + results[8].toString() + "\n" +
                            "12: " + results[9].toString() + "\n\n" +
                            "Total score: $score"
                )
                setPositiveButton(R.string.play_again) { _, _ ->
                    game.initNewGame()
                    updateScoreAndRounds()
                    refreshDiceView(true)
                    setupSpinner()
                }
                setNegativeButton(R.string.exit) { _, _ ->
                    moveTaskToBack(true)
                    exitProcess(-1)
                }
            }
            builder.create()
        }?.show()
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "On resume")
        reDrawAllSelectedDice()
    }
}
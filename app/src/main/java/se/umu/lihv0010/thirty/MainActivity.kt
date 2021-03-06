package se.umu.lihv0010.thirty

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import se.umu.lihv0010.thirty.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
private val whiteDices = intArrayOf(R.drawable.white1, R.drawable.white2, R.drawable.white3, R.drawable.white4, R.drawable.white5, R.drawable.white6)
private val redDices = intArrayOf(R.drawable.red1, R.drawable.red2, R.drawable.red3, R.drawable.red4, R.drawable.red5, R.drawable.red6)

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var game: GameViewModel
    private lateinit var diceViews: Array<ImageButton>

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "ACTIVITY RETURNED")
            game.initNewGame()
            updateScoreAndRounds()
            refreshDiceView(true)
            setupSpinner()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        game = ViewModelProvider(this)[GameViewModel::class.java]
        diceViews = arrayOf(binding.dice1, binding.dice2, binding.dice3,
                            binding.dice4, binding.dice5, binding.dice6)
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
        for ((index, currentView) in diceViews.withIndex()) {

            val idOffset = dices[index].getDiceValue() - 1 // For array offset
            currentView.setImageResource(whiteDices[idOffset])
            if (unDrawSelected) {
                unDrawSelected(currentView, dices[index].getDiceValue())
            }
            checkRollButton()
        }
    }

    private fun setupOnClickDiceLogic() { // Sets up logic for selection of dice
        for ((index, currView) in diceViews.withIndex()) { // Sets listeners for all dice
            currView.setOnClickListener {
                //println("You pressed dice: $index")
                if (!game.currentRound.selected.contains(index)) { // Adds to selected list if not already in there, if so removes it
                    game.currentRound.selected.add(index)
                    drawSelected(currView, game.currentRound.dices[index].getDiceValue())
                } else { // If already in list, remove from selected list
                    game.currentRound.selected.remove(index)
                    unDrawSelected(currView, game.currentRound.dices[index].getDiceValue())
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

    private fun reDrawAllSelectedDice() { // Draw all selected dice
        for (id in game.currentRound.selected) {
            val faceValue: Int = game.currentRound.dices[id].getDiceValue() - 1
            val myView: ImageButton = diceViews[id]
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

        val intent = Intent(this, ShowResultsActivity::class.java).apply {
            putExtra("RESULTS", game.results.toIntArray())
            putExtra("SCORE", game.score)
        }

        resultLauncher.launch(intent)
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "On resume")
        reDrawAllSelectedDice()
    }
}
package se.umu.lihv0010.thirty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import se.umu.lihv0010.thirty.databinding.ActivityShowResultsBinding
import kotlin.system.exitProcess

class ShowResultsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowResultsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showScore()
        showResults()
        buttonListeners()
    }

    private fun buttonListeners() { // Listeners for the two buttons
        binding.quitButton.setOnClickListener { // Exits if exit button is pressed
            moveTaskToBack(true)
            exitProcess(-1)
        }
        binding.newGameButton.setOnClickListener { // Returns to resultLauncher and a new game is created
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun showScore() {
        val score = intent.getIntExtra("SCORE", 0)
        binding.scoreText.text = getString(R.string.score, score.toString())
    }

    private fun showResults() {
        val results = intent.getIntArrayExtra("RESULTS")
        if (results != null) {
            binding.resultText.text = resources.getString(R.string.resultText,
                results[0].toString(), results[1].toString(), results[2].toString(),
                results[3].toString(), results[4].toString(), results[5].toString(),
                results[6].toString(), results[7].toString(), results[8].toString(),
                results[9].toString())
        }
    }
}
package se.umu.lihv0010.thirty

import android.app.Activity
import android.content.Intent
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

        writeScore()
        writeResults()
        buttonListeners()
    }

    private fun buttonListeners() {
        binding.quitButton.setOnClickListener {
            moveTaskToBack(true)
            exitProcess(-1)
        }
        binding.newGameButton.setOnClickListener {
            println("Finishing activity")

            setResult(RESULT_OK)

            finish()

        }
    }

    private fun writeScore() {
        val score = intent.getIntExtra("SCORE", 0)
        binding.scoreText.text = "Score: " + score.toString()
    }

    private fun writeResults() {
        val results = intent.getIntArrayExtra("RESULTS")
        if (results != null) {
            binding.resultText.text =
                "Low: " + results[0].toString() + "\n" +
                        "4: " + results[1].toString() + "\n" +
                        "5: " + results[2].toString() + "\n" +
                        "6: " + results[3].toString() + "\n" +
                        "7: " + results[4].toString() + "\n" +
                        "8: " + results[5].toString() + "\n" +
                        "9: " + results[6].toString() + "\n" +
                        "10: " + results[7].toString() + "\n" +
                        "11: " + results[8].toString() + "\n" +
                        "12: " + results[9].toString() + "\n"
        }
    }
}
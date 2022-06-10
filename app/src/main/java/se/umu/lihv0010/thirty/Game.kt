package se.umu.lihv0010.thirty

class Game {
    var currentRound = Round()
    var score: Int = 0
    var roundsPlayed: Int = 0

    fun completeRound() {
        // TODO: Calculate score

        newRound()
    }

    private fun newRound() {
        roundsPlayed++
        currentRound = Round()
    }
}
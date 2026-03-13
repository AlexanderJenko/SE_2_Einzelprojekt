package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.math.max
import kotlin.math.min

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {

    /*
    @GetMapping
    fun getLeaderboard(): List<GameResult> =
        gameResultService.getGameResults().sortedWith(compareBy({ -it.score }, { it.id }))
    */

    @GetMapping
    // List -> ResponseEntity<List<GameResult>>
    // Ermöglicht das setzen von HTTP-Statuse (Liste ist im "Body" gespeichert)
    fun getLeaderboard(@RequestParam(required = false) rank: Int?): ResponseEntity<List<GameResult>> {

        // Sortierung: Score absteigend, dann Zeit aufsteigend
        val sortedLeaderboard = gameResultService.getGameResults()
            .sortedWith(compareBy({ -it.score }, { it.timeInSeconds }))

        // Kein Parameter -> Alles zurückgeben
        if (rank == null) {
            //ResponseEntity.ok setzt HTTP-Status auf 200
            return ResponseEntity.ok(sortedLeaderboard)
        }

        // HTTP 400 (ResponseEntity.badRequest().build()) bei ungültigem Rang
        if (rank <= 0 || rank > sortedLeaderboard.size) {
            return ResponseEntity.badRequest().build()
        }

        // Index-Grenzen berechnen
        val targetIndex = rank - 1
        val startIndex = max(0, targetIndex - 3)
        val endIndex = min(sortedLeaderboard.size - 1, targetIndex + 3)

        // subList nimmt den Endindex exklusiv, daher endIndex + 1
        val subset = sortedLeaderboard.subList(startIndex, endIndex + 1)

        return ResponseEntity.ok(subset)
    }
}
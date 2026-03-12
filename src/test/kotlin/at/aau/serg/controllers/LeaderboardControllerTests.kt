package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res = controller.getLeaderboard(null)

        verify(mockedService).getGameResults()
        assertEquals(HttpStatus.OK, res.statusCode)
        assertEquals(3, res.body!!.size)
        assertEquals(first, res.body!![0])
        assertEquals(second, res.body!![1])
        assertEquals(third, res.body!![2])
    }

    @Test
    fun test_getLeaderboard_sameScore_CorrectTimeSorting() {
        val first = GameResult(1, "first", 20, 10.0)
        val second = GameResult(2, "second", 20, 15.0)
        val third = GameResult(3, "third", 20, 20.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res = controller.getLeaderboard(null)

        verify(mockedService).getGameResults()
        assertEquals(HttpStatus.OK, res.statusCode)
        assertEquals(3, res.body!!.size)
        assertEquals(first, res.body!![0])
        assertEquals(second, res.body!![1])
        assertEquals(third, res.body!![2])
    }

    // Testen des "rank" Parameters
    @Test
    fun test_getLeaderboard_invalidRank_returnsBadRequest() {
        whenever(mockedService.getGameResults()).thenReturn(listOf(GameResult(1, "p", 10, 10.0)))

        val resTooSmall = controller.getLeaderboard(0)
        assertEquals(HttpStatus.BAD_REQUEST, resTooSmall.statusCode)

        val resTooLarge = controller.getLeaderboard(2)
        assertEquals(HttpStatus.BAD_REQUEST, resTooLarge.statusCode)
    }

    // Sublisten Test
    @Test
    fun test_getLeaderboard_validRank_returnsSublist() {
        val p1 = GameResult(1, "p1", 100, 10.0)
        val p2 = GameResult(2, "p2", 90, 10.0)
        val p3 = GameResult(3, "p3", 80, 10.0)
        val p4 = GameResult(4, "p4", 70, 10.0)
        val p5 = GameResult(5, "p5", 60, 10.0)
        val p6 = GameResult(6, "p6", 50, 10.0)
        val p7 = GameResult(7, "p7", 40, 10.0)
        val p8 = GameResult(8, "p8", 30, 10.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(p1, p2, p3, p4, p5, p6, p7, p8))

        val res = controller.getLeaderboard(4)

        assertEquals(HttpStatus.OK, res.statusCode)
        assertEquals(7, res.body!!.size)
        assertEquals(p1, res.body!![0])
        assertEquals(p7, res.body!![6])
    }

}
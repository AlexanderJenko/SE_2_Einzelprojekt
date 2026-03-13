package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever

class GameResultControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = GameResultController(mockedService)
    }

    @Test
    fun test_getGameResult_callsService() {
        val expected = GameResult(1, "p1", 10, 10.0)
        whenever(mockedService.getGameResult(1)).thenReturn(expected)

        val result = controller.getGameResult(1)

        verify(mockedService).getGameResult(1)
        assertEquals(expected, result)
    }

    @Test
    fun test_getAllGameResults_callsService() {
        val expectedList = listOf(GameResult(1, "p1", 10, 10.0))
        whenever(mockedService.getGameResults()).thenReturn(expectedList)

        val result = controller.getAllGameResults()

        verify(mockedService).getGameResults()
        assertEquals(expectedList, result)
    }

    @Test
    fun test_addGameResult_callsService() {
        val gameResult = GameResult(1, "p1", 10, 10.0)

        controller.addGameResult(gameResult)

        verify(mockedService).addGameResult(gameResult)
    }

    @Test
    fun test_deleteGameResult_callsService() {
        controller.deleteGameResult(1)

        verify(mockedService).deleteGameResult(1)
    }
}
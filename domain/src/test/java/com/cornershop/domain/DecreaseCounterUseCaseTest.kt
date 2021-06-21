package com.cornershop.domain

import com.cornershop.data.models.Counter
import com.cornershop.data.models.CounterError
import com.cornershop.data.models.Result
import com.cornershop.data.repositories.ICounterRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class DecreaseCounterUseCaseTest {

    lateinit var useCase: IDecreaseCounterUseCase

    var mockCounterRepository: ICounterRepository = Mockito.mock(ICounterRepository::class.java)

    @BeforeEach
    fun setUp() {
        useCase = DecreaseCounterUseCase(
            mockCounterRepository
        )
    }

    @AfterEach
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(mockCounterRepository)
    }

    @Test
    fun `Invoke - response is success`() {
        runBlocking {
            val counter = Counter(1, 1, "counter")

            Mockito.`when`(mockCounterRepository.decreaseCounter(counter))
                .thenReturn(Result.Success(data = true))

            val result = useCase(counter)

            Mockito.verify(mockCounterRepository).decreaseCounter(counter)
            assertTrue(result is Result.Success)
            assertTrue((result as Result.Success).data)
        }
    }

    @Test
    fun `Invoke - response is failure`() {
        runBlocking {
            val counter = Counter(1, 1, "counter")

            Mockito.`when`(mockCounterRepository.decreaseCounter(counter))
                .thenReturn(Result.Failure(error = CounterError.NETWORK_ERROR))

            val result = useCase(counter)

            Mockito.verify(mockCounterRepository).decreaseCounter(counter)
            assertTrue(result is Result.Failure)
            assertEquals(CounterError.NETWORK_ERROR, (result as Result.Failure).error)
        }
    }
}
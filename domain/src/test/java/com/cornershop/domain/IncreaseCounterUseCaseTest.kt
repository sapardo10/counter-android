package com.cornershop.domain;

import com.cornershop.data.models.Counter
import com.cornershop.data.models.CounterError
import com.cornershop.data.models.Result
import com.cornershop.data.repositories.ICounterRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito.*

class IncreaseCounterUseCaseTest {

    lateinit var useCase: IIncreaseCounterUseCase

    var mockCounterRepository: ICounterRepository = mock(ICounterRepository::class.java)

    @BeforeEach
    fun setUp() {
        useCase = IncreaseCounterUseCase(
            mockCounterRepository
        )
    }

    @AfterEach
    fun tearDown() {
        verifyNoMoreInteractions(mockCounterRepository)
    }

    @Test
    fun `Invoke - response is success`() {
        runBlocking {
            val counter = Counter(1,1,"counter")

            `when`(mockCounterRepository.increaseCounter(counter)).thenReturn(Result.Success(data = true))

            val result = useCase(counter)

            verify(mockCounterRepository).increaseCounter(counter)
            assertTrue(result is Result.Success)
            assertTrue((result as Result.Success).data)
        }
    }

    @Test
    fun `Invoke - response is failure`() {
        runBlocking {
            val counter = Counter(1,1,"counter")

            `when`(mockCounterRepository.increaseCounter(counter)).thenReturn(Result.Failure(error = CounterError.NETWORK_ERROR))

            val result = useCase(counter)

            verify(mockCounterRepository).increaseCounter(counter)
            assertTrue(result is Result.Failure)
            assertEquals(CounterError.NETWORK_ERROR, (result as Result.Failure).error)
        }
    }
}
package com.cornershop.domain

import com.cornershop.data.models.CounterError
import com.cornershop.data.models.Result
import com.cornershop.data.repositories.ICounterRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

internal class CreateCounterUseCaseTest {

    lateinit var useCase: ICreateCounterUseCase

    private var mockCounterRepository: ICounterRepository =
        Mockito.mock(ICounterRepository::class.java)

    @BeforeEach
    fun setUp() {
        useCase = CreateCounterUseCase(
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
            val counterName = "counterExample"

            Mockito.`when`(mockCounterRepository.createCounter(counterName))
                .thenReturn(Result.Success(data = true))

            val result = useCase(counterName)

            Mockito.verify(mockCounterRepository).createCounter(counterName)
            Assertions.assertTrue(result is Result.Success)
            Assertions.assertTrue((result as Result.Success).data)
        }
    }

    @Test
    fun `Invoke - response is failure`() {
        runBlocking {
            val counterName = "counterExample"

            Mockito.`when`(mockCounterRepository.createCounter(counterName))
                .thenReturn(Result.Failure(error = CounterError.NETWORK_ERROR))

            val result = useCase(counterName)

            Mockito.verify(mockCounterRepository).createCounter(counterName)
            Assertions.assertTrue(result is Result.Failure)
            Assertions.assertEquals(CounterError.NETWORK_ERROR, (result as Result.Failure).error)
        }
    }

}
package com.cornershop.domain

import com.cornershop.data.models.Counter
import com.cornershop.data.models.CounterError
import com.cornershop.data.models.Result
import com.cornershop.data.repositories.ICounterRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class DeleteMultipleCounterUseCaseTest {

    lateinit var useCase: IDeleteMultipleCounterUseCase

    private var mockCounterRepository: ICounterRepository =
        Mockito.mock(ICounterRepository::class.java)

    @BeforeEach
    fun setUp() {
        useCase = DeleteMultipleCounterUseCase(
            mockCounterRepository
        )
    }

    @AfterEach
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(mockCounterRepository)
    }

    @Test
    fun `Invoke - response is success none failed`() {
        runBlocking {
            val counter = Counter(1, "jdkfshas", "counter")
            val counterTwo = Counter(2, "asdad", "counterTwo")

            Mockito.`when`(mockCounterRepository.deleteCounter(counter))
                .thenReturn(Result.Success(data = true))
            Mockito.`when`(mockCounterRepository.deleteCounter(counterTwo))
                .thenReturn(Result.Success(data = true))

            val result = useCase(listOf(counter, counterTwo))

            Mockito.verify(mockCounterRepository).deleteCounter(counter)
            Mockito.verify(mockCounterRepository).deleteCounter(counterTwo)
            assertEquals(0, result.size)
        }
    }

    @Test
    fun `Invoke - response is failure due to one failing`() {
        runBlocking {
            val counter = Counter(1, "jdkfshas", "counter")
            val counterTwo = Counter(2, "asdad", "counterTwo")

            Mockito.`when`(mockCounterRepository.deleteCounter(counter))
                .thenReturn(Result.Failure(error = CounterError.NETWORK_ERROR))
            Mockito.`when`(mockCounterRepository.deleteCounter(counterTwo))
                .thenReturn(Result.Success(data = true))

            val result = useCase(listOf(counter, counterTwo))

            Mockito.verify(mockCounterRepository).deleteCounter(counter)
            Mockito.verify(mockCounterRepository).deleteCounter(counterTwo)
            assertEquals(1, result.size)
            assertEquals(counter, (result[0] as Result.Success).data)
        }
    }

    @Test
    fun `Invoke - response is failure due to all failing`() {
        runBlocking {
            val counter = Counter(1, "jdkfshas", "counter")
            val counterTwo = Counter(2, "asdad", "counterTwo")

            Mockito.`when`(mockCounterRepository.deleteCounter(counter))
                .thenReturn(Result.Failure(error = CounterError.NETWORK_ERROR))
            Mockito.`when`(mockCounterRepository.deleteCounter(counterTwo))
                .thenReturn(Result.Failure(error = CounterError.NETWORK_ERROR))

            val result = useCase(listOf(counter, counterTwo))

            Mockito.verify(mockCounterRepository).deleteCounter(counter)
            Mockito.verify(mockCounterRepository).deleteCounter(counterTwo)
            assertEquals(2, result.size)
            assertEquals(counter, (result[0] as Result.Success).data)
            assertEquals(counterTwo, (result[1] as Result.Success).data)
        }
    }
}
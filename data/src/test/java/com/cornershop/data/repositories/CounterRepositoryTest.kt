package com.cornershop.data.repositories

import com.cornershop.data.datasources.ICounterLocalDataSource
import com.cornershop.data.datasources.ICounterRemoteDataSource
import com.cornershop.data.models.Counter
import com.cornershop.data.models.CounterError
import com.cornershop.data.models.Result
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class CounterRepositoryTest {

    /**
     * Class Under Testing
     */
    private lateinit var counterRepository: ICounterRepository

    /**
     * Mocks
     */
    private val mockLocalDataSource: ICounterLocalDataSource =
        mock(ICounterLocalDataSource::class.java)
    private val mockRemoteDataSource: ICounterRemoteDataSource =
        mock(ICounterRemoteDataSource::class.java)

    @BeforeEach
    fun setUp() {
        counterRepository = CounterRepository(
            localDataSource = mockLocalDataSource,
            remoteDataSource = mockRemoteDataSource
        )
    }

    @AfterEach
    fun tearDown() {
        verifyNoMoreInteractions(
            mockLocalDataSource,
            mockRemoteDataSource
        )
    }

    @Test
    fun createCounter() {
    }

    @Test
    fun `Decrease counter - service response is failure`() {
        runBlocking {
            val counter = Counter(id = "jdkfshas", count = 3, name = "counter")

            `when`(mockRemoteDataSource.decreaseCounter(counter)).thenReturn(Result.Failure(error = CounterError.NETWORK_ERROR))

            val result = counterRepository.decreaseCounter(counter)

            verify(mockRemoteDataSource).decreaseCounter(counter)
            assertTrue(result is Result.Failure)
            assertEquals(CounterError.NETWORK_ERROR, (result as Result.Failure).error)
        }
    }

    @Test
    fun `Decrease counter - service response is successful`() {
        runBlocking {
            val counter = Counter(id = "jdkfshas", count = 3, name = "counter")
            val list = listOf<Counter>(counter)

            `when`(mockRemoteDataSource.decreaseCounter(counter)).thenReturn(Result.Success(data = list))

            val result = counterRepository.decreaseCounter(counter)

            verify(mockRemoteDataSource).decreaseCounter(counter)
            verify(mockLocalDataSource).setAll(list)
            assertTrue(result is Result.Success)
            assertTrue((result as Result.Success).data)
        }
    }

    @Test
    fun `Delete counter - service response is failure`() {
        runBlocking {
            val counter = Counter(id = "jdkfshas", count = 3, name = "counter")

            `when`(mockRemoteDataSource.deleteCounter(counter)).thenReturn(Result.Failure(error = CounterError.NETWORK_ERROR))

            val result = counterRepository.deleteCounter(counter)

            verify(mockRemoteDataSource).deleteCounter(counter)
            assertTrue(result is Result.Failure)
            assertEquals(CounterError.NETWORK_ERROR, (result as Result.Failure).error)
        }
    }

    @Test
    fun `Delete counter - service response is successful`() {
        runBlocking {
            val counter = Counter(id = "jdkfshas", count = 3, name = "counter")
            val list = listOf<Counter>(counter)

            `when`(mockRemoteDataSource.deleteCounter(counter)).thenReturn(Result.Success(data = list))

            val result = counterRepository.deleteCounter(counter)

            verify(mockRemoteDataSource).deleteCounter(counter)
            verify(mockLocalDataSource).delete(counter)
            assertTrue(result is Result.Success)
            assertTrue((result as Result.Success).data)
        }
    }

    @Test
    fun getAll() {
    }

    @Test
    fun `Increase counter - service response is failure`() {
        runBlocking {
            val counter = Counter(id = "jdkfshas", count = 3, name = "counter")

            `when`(mockRemoteDataSource.increaseCounter(counter)).thenReturn(Result.Failure(error = CounterError.NETWORK_ERROR))

            val result = counterRepository.increaseCounter(counter)

            verify(mockRemoteDataSource).increaseCounter(counter)
            assertTrue(result is Result.Failure)
            assertEquals(CounterError.NETWORK_ERROR, (result as Result.Failure).error)
        }
    }

    @Test
    fun `Increase counter - service response is successful`() {
        runBlocking {
            val counter = Counter(id = "jdkfshas", count = 3, name = "counter")
            val list = listOf<Counter>(counter)

            `when`(mockRemoteDataSource.increaseCounter(counter)).thenReturn(Result.Success(data = list))

            val result = counterRepository.increaseCounter(counter)

            verify(mockRemoteDataSource).increaseCounter(counter)
            verify(mockLocalDataSource).setAll(list)
            assertTrue(result is Result.Success)
            assertTrue((result as Result.Success).data)
        }
    }

}
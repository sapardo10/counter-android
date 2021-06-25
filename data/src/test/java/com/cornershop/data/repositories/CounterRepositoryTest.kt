package com.cornershop.data.repositories

import com.cornershop.data.datasources.ICounterLocalDataSource
import com.cornershop.data.datasources.ICounterRemoteDataSource
import com.cornershop.data.models.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
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
    fun `Create counter - service response is failure`() {
        runBlocking {
            val name = "counterName"
            val counter = Counter(1, "ashdakshjd", name)
            val counterList = listOf<Counter>(
                counter
            )

            `when`(mockRemoteDataSource.createCounter(title = name)).thenReturn(
                Result.Failure(error = CounterError.NETWORK_ERROR)
            )

            val result = counterRepository.createCounter(name)

            verify(mockRemoteDataSource).createCounter(name)
            assertTrue(result is Result.Failure)
            assertTrue((result as Result.Failure).error == CounterError.NETWORK_ERROR)
        }
    }

    @Test
    fun `Create counter - service response is successful`() {
        runBlocking {
            val name = "counterName"
            val counter = Counter(1, "ashdakshjd", name)
            val counterList = listOf<Counter>(
                counter
            )

            `when`(mockRemoteDataSource.createCounter(title = name)).thenReturn(
                Result.Success(
                    data = counterList
                )
            )

            val result = counterRepository.createCounter(name)

            verify(mockRemoteDataSource).createCounter(name)
            verify(mockLocalDataSource).setAllCounters(counterList)
            assertTrue(result is Result.Success)
            assertTrue((result as Result.Success).data)
        }
    }

    @Test
    fun `Decrease counter - service response is failure`() {
        runBlocking {
            val counter = Counter(id = "jdkfshas", count = 3, name = "counter")

            `when`(mockRemoteDataSource.decreaseCounter(counter)).thenReturn(Result.Failure(error = CounterError.NETWORK_ERROR))

            val result = counterRepository.decreaseCounter(counter)

            verify(mockLocalDataSource).decreaseCounter(counter)
            verify(mockRemoteDataSource).decreaseCounter(counter)
            verify(mockLocalDataSource).increaseCounter(counter)
            assertTrue(result is Result.Failure)
            assertEquals(CounterError.NETWORK_ERROR, (result as Result.Failure).error)
        }
    }

    @Test
    fun `Decrease counter - service response is successful`() {
        runBlocking {
            val counter = Counter(id = "jdkfshas", count = 3, name = "counter")
            val list = listOf(counter)

            `when`(mockRemoteDataSource.decreaseCounter(counter)).thenReturn(Result.Success(data = list))

            val result = counterRepository.decreaseCounter(counter)

            verify(mockLocalDataSource).decreaseCounter(counter)
            verify(mockRemoteDataSource).decreaseCounter(counter)
            verify(mockLocalDataSource).setAllCounters(list)
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
            val list = listOf(counter)

            `when`(mockRemoteDataSource.deleteCounter(counter)).thenReturn(Result.Success(data = list))

            val result = counterRepository.deleteCounter(counter)

            verify(mockRemoteDataSource).deleteCounter(counter)
            verify(mockLocalDataSource).deleteCounter(counter)
            assertTrue(result is Result.Success)
            assertTrue((result as Result.Success).data)
        }
    }

    @Test
    fun `Get all - service response is failure`() {
        runBlocking {
            val counter = Counter(id = "jdkfshas", count = 3, name = "counter")
            val counterTwo = Counter(id = "ajhdkjas", count = 3, name = "counter")

            val list = listOf(counter, counterTwo)

            `when`(mockRemoteDataSource.getAll()).thenReturn(Result.Failure(error = CounterError.NETWORK_ERROR))
            `when`(mockLocalDataSource.getAllCounters()).thenReturn(flow { Result.Success(data = list) })

            var resultIndex = 0
            val result = counterRepository.getAll().collect { result ->
                if (resultIndex == 0) {
                    assertTrue(result is Result.Failure)
                    assertEquals(CounterError.NETWORK_ERROR, (result as Result.Failure).error)
                } else if (resultIndex == 1) {
                    assertTrue(result is Result.Success)
                    assertEquals(2, (result as Result.Success).data.size)
                    assertEquals(counter, result.data[0])
                    assertEquals(counterTwo, result.data[1])
                }
                resultIndex++
            }

            verify(mockRemoteDataSource).getAll()
            verify(mockLocalDataSource).getAllCounters()

        }
    }

    @Test
    fun `Get all - service response is success`() {
        runBlocking {
            val counter = Counter(id = "jdkfshas", count = 3, name = "counter")
            val counterTwo = Counter(id = "ajhdkjas", count = 3, name = "counter")

            val list = listOf(counter, counterTwo)

            `when`(mockRemoteDataSource.getAll()).thenReturn(Result.Success(data = list))
            `when`(mockLocalDataSource.getAllCounters()).thenReturn(flow { Result.Success(data = list) })

            val result = counterRepository.getAll().collect { first ->
                assertTrue(first is Result.Success)
                assertEquals(2, (first as Result.Success).data.size)
                assertEquals(counter, first.data[0])
                assertEquals(counterTwo, first.data[1])
            }

            verify(mockRemoteDataSource).getAll()
            verify(mockLocalDataSource).setAllCounters(list)
            verify(mockLocalDataSource).getAllCounters()

        }
    }

    @Test
    fun `Get all category suggestions - success`() {
        runBlocking {
            val suggestion = Suggestion(
                name = "ajsdkas34985u"
            )
            val counterCategorySuggestion = SuggestionsCategory(
                title = "asdasdas",
                suggestions = listOf(
                    suggestion
                )
            )
            val categories = listOf<SuggestionsCategory>(counterCategorySuggestion)

            `when`(mockLocalDataSource.getAllCounterCategorySuggestions()).thenReturn(categories)

            val result = counterRepository.getAllCategorySuggestions()

            verify(mockLocalDataSource).getAllCounterCategorySuggestions()
            assertEquals(1, result.size)
            assertEquals(counterCategorySuggestion, result[0])
            assertEquals(suggestion, result[0].suggestions[0])
        }
    }

    @Test
    fun `Increase counter - service response is failure`() {
        runBlocking {
            val counter = Counter(id = "jdkfshas", count = 3, name = "counter")

            `when`(mockRemoteDataSource.increaseCounter(counter)).thenReturn(Result.Failure(error = CounterError.NETWORK_ERROR))

            val result = counterRepository.increaseCounter(counter)

            verify(mockLocalDataSource).increaseCounter(counter)
            verify(mockRemoteDataSource).increaseCounter(counter)
            verify(mockLocalDataSource).decreaseCounter(counter)
            assertTrue(result is Result.Failure)
            assertEquals(CounterError.NETWORK_ERROR, (result as Result.Failure).error)
        }
    }

    @Test
    fun `Increase counter - service response is successful`() {
        runBlocking {
            val counter = Counter(id = "jdkfshas", count = 3, name = "counter")
            val list = listOf(counter)

            `when`(mockRemoteDataSource.increaseCounter(counter)).thenReturn(Result.Success(data = list))

            val result = counterRepository.increaseCounter(counter)

            verify(mockLocalDataSource).increaseCounter(counter)
            verify(mockRemoteDataSource).increaseCounter(counter)
            verify(mockLocalDataSource).setAllCounters(list)
            assertTrue(result is Result.Success)
            assertTrue((result as Result.Success).data)
        }
    }

}
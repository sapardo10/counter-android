package com.cornershop.data.repositories

import com.cornershop.data.datasources.ICounterLocalDataSource
import com.cornershop.data.datasources.ICounterRemoteDataSource
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verifyNoInteractions

internal class CounterRepositoryTest {

    /**
     * Class Under Testing
     */
    private var counterRepository: ICounterRepository? = null

    /**
     * Mocks
     */
    private val mockLocalDataSource: ICounterLocalDataSource = mock(ICounterLocalDataSource::class.java)
    private val mockRemoteDataSource: ICounterRemoteDataSource = mock(ICounterRemoteDataSource::class.java)

    @BeforeEach
    fun setUp() {
        counterRepository = CounterRepository(
            localDataSource = mockLocalDataSource,
            remoteDataSource = mockRemoteDataSource
        )
    }

    @AfterEach
    fun tearDown() {
        counterRepository = null
        verifyNoInteractions(mockLocalDataSource)
        verifyNoInteractions(mockRemoteDataSource)
    }

    @Test
    fun createCounter() {
    }

    @Test
    fun decreaseCounter() {
    }

    @Test
    fun deleteCounter() {
    }

    @Test
    fun getAll() {
    }

    @Test
    fun increaseCounter() {
    }
}
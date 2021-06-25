package com.cornershop.domain

import com.cornershop.data.repositories.ICounterRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class GetAllCountersUseCaseTest {

    lateinit var useCase: IGetAllCountersUseCase

    private var mockCounterRepository: ICounterRepository =
        Mockito.mock(ICounterRepository::class.java)

    @BeforeEach
    fun setUp() {
        useCase = GetAllCountersUseCase(
            mockCounterRepository
        )
    }

    @AfterEach
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(mockCounterRepository)
    }

    @Test
    fun `Invoke - success`() {
        runBlocking {
            `when`(mockCounterRepository.getAll()).thenReturn(flow {})

            val result = useCase()

            verify(mockCounterRepository).getAll()
        }
    }
}
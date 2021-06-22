package com.cornershop.counterstest.features.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cornershop.counterstest.MainCoroutineRule
import com.cornershop.counterstest.utils.SingleLiveEventObserver
import com.cornershop.data.models.Counter
import com.cornershop.data.models.CounterError
import com.cornershop.data.models.Result
import com.cornershop.domain.IGetAllCountersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class MainViewModelTest {

    @ExperimentalCoroutinesApi
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    /**
     * Class under testing
     */
    private lateinit var viewModel: MainViewModel

    /**
     * Mocks
     */
    private var mockActionsObserver: SingleLiveEventObserver<MainViewModelActions?> =
        Mockito.mock(SingleLiveEventObserver::class.java) as SingleLiveEventObserver<MainViewModelActions?>
    private val mockGetAllCountersUseCase = Mockito.mock(IGetAllCountersUseCase::class.java)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel(
            getAllCountersUseCase = mockGetAllCountersUseCase,
        )
        viewModel.actions.observeForever(mockActionsObserver)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(
            mockGetAllCountersUseCase,
        )

        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Initialize view - fetch counter success with non-empty list`() {
        runBlocking {
            val counter = Counter(1, 1, "counter")

            `when`(mockGetAllCountersUseCase.invoke()).thenReturn(flow {
                emit(Result.Success(data = listOf(counter)))
            })

            viewModel.initializeView()

            verify(mockGetAllCountersUseCase).invoke()
            verify(mockActionsObserver).onChanged(MainViewModelActions.ShowListCounter)
        }
    }

    @Test
    fun `Initialize view - fetch counter success with empty list`() {
        runBlocking {

            `when`(mockGetAllCountersUseCase.invoke()).thenReturn(flow {
                emit(Result.Success<List<Counter>>(data = listOf()))
            })

            viewModel.initializeView()

            verify(mockGetAllCountersUseCase).invoke()
            verify(mockActionsObserver).onChanged(MainViewModelActions.ShowEmptyState)
        }
    }

    @Test
    fun `Initialize view - fetch counter failed`() {
        runBlocking {

            `when`(mockGetAllCountersUseCase.invoke()).thenReturn(flow {
                emit(Result.Failure<List<Counter>>(error = CounterError.NETWORK_ERROR))
            })

            viewModel.initializeView()

            verify(mockGetAllCountersUseCase).invoke()
            verify(mockActionsObserver).onChanged(MainViewModelActions.ShowListCounterNoInternetConnectionDialog)
        }
    }

    @Test
    fun `on create counter button tapped`() {
        viewModel.onCreateCounterButtonTapped()

        verify(mockActionsObserver).onChanged(MainViewModelActions.GoToCreateScreen)
    }
}
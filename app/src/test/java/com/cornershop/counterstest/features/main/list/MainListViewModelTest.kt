package com.cornershop.counterstest.features.main.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.cornershop.counterstest.MainCoroutineRule
import com.cornershop.data.models.Counter
import com.cornershop.data.models.CounterError
import com.cornershop.data.models.Result
import com.cornershop.domain.IDecreaseCounterUseCase
import com.cornershop.domain.IDeleteMultipleCounterUseCase
import com.cornershop.domain.IGetAllCountersUseCase
import com.cornershop.domain.IIncreaseCounterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.*

class MainListViewModelTest {

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
    private lateinit var viewModel: MainListViewModel

    /**
     * Mocks
     */
    private var mockActionsObserver: Observer<MainListViewModelActions> =
        mock(Observer::class.java) as Observer<MainListViewModelActions>
    private var mockCountersObserver: Observer<List<CounterViewModel>> =
        mock(Observer::class.java) as Observer<List<CounterViewModel>>
    private var mockViewStateObserver: Observer<MainViewState> =
        mock(Observer::class.java) as Observer<MainViewState>
    private val mockDecreaseCounterUseCase = mock(IDecreaseCounterUseCase::class.java)
    private val mockDeleteMultipleCounterUseCase = mock(IDeleteMultipleCounterUseCase::class.java)
    private val mockGetAllCountersUseCase = mock(IGetAllCountersUseCase::class.java)
    private val mockIncreaseCounterUseCase = mock(IIncreaseCounterUseCase::class.java)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MainListViewModel(
            decreaseCounterUseCase = mockDecreaseCounterUseCase,
            deleteMultipleCounterUseCase = mockDeleteMultipleCounterUseCase,
            getAllCountersUseCase = mockGetAllCountersUseCase,
            increaseCounterUseCase = mockIncreaseCounterUseCase
        )
        viewModel.actions.observeForever(mockActionsObserver)
        viewModel.countersViewModel.observeForever(mockCountersObserver)
        viewModel.viewState.observeForever(mockViewStateObserver)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        verifyNoMoreInteractions(
            mockCountersObserver,
            mockDecreaseCounterUseCase,
            mockDeleteMultipleCounterUseCase,
            mockGetAllCountersUseCase,
            mockIncreaseCounterUseCase
        )

        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Delete items - failure network error`() {
        runBlocking {
            val counter = Counter(1, "jdkfshas", "counter")
            viewModel.selectedCounters.add(counter)

            `when`(mockDeleteMultipleCounterUseCase.invoke(listOf(counter))).thenReturn(
                listOf(
                    Result.Failure(error = CounterError.NETWORK_ERROR)
                )
            )

            viewModel.deleteItems()

            verify(mockDeleteMultipleCounterUseCase).invoke(listOf(counter))
            assertTrue(viewModel.selectedCounters.isNotEmpty())
            verify(
                mockActionsObserver,
                times(1)
            ).onChanged(
                MainListViewModelActions.ShowDialogDeleteNetworkError
            )
        }
    }

    @Test
    fun `Delete items - success`() {
        runBlocking {
            val counter = Counter(1, "jdkfshas", "counter")
            val list = listOf(counter)
            viewModel.selectedCounters.add(counter)

            `when`(mockDeleteMultipleCounterUseCase.invoke(list)).thenReturn(listOf())

            viewModel.deleteItems()

            verify(mockDeleteMultipleCounterUseCase).invoke(listOf())
            assertTrue(viewModel.selectedCounters.isEmpty())
            verify(mockViewStateObserver, times(2)).onChanged(MainViewState.NORMAL_STATE)
        }
    }

    @Test
    fun `Initialize view - Non empty list of counters`() {
        runBlocking {
            val counter = Counter(1, "jdkfshas", "counter")

            `when`(mockGetAllCountersUseCase.invoke()).thenReturn(
                flow {
                    emit(Result.Success(data = listOf(counter)))
                }
            )

            viewModel.initializeView()

            verify(mockActionsObserver).onChanged(MainListViewModelActions.ShowNormalList)
            verify(mockGetAllCountersUseCase).invoke()
            verify(mockCountersObserver, times(2)).onChanged(any())
            assertEquals(1, viewModel.countersViewModel.value?.size)
            val item = viewModel.countersViewModel.value?.first()
            assertEquals(counter, item?.counter)
        }
    }

    @Test
    fun `Initialize view - Empty list of counters`() {
        runBlocking {
            `when`(mockGetAllCountersUseCase.invoke()).thenReturn(
                flow {
                    emit(Result.Success(data = listOf<Counter>()))
                }
            )

            viewModel.initializeView()

            verify(mockActionsObserver).onChanged(MainListViewModelActions.ShowNormalList)
            verify(mockGetAllCountersUseCase).invoke()
            verify(mockCountersObserver, times(2)).onChanged(any())
            assertEquals(0, viewModel.countersViewModel.value?.size)
        }
    }

    @Test
    fun `Initialize view - Network error`() {
        runBlocking {
            `when`(mockGetAllCountersUseCase.invoke()).thenReturn(
                flow {
                    emit(Result.Failure<List<Counter>>(error = CounterError.NETWORK_ERROR))
                }
            )

            viewModel.initializeView()

            verify(mockGetAllCountersUseCase).invoke()
            verify(mockActionsObserver).onChanged(MainListViewModelActions.ShowNetworkError)
            verify(mockCountersObserver, times(2)).onChanged(listOf())
        }
    }

    @Test
    fun `Map Counter to CounterViewModel`() {
        val counter = Counter(1, "jdkfshas", "counter")

        val result = viewModel.mapCounterToCounterViewModel(counter = counter)

        assertEquals(counter, result.counter)
        assertNotNull(result.isSelected)
        assertNotNull(result.onCheckTapped)
        assertNotNull(result.onLongTap)
        assertNotNull(result.onMinusTapped)
        assertNotNull(result.onPlusTapped)
    }

    @Test
    fun `On item minus tap`() {
        runBlocking {
            val counter = Counter(1, "jdkfshas", "counter")

            viewModel.onItemMinusTap(counter)

            verify(mockDecreaseCounterUseCase).invoke(counter)
        }
    }

    @Test
    fun `On item plus tap`() {
        runBlocking {
            val counter = Counter(1, "jdkfshas", "counter")

            viewModel.onItemPlusTap(counter)

            verify(mockIncreaseCounterUseCase).invoke(counter)
        }
    }

    @Test
    fun `Is item selected - true`() {
        val counter = Counter(1, "jdkfshas", "counter")
        viewModel.selectedCounters.add(counter)

        val result = viewModel.isItemSelected(counter)

        assertTrue(result)
    }

    @Test
    fun `Is item selected - false`() {
        val counter = Counter(1, "jdkfshas", "counter")

        val result = viewModel.isItemSelected(counter)

        assertFalse(result)
    }

    @Test
    fun `On item selection tapped - counter has been selected before and deletionMode is set to off`() {
        val counter = Counter(1, "jdkfshas", "counter")
        viewModel.selectedCounters.add(counter)
        viewModel.viewState.value = MainViewState.DELETE_STATE

        viewModel.onItemSelectionTapped(counter)

        assertEquals(0, viewModel.selectedCounters.size)
        verify(mockCountersObserver).onChanged(any())
        verify(mockViewStateObserver, times(2)).onChanged(MainViewState.NORMAL_STATE)
    }

    @Test
    fun `On item selection tapped - counter has been selected before and deletionMode is not changed`() {
        val counter = Counter(1, "jdkfshas", "counter")
        val counterTwo = Counter(2, "jdkfshas", "counter two")
        viewModel.selectedCounters.add(counter)
        viewModel.selectedCounters.add(counterTwo)
        viewModel.viewState.value = MainViewState.DELETE_STATE

        viewModel.onItemSelectionTapped(counter)

        assertEquals(1, viewModel.selectedCounters.size)
        verify(mockViewStateObserver, times(2)).onChanged(MainViewState.DELETE_STATE)
        verify(mockCountersObserver).onChanged(any())
    }

    @Test
    fun `On item selection tapped - counter has not been selected before and deletionMode is set to on`() {
        val counter = Counter(1, "jdkfshas", "counter")
        viewModel.selectedCounters.clear()
        viewModel.viewState.value = MainViewState.NORMAL_STATE

        viewModel.onItemSelectionTapped(counter)

        verify(mockViewStateObserver).onChanged(MainViewState.DELETE_STATE)
        assertEquals(1, viewModel.selectedCounters.size)
        verify(mockCountersObserver).onChanged(any())
    }
}
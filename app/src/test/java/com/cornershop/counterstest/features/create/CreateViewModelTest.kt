package com.cornershop.counterstest.features.create

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cornershop.counterstest.MainCoroutineRule
import com.cornershop.counterstest.utils.SingleLiveEventObserver
import com.cornershop.data.models.CounterError
import com.cornershop.data.models.Result
import com.cornershop.domain.ICreateCounterUseCase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class CreateViewModelTest {

    @ExperimentalCoroutinesApi
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CreateViewModel

    private var mockActionsObserver: SingleLiveEventObserver<CreateViewModelActions> =
        mock(SingleLiveEventObserver::class.java) as SingleLiveEventObserver<CreateViewModelActions>
    private var mockCreateCounterUseCase: ICreateCounterUseCase =
        mock(ICreateCounterUseCase::class.java)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = CreateViewModel(mockCreateCounterUseCase)
        viewModel.actions.observeForever(mockActionsObserver)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        verifyNoMoreInteractions(mockActionsObserver)
        verifyNoMoreInteractions(mockCreateCounterUseCase)

        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Create counter - text is blank`() {
        runBlocking {
            viewModel.newCounterTitle = "   "

            viewModel.createCounter()
            testDispatcher.advanceUntilIdle()

            verify(mockActionsObserver).onChanged(CreateViewModelActions.SHOW_CREATING_LOADING)
            verify(mockActionsObserver).onChanged(CreateViewModelActions.SHOW_COUNTER_EMPTY_ERROR)
            verify(mockActionsObserver).onChanged(CreateViewModelActions.HIDE_COUNTER_EMPTY_ERROR)
            verify(mockActionsObserver).onChanged(CreateViewModelActions.HIDE_CREATING_LOADING)
        }
    }

    @Test
    fun `Create counter - creation is failure`() {
        runBlocking {
            val name = "ashdoio"
            viewModel.newCounterTitle = name

            `when`(mockCreateCounterUseCase(name)).thenReturn(Result.Failure(error = CounterError.NETWORK_ERROR))

            viewModel.createCounter()
            testDispatcher.advanceUntilIdle()

            verify(mockActionsObserver).onChanged(CreateViewModelActions.SHOW_CREATING_LOADING)
            verify(mockCreateCounterUseCase)(name)
            verify(mockActionsObserver).onChanged(CreateViewModelActions.SHOW_NETWORK_ERROR)
            verify(mockActionsObserver).onChanged(CreateViewModelActions.HIDE_CREATING_LOADING)
        }
    }

    @Test
    fun `Create counter - creation is successful`() {
        runBlocking {
            val name = "ashdoio"
            viewModel.newCounterTitle = name

            `when`(mockCreateCounterUseCase(name)).thenReturn(Result.Success(data = true))

            viewModel.createCounter()
            testDispatcher.advanceUntilIdle()

            verify(mockActionsObserver).onChanged(CreateViewModelActions.SHOW_CREATING_LOADING)
            verify(mockCreateCounterUseCase)(name)
            verify(mockActionsObserver).onChanged(CreateViewModelActions.NAVIGATE_BACK)
            verify(mockActionsObserver).onChanged(CreateViewModelActions.HIDE_CREATING_LOADING)
        }
    }

    @Test
    fun `On see examples clicked - success`() {
        runBlocking {
            viewModel.onSeeExamplesClicked()

            verify(mockActionsObserver).onChanged(CreateViewModelActions.GO_TO_EXAMPLES_SCREEN)
        }
    }

    @Test
    fun `On text changed`() {
        viewModel.newCounterTitle = "hsjadgahsbdjsa"
        val newText = "skdla"

        viewModel.onTextChanged(newText)

        assertEquals(newText, viewModel.newCounterTitle)
    }

    @Test
    fun `Show soft keyboard`() {
        runBlocking {
            viewModel.showSoftKeyboard()
            testDispatcher.advanceUntilIdle()

            verify(mockActionsObserver).onChanged(CreateViewModelActions.SHOW_SOFT_KEYBOARD)
        }
    }
}
package com.cornershop.counterstest.features.welcome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cornershop.counterstest.MainCoroutineRule
import com.cornershop.counterstest.session.IUserPreferencesHelper
import com.cornershop.counterstest.utils.SingleLiveEventObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class WelcomeViewModelTest {

    @ExperimentalCoroutinesApi
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: WelcomeViewModel

    private var mockActionsObserver: SingleLiveEventObserver<WelcomeViewModelActions> =
        mock(SingleLiveEventObserver::class.java) as SingleLiveEventObserver<WelcomeViewModelActions>
    private var mockUserPreferencesHelper: IUserPreferencesHelper =
        mock(IUserPreferencesHelper::class.java)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = WelcomeViewModel(mockUserPreferencesHelper)
        viewModel.actions.observeForever(mockActionsObserver)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        verifyNoMoreInteractions(mockActionsObserver)
        verifyNoMoreInteractions(mockUserPreferencesHelper)

        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Initialize view - user has not seen welcome screen before`() {
        runBlockingTest {
            `when`(mockUserPreferencesHelper.hasUserSeenWelcomeScreenBefore()).thenReturn(false)

            viewModel.initializeView()
            testDispatcher.advanceUntilIdle()

            verify(mockActionsObserver).onChanged(WelcomeViewModelActions.SHOW_VIEWS)
            verify(mockUserPreferencesHelper).hasUserSeenWelcomeScreenBefore()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Initialize view - user has seen welcome screen before`() {
        runBlockingTest {
            `when`(mockUserPreferencesHelper.hasUserSeenWelcomeScreenBefore()).thenReturn(true)

            viewModel.initializeView()
            testDispatcher.advanceUntilIdle()

            verify(mockActionsObserver).onChanged(WelcomeViewModelActions.GO_TO_MAIN_SCREEN)
            verify(mockUserPreferencesHelper).hasUserSeenWelcomeScreenBefore()
        }
    }

    @Test
    fun `On start button tapped`() {
        viewModel.onStartButtonTapped()

        verify(mockActionsObserver).onChanged(WelcomeViewModelActions.GO_TO_MAIN_SCREEN)
        verify(mockUserPreferencesHelper).setHasUserSeenWelcomeScreenBefore(true)
    }
}
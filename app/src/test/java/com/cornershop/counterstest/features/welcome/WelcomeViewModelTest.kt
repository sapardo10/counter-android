package com.cornershop.counterstest.features.welcome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.cornershop.counterstest.session.IUserPreferencesHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class WelcomeViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var viewModel: WelcomeViewModel

    private var mockActionsObserver: Observer<WelcomeViewModelActions> = mock(Observer::class.java) as Observer<WelcomeViewModelActions>
    private var mockUserPreferencesHelper: IUserPreferencesHelper = mock(IUserPreferencesHelper::class.java)

    @Before
    fun setUp() {
        viewModel = WelcomeViewModel(mockUserPreferencesHelper)
        viewModel.actions.observeForever(mockActionsObserver)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(mockActionsObserver)
        verifyNoMoreInteractions(mockUserPreferencesHelper)
    }

    @Test
    fun `Initialize view - user has not seen welcome screen before`() {
        `when`(mockUserPreferencesHelper.hasUserSeenWelcomeScreenBefore()).thenReturn(false)

        viewModel.initializeView()

        verify(mockUserPreferencesHelper).hasUserSeenWelcomeScreenBefore()
    }

    @Test
    fun `Initialize view - user has seen welcome screen before`() {
        `when`(mockUserPreferencesHelper.hasUserSeenWelcomeScreenBefore()).thenReturn(true)

        viewModel.initializeView()

        verify(mockActionsObserver).onChanged(WelcomeViewModelActions.GO_TO_MAIN_SCREEN)
        verify(mockUserPreferencesHelper).hasUserSeenWelcomeScreenBefore()
    }

    @Test
    fun `On start button tapped`() {
        viewModel.onStartButtonTapped()

        verify(mockActionsObserver).onChanged(WelcomeViewModelActions.GO_TO_MAIN_SCREEN)
        verify(mockUserPreferencesHelper).setHasUserSeenWelcomeScreenBefore(true)
    }
}
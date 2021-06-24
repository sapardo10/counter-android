package com.cornershop.counterstest.features.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.counterstest.session.IUserPreferencesHelper
import com.cornershop.counterstest.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val userPreferencesHelper: IUserPreferencesHelper
) : ViewModel() {

    val actions = SingleLiveEvent<WelcomeViewModelActions>()

    /**
     * -------------------------------------- PUBLIC METHODS ---------------------------------------
     */

    /**
     * Method that runs all the login for the view when it is initialized
     * 1. It checks if the user has already seen the welcome screen
     */
    fun initializeView() {
        viewModelScope.launch {
            delay(500)
            val userHasSeenWelcomeScreenBefore =
                userPreferencesHelper.hasUserSeenWelcomeScreenBefore()
            if (userHasSeenWelcomeScreenBefore) {
                actions.postValue(WelcomeViewModelActions.GO_TO_MAIN_SCREEN)
            } else {
                actions.postValue(WelcomeViewModelActions.SHOW_VIEWS)
            }
        }
    }

    /**
     * Method called when the user taps on the start flow button
     */
    fun onStartButtonTapped() {
        userPreferencesHelper.setHasUserSeenWelcomeScreenBefore(hasSeenIt = true)
        actions.postValue(WelcomeViewModelActions.GO_TO_MAIN_SCREEN)
    }

}

enum class WelcomeViewModelActions {
    GO_TO_MAIN_SCREEN,
    SHOW_VIEWS
}
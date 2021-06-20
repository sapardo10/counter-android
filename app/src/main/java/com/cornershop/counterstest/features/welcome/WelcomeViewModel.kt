package com.cornershop.counterstest.features.welcome

import androidx.lifecycle.*
import com.cornershop.counterstest.session.IUserPreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
        private val userPreferencesHelper: IUserPreferencesHelper
): ViewModel() {

    val actions = MutableLiveData<WelcomeViewModelActions>()

    /**
     * -------------------------------------- PUBLIC METHODS ---------------------------------------
     */

    /**
     * Method that runs all the login for the view when it is initialized
     * 1. It checks if the user has already seen the welcome screen
     */
    fun initializeView() {
        val userHasSeenWelcomeScreenBefore = userPreferencesHelper.hasUserSeenWelcomeScreenBefore()
        if(userHasSeenWelcomeScreenBefore) {
            actions.postValue(WelcomeViewModelActions.GO_TO_MAIN_SCREEN)
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
    GO_TO_MAIN_SCREEN
}
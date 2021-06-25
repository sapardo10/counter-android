package com.cornershop.counterstest.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.counterstest.utils.SingleLiveEvent
import com.cornershop.data.models.Counter
import com.cornershop.data.models.CounterError
import com.cornershop.data.models.Result
import com.cornershop.domain.IGetAllCountersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllCountersUseCase: IGetAllCountersUseCase
) : ViewModel() {

    val actions = SingleLiveEvent<MainViewModelActions?>()
    private var isShowingList = false

    /**
     * -------------------------------------- PUBLIC METHODS ---------------------------------------
     */

    /**
     * Method that runs all the login for the view when it is initialized
     * 1. It checks if the user has already seen the welcome screen
     */
    fun initializeView() {
        viewModelScope.launch {
            getAllCountersUseCase().collect { result ->
                when (result) {
                    is Result.Success -> onCountersFetchedSucceeded(result.data)
                    is Result.Failure -> onCountersFetchedFailed(result.error)
                    else -> actions.postValue(MainViewModelActions.ShowEmptyState)
                }
            }
        }
    }

    /**
     * Method called when the user wants to create a new counter
     */
    fun onCreateCounterButtonTapped() {
        actions.postValue(MainViewModelActions.GoToCreateScreen)
    }

    /**
     * -------------------------------------- PRIVATE METHODS --------------------------------------
     */

    /**
     * Method called when the fetching of the counters failed
     * @param error [CounterError] why the fetching failed
     */
    private fun onCountersFetchedFailed(error: CounterError) {
        if (error == CounterError.NETWORK_ERROR) {
            actions.postValue(MainViewModelActions.ShowListCounter)
        }
    }

    /**
     * Method called when the fetching of the counters was a success
     * @param list [List] of [Counter] of the application
     */
    private fun onCountersFetchedSucceeded(list: List<Counter>?) {
        if (list?.isEmpty() == true) {
            isShowingList = false
            actions.postValue(MainViewModelActions.ShowEmptyState)
        } else {
            if (!isShowingList) {
                actions.postValue(MainViewModelActions.ShowListCounter)
            }
            isShowingList = true
        }
    }
}

sealed class MainViewModelActions {
    object GoToCreateScreen : MainViewModelActions()
    object ShowEmptyState : MainViewModelActions()
    object ShowListCounter : MainViewModelActions()
}
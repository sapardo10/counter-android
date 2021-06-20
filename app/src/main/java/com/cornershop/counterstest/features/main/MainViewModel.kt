package com.cornershop.counterstest.features.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.counterstest.utils.SingleLiveEvent
import com.cornershop.data.models.Counter
import com.cornershop.data.models.CounterError
import com.cornershop.data.models.Result
import com.cornershop.domain.GetAllCountersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllCountersUseCase: GetAllCountersUseCase
): ViewModel() {

    val counters = MutableLiveData<List<Counter>>()
    val actions = SingleLiveEvent<MainViewModelActions?>()

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
                when(result) {
                    is Result.Success -> result.data.let { data -> counters.postValue(data) }
                    is Result.Failure -> {
                        if(result.error == CounterError.NETWORK_ERROR) {
                            actions.postValue(MainViewModelActions.ShowListCounterNoInternetConnectionDialog)
                        }
                    }
                    else -> {
                        counters.postValue(listOf())
                    }
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
}

sealed class MainViewModelActions {
    object GoToCreateScreen: MainViewModelActions()
    object ShowListCounterNoInternetConnectionDialog: MainViewModelActions()
    data class ShowUpdateCounterNoInternetConnectionDialog(
        val counterName: String,
        val counterAfterUpdate: Int
    ): MainViewModelActions()
}
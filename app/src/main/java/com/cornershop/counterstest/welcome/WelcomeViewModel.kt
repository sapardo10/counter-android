package com.cornershop.counterstest.welcome

import androidx.lifecycle.*
import com.cornershop.data.models.Counter
import com.cornershop.data.models.Result
import com.cornershop.domain.CreateCounterUseCase
import com.cornershop.domain.GetAllCountersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val createCounterUseCase: CreateCounterUseCase,
    private val getAllCountersUseCase: GetAllCountersUseCase,
): ViewModel() {

    val counters = MutableLiveData<List<Counter>?>()
    private val errors: MutableLiveData<WelcomeViewModelErrors> = MutableLiveData()

    fun initViewModel() {
        viewModelScope.launch {
            getAllCountersUseCase().collect {
                when(it) {
                    is Result.Success -> counters.postValue(it.data)
                    else -> errors.postValue(WelcomeViewModelErrors.NETWORK_ERROR)
                }
            }
        }
    }

    fun onCreateCounterTapped() {
        viewModelScope.launch { createCounterUseCase() }
    }
}

enum class WelcomeViewModelErrors {
    NETWORK_ERROR
}
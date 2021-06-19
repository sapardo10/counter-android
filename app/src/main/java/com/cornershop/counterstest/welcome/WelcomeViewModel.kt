package com.cornershop.counterstest.welcome

import androidx.lifecycle.*
import com.cornershop.domain.GetAllCountersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val getAllCountersUseCase: GetAllCountersUseCase,
): ViewModel() {

    val counters = getAllCountersUseCase().asLiveData(viewModelScope.coroutineContext)
    val errors: MutableLiveData<WelcomeViewModelErrors> = MutableLiveData()

    ///TODO: delete this method
    fun initViewModel() {
        println("view model")
        val result = getAllCountersUseCase()

    }
}

enum class WelcomeViewModelErrors {
    NETWORK_ERROR
}
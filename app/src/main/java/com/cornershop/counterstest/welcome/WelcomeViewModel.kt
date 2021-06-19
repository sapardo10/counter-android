package com.cornershop.counterstest.welcome

import androidx.lifecycle.ViewModel
import com.cornershop.domain.GetAllCountersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val getAllCountersUseCase: GetAllCountersUseCase
): ViewModel() {

    ///TODO: delete this method
    fun test() {
        println("view model")
        getAllCountersUseCase()
    }
}
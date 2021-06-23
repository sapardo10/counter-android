package com.cornershop.counterstest.features.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.counterstest.utils.SingleLiveEvent
import com.cornershop.data.models.CounterError
import com.cornershop.data.models.Result
import com.cornershop.domain.ICreateCounterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val createCounterUseCase: ICreateCounterUseCase
) : ViewModel() {

    val actions: SingleLiveEvent<CreateViewModelActions> = SingleLiveEvent()
    private var newCounterTitle: String = ""

    fun createCounter() {
        actions.postValue(CreateViewModelActions.SHOW_CREATING_LOADING)
        viewModelScope.launch {
            if (newCounterTitle.isBlank()) {
                actions.postValue(CreateViewModelActions.SHOW_COUNTER_EMPTY_ERROR)
                delay(5000)
                actions.postValue(CreateViewModelActions.HIDE_COUNTER_EMPTY_ERROR)
            } else {
                when (val result = createCounterUseCase(title = newCounterTitle)) {
                    is Result.Success -> {
                        actions.postValue(CreateViewModelActions.NAVIGATE_BACK)
                    }
                    is Result.Failure -> {
                        if (result.error == CounterError.NETWORK_ERROR) {
                            actions.postValue(CreateViewModelActions.SHOW_NETWORK_ERROR)
                        }
                    }
                }
            }
            delay(100)
            actions.postValue(CreateViewModelActions.HIDE_CREATING_LOADING)
        }
    }

    fun onSeeExamplesClicked() {
        actions.postValue(CreateViewModelActions.GO_TO_EXAMPLES_SCREEN)
    }

    fun onTextChanged(newText: String) {
        newCounterTitle = newText
    }
}

enum class CreateViewModelActions {
    GO_TO_EXAMPLES_SCREEN,
    HIDE_COUNTER_EMPTY_ERROR,
    HIDE_CREATING_LOADING,
    NAVIGATE_BACK,
    SHOW_COUNTER_EMPTY_ERROR,
    SHOW_CREATING_LOADING,
    SHOW_NETWORK_ERROR
}
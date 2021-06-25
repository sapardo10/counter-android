package com.cornershop.counterstest.features.create

import androidx.annotation.VisibleForTesting
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

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var newCounterTitle: String = ""

    /**
     * ------------------------------------- PUBLIC METHODS ----------------------------------------
     */

    /**
     * Method that performs all the validations to create a counter with the name given by the user.
     * The name of the counter should not be blank.
     */
    fun createCounter() {
        actions.postValue(CreateViewModelActions.SHOW_CREATING_LOADING)
        viewModelScope.launch {
            if (newCounterTitle.isBlank()) {
                actions.postValue(CreateViewModelActions.SHOW_COUNTER_EMPTY_ERROR)
                ///Allow the user 5 seconds to see the error
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

    /**
     * Method called when the user clicks on the see suggestions text
     */
    fun onSeeExamplesClicked() {
        actions.postValue(CreateViewModelActions.GO_TO_EXAMPLES_SCREEN)
    }

    /**
     * Method called when the user types or changes the text displayed on the text field
     * @param newText [String] text after the change
     */
    fun onTextChanged(newText: String) {
        newCounterTitle = newText
    }

    /**
     * Method that should trigger the view to display the soft keyboard
     */
    fun showSoftKeyboard() {
        viewModelScope.launch {
            delay(500)
            actions.postValue(CreateViewModelActions.SHOW_SOFT_KEYBOARD)
        }
    }
}

enum class CreateViewModelActions {
    GO_TO_EXAMPLES_SCREEN,
    HIDE_COUNTER_EMPTY_ERROR,
    HIDE_CREATING_LOADING,
    NAVIGATE_BACK,
    SHOW_COUNTER_EMPTY_ERROR,
    SHOW_CREATING_LOADING,
    SHOW_NETWORK_ERROR,
    SHOW_SOFT_KEYBOARD
}
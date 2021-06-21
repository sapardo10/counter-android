package com.cornershop.counterstest.features.main.list

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.data.models.Counter
import com.cornershop.data.models.Result
import com.cornershop.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainListViewModel @Inject constructor(
    private val decreaseCounterUseCase: IDecreaseCounterUseCase,
    private val getAllCountersUseCase: IGetAllCountersUseCase,
    private val increaseCounterUseCase: IIncreaseCounterUseCase,
): ViewModel() {

    val countersViewModel = MutableLiveData<List<CounterViewModel>>()
    var deletionMode = false
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val selectedCounters = mutableListOf<Counter>()

    /**
     * -------------------------------------- PUBLIC METHODS ---------------------------------------
     */

    /**
     * Method that initializes the logic when the view is loaded
     * 1. Starts listening to the list of counters for the user to show them on the list.
     */
    fun initializeView() {
        viewModelScope.launch {
            getAllCountersUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        result.data
                            .map { mapCounterToCounterViewModel(it) }
                            .let { data -> countersViewModel.postValue(data) }
                    }
                    else -> {
                        countersViewModel.postValue(listOf())
                    }
                }
            }
        }
    }

    /**
     * ------------------------------------- PRIVATE METHODS ---------------------------------------
     */

    /**
     * Method that transforms a [Counter] into a [CounterViewModel]
     * @param counter [Counter] to be converted
     * @return [CounterViewModel] from the [Counter] passed as parameter
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun mapCounterToCounterViewModel(counter: Counter): CounterViewModel {
        return CounterViewModel(
            counter = counter,
            isSelected = { isItemSelected(counter) },
            onCheckTapped = { onItemSelectionTapped(counter) },
            onLongTap = { onItemLongTap(counter) },
            onMinusTapped = { onItemMinusTap(counter) },
            onPlusTapped = { onItemPlusTap(counter) }
        )
    }

    /**
     * Method called when the user taps on the minus on any item
     * @param counter [Counter] representing the item the user tap minus into
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun onItemMinusTap(counter: Counter) {
        viewModelScope.launch { decreaseCounterUseCase(counterToDecrement = counter) }
    }

    /**
     * Method called when the user long taps  on any item
     * @param counter [Counter] representing the item the user long taps on
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun onItemLongTap(counter: Counter) {
        deletionMode = !deletionMode
        selectedCounters.add(counter)
        countersViewModel.postValue(
            countersViewModel.value
        )
    }

    /**
     * Method called when the user taps on the plus on any item
     * @param counter [Counter] representing the item the user tap plus into
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun onItemPlusTap(counter: Counter) {
        viewModelScope.launch { increaseCounterUseCase(counterToIncrement = counter) }
    }

    /**
     * Method that tells if an item has been already selected or not
     * @param counter [Counter] that wants to be checked
     * @return [Boolean] true if the item has been selected, false otherwise
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun isItemSelected(counter: Counter): Boolean {
        return selectedCounters.contains(counter)
    }

    /**
     * Method called when the user is on delete mode on the screen and wants to select or deselect
     * any of the items on the screen
     * @param counter [Counter] the user wants to select or deselect
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun onItemSelectionTapped(counter: Counter) {
        if(selectedCounters.contains(counter)) {
            selectedCounters.remove(counter)
        } else {
            selectedCounters.add(counter)
        }

        deletionMode = selectedCounters.isNotEmpty()

        countersViewModel.postValue(
            countersViewModel.value
        )
    }
}
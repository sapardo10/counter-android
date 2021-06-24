package com.cornershop.counterstest.features.main.list

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.data.models.Counter
import com.cornershop.data.models.CounterError
import com.cornershop.data.models.Result
import com.cornershop.domain.IDecreaseCounterUseCase
import com.cornershop.domain.IDeleteMultipleCounterUseCase
import com.cornershop.domain.IGetAllCountersUseCase
import com.cornershop.domain.IIncreaseCounterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainListViewModel @Inject constructor(
    private val decreaseCounterUseCase: IDecreaseCounterUseCase,
    private val deleteMultipleCounterUseCase: IDeleteMultipleCounterUseCase,
    private val getAllCountersUseCase: IGetAllCountersUseCase,
    private val increaseCounterUseCase: IIncreaseCounterUseCase,
) : ViewModel() {

    val actions = MutableLiveData<MainListViewModelActions>()
    val countersViewModel = MutableLiveData<List<CounterViewModel>>()
    var deletionMode = MutableLiveData(false)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var searchText: String = ""

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val selectedCounters = mutableListOf<Counter>()

    /**
     * -------------------------------------- PUBLIC METHODS ---------------------------------------
     */

    fun showDeleteRationale() {
        actions.postValue(MainListViewModelActions.ShowDeleteRationaleDialog(
            isBatchDelete = selectedCounters.size > 1,
            counterName = selectedCounters.firstOrNull()?.name ?: "",
            countersAmount = selectedCounters.size,
            onConfirm = {
                deleteItems()
            }
        ))
    }

    /**
     * Method that deletes the items selected by the user
     */
    fun deleteItems() {
        viewModelScope.launch {
            val deletionFailures = deleteMultipleCounterUseCase(selectedCounters)
            if (deletionFailures.isEmpty()) {
                selectedCounters.clear()
                deletionMode.postValue(false)
            } else {
                actions.postValue(
                    MainListViewModelActions.ShowDialogDeleteNetworkError
                )
            }
        }
    }

    /**
     * Method that filters the [List] of [CounterViewModel] given the current parameters
     * @param viewModels [List] of [CounterViewModel] to be filtered
     * @return [List] of [CounterViewModel] after being filtered
     */
    fun filterCountersViewModels(viewModels: List<CounterViewModel>): List<CounterViewModel> {
        return viewModels
            .filter { it.counter.name.contains(searchText, ignoreCase = true) }
    }

    /**
     * Method that initializes the logic when the view is loaded
     * 1. Starts listening to the list of counters for the user to show them on the list.
     */
    fun initializeView() {
        actions.postValue(MainListViewModelActions.ShowNormalList)
        countersViewModel.postValue(listOf())
        viewModelScope.launch {
            getAllCountersUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        result.data
                            .map { mapCounterToCounterViewModel(it) }
                            .filter { it.counter.name.contains(searchText, ignoreCase = true) }
                            .let { data -> countersViewModel.postValue(data) }
                    }
                    else -> {
                        if (countersViewModel.value?.isNotEmpty() == true) {
                            actions.postValue(MainListViewModelActions.ShowNormalList)
                        } else {
                            actions.postValue(MainListViewModelActions.ShowNetworkError)
                        }
                        countersViewModel.postValue(listOf())
                    }
                }
            }
        }
    }

    /**
     * Method called when the user changes the text on the search field
     * @param newText [String] representing the new search text entered by the user, if empty
     * it will disappear the search filter
     */
    fun onSearchTextChanged(newText: String) {
        searchText = newText
        countersViewModel.postValue(
            countersViewModel.value
        )
    }

    /**
     * Method called when the user wishes to share the selected item
     */
    fun shareItems() {
        actions.postValue(
            MainListViewModelActions.ShowShareBottomSheet(counters = selectedCounters)
        )
    }

    /**
     * ------------------------------------- PRIVATE METHODS ---------------------------------------
     */

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
        viewModelScope.launch {
            val result = decreaseCounterUseCase(counterToDecrement = counter)
            if (result is Result.Failure && result.error == CounterError.NETWORK_ERROR) {
                actions.postValue(MainListViewModelActions.ShowDialogUpdateNetworkError(
                    counterName = counter.name,
                    counterNewValue = counter.count - 1,
                    retryAction = {
                        onItemPlusTap(counter)
                    }
                ))
            }
        }
    }

    /**
     * Method called when the user long taps  on any item
     * @param counter [Counter] representing the item the user long taps on
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun onItemLongTap(counter: Counter) {
        selectedCounters.add(counter)
        deletionMode.postValue(deletionMode.value != true)
        countersViewModel.postValue(countersViewModel.value)
    }

    /**
     * Method called when the user taps on the plus on any item
     * @param counter [Counter] representing the item the user tap plus into
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun onItemPlusTap(counter: Counter) {
        viewModelScope.launch {
            val result = increaseCounterUseCase(counterToIncrement = counter)
            if (result is Result.Failure && result.error == CounterError.NETWORK_ERROR) {
                actions.postValue(MainListViewModelActions.ShowDialogUpdateNetworkError(
                    counterName = counter.name,
                    counterNewValue = counter.count + 1,
                    retryAction = {
                        onItemPlusTap(counter)
                    }
                ))
            }
        }
    }

    /**
     * Method called when the user is on delete mode on the screen and wants to select or deselect
     * any of the items on the screen
     * @param counter [Counter] the user wants to select or deselect
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun onItemSelectionTapped(counter: Counter) {
        if (selectedCounters.contains(counter)) {
            selectedCounters.remove(counter)
        } else {
            selectedCounters.add(counter)
        }

        deletionMode.postValue(selectedCounters.isNotEmpty())
        countersViewModel.postValue(countersViewModel.value)
    }
}

sealed class MainListViewModelActions {
    object ShowNormalList : MainListViewModelActions()
    object ShowNetworkError : MainListViewModelActions()
    object ShowDialogDeleteNetworkError : MainListViewModelActions()
    data class ShowDialogUpdateNetworkError(
        val counterName: String,
        val counterNewValue: Int,
        val retryAction: () -> Unit
    ) : MainListViewModelActions()

    data class ShowDeleteRationaleDialog(
        val isBatchDelete: Boolean,
        val counterName: String,
        val countersAmount: Int,
        val onConfirm: () -> Unit
    ) : MainListViewModelActions()

    data class ShowShareBottomSheet(val counters: List<Counter>) : MainListViewModelActions()
}
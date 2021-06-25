package com.cornershop.counterstest.features.main.list

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.FragmentMainListBinding
import com.cornershop.counterstest.utils.buildDialog
import com.cornershop.counterstest.utils.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainListFragment : Fragment() {

    private val viewModel: MainListViewModel by viewModels()

    private var _binding: FragmentMainListBinding? = null
    private val binding get() = _binding!!

    /**
     * ------------------------------------ LIFECYCLE METHODS --------------------------------------
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainListBinding.inflate(inflater, container, false)
        configureCounterList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeObservers()
        initializeInteractionsListener()
        configureSwipeRefreshStyle()
        viewModel.initializeView()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onSearchTextChanged("")
        viewModel.closeDeleteToolbar()
    }


    /**
     * -------------------------------------- PRIVATE METHODS --------------------------------------
     */

    /**
     * Method that configures the recycler view on the activity to be able to show the counter
     * items
     */
    private fun configureCounterList() {
        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            adapter =
                CounterListRecyclerViewAdapter(
                    deletionMode = viewModel.viewState.value == MainViewState.DELETE_STATE,
                    listOf()
                )
        }
    }

    /**
     * Method that sets the loader color for the swipe refresh animations
     */
    private fun configureSwipeRefreshStyle() {
        binding.swipeRefresh.setColorSchemeResources(R.color.orange)
    }


    /**
     * Method that sets all the necessary characteristics and listeners to the search view on the
     * toolbar so the user can interact normally with it.
     * @param searchView [SearchView] that will be configured
     * @param searchManager [SearchManager] that handles the search on the view
     */
    private fun configureToolbarSearchView(
        searchView: SearchView,
        searchManager: SearchManager?
    ) {
        with(searchView) {
            setQuery(viewModel.searchText, false)
            queryHint = getString(R.string.search_counters)
            setIconifiedByDefault(false)
            setOnQueryTextFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    showInputMethod(view.findFocus())
                }
            }
            setSearchableInfo(searchManager!!.getSearchableInfo(activity?.componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    if (query != null) {
                        viewModel.onSearchTextChanged(query)
                    }
                    return true
                }
            })
            searchView.requestFocus()
        }
    }

    /**
     * Method that initializes the toolbar of the delete mode
     */
    private fun initializeDeleteToolbar() {
        with(binding.toolbar) {
            menu.clear()
            inflateMenu(R.menu.toolbar_delete_mode)
            setNavigationIcon(R.drawable.ic_close)
            setNavigationOnClickListener { viewModel.closeDeleteToolbar() }
            setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.ic_delete) {
                    viewModel.showDeleteRationale()
                } else if (menuItem.itemId == R.id.ic_share) {
                    viewModel.shareItems()
                }
                return@setOnMenuItemClickListener true
            }
        }
    }

    /**
     * Method that initializes the interactions the view can have
     */
    private fun initializeInteractionsListener() {
        initializeTextFieldInteractions()
        with(binding) {
            swipeRefresh.setOnRefreshListener {
                viewModel.initializeView()
            }
            retryButton.setOnSingleClickListener {
                viewModel.initializeView()
            }
        }
    }

    /**
     * Method that initializes the toolbar of the delete mode
     */
    private fun initializeSearchToolbar() {
        with(binding.toolbar) {
            menu.clear()
            inflateMenu(R.menu.toolbar_search_mode)
            title = ""
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener { viewModel.closeDeleteToolbar() }
            val searchManager: SearchManager? =
                context?.getSystemService(Context.SEARCH_SERVICE) as? SearchManager
            val searchView: SearchView = menu.findItem(R.id.menu_search).actionView as SearchView
            configureToolbarSearchView(searchView, searchManager)
        }
    }

    /**
     * Method that initializes the interactions the EditText on the view can have
     */
    private fun initializeTextFieldInteractions() {
        with(binding.searchEditText) {
            isFocusable = false
            isClickable = true
            setOnClickListener {
                viewModel.viewState.postValue(MainViewState.SEARCH_STATE)
            }
        }
    }

    /**
     * Method that initializes the observers of the view
     */
    private fun initializeObservers() {
        initializeObserverOverActions()
        initializeObserverOverCounterViewModels()
        initializeObserverOverViewState()
    }

    /**
     * Method that sets the observer of the view over the actions component of the view model
     */
    private fun initializeObserverOverActions() {
        viewModel.actions.observe(viewLifecycleOwner, { nullableAction ->
            nullableAction?.let { action ->
                when (action) {
                    is MainListViewModelActions.ShowNormalList -> showNormalList()
                    is MainListViewModelActions.ShowNetworkError -> showNetworkError()
                    is MainListViewModelActions.ShowDialogUpdateNetworkError -> showUpdateNetworkError(
                        action
                    )
                    is MainListViewModelActions.ShowShareBottomSheet -> showShareBottomSheet(action)
                    is MainListViewModelActions.ShowDialogDeleteNetworkError -> showDeleteNetworkError()
                    is MainListViewModelActions.ShowDeleteRationaleDialog -> showDeleteCounterRationale(
                        action
                    )
                }
            }
        })
    }

    /**
     * Method that initializes the observer over the counter view models on the view model
     */
    private fun initializeObserverOverCounterViewModels() {
        viewModel.countersViewModel.observe(viewLifecycleOwner, { list ->
            list?.let {
                with(binding) {
                    swipeRefresh.isRefreshing = false
                    updateList(
                        deletionMode = viewModel.viewState.value == MainViewState.DELETE_STATE,
                        list = viewModel.countersViewModel.value
                    )
                }
            }
        })
    }

    /**
     * Method that sets the observer of the view over the view state component of the view model
     */
    private fun initializeObserverOverViewState() {
        viewModel.viewState.observe(viewLifecycleOwner, {
            it?.let { viewState ->
                when (viewState) {
                    MainViewState.NORMAL_STATE -> showStateNormal(viewState)
                    MainViewState.DELETE_STATE -> showStateDelete(viewState)
                    MainViewState.SEARCH_STATE -> showStateSearch()
                }
            }
        })
    }

    /**
     * Method that shows a dialog to make sure the user wants to delete all selected items
     * @param action [MainListViewModelActions.ShowDeleteRationaleDialog] that triggered the showing
     * of the dialog
     */
    private fun showDeleteCounterRationale(action: MainListViewModelActions.ShowDeleteRationaleDialog) {
        val title = if (action.isBatchDelete) {
            resources.getQuantityString(
                R.plurals.delete_x_amount_of_counters_questions,
                action.countersAmount,
                action.countersAmount
            )
        } else {
            getString(
                R.string.delete_x_question,
                action.counterName
            )
        }
        val dialog = activity?.buildDialog(
            negativeButtonText = getString(R.string.cancel),
            title = title,
            onPositiveClicked = action.onConfirm,
            positiveButtonText = getString(R.string.delete),
        )
        dialog?.show()
    }

    /**
     * Method that shows a dialog informing the user that there was a network error and the delete
     * action could not be completed
     */
    private fun showDeleteNetworkError() {
        val dialog = activity?.buildDialog(
            message = getString(R.string.connection_error_description),
            negativeButtonText = getString(R.string.ok),
            title = getString(
                R.string.error_deleting_counter_title,
            )
        )
        dialog?.show()
    }

    /**
     * Method that shows the UI needed to display a list of counters
     */
    private fun showNormalList() {
        binding.failInternetConnectionGroup.visibility = View.GONE
        binding.normalListGroup.visibility = View.VISIBLE
    }

    /**
     * Method that shows the UI needed to display a network error
     */
    private fun showNetworkError() {
        binding.normalListGroup.visibility = View.GONE
        binding.failInternetConnectionGroup.visibility = View.VISIBLE
    }

    /**
     * Method that displays the share bottom sheet with the info of all counters selected
     * @param action [MainListViewModelActions.ShowDeleteRationaleDialog] that triggered the showing
     * of the dialog
     */
    private fun showShareBottomSheet(action: MainListViewModelActions.ShowShareBottomSheet) {
        val textToShareBuffer = StringBuffer()
        for ((i, counter) in action.counters.withIndex()) {
            textToShareBuffer.append(
                String.format(
                    getString(R.string.n_per_counter_name),
                    counter.count,
                    counter.name
                )
            )
            if (i < action.counters.size - 1) {
                textToShareBuffer.append("\n")
            }
        }
        val sendIntent: Intent = Intent().apply {
            this.action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                textToShareBuffer.toString()
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    /**
     * Method that modifies the UI so it is on the delete state
     */
    private fun showStateDelete(viewState: MainViewState) {
        initializeDeleteToolbar()
        binding.toolbar.visibility = View.VISIBLE
        binding.searchEditText.visibility = View.GONE

        updateList(
            deletionMode = viewState == MainViewState.DELETE_STATE,
            list = viewModel.countersViewModel.value
        )
    }

    /**
     * Method that modifies the UI so it is on the normal state
     */
    private fun showStateNormal(viewState: MainViewState) {
        binding.toolbar.visibility = View.GONE
        binding.searchEditText.visibility = View.VISIBLE
        binding.searchEditText.setText(
            viewModel.searchText,
            TextView.BufferType.EDITABLE
        )

        updateList(
            deletionMode = viewState == MainViewState.DELETE_STATE,
            list = viewModel.countersViewModel.value
        )
    }

    /**
     * Method that modifies the UI so it is on the search state
     */
    private fun showStateSearch() {
        initializeSearchToolbar()
        binding.toolbar.visibility = View.VISIBLE
        binding.searchEditText.visibility = View.GONE
    }

    /**
     * Method that shows a dialog informing the user that there was a network error and the update
     * action could not be completed
     */
    private fun showUpdateNetworkError(action: MainListViewModelActions.ShowDialogUpdateNetworkError) {
        val dialog = activity?.buildDialog(
            message = getString(R.string.connection_error_description),
            negativeButtonText = getString(R.string.dismiss),
            onPositiveClicked = action.retryAction,
            positiveButtonText = getString(R.string.retry),
            title = String.format(
                getString(R.string.error_updating_counter_title),
                action.counterName,
                action.counterNewValue
            )
        )
        dialog?.show()
    }

    /**
     * Method that displays the soft keyboard focused on the [View] passed as parameter
     * @param view [View] that wants to be focused
     */
    private fun showInputMethod(view: View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
        imm?.showSoftInput(view, 0)
    }

    /**
     * Method that updates the labels of amount of items, and total counter count.
     * @param list [List] of [CounterViewModel] that will be shown on the screen
     */
    private fun updateLabels(list: List<CounterViewModel>) {
        with(binding) {
            var count = 0
            for (viewModel in list) count += viewModel.counter.count
            nItemsLabel.text = resources.getQuantityString(R.plurals.n_items, list.size, list.size)
            nTimesLabel.text = resources.getQuantityString(R.plurals.n_times, count, count)
            binding.toolbar.title = resources.getQuantityString(
                R.plurals.n_selected,
                viewModel.selectedCounters.size,
                viewModel.selectedCounters.size
            )
        }
    }

    /**
     * Method that updates the list of items
     * @param deletionMode [Boolean] if true it means the user is deleting some items, false
     * otherwise
     * @param list [List] of [CounterViewModel] to be shown
     */
    private fun updateList(deletionMode: Boolean, list: List<CounterViewModel>?) {
        val finalList = list ?: listOf()
        val filteredList = viewModel.filterCountersViewModels(
            finalList
        )
        binding.list.adapter = CounterListRecyclerViewAdapter(
            deletionMode = deletionMode,
            filteredList
        )
        updateLabels(filteredList)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainListFragment()
    }
}
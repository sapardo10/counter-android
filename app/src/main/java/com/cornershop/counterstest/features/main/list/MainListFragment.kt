package com.cornershop.counterstest.features.main.list

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.FragmentMainListBinding
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
        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            adapter =
                CounterListRecyclerViewAdapter(
                    deletionMode = viewModel.deletionMode.value == true,
                    listOf()
                )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeObservers()
        initializeInteractionsListener()
        binding.swipeRefresh.setColorSchemeResources(R.color.orange)
        initializeToolbar()
        viewModel.initializeView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * -------------------------------------- PRIVATE METHODS --------------------------------------
     */

    /**
     * Method that initializes the interactions the view can have
     */
    private fun initializeInteractionsListener() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                ///Do nothing, it does not interest the application so far
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onSearchTextChanged(newText = s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                ///Do nothing, it does not interest the application so far
            }
        })
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.initializeView()
        }
        binding.retryButton.setOnSingleClickListener {
            viewModel.initializeView()
        }
    }

    /**
     * Method that initializes the observers of the view
     */
    private fun initializeObservers() {
        viewModel.countersViewModel.observe(viewLifecycleOwner, { list ->
            list?.let {
                with(binding) {
                    swipeRefresh.isRefreshing = false
                    updateList(
                        deletionMode = viewModel.deletionMode.value == true,
                        list = viewModel.countersViewModel.value
                    )
                }
            }
        })
        viewModel.actions.observe(viewLifecycleOwner, {
            it?.let { action ->
                when (action) {
                    MainListViewModelActions.ShowNormalList -> {
                        binding.failInternetConnectionGroup.visibility = View.GONE
                        binding.normalListGroup.visibility = View.VISIBLE
                    }
                    MainListViewModelActions.ShowNetworkError -> {
                        binding.normalListGroup.visibility = View.GONE
                        binding.failInternetConnectionGroup.visibility = View.VISIBLE
                    }
                    is MainListViewModelActions.ShowDialogNetworkError -> {
                        //TODO: show network error on dialog
                    }
                    is MainListViewModelActions.ShowShareBottomSheet -> {
                        val counterToShare = action.counter
                        val sendIntent: Intent = Intent().apply {
                            this.action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "${counterToShare.count} x ${counterToShare.name}"
                            )
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(sendIntent, null)
                        startActivity(shareIntent)
                    }
                }
            }
        })
        viewModel.deletionMode.observe(viewLifecycleOwner, {
            it?.let { deletionMode ->
                with(binding) {
                    searchEditText.visibility = if (deletionMode) View.GONE else View.VISIBLE
                    toolbar.visibility = if (deletionMode) View.VISIBLE else View.GONE
                    updateList(
                        deletionMode = deletionMode,
                        list = viewModel.countersViewModel.value
                    )
                }
            }
        })
    }

    /**
     * Method that initializes the toolbar of the delete mode
     */
    private fun initializeToolbar() {
        binding.toolbar.inflateMenu(R.menu.toolbar_delete_mode)
        binding.toolbar.setNavigationIcon(R.drawable.ic_close)
        binding.toolbar.setNavigationOnClickListener { viewModel.deletionMode.postValue(false) }
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.ic_delete) {
                viewModel.deleteItems()
            } else if (menuItem.itemId == R.id.ic_share) {
                viewModel.shareItems()
            }
            return@setOnMenuItemClickListener true
        }
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
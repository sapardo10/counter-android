package com.cornershop.counterstest.features.main.list

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.FragmentMainListBinding
import com.cornershop.counterstest.features.main.list.MainListViewModelActions.SHOW_NETWORK_ERROR
import com.cornershop.counterstest.features.main.list.MainListViewModelActions.SHOW_NORMAL_LIST
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
                CounterListRecyclerViewAdapter(deletionMode = viewModel.deletionMode, listOf())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeObservers()
        viewModel.initializeView()
        initializeInteractionsListener()
        binding.swipeRefresh.setColorSchemeResources(R.color.orange)
        initializeToolbar()
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
                binding.swipeRefresh.isRefreshing = false
                val filteredList = viewModel.filterCountersViewModels(list)
                binding.list.adapter =
                    CounterListRecyclerViewAdapter(
                        deletionMode = viewModel.deletionMode,
                        filteredList
                    )
                updateLabels(filteredList)
            }
        })
        viewModel.actions.observe(viewLifecycleOwner, {
            it?.let { action ->
                when (action) {
                    SHOW_NORMAL_LIST -> {
                        binding.failInternetConnectionGroup.visibility = View.GONE
                        binding.normalListGroup.visibility = View.VISIBLE
                    }
                    SHOW_NETWORK_ERROR -> {
                        binding.normalListGroup.visibility = View.GONE
                        binding.failInternetConnectionGroup.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    /**
     * Method that initiliazes the toolbar of the delete mode
     */
    private fun initializeToolbar() {
        binding.toolbar.inflateMenu(R.menu.toolbar_delete_mode)
        val menu: Menu = binding.toolbar.menu
    }

    /**
     * Method that updates the labels of amount of items, and total counter count.
     * @param list [List] of [CounterViewModel] that will be shown on the screen
     */
    private fun updateLabels(list: List<CounterViewModel>) {
        with(binding) {
            var count = 0
            list.forEach { count += it.counter.count }
            nItemsLabel.text = getString(R.string.n_items, list.size)
            nTimesLabel.text = getString(R.string.n_times, count)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainListFragment()
    }
}
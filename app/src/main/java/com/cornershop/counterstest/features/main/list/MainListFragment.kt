package com.cornershop.counterstest.features.main.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.FragmentMainListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainListFragment : Fragment() {

    private val viewModel: MainListViewModel by viewModels()

    private var _binding: FragmentMainListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainListBinding.inflate(inflater, container, false)
        val view = binding.root
        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            adapter =
                CounterListRecyclerViewAdapter(deletionMode = viewModel.deletionMode, listOf())
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.countersViewModel.observe(viewLifecycleOwner, { list ->
            list?.let {
                binding.list.adapter =
                    CounterListRecyclerViewAdapter(deletionMode = viewModel.deletionMode, it)
                updateLabels(it)
            }
        })
        viewModel.initializeView()
    }

    private fun updateLabels(list: List<CounterViewModel>) {
        with(binding) {
            var count = 0
            list.forEach { count += it.counter.count }
            nItemsLabel.text = getString(R.string.n_items, list.size)
            nTimesLabel.text = getString(R.string.n_times, count)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainListFragment()
    }
}
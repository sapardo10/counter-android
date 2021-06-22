package com.cornershop.counterstest.features.main.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.databinding.FragmentCounterItemBinding
import com.cornershop.counterstest.utils.setOnSingleClickListener

class CounterListRecyclerViewAdapter(
    val deletionMode: Boolean,
    private val values: List<CounterViewModel>
) : RecyclerView.Adapter<CounterListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            FragmentCounterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(private val binding: FragmentCounterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: CounterViewModel) {
            with(binding) {
                countText.text = viewModel.counter.count.toString()
                nameLabel.text = viewModel.counter.name
                if (deletionMode) {
                    normalActionsGroup.visibility = View.GONE
                    val visibilitySelected = if (viewModel.isSelected()) View.VISIBLE else View.GONE
                    selectedIcon.visibility = visibilitySelected
                    itemOverlay.visibility = visibilitySelected
                    root.setOnSingleClickListener {
                        viewModel.onCheckTapped()
                    }
                    root.setOnLongClickListener(null)
                } else {
                    normalActionsGroup.visibility = View.VISIBLE
                    selectedIcon.visibility = View.GONE
                    itemOverlay.visibility = View.GONE

                    minusIcon.setOnSingleClickListener {
                        viewModel.onMinusTapped()
                    }

                    plusIcon.setOnSingleClickListener {
                        viewModel.onPlusTapped()
                    }

                    root.setOnSingleClickListener(null)
                    root.setOnLongClickListener {
                        viewModel.onLongTap()
                        return@setOnLongClickListener true
                    }

                }


                normalActionsGroup.visibility = if (deletionMode) View.GONE else View.VISIBLE


            }
        }
    }
}
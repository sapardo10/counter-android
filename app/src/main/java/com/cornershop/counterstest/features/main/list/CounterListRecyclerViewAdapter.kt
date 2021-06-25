package com.cornershop.counterstest.features.main.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.databinding.FragmentCounterItemBinding
import com.cornershop.counterstest.utils.autoNotify
import com.cornershop.counterstest.utils.setOnSingleClickListener
import kotlin.properties.Delegates

class CounterListRecyclerViewAdapter() :
    RecyclerView.Adapter<CounterListRecyclerViewAdapter.ViewHolder>() {

    /**
     * ------------------------------------- PUBLIC METHODS ----------------------------------------
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            FragmentCounterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    var deletionMode: Boolean by Delegates.observable(false) { _, oldValue, newValue ->
        notifyDataSetChanged()
    }

    var items: List<CounterViewModel> by Delegates.observable(emptyList()) { _, oldList, newList ->
        autoNotify(oldList, newList) { o, n -> o.counter.id == n.counter.id }
    }

    /**
     * [ViewHolder] from [CounterListRecyclerViewAdapter]
     */
    inner class ViewHolder(private val binding: FragmentCounterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Method used when the view is being load with new information to set the necessary UI give
         * the [CounterViewModel]
         */
        fun bind(viewModel: CounterViewModel) {
            with(binding) {
                countText.text = viewModel.counter.count.toString()
                nameLabel.text = viewModel.counter.name
                if (deletionMode) {
                    configureDeletionModeItem(viewModel)
                } else {
                    configureNormalModeItem(viewModel)
                }
                normalActionsGroup.visibility = if (deletionMode) View.GONE else View.VISIBLE
            }
        }

        /**
         * Method that paints the item and configures it as it is needed when the user is seeing
         * and interacting normally items
         * @param viewModel [CounterViewModel] of the item to be shown
         */
        private fun configureNormalModeItem(
            viewModel: CounterViewModel
        ) {
            with(binding) {
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
        }

        /**
         * Method that paints the item and configures it as it is needed when the user is deleting
         * items
         * @param viewModel [CounterViewModel] of the item to be shown
         */
        private fun configureDeletionModeItem(
            viewModel: CounterViewModel
        ) {
            with(binding) {
                normalActionsGroup.visibility = View.GONE
                val visibilitySelected = if (viewModel.isSelected()) View.VISIBLE else View.GONE
                selectedIcon.visibility = visibilitySelected
                itemOverlay.visibility = visibilitySelected
                root.setOnSingleClickListener {
                    viewModel.onCheckTapped()
                }
                root.setOnLongClickListener(null)
            }
        }
    }
}
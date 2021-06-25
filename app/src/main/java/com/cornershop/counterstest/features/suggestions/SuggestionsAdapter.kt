package com.cornershop.counterstest.features.suggestions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.databinding.SuggestionItemBinding


class SuggestionsAdapter(
    val suggestions: List<SuggestionItemViewModel>
) : RecyclerView.Adapter<SuggestionsAdapter.ViewHolder>() {

    /**
     * ------------------------------------- PUBLIC METHODS ----------------------------------------
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            SuggestionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = suggestions[position]
        holder.bind(item)
    }

    override fun getItemCount() = suggestions.size

    /**
     * [ViewHolder] from [SuggestionsAdapter]
     */
    inner class ViewHolder(private val binding: SuggestionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Method used when the view is being load with new information to set the necessary UI give
         * the [SuggestionItemViewModel]
         */
        fun bind(item: SuggestionItemViewModel) {
            with(binding) {
                root.text = item.name
                root.setOnClickListener {
                    item.onTap()
                }
            }
        }

    }
}
package com.cornershop.counterstest.features.suggestions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.databinding.SuggestionsCategoryItemBinding
import com.cornershop.counterstest.utils.SpacesItemDecoration

class SuggestionsCategoryAdapter(
    private val suggestionsCategories: List<CategorySuggestionViewModel>
) : RecyclerView.Adapter<SuggestionsCategoryAdapter.ViewHolder>() {

    /**
     * ------------------------------------- PUBLIC METHODS ----------------------------------------
     */

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SuggestionsCategoryAdapter.ViewHolder {
        val itemBinding =
            SuggestionsCategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SuggestionsCategoryAdapter.ViewHolder, position: Int) {
        val item = suggestionsCategories[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = suggestionsCategories.size

    /**
     * [ViewHolder] from [SuggestionsCategoryAdapter]
     */
    inner class ViewHolder(private val binding: SuggestionsCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Method used when the view is being load with new information to set the necessary UI give
         * the [CategorySuggestionViewModel]
         */
        fun bind(item: CategorySuggestionViewModel) {
            with(binding) {
                val context = root.context
                categoryName.text = item.title
                with(list) {
                    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    adapter = SuggestionsAdapter(item.suggestions)
                    addItemDecoration(
                        SpacesItemDecoration(
                            16,
                            RecyclerView.HORIZONTAL
                        )
                    )
                }
            }
        }
    }
}
package com.cornershop.counterstest.features.suggestions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.databinding.SuggestionsCategoryItemBinding

class SuggestionsCategoryAdapter(
    private val suggestionsCategories: List<CategorySuggestionViewModel>
) : RecyclerView.Adapter<SuggestionsCategoryAdapter.ViewHolder>() {

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

    inner class ViewHolder(private val binding: SuggestionsCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

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
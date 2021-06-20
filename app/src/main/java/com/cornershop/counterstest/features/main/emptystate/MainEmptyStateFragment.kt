package com.cornershop.counterstest.features.main.emptystate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cornershop.counterstest.R

class MainEmptyStateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_empty_state, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainEmptyStateFragment()
    }
}
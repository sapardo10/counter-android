package com.cornershop.counterstest.features.main.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cornershop.counterstest.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainLoadingFragment : Fragment() {

    /**
     * ------------------------------------ LIFECYCLE METHODS --------------------------------------
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_loading, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainLoadingFragment()
    }
}
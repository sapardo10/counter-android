package com.cornershop.counterstest.features.main.loading

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cornershop.counterstest.R

class MainLoadingFragment : Fragment() {

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
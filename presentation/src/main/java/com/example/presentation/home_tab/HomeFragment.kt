package com.example.presentation.home_tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.presentation.BaseFragment
import com.example.presentation.R
import com.example.presentation.databinding.HomeFragmentBinding

class HomeFragment : BaseFragment<HomeFragmentBinding>(R.layout.home_fragment) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}

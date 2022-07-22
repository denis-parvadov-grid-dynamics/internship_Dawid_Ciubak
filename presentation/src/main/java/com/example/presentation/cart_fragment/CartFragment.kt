package com.example.presentation.cart_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.presentation.BaseFragment
import com.example.presentation.R

class CartFragment : BaseFragment(R.layout.cart_fragment) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("cartFragment", "onCreateView: ")
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}

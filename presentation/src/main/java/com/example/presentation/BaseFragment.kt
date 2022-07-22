package com.example.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

abstract class BaseFragment(@LayoutRes private val fragmentRes: Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(fragmentRes, container, false)
    }

    fun navigateTo(@LayoutRes targetFragmentRes: Int) {
        findNavController().navigate(targetFragmentRes)
    }

    fun navigateUp() {
        findNavController().navigateUp()
    }
}

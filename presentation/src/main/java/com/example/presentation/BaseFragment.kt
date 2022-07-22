package com.example.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

abstract class BaseFragment(private val fragmentRes: Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(fragmentRes, container, false)
    }

    fun navigateTo(targetGraph: Int) {
        findNavController().navigate(targetGraph)
    }

    fun navigateUp() {
        findNavController().navigateUp()
    }
}

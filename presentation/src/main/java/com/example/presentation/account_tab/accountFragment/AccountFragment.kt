package com.example.presentation.account_tab.accountFragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.example.presentation.BaseFragment
import com.example.presentation.R
import com.example.presentation.databinding.AccountFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : BaseFragment<AccountFragmentBinding>(R.layout.account_fragment) {
    private val viewModel: AccountFragmentViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
    }
}

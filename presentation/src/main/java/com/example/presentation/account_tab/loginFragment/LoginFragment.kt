package com.example.presentation.account_tab.loginFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.example.domain.common.LoginCredentialsWrapper
import com.example.presentation.BaseFragment
import com.example.presentation.R
import com.example.presentation.databinding.LoginPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginPageBinding>(R.layout.login_page) {

    private val viewModel: LoginFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpNavigationElements()
        setUpLoginButton()
        viewModel.userList.observe(viewLifecycleOwner) {
            Log.i("idk1", "onViewCreated: $it")
            navigateTo(R.id.accountFragment)
        }
    }

    private fun setUpNavigationElements() {
        binding.registerNowTextView.setOnClickListener {
            navigateTo(R.id.registerFragment)
        }
    }

    private fun setUpLoginButton() {
        binding.loginButton.setOnClickListener {
            val username = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.checkForUserWithMatchingCredentials(LoginCredentialsWrapper(username, password))
        }
    }
}

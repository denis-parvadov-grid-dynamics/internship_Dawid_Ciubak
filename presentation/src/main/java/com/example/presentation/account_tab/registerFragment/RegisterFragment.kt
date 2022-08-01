package com.example.presentation.account_tab.registerFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.domain.model.User
import com.example.presentation.BaseFragment
import com.example.presentation.R
import com.example.presentation.databinding.RegisterPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<RegisterPageBinding>(R.layout.register_page) {
    private val viewModel: RegisterFragmentViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpNavigationElements()
        setUpRegistration()
    }

    private fun setUpNavigationElements() {
        binding.orLogIntoYourAccountTextView.setOnClickListener {
            navigateTo(R.id.loginFragment)
        }
    }

    private fun setUpRegistration() {
        binding.createAnAccountButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val firstName = binding.firstNameEditText.text.toString()
            val lastName = binding.lastNameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val user = User(username = email, firstName = firstName, lastName = lastName, password = password)
            viewModel.saveUserInDatabase(user)
            navigateTo(R.id.loginFragment)
        }
    }
}

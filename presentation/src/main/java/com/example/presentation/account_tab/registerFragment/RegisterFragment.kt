package com.example.presentation.account_tab.registerFragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.domain.common.validateAsEmail
import com.example.domain.common.validateAsName
import com.example.domain.common.validateAsPassword
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
        setUpRulesForTextFields()
        viewModel.observableLoggedInUserLiveData.observe(viewLifecycleOwner) {
            navigateTo(R.id.accountFragment)
        }
    }

    private fun setUpNavigationElements() {
        binding.orLogIntoYourAccountTextView.setOnClickListener {
            navigateTo(R.id.loginFragment)
        }
    }

    private fun setUpRulesForTextFields() {
        binding.emailEditText.setOnFocusChangeListener { _, focused ->
            val usersEmailInput = binding.emailEditText.text.toString()
            if (!focused) {
                binding.emailTextInputLayout.helperText = usersEmailInput.validateAsEmail()
            }
        }

        binding.passwordEditText.setOnFocusChangeListener { _, focused ->
            val usersPasswordInput = binding.passwordEditText.text.toString()
            if (!focused) {
                binding.PasswordTextInputLayout.helperText = usersPasswordInput.validateAsPassword()
            }
        }

        binding.firstNameEditText.setOnFocusChangeListener { _, focused ->
            val usersFirstNameInput = binding.firstNameEditText.text.toString()
            if (!focused) {
                binding.firstNameTextInputLayout.helperText = usersFirstNameInput.validateAsName()
            }
        }

        binding.lastNameEditText.setOnFocusChangeListener { _, focused ->
            val usersLastNameInput = binding.lastNameEditText.text.toString()
            if (!focused) {
                binding.lastNameTextInputLayout.helperText = usersLastNameInput.validateAsName()
            }
        }
    }

    private fun setUpRegistration() {
        binding.createAnAccountButton.setOnClickListener {
            requireView().clearFocus() // clear focus from all text input fields

            val isEmailValid = binding.emailTextInputLayout.helperText == null
            val isPasswordValid = binding.PasswordTextInputLayout.helperText == null
            val isFirstNameValid = binding.firstNameTextInputLayout.helperText == null
            val isLastNameValid = binding.lastNameTextInputLayout.helperText == null

            if (isEmailValid && isPasswordValid && isFirstNameValid && isLastNameValid) {

                binding.progressBar.isVisible = true // visual indication for the user

                val email = binding.emailEditText.text.toString()
                val firstName = binding.firstNameEditText.text.toString()
                val lastName = binding.lastNameEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                val user = User(username = email, firstName = firstName, lastName = lastName, password = password)
                viewModel.saveUserInDatabase(user)
                navigateTo(R.id.loginFragment)
            } else {
                Toast.makeText(requireContext(), "Invalid Form Data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

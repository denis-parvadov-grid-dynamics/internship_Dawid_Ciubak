package com.example.presentation.account_tab.loginFragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.domain.common.LoginCredentialsWrapper
import com.example.domain.common.validateAsPassword
import com.example.domain.common.validateAsUsername
import com.example.presentation.BaseFragment
import com.example.presentation.R
import com.example.presentation.databinding.LoginFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginFragmentBinding>(R.layout.login_fragment) {

    private val viewModel: LoginFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpNavigationElements()
        setUpLoginButton()
        setUpRulesForTextFields()
        observeForChangesInLoggedUsers()
    }

    private fun setUpNavigationElements() {
        binding.registerNowTextView.setOnClickListener {
            navigateTo(R.id.registerFragment)
        }
    }

    private fun setUpRulesForTextFields() {
        // validating username
        binding.usernameEditText.setOnFocusChangeListener { _, focused ->
            val usersUsernameInput = binding.usernameEditText.text.toString()
            if (!focused) {
                binding.usernameTextInputLayout.helperText = usersUsernameInput.validateAsUsername()
            }
        }

        // validating password
        binding.passwordEditText.setOnFocusChangeListener { _, focused ->
            val usersPasswordInput = binding.passwordEditText.text.toString()
            if (!focused) {
                binding.passwordTextInputLayout.helperText = usersPasswordInput.validateAsPassword()
            }
        }
    }

    private fun setUpLoginButton() {
        binding.loginButton.setOnClickListener {
            requireView().clearFocus() // clear focus from all text input fields

            val validUsername = binding.usernameTextInputLayout.helperText == null
            val validPassword = binding.passwordTextInputLayout.helperText == null

            if (validUsername && validPassword) {
                changeVisibilityStateOfTheProgressBar() // make the progress bar visible

                val username = binding.usernameEditText.text.toString()
                val password = binding.passwordEditText.text.toString()

                viewModel.loginUser(LoginCredentialsWrapper(username, password))
            } else {
                Toast.makeText(requireContext(), "Invalid Form Data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeForChangesInLoggedUsers() {
        viewModel.observableLoggedInUserLiveData.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                navigateTo(R.id.accountFragment)
            } else {
                changeVisibilityStateOfTheProgressBar() // the progress bar should not be visible
                result.onFailure { throwable ->
                    // display the message to the user
                    displayLoginError(throwable.localizedMessage ?: "")
                }
            }
        }
    }

    private fun changeVisibilityStateOfTheProgressBar() {
        binding.progressBar.isVisible = !binding.progressBar.isVisible
    }

    private fun displayLoginError(errorMessage: String) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }
}

package com.example.presentation.account_tab.accountFragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.example.domain.common.toTrimmedString
import com.example.domain.common.validateAsName
import com.example.domain.common.validateAsUsername
import com.example.domain.model.User
import com.example.presentation.BaseFragment
import com.example.presentation.R
import com.example.presentation.databinding.AccountFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class AccountFragment : BaseFragment<AccountFragmentBinding>(R.layout.account_fragment) {

    private val viewModel: AccountFragmentViewModel by viewModels()

    // a variable that holds the state for all of the textFields and profile picture
    private var currentPhotoUri: Uri = Uri.EMPTY
    private var isChanged = false

    // intent launcher used for the profile picture image, on result it updates the picture
    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val photoResult: Uri? = it.data?.data
                if (photoResult != null) {
                    // user picked from gallery
                    // this prevents a security exception (permission denial)
                    requireActivity().contentResolver.takePersistableUriPermission(
                        photoResult, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    currentPhotoUri = photoResult
                    changeProfilePicture(photoResult)
                } else {
                    // user made a photo
                    Log.i("idk1", "userMadeAPhoto: $currentPhotoUri ")
                    changeProfilePicture(currentPhotoUri)
                }
                isChanged = true
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpLogoutButton()

        setUpRulesForTextFields()
        showUsersDataAndObserveForChanges()

        manageExtendabilityOfTheCardViews()
        setUpPrivacyPolicyTextViews()

        setUpUserPictureButton()
    }

    private fun setUpUserPictureButton() {
        binding.userPickImageView.setOnClickListener {
            if (checkPermissions()) {
                openIntentChooserForImageSources()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Missing permissions to preform that action",
                    Toast.LENGTH_SHORT
                ).show()
                requestMissingPermissions()
            }
        }
    }

    private fun changeProfilePicture(uri: Uri) {
        binding.userPickImageView.setImageURI(uri)
    }

    private fun openIntentChooserForImageSources() {
        // creating gallery intent
        val galleryIntent =
            Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)

        // creating camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (e: IOException) {
                    null
                }
                photoFile?.also {
                    val photoFileUri: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        requireActivity().packageName,
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFileUri)
                }
            }
        }

        val intentChooser = Intent.createChooser(galleryIntent, "Select an app")
        intentChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        intentLauncher.launch(intentChooser)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        Log.i("idk1", "createImageFile: called")
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoUri = this.toUri()
        }
    }

    private fun checkPermissions(): Boolean {
        val cameraPermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        val extStoragePermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        return (cameraPermissionGranted && extStoragePermissionGranted)
    }

    private fun requestMissingPermissions() {
        val listOfMissingPermissions = arrayListOf<String>()

        val cameraPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        )
        val extStoragePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listOfMissingPermissions.add(Manifest.permission.CAMERA)
        }
        if (extStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listOfMissingPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (listOfMissingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                listOfMissingPermissions.toTypedArray(),
                101
            )
        }
    }

    private fun setUpPrivacyPolicyTextViews() {
        // adding the "redirect" to both of the text views (but only to the end characters, not
        // the whole textView (seems more reasonable))
        val privacyPolicyCardPersonalData = binding.atBrandNamePersonalDataTextView
        val privacyPolicyCardPasswordChange = binding.atBrandsNamePasswordChangeTextView

        val brandsPrivacyPolicyText =
            SpannableString(privacyPolicyCardPasswordChange.text.toString())
        val linkMovementMethod = LinkMovementMethod.getInstance()

        privacyPolicyCardPasswordChange.movementMethod = linkMovementMethod
        privacyPolicyCardPersonalData.movementMethod = linkMovementMethod

        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://stackoverflow.com")
                    )
                )
            }
        }

        // 187 - 201 are the indexes of the "Privacy Policy" at the end of the string
        // I could write a custom implementation to find the indexes, but it won't ever change
        // so this saves a bit of time
        brandsPrivacyPolicyText.setSpan(clickableSpan, 187, 201, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        privacyPolicyCardPasswordChange.text = brandsPrivacyPolicyText
        privacyPolicyCardPersonalData.text = brandsPrivacyPolicyText
    }

    private fun setUpUpdatePersonalDataButton(user: User) {
        // first card:
        binding.updatePersonalDataPersonalDataButton.setOnClickListener {
            requireView().clearFocus() // clear focus from all text input fields

            // checking whether all of the fields are filled up with correct data (length)
            val validFirstName = binding.firstNamePersonalDataTextInputLayout.helperText == null
            val validLastName = binding.lastNamePersonalDataTextInputLayout.helperText == null
            val validUsername = binding.usernamePersonalDataTextInputLayout.helperText == null

            if (validFirstName && validLastName && validUsername && isChanged) {
                val firstName = binding.firstNamePersonalDataEditText.text.toTrimmedString()
                val lastName = binding.lastNamePersonalDataEditText.text.toTrimmedString()
                val username = binding.usernamePersonalDataEditText.text.toTrimmedString()
                viewModel.updateUser(
                    user.copy(
                        username = username,
                        firstName = firstName,
                        lastName = lastName,
                        profilePicture = currentPhotoUri
                    )
                )
                Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
                isChanged = false
            } else {
                Toast.makeText(requireContext(), "Invalid Form Data", Toast.LENGTH_SHORT).show()
            }
        }

        binding.updatePersonalDataPasswordChangeButton.setOnClickListener {
            requireView().clearFocus() // clear focus from all text input fields

            // checking whether all of the fields are filled up with correct data (length)
            val validFirstName = binding.firstNamePasswordChangeTextInputLayout.helperText == null
            val validLastName = binding.lastNamePasswordChangeTextInputLayout.helperText == null
            val validUsername = binding.usernamePasswordChangeTextInputLayout.helperText == null

            if (validFirstName && validLastName && validUsername && isChanged) {
                val firstName = binding.firstNamePasswordChangeEditText.text.toTrimmedString()
                val lastName = binding.lastNamePasswordChangeEditText.text.toTrimmedString()
                val username = binding.usernamePasswordChangeEditText.text.toTrimmedString()
                viewModel.updateUser(
                    user.copy(
                        username = username,
                        firstName = firstName,
                        lastName = lastName,
                        profilePicture = currentPhotoUri
                    )
                )
                Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
                isChanged = false
            } else {
                Toast.makeText(requireContext(), "Invalid Form Data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpLogoutButton() {
        binding.logoutPasswordChangeButton.setOnClickListener {
            viewModel.logoutUser()
            navigateTo(R.id.loginFragment)
        }
    }

    private fun showUsersDataAndObserveForChanges() {
        viewModel.observableUserLiveData.observe(viewLifecycleOwner) { result ->
            result.onSuccess { user ->
                // pass the "old" (currently logged in) user to be able to compare changes
                // (if the user changes anything and then changes it back to what it was,
                // the button should not be active)
                detectChangesInUserData(user)

                // this function also requires the old user instance in order to update it if
                // the user presses the update button
                setUpUpdatePersonalDataButton(user)

                // setting the current user data to the text fields and profile picture
                if (user.profilePicture != Uri.EMPTY) {
                    binding.userPickImageView.setImageURI(user.profilePicture)
                }

                binding.usernamePersonalDataEditText.setText(user.username)
                binding.usernamePasswordChangeEditText.setText(user.username)

                binding.firstNamePersonalDataEditText.setText(user.firstName)
                binding.firstNamePasswordChangeEditText.setText(user.firstName)

                binding.lastNamePersonalDataEditText.setText(user.lastName)
                binding.lastNamePasswordChangeEditText.setText(user.lastName)
            }
            result.onFailure {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun detectChangesInUserData(user: User) {
        // first card:
        val firstNamePersonalData = binding.firstNamePersonalDataEditText
        val lastNamePersonalData = binding.lastNamePersonalDataEditText
        val usernamePersonalData = binding.usernamePersonalDataEditText

        firstNamePersonalData.doAfterTextChanged {
            // if isChanged is already true, even if the field is edited BUT nothing changed 
            // (user could press the text fields and add something, and later delete that)
            // we shouldn't change the global isChanged variable value, because it must've been
            // modified somewhere else (other text field)
            if (!isChanged) {
                isChanged = (it.toString() != user.firstName)
            }
        }

        lastNamePersonalData.doAfterTextChanged {
            if (!isChanged) {
                isChanged = (it.toString() != user.lastName)
            }
        }

        usernamePersonalData.doAfterTextChanged {
            if (!isChanged) {
                isChanged = (it.toString() != user.username)
            }
        }

        // second card:
        val firstNamePasswordChange = binding.firstNamePasswordChangeEditText
        val lastNamePasswordChange = binding.lastNamePasswordChangeEditText
        val usernamePasswordChange = binding.usernamePasswordChangeEditText

        firstNamePasswordChange.doAfterTextChanged {
            if (!isChanged) {
                isChanged = (it.toString() != user.firstName)
            }
        }

        lastNamePasswordChange.doAfterTextChanged {
            if (!isChanged) {
                isChanged = (it.toString() != user.lastName)
            }
        }

        usernamePasswordChange.doAfterTextChanged {
            if (!isChanged) {
                isChanged = (it.toString() != user.username)
            }
        }
    }

    private fun setUpRulesForTextFields() {
        // sets up requirements for all of the textInputLayouts (both of the card views):

        // personal data cardView
        binding.firstNamePersonalDataEditText.setOnFocusChangeListener { _, focused ->
            val usersFirstNameInput = binding.firstNamePersonalDataEditText.text.toString()
            if (!focused) {
                binding.firstNamePersonalDataTextInputLayout.helperText =
                    usersFirstNameInput.validateAsName()
            }
        }

        binding.lastNamePersonalDataEditText.setOnFocusChangeListener { _, focused ->
            val usersLastNameInput = binding.lastNamePersonalDataEditText.text.toString()
            if (!focused) {
                binding.lastNamePersonalDataTextInputLayout.helperText =
                    usersLastNameInput.validateAsName()
            }
        }

        binding.usernamePersonalDataEditText.setOnFocusChangeListener { _, focused ->
            val usersUsernameInput = binding.usernamePersonalDataEditText.text.toString()
            if (!focused) {
                binding.usernamePersonalDataTextInputLayout.helperText =
                    usersUsernameInput.validateAsUsername()
            }
        }

        // Password change cardView
        binding.firstNamePasswordChangeEditText.setOnFocusChangeListener { _, focused ->
            val usersFirstNameInput = binding.firstNamePasswordChangeEditText.text.toString()
            if (!focused) {
                binding.firstNamePasswordChangeTextInputLayout.helperText =
                    usersFirstNameInput.validateAsName()
            }
        }

        binding.lastNamePasswordChangeEditText.setOnFocusChangeListener { _, focused ->
            val usersLastNameInput = binding.lastNamePasswordChangeEditText.text.toString()
            if (!focused) {
                binding.lastNamePasswordChangeTextInputLayout.helperText =
                    usersLastNameInput.validateAsName()
            }
        }

        binding.usernamePasswordChangeEditText.setOnFocusChangeListener { _, focused ->
            val usersUsernameInput = binding.usernamePasswordChangeEditText.text.toString()
            if (!focused) {
                binding.usernamePasswordChangeTextInputLayout.helperText =
                    usersUsernameInput.validateAsUsername()
            }
        }
    }

    private fun manageExtendabilityOfTheCardViews() {
        // first card
        val personalDataArrowIconButton = binding.personalDataArrowIconButton
        val personalDataHiddenView = binding.hiddenViewPersonalData
        val personalDataCardView = binding.personalDataCardView
        personalDataArrowIconButton.setOnClickListener {
            if (personalDataHiddenView.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(personalDataCardView, AutoTransition())
                personalDataHiddenView.visibility = View.GONE
                personalDataArrowIconButton.setImageResource(R.drawable.ic_arrow_more)
            } else {
                TransitionManager.beginDelayedTransition(personalDataCardView, AutoTransition())
                personalDataHiddenView.visibility = View.VISIBLE
                personalDataArrowIconButton.setImageResource(R.drawable.ic_arrow_less)
            }
        }

        // second card
        val passwordChangeArrowIconButton = binding.passwordChangeArrowImageButton
        val passwordChangeHiddenView = binding.hiddenViewPasswordChange
        val passwordChangeCardView = binding.passwordChangeCardView
        passwordChangeArrowIconButton.setOnClickListener {
            if (passwordChangeHiddenView.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(passwordChangeCardView, AutoTransition())
                passwordChangeHiddenView.visibility = View.GONE
                passwordChangeArrowIconButton.setImageResource(R.drawable.ic_arrow_more)
            } else {
                TransitionManager.beginDelayedTransition(passwordChangeCardView, AutoTransition())
                passwordChangeHiddenView.visibility = View.VISIBLE
                passwordChangeArrowIconButton.setImageResource(R.drawable.ic_arrow_less)
            }
        }
    }
}

package com.shpp.eorlov.assignment1.ui.editprofile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shpp.eorlov.assignment1.databinding.FragmentEditProfileBinding
import com.shpp.eorlov.assignment1.di.SharedPrefStorage
import com.shpp.eorlov.assignment1.storage.Storage
import com.shpp.eorlov.assignment1.ui.MainActivity
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.ValidateOperation
import com.shpp.eorlov.assignment1.utils.evaluateErrorMessage
import com.shpp.eorlov.assignment1.utils.ext.loadImage
import com.shpp.eorlov.assignment1.validator.Validator
import javax.inject.Inject
import kotlin.math.abs

class EditProfileFragment : Fragment() {

    //todo remove ALL hardcoded data in dialog fragment and edit profile fields

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var validator: Validator

    @Inject
    @field:SharedPrefStorage
    lateinit var storage: Storage

    private lateinit var viewModel: EditProfileViewModel
    private lateinit var binding: FragmentEditProfileBinding

    private var pathToLoadedImageFromGallery: String = ""
    private var imageLoaderLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val imageView: AppCompatImageView = binding.imageViewPersonPhoto
            if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
                val imageData = result.data?.data ?: return@registerForActivityResult
                pathToLoadedImageFromGallery = imageData.toString()
                imageView.loadImage(imageData)
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).contactComponent.inject(this)

        viewModel =
            ViewModelProvider(this, viewModelFactory)[EditProfileViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeData()
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        viewModel.apply {
            userLiveData.observe(viewLifecycleOwner) {
//                saveUserProfileData(it)
            }

            loadEvent.apply {
                observe(viewLifecycleOwner) { event ->
                    when (event) {
                        Results.OK -> {
                            unlockUI()
                            binding.contentLoadingProgressBarRecyclerView.isVisible = false
                        }

                        Results.LOADING -> {
                            lockUI()
                            binding.contentLoadingProgressBarRecyclerView.isVisible = true
                        }

                        Results.INITIALIZE_DATA_ERROR -> {
                            unlockUI()
                            binding.contentLoadingProgressBarRecyclerView.isVisible = false
                            Toast.makeText(requireContext(), event.name, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                        }
                    }

                }
            }
        }
    }

    //todo move to another class
    private fun lockUI() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun unlockUI() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }


    private fun initializeData() {

        viewModel.initializeData()

        viewModel.userLiveData.value?.apply {
            pathToLoadedImageFromGallery = photo

            binding.apply {
                textInputEditTextUsername.setText(name)
                textInputEditTextCareer.setText(profession)
                textInputEditTextAddress.setText(residenceAddress)
                textInputEditTextBirthdate.setText(birthDate)
                textInputEditTextPhone.setText(phoneNumber)
                textInputEditTextEmail.setText(email)
                imageViewPersonPhoto.loadImage(pathToLoadedImageFromGallery)
            }
        }
    }

    private fun loadImageFromGallery() {
        val gallery = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI
        )
        imageLoaderLauncher.launch(gallery)
    }


    private var previousClickTimestamp = SystemClock.uptimeMillis()

    private fun setListeners() {
        binding.apply {
            buttonSave.setOnClickListener {
                if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                    if (canAddNewContact()) {
                        activity?.onBackPressed()
                        previousClickTimestamp = SystemClock.uptimeMillis()
                    }
                }
            }

            imageViewImageLoader.setOnClickListener {
                if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                    loadImageFromGallery()
                    previousClickTimestamp = SystemClock.uptimeMillis()
                }
            }

            imageButtonContactDialogCloseButton.setOnClickListener {
                if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                    activity?.onBackPressed()
                }
            }


            addListenerToEditText(
                textInputEditTextAddress,
                textInputLayoutAddress,
                ValidateOperation.EMPTY
            )
            addListenerToEditText(
                textInputEditTextBirthdate,
                textInputLayoutBirthdate,
                ValidateOperation.BIRTHDAY
            )
            addListenerToEditText(
                textInputEditTextCareer,
                textInputLayoutCareer,
                ValidateOperation.EMPTY
            )
            addListenerToEditText(
                textInputEditTextEmail,
                textInputLayoutEmail,
                ValidateOperation.EMAIL
            )
            addListenerToEditText(
                textInputEditTextUsername,
                textInputLayoutUsername,
                ValidateOperation.EMPTY
            )
            addListenerToEditText(
                textInputEditTextPhone,
                textInputLayoutPhone,
                ValidateOperation.PHONE_NUMBER
            )
        }
    }


    // todo move to anoter class
    /**
     * Returns true if user entered valid data,
     * otherwise false
     */
    private fun canAddNewContact(): Boolean {
        processEnteredValues()
        binding.apply {

            return textInputLayoutAddress.error.isNullOrEmpty() &&
                    textInputLayoutBirthdate.error.isNullOrEmpty() &&
                    textInputLayoutCareer.error.isNullOrEmpty() &&
                    textInputLayoutEmail.error.isNullOrEmpty() &&
                    textInputLayoutUsername.error.isNullOrEmpty() &&
                    textInputLayoutPhone.error.isNullOrEmpty()
        }
    }

    /**
     * Processing of user-entered values
     */
    private fun processEnteredValues() {
        binding.apply {
            textInputLayoutAddress.error = processErrorOfEnteredValue(
                textInputEditTextAddress.text.toString(),
                ValidateOperation.EMPTY
            )

            textInputLayoutBirthdate.error = processErrorOfEnteredValue(
                textInputEditTextBirthdate.text.toString(),
                ValidateOperation.BIRTHDAY
            )
            textInputLayoutCareer.error = processErrorOfEnteredValue(
                textInputEditTextCareer.text.toString(),
                ValidateOperation.EMPTY
            )
            textInputLayoutEmail.error = processErrorOfEnteredValue(
                textInputEditTextEmail.text.toString(),
                ValidateOperation.EMAIL
            )
            textInputLayoutUsername.error = processErrorOfEnteredValue(
                textInputEditTextUsername.text.toString(),
                ValidateOperation.EMPTY
            )
            textInputLayoutPhone.error = processErrorOfEnteredValue(
                textInputEditTextPhone.text.toString(),
                ValidateOperation.PHONE_NUMBER
            )
        }
    }

    /**
     * Returns error text of given field if it exist,
     * otherwise returns empty string
     */
    private fun processErrorOfEnteredValue(
        text: String,
        validateOperation: ValidateOperation
    ): String {
        val error = viewModel.isValidField(
            text,
            validateOperation
        )

        if(error.isNullOrEmpty()) {
//            viewModel.saveUserProfileData()
        }

        return error
    }

    /**
     * Set listener to given EditText
     */
    private fun addListenerToEditText(
        editText: TextInputEditText,
        textInput: TextInputLayout,
        validateOperation: ValidateOperation
    ) {
        editText.addTextChangedListener {
            textInput.error =
                when (validateOperation) {
                    ValidateOperation.EMAIL -> evaluateErrorMessage(
                        validator.validateEmail(
                            editText.text.toString()
                        )
                    )
                    ValidateOperation.PHONE_NUMBER -> evaluateErrorMessage(
                        validator.validatePhoneNumber(
                            editText.text.toString()
                        )
                    )
                    ValidateOperation.BIRTHDAY -> evaluateErrorMessage(
                        validator.validateBirthdate(
                            editText.text.toString()
                        )
                    )
                    ValidateOperation.EMPTY -> evaluateErrorMessage(
                        validator.checkIfFieldIsNotEmpty(
                            editText.text.toString()
                        )
                    )
                }
        }

        viewModel.userLiveData.value = viewModel.userLiveData.value
    }

}
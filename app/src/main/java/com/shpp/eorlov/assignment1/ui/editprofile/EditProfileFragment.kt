package com.shpp.eorlov.assignment1.ui.editprofile

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentEditProfileBinding
import com.shpp.eorlov.assignment1.models.UserModel
import com.shpp.eorlov.assignment1.storage.SharedPreferencesStorageImplementation
import com.shpp.eorlov.assignment1.ui.SharedViewModel
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Constants.DATE_FORMAT
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.ext.loadImage
import com.shpp.eorlov.assignment1.validator.ValidateOperation
import com.shpp.eorlov.assignment1.validator.Validator
import com.shpp.eorlov.assignment1.validator.evaluateErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class EditProfileFragment : BaseFragment() {


    @Inject
    lateinit var validator: Validator

    @Inject
    lateinit var storage: SharedPreferencesStorageImplementation

    private val args: EditProfileFragmentArgs by navArgs()
    private val myCalendar: Calendar = Calendar.getInstance()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: EditProfileViewModel by viewModels()

    private lateinit var binding: FragmentEditProfileBinding

    private var previousClickTimestamp = SystemClock.uptimeMillis()
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

    override fun onResume() {
        super.onResume()
        printLog("On resume")
    }

    private fun setObservers() {
        viewModel.apply {
            userLiveData.observe(viewLifecycleOwner) { list ->
                sharedViewModel.updatedUser.value = list
            }
            loadEvent.apply {
                observe(viewLifecycleOwner) { event ->
                    when (event) {
                        Results.OK -> {
                            unlockUI()
                            binding.contentLoadingProgressBar.isVisible = false
                        }

                        Results.LOADING -> {
                            lockUI()
                            binding.contentLoadingProgressBar.isVisible = true
                        }

                        Results.INITIALIZE_DATA_ERROR -> {
                            unlockUI()
                            binding.contentLoadingProgressBar.isVisible = false
                            Toast.makeText(requireContext(), event.name, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                        }
                    }
                }
            }
        }
    }


    private fun initializeData() {
        val receivedUserModel = args.userModel
        viewModel.initializeData(receivedUserModel)

        viewModel.userLiveData.value?.apply {
            pathToLoadedImageFromGallery = photo

            binding.apply {
                textInputEditTextUsername.setText(receivedUserModel.name)
                textInputEditTextCareer.setText(profession)
                textInputEditTextAddress.setText(residenceAddress)
                textInputEditTextBirthdate.setText(birthDate)
                textInputEditTextPhone.setText(receivedUserModel.phoneNumber)
                textInputEditTextEmail.setText(receivedUserModel.email)
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

    private fun setListeners() {
        binding.apply {
            buttonSave.setOnClickListener {
                if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                    if (canAddNewContact()) {

                        viewModel.userLiveData.value = getProfileData()
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


            val date =
                OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    myCalendar.set(Calendar.YEAR, year)
                    myCalendar.set(Calendar.MONTH, monthOfYear)
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateLabel()
                }

            textInputEditTextBirthdate.setOnClickListener {
                DatePickerDialog(
                    requireActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            addListenerToEditText(
                textInputEditTextAddress,
                textInputLayoutAddress,
                ValidateOperation.EMPTY
            )

            addListenerToEditText(
                textInputEditTextCareer,
                textInputLayoutCareer,
                ValidateOperation.EMPTY
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

    private fun updateLabel() {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
        binding.textInputEditTextBirthdate.setText(sdf.format(myCalendar.time))
    }

    private fun getProfileData() = UserModel(
        binding.textInputEditTextUsername.text.toString(),
        binding.textInputEditTextCareer.text.toString(),
        pathToLoadedImageFromGallery,
        binding.textInputEditTextAddress.text.toString(),
        binding.textInputEditTextBirthdate.text.toString(),
        binding.textInputEditTextPhone.text.toString(),
        binding.textInputEditTextEmail.text.toString(),
    )


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

            textInputLayoutCareer.error = processErrorOfEnteredValue(
                textInputEditTextCareer.text.toString(),
                ValidateOperation.EMPTY
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

        return viewModel.isValidField(
            text,
            validateOperation
        )
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
    }

}
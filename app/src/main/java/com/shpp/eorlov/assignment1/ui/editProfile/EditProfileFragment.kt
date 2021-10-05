package com.shpp.eorlov.assignment1.ui.editProfile

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.data.storage.SharedPreferencesStorage
import com.shpp.eorlov.assignment1.databinding.FragmentEditProfileBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.SharedViewModel
import com.shpp.eorlov.assignment1.utils.Constants.DATE_FORMAT
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce
import com.shpp.eorlov.assignment1.utils.ext.loadImage
import com.shpp.eorlov.assignment1.validator.ValidateOperation
import com.shpp.eorlov.assignment1.validator.Validator
import com.shpp.eorlov.assignment1.validator.evaluateErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment : BaseFragment() {


    @Inject
    lateinit var validator: Validator

    @Inject
    lateinit var storage: SharedPreferencesStorage

    private val args: EditProfileFragmentArgs by navArgs()
    private val myCalendar: Calendar = Calendar.getInstance()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: EditProfileViewModel by viewModels()

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
            pathToLoadedImageFromGallery = photo ?: ""

            binding.apply {
                textInputEditTextUsername.setText(receivedUserModel.name)
                textInputEditTextCareer.setText(career)
                textInputEditTextAddress.setText(address)
                textInputEditTextBirthdate.setText(birthday)
                textInputEditTextPhone.setText(receivedUserModel.phone)
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
        setOnClickListeners()
        setOnEditorActionListeners()
    }

    private fun setOnClickListeners() {
        binding.apply {

            buttonSave.clickWithDebounce {
                if (canAddNewContact()) {

                    viewModel.userLiveData.value = getProfileData()
                    activity?.onBackPressed()

                }
            }

            imageViewImageLoader.clickWithDebounce {
                loadImageFromGallery()
            }

            imageButtonContactDialogCloseButton.clickWithDebounce {
                activity?.onBackPressed()
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
        }
    }

    private fun setOnEditorActionListeners() {
        binding.apply {
            setOnEditorActionListener(
                textInputEditTextAddress,
                textInputLayoutAddress,
                ValidateOperation.EMPTY
            )

            setOnEditorActionListener(
                textInputEditTextCareer,
                textInputLayoutCareer,
                ValidateOperation.EMPTY
            )

            setOnEditorActionListener(
                textInputEditTextUsername,
                textInputLayoutUsername,
                ValidateOperation.EMPTY
            )
            setOnEditorActionListener(
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
        name = binding.textInputEditTextUsername.text.toString(),
        career = binding.textInputEditTextCareer.text.toString(),
        photo = pathToLoadedImageFromGallery,
        address = binding.textInputEditTextAddress.text.toString(),
        birthday = binding.textInputEditTextBirthdate.text.toString(),
        phone = binding.textInputEditTextPhone.text.toString(),
        email = binding.textInputEditTextEmail.text.toString(),
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


    private fun setOnEditorActionListener(
        editText: TextInputEditText,
        textInput: TextInputLayout,
        validateOperation: ValidateOperation
    ) {
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
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
            false
        }
    }
}
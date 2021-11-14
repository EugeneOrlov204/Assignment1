package com.shpp.eorlov.assignment1.ui.editProfile

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentEditProfileBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.SharedViewModel
import com.shpp.eorlov.assignment1.ui.imageLoaderDialog.ImageLoaderDialogFragment
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Constants.DATE_FORMAT
import com.shpp.eorlov.assignment1.utils.FeatureNavigationEnabled.featureNavigationEnabled
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.TransitionKeys.USER_MODEL_KEY
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce
import com.shpp.eorlov.assignment1.utils.ext.loadCircleImage
import com.shpp.eorlov.assignment1.validator.ValidateOperation
import com.shpp.eorlov.assignment1.validator.evaluateErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EditProfileFragment : BaseFragment() {

    private val args: EditProfileFragmentArgs by navArgs()
    private val myCalendar: Calendar = Calendar.getInstance()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: EditProfileViewModel by viewModels()

    private lateinit var dialog: ImageLoaderDialogFragment

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var userModel: UserModel

    private var pathToLoadedImage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userModel = if (featureNavigationEnabled) {
            args.userModel
        } else {
            requireArguments().getParcelable(USER_MODEL_KEY) ?: return
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
        initViews()
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
                sharedViewModel.updatedUserLiveData.value = list
            }
            viewModel.editUserLiveData.observe(viewLifecycleOwner) {
                loadEventLiveData.value = Results.OK
                activity?.onBackPressed()
            }
            loadEventLiveData.apply {
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

        sharedViewModel.newPhotoLiveData.observe(viewLifecycleOwner) {
            pathToLoadedImage = it
            loadProfileImage()
        }
    }


    private fun initViews() {

        viewModel.initializeData(userModel)

        viewModel.userLiveData.value?.apply {
            pathToLoadedImage = image ?: ""

            binding.apply {
                textInputEditTextUsername.setText(userModel.name)
                textInputEditTextCareer.setText(career)
                textInputEditTextAddress.setText(address)
                textInputEditTextBirthdate.setText(birthday)
                textInputEditTextPhone.setText(userModel.phone)
                textInputEditTextEmail.setText(userModel.email)
                loadProfileImage()
            }
        }
    }

    private fun loadProfileImage() {
        if(pathToLoadedImage.isNotEmpty()) {
            binding.imageViewPersonPhoto.loadCircleImage(pathToLoadedImage)
        } else {
            binding.imageViewPersonPhoto.loadCircleImage(R.drawable.ic_user_mockup)
        }
    }


    private fun setListeners() {
        setOnClickListeners()
        setOnEditorActionListeners()
    }

    private fun setOnClickListeners() {
        binding.apply {

            buttonSave.clickWithDebounce {
                if (canAddNewContact()) {
                    val newContact = getProfileData()
                    viewModel.editUser(newContact)
                    viewModel.userLiveData.value = newContact
                }
            }

            imageViewImageLoader.clickWithDebounce {
                dialog = ImageLoaderDialogFragment()
                dialog.show(childFragmentManager, Constants.IMAGE_LOADER_DIALOG_TAG)
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
        image = pathToLoadedImage,
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
                            viewModel.validateEmail(
                                editText.text.toString()
                            )
                        )
                        ValidateOperation.PHONE_NUMBER -> evaluateErrorMessage(
                            viewModel.validatePhoneNumber(
                                editText.text.toString()
                            )
                        )
                        ValidateOperation.BIRTHDAY -> evaluateErrorMessage(
                            viewModel.validateBirthdate(
                                editText.text.toString()
                            )
                        )
                        ValidateOperation.EMPTY -> evaluateErrorMessage(
                            viewModel.checkIfFieldIsNotEmpty(
                                editText.text.toString()
                            )
                        )
                    }
            }
            false
        }
    }
}
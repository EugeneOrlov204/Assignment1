package com.shpp.eorlov.assignment1.ui.dialogfragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.databinding.AddContactDialogBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.MainActivity
import com.shpp.eorlov.assignment1.ui.SharedViewModel
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Constants.GENERATE_ID_CODE
import com.shpp.eorlov.assignment1.utils.ValidateOperation
import com.shpp.eorlov.assignment1.utils.evaluateErrorMessage
import com.shpp.eorlov.assignment1.utils.ext.loadImage
import com.shpp.eorlov.assignment1.validator.Validator
import javax.inject.Inject
import kotlin.math.abs


class ContactDialogFragment : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ContactDialogFragmentViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var dialogBinding: AddContactDialogBinding

    private var pathToLoadedImageFromGallery: String = ""
    private var imageLoaderLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val imageView: AppCompatImageView = dialogBinding.imageViewPersonPhoto
            if (result.resultCode == RESULT_OK && result.data?.data != null) {
                val imageData = result.data?.data ?: return@registerForActivityResult
                pathToLoadedImageFromGallery = imageData.toString()
                imageView.loadImage(imageData)
            }
        }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).contactComponent.inject(this)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[ContactDialogFragmentViewModel::class.java]

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialogBinding = AddContactDialogBinding.inflate(LayoutInflater.from(context))
        return dialogBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeData()
        setListeners()
        setObserver()
    }

    override fun onStart() {
        super.onStart()

        //Set size of DialogFragment to all size of parent
        if (dialog != null && dialog?.window != null) {
            val params: ViewGroup.LayoutParams =
                dialog?.window?.attributes ?: return
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.window?.attributes = params as WindowManager.LayoutParams
        }
    }

    private fun setObserver() {
        viewModel.newUser.observe(viewLifecycleOwner) { user ->
            sharedViewModel.newUser.value = user
            dismiss()
        }
    }

    /**
     * Initialize date and set listeners to EditTexts
     */
    private fun initializeData() {
        dialogBinding.imageViewPersonPhoto.loadImage(R.mipmap.ic_launcher)
    }


    /**
     * Add new contact to RecyclerView if all field are valid
     */
    private fun addContact() {
        if (!canAddNewContact()) {
            return
        }

        val imageData = pathToLoadedImageFromGallery

        val newContact =
            UserModel(
                GENERATE_ID_CODE,
                dialogBinding.textInputEditTextUsername.text.toString(),
                dialogBinding.textInputEditTextCareer.text.toString(),
                imageData,
                dialogBinding.textInputEditTextAddress.text.toString(),
                dialogBinding.textInputEditTextBirthdate.text.toString(),
                dialogBinding.textInputEditTextPhone.text.toString(),
                dialogBinding.textInputEditTextEmail.text.toString()
            )

        pathToLoadedImageFromGallery = ""
        viewModel.addItem(newContact)
    }

    /**
     * Returns true if user entered valid data,
     * otherwise false
     */
    private fun canAddNewContact(): Boolean {
        processEnteredValues()
        dialogBinding.apply {

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
        dialogBinding.apply {
            textInputLayoutAddress.error = viewModel.isValidField(
                textInputEditTextAddress.text.toString(),
                ValidateOperation.EMPTY
            )
            textInputLayoutBirthdate.error = viewModel.isValidField(
                textInputEditTextBirthdate.text.toString(),
                ValidateOperation.BIRTHDAY
            )
            textInputLayoutCareer.error = viewModel.isValidField(
                textInputEditTextCareer.text.toString(),
                ValidateOperation.EMPTY
            )
            textInputLayoutEmail.error = viewModel.isValidField(
                textInputEditTextEmail.text.toString(),
                ValidateOperation.EMAIL
            )
            textInputLayoutUsername.error = viewModel.isValidField(
                textInputEditTextUsername.text.toString(),
                ValidateOperation.EMPTY
            )
            textInputLayoutPhone.error = viewModel.isValidField(
                textInputEditTextPhone.text.toString(),
                ValidateOperation.PHONE_NUMBER
            )
        }
    }


    private var previousClickTimestamp = SystemClock.uptimeMillis()


    private fun setListeners() {
        dialogBinding.apply {
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


            imageViewImageLoader.setOnClickListener {
                if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                    loadImageFromGallery()
                    previousClickTimestamp = SystemClock.uptimeMillis()
                }
            }


            imageButtonContactDialogCloseButton.setOnClickListener {
                if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                    dismiss()
                    previousClickTimestamp = SystemClock.uptimeMillis()
                }
            }


            buttonSave.setOnClickListener {
                if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                    addContact()
                    previousClickTimestamp = SystemClock.uptimeMillis()
                }
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


    /**
     * Set listener to given EditText
     */
    private fun addListenerToEditText(
        editText: TextInputEditText,
        textInput: TextInputLayout,
        validateOperation: ValidateOperation
    ) {
        val validator = Validator(requireContext())
        editText.addTextChangedListener {
            textInput.error =
                when (validateOperation) {
                    ValidateOperation.EMAIL -> evaluateErrorMessage(validator.validateEmail(editText.text.toString()))
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

package com.shpp.eorlov.assignment1.ui.contact_dialog_fragment

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.databinding.AddContactDialogBinding
import com.shpp.eorlov.assignment1.models.UserModel
import com.shpp.eorlov.assignment1.ui.SharedViewModel
import com.shpp.eorlov.assignment1.utils.Constants
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
class ContactDialogFragment : DialogFragment() {

    private val myCalendar: Calendar = Calendar.getInstance()

    @Inject
    lateinit var validator: Validator

    private val viewModel: ContactDialogViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
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
        dialogBinding.imageViewPersonPhoto.loadImage(R.drawable.ic_default_user_image)
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


    private fun setListeners() {
        setOnEditorActionListeners()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        dialogBinding.apply {

            imageViewImageLoader.clickWithDebounce {
                loadImageFromGallery()
            }


            imageButtonContactDialogCloseButton.clickWithDebounce {
                dismiss()
            }


            buttonSave.clickWithDebounce {
                addContact()
            }

            val date =
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
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
        dialogBinding.apply {
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
                textInputEditTextEmail,
                textInputLayoutEmail,
                ValidateOperation.EMAIL
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
        val sdf = SimpleDateFormat(Constants.DATE_FORMAT, Locale.US)
        dialogBinding.textInputEditTextBirthdate.setText(sdf.format(myCalendar.time))
    }

    private fun loadImageFromGallery() {
        val gallery = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI
        )
        imageLoaderLauncher.launch(gallery)
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




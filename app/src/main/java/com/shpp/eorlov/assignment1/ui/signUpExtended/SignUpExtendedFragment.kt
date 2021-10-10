package com.shpp.eorlov.assignment1.ui.signUpExtended

import android.app.Activity
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentSignUpExtendedBinding
import com.shpp.eorlov.assignment1.ui.SharedViewModel
import com.shpp.eorlov.assignment1.ui.imageLoaderDialog.ImageLoaderDialogFragment
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce
import com.shpp.eorlov.assignment1.utils.ext.hideKeyboard
import com.shpp.eorlov.assignment1.utils.ext.loadImage
import com.shpp.eorlov.assignment1.validator.Validator
import com.shpp.eorlov.assignment1.validator.evaluateErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpExtendedFragment : BaseFragment() {

    @Inject
    lateinit var validator: Validator

    private val viewModel: SignUpExtendedViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val args: SignUpExtendedFragmentArgs by navArgs()
    private lateinit var binding: FragmentSignUpExtendedBinding
    private lateinit var dialog: ImageLoaderDialogFragment

    private var pathToLoadedImageFromGallery: String = ""
    private var imageLoaderLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val imageView: AppCompatImageView = binding.imageViewUserImage
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
        binding = FragmentSignUpExtendedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initializeData()
        setListeners()
        setObservers()
    }

    override fun onResume() {
        super.onResume()
        printLog("On resume")
    }


    //todo remove "shared" from methods' name
    private fun setObservers() {
        setViewModelObserver()
        setSharedViewModelObserver()
    }

    private fun setViewModelObserver() {
        viewModel.loadEventLiveData.observe(viewLifecycleOwner) { event ->
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

                Results.INTERNET_ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.internet_error),
                        Toast.LENGTH_LONG
                    ).show()
                }

                Results.EXISTED_ACCOUNT_ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.existed_account_error_text),
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {
                }
            }
        }
    }

    private fun setSharedViewModelObserver() {
        viewModel.registerUserLiveData.observe(viewLifecycleOwner) {
            if (it?.code == Constants.SUCCESS_RESPONSE_CODE && it.data != null) {

                viewModel.editUser(
                    name = binding.textInputEditTextUserName.text.toString(),
                    phone = binding.textInputEditTextMobilePhone.text.toString(),
                    address = "",
                    career = "",
                    birthday = "",
                    accessToken = it.data.accessToken
                )
                viewModel.rememberCurrentEmail(args.email)
                pathToLoadedImageFromGallery = ""

            } else if (it == null) {
                viewModel.loadEventLiveData.value = Results.INTERNET_ERROR
            } else {
                viewModel.loadEventLiveData.value = Results.EXISTED_ACCOUNT_ERROR
            }
        }

        viewModel.editUserLiveData.observe(viewLifecycleOwner) {
            if (it?.code == Constants.SUCCESS_RESPONSE_CODE) {
                val action =
                    SignUpExtendedFragmentDirections.actionSignUpExtendedFragmentToCollectionContactFragment()
                findNavController().navigate(action)
            } else if (it == null) {
                viewModel.loadEventLiveData.value = Results.INTERNET_ERROR
            } else {
                viewModel.loadEventLiveData.value = Results.NOT_EXISTED_ACCOUNT_ERROR
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

        binding.buttonForward.clickWithDebounce {
            goToMyProfile()
        }

        binding.buttonCancel.clickWithDebounce {
            activity?.onBackPressed()
        }

        binding.imageViewImageLoader.clickWithDebounce {
//            dialog = ImageLoaderDialogFragment()
//            dialog.show(childFragmentManager, Constants.IMAGE_LOADER_DIALOG_TAG)
            loadImageFromGallery()
        }

        binding.root.setOnClickListener {
            it.hideKeyboard()
        }

        binding.textInputEditTextMobilePhone.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.textInputLayoutMobilePhone.error = evaluateErrorMessage(
                        validator.validatePhoneNumber(text.toString())
                    )
                    goToMyProfile()
                }
                false
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding.textInputLayoutMobilePhone.error = ""
                }
            }

        }
        binding.textInputEditTextUserName.apply {
            setOnEditorActionListener { _, actionId, _ ->
                binding.textInputLayoutUserName.error = evaluateErrorMessage(
                    validator.validateUserName(text.toString())
                )

                false
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding.textInputLayoutUserName.error = ""
                }
            }
        }
    }

    private fun goToMyProfile() {
        binding.apply {
            if (isFieldsInvalid()) {

                textInputLayoutMobilePhone.error = evaluateErrorMessage(
                    validator.validatePhoneNumber(textInputEditTextMobilePhone.text.toString())
                )

                textInputLayoutUserName.error = evaluateErrorMessage(
                    validator.validateUserName(textInputEditTextUserName.text.toString())
                )

                return
            }

            viewModel.registerUser(email = args.email, password = args.password)
        }
    }


    private fun isFieldsInvalid(): Boolean {
        return !binding.textInputLayoutUserName.error.isNullOrEmpty() ||
                binding.textInputEditTextUserName.text.toString().isEmpty() ||
                !binding.textInputLayoutMobilePhone.error.isNullOrEmpty() ||
                binding.textInputEditTextMobilePhone.text.toString().isEmpty()
    }
}



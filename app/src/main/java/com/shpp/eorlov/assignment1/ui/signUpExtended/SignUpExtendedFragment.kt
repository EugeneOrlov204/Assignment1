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
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentSignUpExtendedBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.imageLoaderDialog.ImageLoaderDialogFragment
import com.shpp.eorlov.assignment1.ui.viewPager.CollectionContactFragment
import com.shpp.eorlov.assignment1.utils.FeatureNavigationEnabled
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.TransitionKeys.EMAIL_KEY
import com.shpp.eorlov.assignment1.utils.TransitionKeys.PASSWORD_KEY
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
    private val args: SignUpExtendedFragmentArgs by navArgs()
    private lateinit var binding: FragmentSignUpExtendedBinding
    private lateinit var dialog: ImageLoaderDialogFragment
    private lateinit var email: String
    private lateinit var password: String


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (FeatureNavigationEnabled.featureNavigationEnabled) {
            email = args.email
            password = args.password
        } else {
            requireArguments().run {
                email = getString(EMAIL_KEY) ?: ""
                password = getString(PASSWORD_KEY) ?: ""
            }
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

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goToSignUp()
            }
        })
    }

    private fun setObservers() {
        setViewModelObserver()
        setRegisterUserLiveDataObserver()
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

    private fun setRegisterUserLiveDataObserver() {
        viewModel.registerUserLiveData.observe(viewLifecycleOwner) {
            viewModel.saveToken(it.data?.accessToken ?: "")
            viewModel.editUser(
                user = UserModel(
                    name = binding.textInputEditTextUserName.text.toString(),
                    phone = binding.textInputEditTextMobilePhone.text.toString(),
                    address = "",
                    career = "",
                    birthday = "",
                    image = pathToLoadedImageFromGallery,
                    email = email
                ),
                accessToken = it.data?.accessToken ?: "",
            )
            viewModel.rememberCurrentEmail(email)
            pathToLoadedImageFromGallery = ""
        }

        viewModel.editUserLiveData.observe(viewLifecycleOwner) {
            if (FeatureNavigationEnabled.featureNavigationEnabled) {
                val action =
                    SignUpExtendedFragmentDirections.actionSignUpExtendedFragmentToCollectionContactFragment()
                findNavController().navigate(action)
            } else {
                val fragmentManager = activity?.supportFragmentManager
                fragmentManager?.commit {
                    setReorderingAllowed(true)
                    replace(R.id.fragmentContainerView, CollectionContactFragment())
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

    private fun setListeners() {

        binding.buttonForward.clickWithDebounce {
            goToMyProfile()
        }

        binding.buttonCancel.clickWithDebounce {
            goToSignUp()
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
            setOnEditorActionListener { _, _, _ ->
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

    private fun goToSignUp() {
        if (FeatureNavigationEnabled.featureNavigationEnabled) {
            activity?.onBackPressed()
        } else {
            val fragmentManager = activity?.supportFragmentManager
            fragmentManager?.popBackStack()
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

            viewModel.registerUser(email = email, password = password)
        }
    }


    private fun isFieldsInvalid(): Boolean {
        return !binding.textInputLayoutUserName.error.isNullOrEmpty() ||
                binding.textInputEditTextUserName.text.toString().isEmpty() ||
                !binding.textInputLayoutMobilePhone.error.isNullOrEmpty() ||
                binding.textInputEditTextMobilePhone.text.toString().isEmpty()
    }
}



package com.shpp.eorlov.assignment1.ui.signup_extended

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.DialogFragmentLoadImageBinding
import com.shpp.eorlov.assignment1.databinding.FragmentSignUpExtendedBinding
import com.shpp.eorlov.assignment1.models.UserModel
import com.shpp.eorlov.assignment1.ui.SharedViewModel
import com.shpp.eorlov.assignment1.ui.contact_dialog_fragment.ContactDialogFragment
import com.shpp.eorlov.assignment1.ui.image_loader_dialog_fragment.ImageLoaderDialogFragment
import com.shpp.eorlov.assignment1.ui.image_loader_dialog_fragment.ImageLoaderDialogFragmentArgs
import com.shpp.eorlov.assignment1.ui.image_loader_dialog_fragment.ImageLoaderDialogFragmentDirections
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.ext.hideKeyboard
import com.shpp.eorlov.assignment1.validator.Validator
import com.shpp.eorlov.assignment1.validator.evaluateErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class SignUpExtendedFragment : BaseFragment() {

    @Inject
    lateinit var validator: Validator

    private val viewModel: SignUpExtendedViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val args: ImageLoaderDialogFragmentArgs by navArgs()
    private lateinit var binding: FragmentSignUpExtendedBinding
    private lateinit var dialog: ImageLoaderDialogFragment
    private var previousClickTimestamp = SystemClock.uptimeMillis()


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

    private fun setObservers() {

        viewModel.loadEvent.observe(viewLifecycleOwner) { event ->
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
        sharedViewModel.registerUser.observe(viewLifecycleOwner) {
            if (it?.code == Constants.SUCCESS_RESPONSE_CODE && it.data != null) {
//                viewModel.saveToken(it.data.user.email ?: "", it.data.accessToken)

                val userModel = UserModel(
                    email = args.email,
                    name = binding.textInputEditTextUserName.text.toString(),
                    profession = "",
                    photo = "", //fixme
                    phoneNumber = binding.textInputEditTextMobilePhone.text.toString(),
                    residenceAddress = "",
                    birthDate = ""
                )


                val action =
                    ImageLoaderDialogFragmentDirections.actionImageLoaderDialogFragmentToCollectionContactFragment(
                        userModel//todo is it necessarily?
                    )
                findNavController().navigate(action)
            } else if (it == null) {
                viewModel.loadEvent.value = Results.INTERNET_ERROR
            } else {
                viewModel.loadEvent.value = Results.EXISTED_ACCOUNT_ERROR
            }
        }
    }

    private fun setListeners() {

        binding.buttonForward.setOnClickListener {
            if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                goToMyProfile()
                previousClickTimestamp = SystemClock.uptimeMillis()
            }
        }

        binding.buttonCancel.setOnClickListener {
            if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                activity?.onBackPressed()
                previousClickTimestamp = SystemClock.uptimeMillis()
            }
        }

        binding.imageViewImageLoader.setOnClickListener {
            if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                dialog = ImageLoaderDialogFragment()
                dialog.show(childFragmentManager, Constants.IMAGE_LOADER_DIALOG_TAG)

                previousClickTimestamp = SystemClock.uptimeMillis()
            }
        }

        binding.root.setOnClickListener {
            it.hideKeyboard()
        }

        binding.textInputEditTextMobilePhone.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                    goToMyProfile()
                    previousClickTimestamp = SystemClock.uptimeMillis()
                }
            }
            false
        }

        binding.apply {
            textInputEditTextMobilePhone.addTextChangedListener {
                textInputLayoutMobilePhone.error = evaluateErrorMessage(
                    validator.validatePhoneNumber(textInputEditTextMobilePhone.text.toString())
                )
            }
            textInputEditTextUserName.addTextChangedListener {
                textInputLayoutUserName.error = evaluateErrorMessage(
                    validator.validateUserName(textInputEditTextUserName.text.toString())
                )
            }
        }
    }

    private fun goToMyProfile() {
        binding.apply {
            if (isFieldsInvalid()) {
                return
            }

            sharedViewModel.registerUser(
                email = args.email,
                password = args.password
            )
        }
    }


    private fun isFieldsInvalid(): Boolean {
        return !binding.textInputLayoutUserName.error.isNullOrEmpty() ||
                binding.textInputEditTextUserName.text.toString().isEmpty() ||
                !binding.textInputLayoutMobilePhone.error.isNullOrEmpty() ||
                binding.textInputEditTextMobilePhone.text.toString().isEmpty()
    }
}



package com.shpp.eorlov.assignment1.ui.signupextended

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentSignUpExtendedBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.MainActivity
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.ext.hideKeyboard
import com.shpp.eorlov.assignment1.validator.Validator
import com.shpp.eorlov.assignment1.validator.evaluateErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class SignUpFragmentExtended : BaseFragment() {

    private val args: SignUpFragmentExtendedArgs by navArgs()
    private val viewModel: SignUpExtendedViewModel by viewModels()

    @Inject
    lateinit var validator: Validator
    private lateinit var binding: FragmentSignUpExtendedBinding

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
        viewModel.loadEvent.apply {
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
    }

    private fun setListeners() {

        binding.textViewSignIn.setOnClickListener {
            if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                goToSignInProfile()
                previousClickTimestamp = SystemClock.uptimeMillis()
            }
        }

        binding.buttonRegister.setOnClickListener {
            if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                goToMyProfile()
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

    private fun goToSignInProfile() {
        if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
            activity?.onBackPressed()
            previousClickTimestamp = SystemClock.uptimeMillis()
        }
    }


    private fun goToMyProfile() {

        if (isFieldsInvalid() ||
            viewModel.isExistingAccount(args.userModel.email)
        ) {
            return
        }



        val userModel = UserModel(
            email = args.userModel.email,
            name = binding.textInputEditTextUserName.text.toString(),
            profession = "",
            photo = "",
            phoneNumber = binding.textInputEditTextMobilePhone.text.toString(),
            residenceAddress = "",
            birthDate = ""
        )

        if (args.rememberMe) {
            viewModel.saveLoginData(
                userModel.email,
                args.password
            )
        }

        val action =
            SignUpFragmentExtendedDirections.actionSignUpFragmentExtendedToCollectionContactFragment(
                userModel
            )
        findNavController().navigate(action)
    }

    private fun isFieldsInvalid() =
        !binding.textInputLayoutMobilePhone.error.isNullOrEmpty() ||
                binding.textInputEditTextMobilePhone.text.toString().isEmpty() ||
                !binding.textInputLayoutUserName.error.isNullOrEmpty() ||
                binding.textInputEditTextUserName.text.toString().isEmpty()
}



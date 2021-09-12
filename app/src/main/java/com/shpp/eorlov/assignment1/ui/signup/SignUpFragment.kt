package com.shpp.eorlov.assignment1.ui.signup

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
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentSignUpBinding
import com.shpp.eorlov.assignment1.models.UserModel
import com.shpp.eorlov.assignment1.ui.SharedViewModel
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Constants.SUCCESS_RESPONSE_CODE
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.ext.hideKeyboard
import com.shpp.eorlov.assignment1.validator.Validator
import com.shpp.eorlov.assignment1.validator.evaluateErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {

    @Inject
    lateinit var validator: Validator

    private val viewModel: SignUpViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentSignUpBinding
    private var previousClickTimestamp = SystemClock.uptimeMillis()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
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

                Results.EXISTED_ACCOUNT_ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.existed_account_error_text),
                        Toast.LENGTH_LONG
                    ).show()
                }

                Results.INTERNET_ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.internet_error),
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {
                }
            }
        }
        sharedViewModel.registerUser.observe(viewLifecycleOwner) {
            if (it?.code == SUCCESS_RESPONSE_CODE && it.data != null) {
                viewModel.saveToken(it.data.user.email ?: "" , it.data.accessToken)

                //todo set default values
                val userModel = UserModel(
                    email = "",
                    name = "",
                    profession = "",
                    photo = "",
                    phoneNumber = "",
                    residenceAddress = "",
                    birthDate = ""
                )


                val action =
                    SignUpFragmentDirections.actionSignUpFragmentToCollectionContactFragment(
                        userModel
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

        binding.textViewSignIn.setOnClickListener {
            if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                goToSignInProfile()
                previousClickTimestamp = SystemClock.uptimeMillis()
            }
        }

        binding.buttonRegister.setOnClickListener {
            if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                goToSignUpExtended()
                previousClickTimestamp = SystemClock.uptimeMillis()
            }
        }

        binding.root.setOnClickListener {
            it.hideKeyboard()
        }

        binding.textInputEditTextPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                    goToSignUpExtended()
                    previousClickTimestamp = SystemClock.uptimeMillis()
                }
            }
            false
        }

        binding.apply {
            textInputEditTextEmail.addTextChangedListener {
                textInputLayoutEmail.error = evaluateErrorMessage(
                    validator.validateEmail(textInputEditTextEmail.text.toString())
                )
            }
            textInputEditTextPassword.addTextChangedListener {
                textInputLayoutPassword.error = evaluateErrorMessage(
                    validator.validatePassword(textInputEditTextPassword.text.toString())
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


    private fun goToSignUpExtended() {
        binding.apply {
            if (isFieldsInvalid()) {
                return
            }

            sharedViewModel.registerUser(
                email = textInputEditTextEmail.text.toString(),
                password = textInputEditTextPassword.text.toString()
            )
        }
    }


    private fun isFieldsInvalid(): Boolean {
        return !binding.textInputLayoutPassword.error.isNullOrEmpty() ||
                binding.textInputEditTextPassword.text.toString().isEmpty() ||
                !binding.textInputLayoutEmail.error.isNullOrEmpty() ||
                binding.textInputEditTextEmail.text.toString().isEmpty()
    }
}



package com.shpp.eorlov.assignment1.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentSignUpBinding
import com.shpp.eorlov.assignment1.ui.SharedViewModel
import com.shpp.eorlov.assignment1.utils.Constants.SUCCESS_RESPONSE_CODE
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce
import com.shpp.eorlov.assignment1.utils.ext.hideKeyboard
import com.shpp.eorlov.assignment1.validator.Validator
import com.shpp.eorlov.assignment1.validator.evaluateErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//todo implement autologin

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {

    @Inject
    lateinit var validator: Validator

    private val viewModel: SignUpViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentSignUpBinding

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
        setViewModelObserver()
        setSharedViewModelObserver()
    }

    private fun setViewModelObserver() {
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
    }

    private fun setSharedViewModelObserver() {
        sharedViewModel.authorizeUser.observe(viewLifecycleOwner) {
            if (it?.code != SUCCESS_RESPONSE_CODE) {
                val action =
                    SignUpFragmentDirections.actionSignUpFragmentToSignUpExtendedFragment(
                        binding.textInputEditTextEmail.text.toString(),
                        binding.textInputEditTextPassword.text.toString(),
                    )
                findNavController().navigate(action)
            } else {
                viewModel.loadEvent.value = Results.EXISTED_ACCOUNT_ERROR
            }
        }
    }

    private fun setListeners() {
        setOnClickListeners()
        setEditTextsListeners()
    }

    private fun setEditTextsListeners() {
        binding.textInputEditTextPassword.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.textInputLayoutPassword.error = evaluateErrorMessage(
                        validator.validatePassword(text.toString())
                    )
                    goToSignUpExtended()
                }
                false
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding.textInputLayoutPassword.error = ""
                }
            }

        }
        binding.textInputEditTextEmail.apply {
            setOnEditorActionListener { _, actionId, _ ->
                binding.textInputLayoutEmail.error = evaluateErrorMessage(
                    validator.validateEmail(text.toString())
                )

                false
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding.textInputLayoutEmail.error = ""
                }
            }
        }
    }

    private fun setOnClickListeners() {
        binding.textViewSignIn.clickWithDebounce {
            goToSignInProfile()
        }

        binding.buttonRegister.clickWithDebounce {
            goToSignUpExtended()
        }

        binding.root.setOnClickListener {
            it.hideKeyboard()
        }
    }

    private fun goToSignInProfile() {
        activity?.onBackPressed()
    }


    private fun goToSignUpExtended() {
        binding.apply {
            if (isFieldsInvalid()) {
                textInputLayoutPassword.error = evaluateErrorMessage(
                    validator.validatePassword(textInputEditTextPassword.text.toString())
                )
                textInputLayoutEmail.error = evaluateErrorMessage(
                    validator.validateEmail(textInputEditTextEmail.text.toString())
                )
                return
            }

            sharedViewModel.authorizeUser(
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



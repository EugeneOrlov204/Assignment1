package com.shpp.eorlov.assignment1.ui.signin

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentSignInBinding
import com.shpp.eorlov.assignment1.ui.MainActivity
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.evaluateErrorMessage
import com.shpp.eorlov.assignment1.utils.ext.hideKeyboard
import com.shpp.eorlov.assignment1.validator.Validator
import javax.inject.Inject
import kotlin.math.abs


class SignInFragment : BaseFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var validator: Validator

    private lateinit var binding: FragmentSignInBinding
    private lateinit var viewModel: SignInViewModel

    private var previousClickTimestamp = SystemClock.uptimeMillis()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).contactComponent.inject(this)

        viewModel =
            ViewModelProvider(this, viewModelFactory)[SignInViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeData()
        setListeners()
        setObservers()
    }

    private fun initializeData() {
        viewModel.initializeData()
        if (!viewModel.getLogin().isNullOrEmpty()
            && !viewModel.getPassword().isNullOrEmpty()
        ) {
            val action =
                SignInFragmentDirections.actionSignInFragmentToCollectionContactFragment(
                    viewModel.getLogin() ?: return
                )
            findNavController().navigate(action)
        }
    }

    private fun setObservers() {
        viewModel.apply {

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

                        Results.NOT_EXISTED_ACCOUNT_ERROR -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.not_existed_account_error_text),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        Results.INVALID_PASSWORD -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.wrong_password),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        else -> {
                        }
                    }

                }
            }
        }
    }

    private fun setListeners() {

        binding.textViewSignUp.setOnClickListener {
            if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                goToSignUpProfile()
                previousClickTimestamp = SystemClock.uptimeMillis()
            }
        }

        binding.buttonLogin.setOnClickListener {
            if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                goToMyProfile()
                previousClickTimestamp = SystemClock.uptimeMillis()
            }
        }

        binding.root.setOnClickListener {
            it.hideKeyboard()
        }

        binding.textInputEditTextPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                    goToMyProfile()
                    previousClickTimestamp = SystemClock.uptimeMillis()
                }
            }
            false
        }

        binding.apply {
            textInputEditTextEmail.addTextChangedListener {
                textInputLayoutEmail.error = evaluateErrorMessage(
                    validator.validateEmail(binding.textInputEditTextEmail.text.toString())
                )
            }
            textInputEditTextPassword.addTextChangedListener {
                textInputLayoutPassword.error = evaluateErrorMessage(
                    validator.validatePassword(binding.textInputEditTextPassword.text.toString())
                )
            }
        }
    }

    private fun goToSignUpProfile() {
        val action =
            SignInFragmentDirections.actionSignInFragmentToAuthFragment()
        findNavController().navigate(action)
    }


    /**
     * Change current activity to MainActivity
     */
    private fun goToMyProfile() {
        val email = binding.textInputEditTextEmail.text.toString()
        if (isFieldsInvalid() ||
            viewModel.isNotExistingAccount(email) ||
            viewModel.isWrongPassword(binding.textInputEditTextPassword.text.toString())
        ) {
            return
        }

        binding.apply {
            if (checkBoxRememberMe.isChecked) {
                viewModel.saveLoginData(
                    email, //Login
                    textInputEditTextPassword.text.toString() //Password
                )
            }
        }

        val action =
            SignInFragmentDirections.actionSignInFragmentToCollectionContactFragment(email)
        findNavController().navigate(action)
    }

    private fun isFieldsInvalid() =
        !binding.textInputLayoutPassword.error.isNullOrEmpty() ||
                binding.textInputEditTextPassword.text.toString().isEmpty() ||
                !binding.textInputLayoutEmail.error.isNullOrEmpty() ||
                binding.textInputEditTextEmail.text.toString().isEmpty()
}



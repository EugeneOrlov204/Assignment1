package com.shpp.eorlov.assignment1.ui.auth

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
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentAuthBinding
import com.shpp.eorlov.assignment1.ui.MainActivity
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.evaluateErrorMessage
import com.shpp.eorlov.assignment1.utils.ext.hideKeyboard
import com.shpp.eorlov.assignment1.validator.Validator
import javax.inject.Inject
import kotlin.math.abs


class AuthFragment : BaseFragment() {
    //todo block create existing account and save existing login data
    //fixme autologin

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var validator: Validator

    private lateinit var binding: FragmentAuthBinding

    //todo save login data

    private lateinit var viewModel: AuthViewModel

    private var previousClickTimestamp = SystemClock.uptimeMillis()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).contactComponent.inject(this)

        viewModel =
            ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restoreLoginData()
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        viewModel.apply {
            userLiveData.observe(viewLifecycleOwner) {
            }
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
                        else -> {
                        }
                    }

                }
            }
        }
    }

    private fun setListeners() {
        binding.buttonRegister.setOnClickListener {
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
            textInputEditTextLogin.addTextChangedListener {
                textInputLayoutLogin.error = evaluateErrorMessage(
                    validator.validateEmail(binding.textInputEditTextLogin.text.toString())
                )
            }
            textInputEditTextPassword.addTextChangedListener {
                textInputLayoutPassword.error = evaluateErrorMessage(
                    validator.validatePassword(binding.textInputEditTextPassword.text.toString())
                )
            }
        }
    }


    private fun restoreLoginData() {
        val login = viewModel.getLogin()
        val password = viewModel.getPassword()
        binding.textInputEditTextLogin.setText(login)
        binding.textInputEditTextPassword.setText(password)
    }


    /**
     * Change current activity to MainActivity
     */
    private fun goToMyProfile() {

        if (isFieldsInvalid()) {
            return
        }

        binding.apply {
            if (checkBoxRememberMe.isChecked) {
                viewModel.saveLoginData(
                    textInputEditTextLogin.text.toString(), //Login
                    textInputEditTextPassword.text.toString() //Password
                )
            }
        }

        val action =
            AuthFragmentDirections.actionAuthFragmentToCollectionContactFragment()
        findNavController().navigate(action)
    }

    private fun isFieldsInvalid() =
        !binding.textInputLayoutPassword.error.isNullOrEmpty() ||
                binding.textInputEditTextPassword.text.toString().isEmpty() ||
                !binding.textInputLayoutLogin.error.isNullOrEmpty() ||
                binding.textInputEditTextLogin.text.toString().isEmpty()
}



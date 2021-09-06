package com.shpp.eorlov.assignment1.ui.signup

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentSignUpBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.retrofit.MainRepository
import com.shpp.eorlov.assignment1.retrofit.RegistrationBody
import com.shpp.eorlov.assignment1.retrofit.RegistrationResponse
import com.shpp.eorlov.assignment1.retrofit.RetrofitService
import com.shpp.eorlov.assignment1.ui.MainActivity
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.ext.hideKeyboard
import com.shpp.eorlov.assignment1.validator.Validator
import com.shpp.eorlov.assignment1.validator.evaluateErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {


    @Inject
    lateinit var validator: Validator

    private val viewModel: SignUpViewModel by viewModels()

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var retrofitService: RetrofitService
    private lateinit var mainRepository: MainRepository
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
        retrofitService = RetrofitService.getInstance()
        mainRepository = MainRepository(retrofitService)
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

                else -> {
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
            val email = textInputEditTextEmail.text.toString()
            if (isFieldsInvalid() ||
                viewModel.isExistingAccount(email)
            ) {
                return
            }


            registerUser()


            val userModel = UserModel(
                email = email,
                name = "",
                profession = "",
                photo = "",
                phoneNumber = "",
                residenceAddress = "",
                birthDate = ""
            )

            val action =
                SignUpFragmentDirections.actionSignUpFragmentToSignUpFragmentExtended(
                    userModel,
                    checkBoxRememberMe.isChecked,
                    textInputEditTextPassword.text.toString()
                )
            findNavController().navigate(action)
        }
    }

    private fun registerUser() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://myserver1.com")
            .build()

        val retrofitService: RetrofitService = retrofit.create(RetrofitService::class.java)

        val registrationBody = RegistrationBody(email = "", password = "")

//        val call: RegistrationResponse = retrofitService.registerUser(registrationBody)

    }


    private fun isFieldsInvalid() =
        !binding.textInputLayoutPassword.error.isNullOrEmpty() ||
                binding.textInputEditTextPassword.text.toString().isEmpty() ||
                !binding.textInputLayoutEmail.error.isNullOrEmpty() ||
                binding.textInputEditTextEmail.text.toString().isEmpty()
}



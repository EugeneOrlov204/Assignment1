package com.shpp.eorlov.assignment1.ui.signin

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
import com.shpp.eorlov.assignment1.databinding.FragmentSignInBinding
import com.shpp.eorlov.assignment1.models.AuthorizationResponseModel
import com.shpp.eorlov.assignment1.models.UserModel
import com.shpp.eorlov.assignment1.ui.SharedViewModel
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.ext.hideKeyboard
import com.shpp.eorlov.assignment1.validator.Validator
import com.shpp.eorlov.assignment1.validator.evaluateErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class SignInFragment : BaseFragment() {


    @Inject
    lateinit var validator: Validator

    private val viewModel: SignInViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var binding: FragmentSignInBinding

    private var previousClickTimestamp = SystemClock.uptimeMillis()


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

    override fun onResume() {
        super.onResume()
        printLog("On resume")
    }

    private fun initializeData() {
        viewModel.initializeData()
        //todo implement autologin
//        if (!viewModel.getLogin().isNullOrEmpty()
//            && !viewModel.getPassword().isNullOrEmpty()
//        ) {
//            val action =
//                SignInFragmentDirections.actionSignInFragmentToCollectionContactFragment(
//                    if (viewModel.getLogin().isNullOrEmpty()) {
//                        viewModel.getUserModelFromStorage()
//                    } else return
//                )
//            findNavController().navigate(action)
//        }
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
            }
        }

        sharedViewModel.authorizeUser.apply {
            observe(viewLifecycleOwner) {
                if (it?.code == Constants.SUCCESS_RESPONSE_CODE && it.data != null) {

                    val userModel = (value as AuthorizationResponseModel).data?.user ?: UserModel(
                        email = "",
                        name = "",
                        profession = "",
                        photo = "",
                        phoneNumber = "",
                        residenceAddress = "",
                        birthDate = ""
                    )

                    println("UserModel is $userModel")


                    val action =
                        SignInFragmentDirections.actionSignInFragmentToCollectionContactFragment(
                            userModel
                        )
                    findNavController().navigate(action)
                } else if (it == null) {
                    viewModel.loadEvent.value = Results.INTERNET_ERROR
                } else {
                    viewModel.loadEvent.value = Results.NOT_EXISTED_ACCOUNT_ERROR
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
            SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        findNavController().navigate(action)
    }


    /**
     * Change current activity to MainActivity
     */
    private fun goToMyProfile() {
        val email = binding.textInputEditTextEmail.text.toString()
        val password = binding.textInputEditTextPassword.text.toString()
        if (isFieldsInvalid()) {
            return
        }

        //todo implement autologin
        binding.apply {
//            if (checkBoxRememberMe.isChecked) {
//                viewModel.saveLoginData(
//                    email, //Login
//                    textInputEditTextPassword.text.toString() //Password
//                )
//            }
        }

        sharedViewModel.authorizeUser(email, password)
    }

    private fun isFieldsInvalid() =
        !binding.textInputLayoutPassword.error.isNullOrEmpty() ||
                binding.textInputEditTextPassword.text.toString().isEmpty() ||
                !binding.textInputLayoutEmail.error.isNullOrEmpty() ||
                binding.textInputEditTextEmail.text.toString().isEmpty()
}



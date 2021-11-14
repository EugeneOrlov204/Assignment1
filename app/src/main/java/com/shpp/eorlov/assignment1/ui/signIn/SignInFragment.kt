package com.shpp.eorlov.assignment1.ui.signIn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentSignInBinding
import com.shpp.eorlov.assignment1.ui.signUp.SignUpFragment
import com.shpp.eorlov.assignment1.ui.viewPager.CollectionContactFragment
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Constants.INVALID_CREDENTIALS_CODE
import com.shpp.eorlov.assignment1.utils.FeatureNavigationEnabled.featureNavigationEnabled
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce
import com.shpp.eorlov.assignment1.utils.ext.hideKeyboard
import com.shpp.eorlov.assignment1.validator.Validator
import com.shpp.eorlov.assignment1.validator.evaluateErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : BaseFragment() {

    //fixme remove all inject from fragments
    @Inject
    lateinit var validator: Validator

    private val viewModel: SignInViewModel by viewModels()

    private lateinit var binding: FragmentSignInBinding

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

        //todo fix autologin
//        if (viewModel.isRememberedUser()) {
//            if(featureNavigationEnabled) {
//                val action =
//                    SignInFragmentDirections.actionSignInFragmentToCollectionContactFragment()
//                findNavController().navigate(action)
//            } else {
//        val fragmentManager = activity?.supportFragmentManager
//        fragmentManager?.commit {
//            setReorderingAllowed(true)
//            add<>(R.id.fragmentContainerView)
//        }
//            }
//        }
    }

    private fun setObservers() {
        viewModel.apply {
            loadEventLiveData.apply {
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
                        Results.INVALID_CREDENTIALS -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.invalid_credentials),
                                Toast.LENGTH_LONG
                            ).show()
                            unlockUI()
                            binding.contentLoadingProgressBar.isVisible = false
                        }
                        Results.INTERNET_ERROR -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.internet_error),
                                Toast.LENGTH_LONG
                            ).show()
                            unlockUI()
                            binding.contentLoadingProgressBar.isVisible = false
                        }
                        Results.UNEXPECTED_RESPONSE -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.unexpected_response),
                                Toast.LENGTH_LONG
                            ).show()
                            unlockUI()
                            binding.contentLoadingProgressBar.isVisible = false
                        }
                        Results.NOT_SUCCESSFUL_RESPONSE -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.not_successful_response),
                                Toast.LENGTH_LONG
                            ).show()
                            unlockUI()
                            binding.contentLoadingProgressBar.isVisible = false
                        }
                        Results.NOT_EXISTED_ACCOUNT_ERROR -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.not_existed_account_error_text),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        else -> {
                        }
                    }

                }
            }
        }

        //todo remove redundant when's error checkers
        viewModel.authorizeUserLiveData.apply {
            observe(viewLifecycleOwner) {
                when {
                    it?.code == Constants.SUCCESS_RESPONSE_CODE -> {
                        viewModel.rememberCurrentEmail(binding.textInputEditTextEmail.text.toString())
                        viewModel.saveToken(it.data.accessToken)
                        viewModel.loadEventLiveData.value = Results.OK
                        if (featureNavigationEnabled) {
                            val action =
                                SignInFragmentDirections.actionSignInFragmentToCollectionContactFragment()
                            findNavController().navigate(action)
                        } else {
                            val fragmentManager = activity?.supportFragmentManager
                            fragmentManager?.commit {
                                setReorderingAllowed(true)
                                replace(R.id.fragmentContainerView, CollectionContactFragment())
                            }
                        }
                    }
                    it == null -> {
                        viewModel.loadEventLiveData.value = Results.INTERNET_ERROR
                    }
                    it.code == INVALID_CREDENTIALS_CODE -> {
                        viewModel.loadEventLiveData.value = Results.INVALID_CREDENTIALS
                    }
                    else -> {
                        viewModel.loadEventLiveData.value = Results.NOT_EXISTED_ACCOUNT_ERROR
                    }
                }
            }
        }
    }

    private fun setListeners() {

        binding.textViewSignUp.clickWithDebounce {
            goToSignUpProfile()
        }

        binding.buttonLogin.clickWithDebounce {
            goToMyProfile()
        }

        binding.root.setOnClickListener {
            it.hideKeyboard()
        }
        binding.textInputEditTextPassword.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.textInputLayoutPassword.error = evaluateErrorMessage(
                        validator.validatePassword(text.toString())
                    )
                    goToMyProfile()
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
            setOnEditorActionListener { _, _, _ ->
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

    private fun goToSignUpProfile() {
        if (featureNavigationEnabled) {
            val action =
                SignInFragmentDirections.actionSignInFragmentToSignUpFragment()

            findNavController().navigate(action)
        } else {
            val fragmentManager = activity?.supportFragmentManager
            fragmentManager?.commit {
                setReorderingAllowed(true)
                replace(R.id.fragmentContainerView, SignUpFragment())
                addToBackStack(null)
            }
        }
    }

    /**
     * Change current activity to MainActivity
     */
    private fun goToMyProfile() {
        binding.apply {
            val email = textInputEditTextEmail.text.toString()
            val password = textInputEditTextPassword.text.toString()
            if (isFieldsInvalid()) {
                textInputLayoutPassword.error = evaluateErrorMessage(
                    validator.validatePassword(textInputEditTextPassword.text.toString())
                )

                textInputLayoutEmail.error = evaluateErrorMessage(
                    validator.validateEmail(textInputEditTextEmail.text.toString())
                )

                return
            }

            if (checkBoxRememberMe.isChecked) {
                viewModel.saveLoginData(
                    email, //Login
                    textInputEditTextPassword.text.toString() //Password
                )
            } else {
                viewModel.clearLoginData()
            }

            viewModel.loadEventLiveData.value = Results.LOADING
            viewModel.authorizeUser(email, password)
        }
    }

    private fun isFieldsInvalid() =
        !binding.textInputLayoutPassword.error.isNullOrEmpty() ||
                binding.textInputEditTextPassword.text.toString().isEmpty() ||
                !binding.textInputLayoutEmail.error.isNullOrEmpty() ||
                binding.textInputEditTextEmail.text.toString().isEmpty()
}







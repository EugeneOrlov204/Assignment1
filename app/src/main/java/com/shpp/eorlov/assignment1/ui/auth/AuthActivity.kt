package com.shpp.eorlov.assignment1.ui.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.shpp.eorlov.assignment1.databinding.ActivityAuthBinding
import com.shpp.eorlov.assignment1.ui.MainActivity
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.evaluateErrorMessage
import com.shpp.eorlov.assignment1.validator.Validator


class AuthActivity : AppCompatActivity() {
    //todo implement login design, block create existing account and save existing login data
    //fixme design register

    private lateinit var binding: ActivityAuthBinding
    private lateinit var settings: SharedPreferences
    private lateinit var validator: Validator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)

        initializeData()
        restoreLoginData()
        restoreUIElementsLogic()
        setListeners()

        setContentView(binding.root)
    }

    private fun setListeners() {
        binding.buttonRegister.setOnClickListener {
            goToMyProfile()
        }
    }

    /**
     * Restores UI elements states.
     * For example the button has become enable
     */
    private fun restoreUIElementsLogic() {
        binding.buttonRegister.isEnabled = true
    }

    private fun restoreLoginData() {
        val login = settings.getString(Constants.PREF_LOGIN, "")
        val password = settings.getString(Constants.PREF_PASSWORD, "")
        binding.textInputEditTextLogin.setText(login)
        binding.textInputEditTextPassword.setText(password)
    }

    /**
     * Initialized objects that contains information about login and password
     */
    private fun initializeData() {
        validator = Validator(this)
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
        settings = getPreferences(MODE_PRIVATE)
    }


    /**
     * Change current activity to MainActivity
     */
    private fun goToMyProfile() {

        if (!binding.textInputLayoutPassword.error.isNullOrEmpty() ||
            !binding.textInputLayoutLogin.error.isNullOrEmpty()
        ) {
            return
        }

        val intent = Intent(this, MainActivity::class.java)
        binding.apply {
            intent.putExtra("personName", getPersonName(textInputEditTextLogin.text.toString()))

            val rememberMe = checkBoxRememberMe
            if (rememberMe.isChecked) {
                saveLoginData(
                    textInputEditTextLogin.text.toString(), //Login
                    textInputEditTextPassword.text.toString() //Password
                )
            } else {
                settings.edit().clear().apply()
            }

            buttonRegister.isEnabled = false
        }

        finish()
        startActivity(intent)
    }

    private fun saveLoginData(login: String, password: String) {
        val prefEditor = settings.edit()
        prefEditor.putString(Constants.PREF_LOGIN, login)
        prefEditor.putString(Constants.PREF_PASSWORD, password)
        prefEditor.apply()
    }

    /**
     * Returns parsed login in the format "Name Surname"
     */
    private fun getPersonName(login: String): String {
        //Removes part of login from '@' to the end
        val parsedLogin = login.replace(Regex("@+.*"), "")

        val pattern = Regex("[^.]+")

        //Gets part of login before '.'
        val name = StringBuilder(pattern.find(parsedLogin, 0)?.value ?: "")

        if (name.equals("")) return ""

        //Gets part of login after '.' if it exists
        val surname = StringBuilder(
            parsedLogin
                .replace("$name", "")
                .replace(".", "")
        )
        //Makes first letters in upper case
        name[0] = name[0].uppercaseChar()
        if (surname.isNotEmpty()) surname[0] = surname[0].uppercaseChar()
        return "$name $surname"
    }
}
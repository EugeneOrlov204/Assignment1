package com.shpp.eorlov.assignment1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shpp.eorlov.assignment1.databinding.ActivityAuthBinding
import com.shpp.eorlov.assignment1.utils.Constants


class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    private lateinit var emailErrorMessage: TextInputLayout
    private lateinit var emailField: TextInputEditText

    private lateinit var passwordErrorMessage: TextInputLayout
    private lateinit var passwordFieldEditText: TextInputEditText

    private lateinit var settings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)

        initializeData()
        restoreLoginData()

        binding.buttonRegister.setOnClickListener {
            goToMainActivity()
        }

        setContentView(binding.root)
    }

    private fun restoreLoginData() {
        val login = settings.getString(Constants.PREF_LOGIN, "")
        val password = settings.getString(Constants.PREF_PASSWORD, "")
        emailField.setText(login)
        passwordFieldEditText.setText(password)
    }

    /**
     * Initialized objects that contains information about email and password
     */
    private fun initializeData() {
        emailErrorMessage = binding.textInputLayoutEmail
        emailField = binding.textInputEditTextEmail
        emailField.addTextChangedListener {
            validateEmail()
        }

        passwordErrorMessage = binding.textInputLayoutPassword
        passwordFieldEditText = binding.textInputEditTextPassword
        passwordFieldEditText.addTextChangedListener {
            validatePassword()
        }

        settings = getPreferences(MODE_PRIVATE)
    }


    private fun validatePassword(): Boolean {
        if (passwordFieldEditText.text.toString().trim { it <= ' ' }.isEmpty()) {
            passwordErrorMessage.error = "Password is required"
            return false
        } else if (passwordFieldEditText.text.toString().length < 6) {
            passwordErrorMessage.error = "Password can't be less than 6 digit"
            return false
        } else {
            passwordErrorMessage.error = ""
        }
        return true
    }

    fun validateEmail(): Boolean {
        if (emailField.text.toString().trim { it <= ' ' }.isEmpty()) {
            emailErrorMessage.error = ""
        } else {
            val emailId = emailField.text.toString()
            val isValid = Patterns.EMAIL_ADDRESS.matcher(emailId).matches()
            if (!isValid) {
                emailErrorMessage.error = "Invalid Email address, ex: abc@example.com"
                return false
            } else {
                emailErrorMessage.error = ""
            }
        }
        return true
    }


    /**
     * Change current activity to MainActivity
     */
    private fun goToMainActivity() {

        if (!validateEmail() || !validatePassword()) {
            return
        }

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("personName", getPersonName())

        val rememberMe = binding.checkBoxRememberMe
        if (rememberMe.isChecked) {
            saveLoginData(
                emailField.text.toString(), //Login
                passwordFieldEditText.text.toString() //Password
            )
        }

        startActivity(intent)
        finish()
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
    private fun getPersonName(): String {
        //Removes part of login from '@' to the end
        val email = emailField.text.toString().replace(Regex("@+.*"), "")

        val pattern = Regex("[^.]+")

        //Gets part of login before '.'
        val name = StringBuilder(pattern.find(email, 0)!!.value)

        //Gets part of login after '.' if it exists
        val surname = StringBuilder(
            email
                .replace("$name", "")
                .replace(".", "")
        )
        //Makes first letters in upper case
        name[0] = name[0].toUpperCase()
        if (surname.isNotEmpty()) surname[0] = surname[0].toUpperCase()
        return "$name $surname"
    }
}
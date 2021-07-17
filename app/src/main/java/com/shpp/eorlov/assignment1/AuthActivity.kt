package com.shpp.eorlov.assignment1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shpp.eorlov.assignment1.databinding.ActivityAuthBinding
import com.shpp.eorlov.assignment1.utils.Constants


class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    private lateinit var emailErrorMessageLayout: TextInputLayout
    private lateinit var emailEditText: TextInputEditText

    private lateinit var passwordErrorMessageLayout: TextInputLayout
    private lateinit var passwordEditText: TextInputEditText

    private lateinit var settings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)

        initializeData()
        restoreLoginData()
        restoreUIElementsLogic()
        binding.buttonRegister.setOnClickListener {
            goToMainActivity()
        }

        setContentView(binding.root)
    }

    /**
     * Restores UI elements states.
     * For example button the button has become enable
     */
    private fun restoreUIElementsLogic() {
        binding.buttonRegister.isEnabled = true
    }

    private fun restoreLoginData() {
        val login = settings.getString(Constants.PREF_LOGIN, "")
        val password = settings.getString(Constants.PREF_PASSWORD, "")
        emailEditText.setText(login)
        passwordEditText.setText(password)
    }

    /**
     * Initialized objects that contains information about email and password
     */
    private fun initializeData() {
        emailErrorMessageLayout = binding.textInputLayoutEmail
        emailEditText = binding.textInputEditTextEmail
        emailEditText.addTextChangedListener {
            validateEmail()
        }

        passwordErrorMessageLayout = binding.textInputLayoutPassword
        passwordEditText = binding.textInputEditTextPassword
        passwordEditText.addTextChangedListener {
            validatePassword()
        }

        settings = getPreferences(MODE_PRIVATE)
    }


    private fun validatePassword(): Boolean {
        if (passwordEditText.text.toString().trim { it <= ' ' }.isEmpty()) {
            passwordErrorMessageLayout.error = "Password is required"
            return false
        } else if (passwordEditText.text.toString().length < 6) {
            passwordErrorMessageLayout.error = "Password can't be less than 6 digit"
            return false
        } else {
            passwordErrorMessageLayout.error = ""
        }
        return true
    }

    private fun validateEmail(): Boolean {
        if (emailEditText.text.toString().trim { it <= ' ' }.isEmpty()) {
            emailErrorMessageLayout.error = ""
        } else {
            val emailId = emailEditText.text.toString()
            val isValid = Patterns.EMAIL_ADDRESS.matcher(emailId).matches()
            if (!isValid) {
                emailErrorMessageLayout.error = "Invalid Email address, ex: abc@example.com"
                return false
            } else {
                emailErrorMessageLayout.error = ""
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
        intent.putExtra("personName", getPersonName(emailEditText.text.toString()))

        val rememberMe = binding.checkBoxRememberMe
        if (rememberMe.isChecked) {
            saveLoginData(
                emailEditText.text.toString(), //Login
                passwordEditText.text.toString() //Password
            )
        } else {
            settings.edit().clear().apply()
        }

        binding.buttonRegister.isEnabled = false

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
    private fun getPersonName(email : String): String {
        //Removes part of login from '@' to the end
        val login = email.replace(Regex("@+.*"), "")

        val pattern = Regex("[^.]+")

        //Gets part of login before '.'
        val name = StringBuilder(pattern.find(login, 0)?.value ?: "")

        if (name.equals("")) return ""

        //Gets part of login after '.' if it exists
        val surname = StringBuilder(
            login
                .replace("$name", "")
                .replace(".", "")
        )
        //Makes first letters in upper case
        name[0] = name[0].toUpperCase()
        if (surname.isNotEmpty()) surname[0] = surname[0].toUpperCase()
        return "$name $surname"
    }
}
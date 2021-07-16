package com.shpp.eorlov.assignment1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.log


class AuthActivity : AppCompatActivity() {

    private var emailErrorMessage: TextInputLayout? = null
    private var emailField: TextInputEditText? = null

    private var passwordErrorMessage: TextInputLayout? = null
    private var passwordField: TextInputEditText? = null

    private val PREF_LOGIN = "Login"
    private val PREF_PASSWORD = "Password"

    private lateinit var settings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        initializeData()
        restoreLoginData()
    }

    private fun restoreLoginData() {
        val login = settings.getString(PREF_LOGIN, "")
        val password = settings.getString(PREF_PASSWORD, "")
        emailField?.setText(login)
        passwordField?.setText(password)

        //Clears shared preferences
        settings.edit().clear().apply()
    }

    /**
     * Initialized objects that contains information about email and password
     */
    private fun initializeData() {
        emailErrorMessage = findViewById(R.id.text_input_layout_email)
        emailField = findViewById(R.id.text_input_edit_text_email)
        emailField?.addTextChangedListener(ValidationTextWatcher(emailField!!))

        passwordErrorMessage = findViewById(R.id.text_input_layout_password)
        passwordField = findViewById(R.id.text_input_edit_text_password)
        passwordField?.addTextChangedListener(ValidationTextWatcher(passwordField!!))

        settings = getPreferences(MODE_PRIVATE)
    }

    /**
     * Specify an explicit soft input mode to use for the window,
     * as per WindowManager.LayoutParams.softInputMode.
     */
    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    private fun validatePassword(): Boolean {
        if (passwordField!!.text.toString().trim { it <= ' ' }.isEmpty()) {
            passwordErrorMessage!!.error = "Password is required"
            requestFocus(passwordField!!)
            return false
        } else if (passwordField!!.text.toString().length < 6) {
            passwordErrorMessage!!.error = "Password can't be less than 6 digit"
            requestFocus(passwordField!!)
            return false
        } else {
            passwordErrorMessage!!.isErrorEnabled = false
        }
        return true
    }

    fun validateEmail(): Boolean {
        if (emailField!!.text.toString().trim { it <= ' ' }.isEmpty()) {
            emailErrorMessage!!.isErrorEnabled = false
        } else {
            val emailId = emailField!!.text.toString()
            val isValid = Patterns.EMAIL_ADDRESS.matcher(emailId).matches()
            if (!isValid) {
                emailErrorMessage!!.error = "Invalid Email address, ex: abc@example.com"
                requestFocus(emailField!!)
                return false
            } else {
                emailErrorMessage!!.isErrorEnabled = false
            }
        }
        return true
    }


    /**
     * Change current activity to MainActivity
     */
    fun goToMainActivity(view: View) {

        if (!validateEmail() || !validatePassword()) {
            return
        }
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("personName", getPersonName())

        val rememberMe = findViewById<CheckBox>(R.id.check_box_remember_me)
        if (rememberMe.isChecked) {
            saveLoginData(
                emailField!!.text.toString(), //Login
                passwordField!!.text.toString() //Password
            )
        }

        startActivity(intent)
        finish()
    }

    private fun saveLoginData(login: String, password: String) {
        val prefEditor = settings.edit()
        prefEditor.putString(PREF_LOGIN, login)
        prefEditor.putString(PREF_PASSWORD, password)
        prefEditor.apply()
    }

    /**
     * Returns parsed login in the format "Name Surname"
     */
    private fun getPersonName(): String {
        //Removes part of login from '@' to the end
        val email = emailField!!.text.toString().replace(Regex("@+.*"), "")

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
        if (!surname.isEmpty()) surname[0] = surname[0].toUpperCase()
        return "$name $surname"
    }


    /**
     * Class to keep track of each character of input text fields
     * for email and password validation
     */
    inner class ValidationTextWatcher constructor(private val view: View) :
        TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            when (view.id) {
                R.id.text_input_edit_text_email -> validateEmail()
                R.id.text_input_edit_text_password -> validatePassword()
            }
        }
    }
}
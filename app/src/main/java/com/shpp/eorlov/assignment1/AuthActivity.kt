package com.shpp.eorlov.assignment1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class AuthActivity : AppCompatActivity() {

    var emailErrorMessage: TextInputLayout? = null
    var emailField: TextInputEditText? = null

    var passwordErrorMessage: TextInputLayout? = null
    var passwordField: TextInputEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        initializeData()
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

        startActivity(Intent(this, MainActivity::class.java))
        finish()
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
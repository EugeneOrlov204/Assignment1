package com.shpp.eorlov.assignment1.validator

import android.content.Context
import android.util.Patterns
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.utils.*
import com.shpp.eorlov.assignment1.utils.Constants.DATE_FORMAT
import com.shpp.eorlov.assignment1.utils.Constants.DATE_REGEX_PATTERN
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class Validator @Inject constructor(private val context: Context?) {


    fun validateEmail(
        _value: String
    ): ValidationError {
        return if (_value.trim { it <= ' ' }.isEmpty()) {
            EmptyFieldError(context?.getString(R.string.empty_field) ?: "")
        } else {
            val isValid = Patterns.EMAIL_ADDRESS.matcher(_value).matches()
            if (!isValid) {
                InvalidEmail(context?.getString(R.string.invalid_email) ?: "")
            } else {
                NotAnError
            }
        }
    }

    fun checkIfFieldIsNotEmpty(
        _value: String
    ): ValidationError {
        return if (_value.trim { it <= ' ' }.isEmpty()) {
            EmptyFieldError(context?.getString(R.string.empty_field) ?: "")
        } else {
            NotAnError
        }
    }

    fun validatePhoneNumber(
        _value: String
    ): ValidationError {
        return if (_value.trim { it <= ' ' }.isEmpty()) {
            NotAnError

        } else {
            val isValid = Patterns.PHONE.matcher(_value).matches()
            if (!isValid) {
                InvalidPhoneNumber(context?.getString(R.string.invalid_phone_number) ?: "")
            } else {
                NotAnError
            }
        }
    }

    fun validateBirthdate(
        _value: String
    ): ValidationError {
        return if (_value.trim { it <= ' ' }.isEmpty()) {
            EmptyFieldError(context?.getString(R.string.empty_field) ?: "")
        } else {
            val isValid = _value.matches(
                Regex(DATE_REGEX_PATTERN)
            )
            if (!isValid) {
                InvalidBirthdate(context?.getString(R.string.invalid_birthdate) ?: "")
            } else {
                val format = SimpleDateFormat(DATE_FORMAT, Locale.CANADA_FRENCH)
                try {
                    format.parse(_value)
                    NotAnError
                } catch (e: ParseException) {
                    InvalidBirthdate(context?.getString(R.string.invalid_birthdate) ?: "")
                }
            }
        }
    }

    fun validatePassword(_value: String): ValidationError {
        return if (_value.trim { it <= ' ' }.isEmpty()) {
            EmptyFieldError(context?.getString(R.string.empty_field) ?: "")
        } else {
            val isValid = _value.length >= Constants.MIN_SIZE_OF_PASSWORD
            if (!isValid) {
                InvalidPassword(context?.getString(R.string.invalid_password) ?: "")
            } else {
                NotAnError
            }
        }
    }
}
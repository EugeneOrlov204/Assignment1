package com.shpp.eorlov.assignment1.ui.contact_dialog_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.models.UserModel
import com.shpp.eorlov.assignment1.validator.ValidateOperation
import com.shpp.eorlov.assignment1.validator.evaluateErrorMessage
import com.shpp.eorlov.assignment1.validator.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactDialogViewModel @Inject constructor() : ViewModel() {

    val newUser = MutableLiveData<UserModel>()

    @Inject
    lateinit var validator: Validator

    /**
     * Adds item to dataset in the end of list
     */
    fun addItem(user: UserModel) {
        newUser.value = user
    }

    /**
     * Return true if all field in add contact's dialog are valid, otherwise false
     */
    fun isValidField(text: String, validateOperation: ValidateOperation): String {
        return getErrorMessage(text, validateOperation)
    }

    /**
     * Returns empty string if given edit text has valid input
     * otherwise returns error message
     */
    private fun getErrorMessage(
        text: String,
        validateOperation: ValidateOperation
    ): String {
        val validationError = when (validateOperation) {
            ValidateOperation.EMAIL -> validator.validateEmail(text)
            ValidateOperation.PHONE_NUMBER -> validator.validatePhoneNumber(text)
            ValidateOperation.BIRTHDAY -> validator.validateBirthdate(text)
            else -> validator.checkIfFieldIsNotEmpty(text)
        }
        return evaluateErrorMessage(validationError)
    }
}
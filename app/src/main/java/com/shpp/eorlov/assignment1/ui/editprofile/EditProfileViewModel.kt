package com.shpp.eorlov.assignment1.ui.editprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.validator.ValidateOperation
import com.shpp.eorlov.assignment1.validator.Validator
import com.shpp.eorlov.assignment1.validator.evaluateErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val validator: Validator
) : ViewModel() {

    val userLiveData: MutableLiveData<UserModel> by lazy(LazyThreadSafetyMode.NONE) {
        MutableLiveData(
            UserModel(
                name = "",
                career = "",
                photo = "",
                address = "",
                birthday = "",
                phone = "",
                email = ""
            )
        )
    }

    val loadEvent = MutableLiveData<Results>()

    fun initializeData(userModel: UserModel) {
        if (userLiveData.value == null) {
            loadEvent.value = Results.INITIALIZE_DATA_ERROR
        } else {
            loadEvent.value = Results.LOADING

            userLiveData.value = userModel

            loadEvent.value = Results.OK
        }
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
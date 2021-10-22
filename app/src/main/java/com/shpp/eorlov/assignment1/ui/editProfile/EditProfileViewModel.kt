package com.shpp.eorlov.assignment1.ui.editProfile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.eorlov.assignment1.data.storage.SharedPreferencesStorageImpl
import com.shpp.eorlov.assignment1.model.Data
import com.shpp.eorlov.assignment1.model.EditUserModel
import com.shpp.eorlov.assignment1.model.ResponseModel
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.repository.MainRepositoryImpl
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.validator.ValidateOperation
import com.shpp.eorlov.assignment1.validator.Validator
import com.shpp.eorlov.assignment1.validator.evaluateErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val validator: Validator,
    private val repository: MainRepositoryImpl,
    private val storage: SharedPreferencesStorageImpl
) : ViewModel() {

    val editUserLiveData = MutableLiveData<ResponseModel<Data?>>()
    val userLiveData: MutableLiveData<UserModel> by lazy(LazyThreadSafetyMode.NONE) {
        MutableLiveData(
            UserModel(
                name = "",
                career = "",
                image = "",
                address = "",
                birthday = "",
                phone = "",
                email = ""
            )
        )
    }

    val loadEventLiveData = MutableLiveData<Results>()

    private var accessToken: String = ""
        get() = fetchToken(field)

    fun initializeData(userModel: UserModel) {
        if (userLiveData.value == null) {
            loadEventLiveData.value = Results.INITIALIZE_DATA_ERROR
        } else {
            userLiveData.value = userModel
        }
    }

    fun editUser(user: UserModel) {
        viewModelScope.launch {
            loadEventLiveData.value = Results.LOADING
            val response = try {
                repository.editUser(
                    EditUserModel(
                        user = UserModel(
                            name = user.name,
                            phone = user.phone,
                            address = user.address,
                            career = user.career,
                            birthday = user.birthday,
                            image = user.image,
                            email = user.email
                        )
                    ),
                    accessToken = "Bearer $accessToken"
                )
            } catch (exception: IOException) {
                loadEventLiveData.value = Results.INTERNET_ERROR
                return@launch
            } catch (exception: HttpException) {
                loadEventLiveData.value = Results.UNEXPECTED_RESPONSE
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                editUserLiveData.postValue(response.body()!!)
            } else {
                loadEventLiveData.value = Results.NOT_SUCCESSFUL_RESPONSE
            }
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

    private fun fetchToken(token: String): String {
        return if (token.isEmpty()) {
            storage.getString(Constants.ACCESS_TOKEN) ?: ""
        } else token
    }
}
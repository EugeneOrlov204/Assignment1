package com.shpp.eorlov.assignment1.ui.signUpExtended

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.eorlov.assignment1.data.storage.SharedPreferencesStorageImpl
import com.shpp.eorlov.assignment1.model.*
import com.shpp.eorlov.assignment1.repository.MainRepositoryImpl
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Constants.CURRENT_EMAIL
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.validator.ValidationError
import com.shpp.eorlov.assignment1.validator.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignUpExtendedViewModel @Inject constructor(
    private val storage: SharedPreferencesStorageImpl,
    private val repository: MainRepositoryImpl,
    private val validator: Validator
) :
    ViewModel() {

    val registerUserLiveData = MutableLiveData<ResponseModel<Data?>>()
    val loadEventLiveData = MutableLiveData<Results>()
    val editUserLiveData = MutableLiveData<ResponseModel<Data?>>()

    fun editUser(
        user: UserModel,
        accessToken: String
    ) {
        viewModelScope.launch {
            loadEventLiveData.value = Results.LOADING
            val response = try {
                repository.editUser(
                    EditUserModel(user),
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

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            loadEventLiveData.value = Results.LOADING
            val response = try {
                repository.registerUser(
                    RegisterModel(
                        email,
                        password
                    )
                )
            } catch (exception: IOException) {
                loadEventLiveData.value = Results.INTERNET_ERROR
                return@launch
            } catch (exception: HttpException) {
                loadEventLiveData.value = Results.UNEXPECTED_RESPONSE
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                registerUserLiveData.postValue(response.body()!!)
            } else {
                loadEventLiveData.value = Results.NOT_SUCCESSFUL_RESPONSE
            }

        }
    }

    fun initializeData() {
        loadEventLiveData.value = Results.OK
    }

    fun rememberCurrentEmail(email: String) {
        storage.save(CURRENT_EMAIL, email)
    }

    fun saveToken(accessToken: String) {
        storage.save(Constants.ACCESS_TOKEN, accessToken)
    }

    fun validatePhoneNumber(phoneNumber: String): ValidationError {
        return validator.validatePhoneNumber(phoneNumber)
    }

    fun validateUserName(userName: String): ValidationError {
        return validator.validateUserName(userName)
    }
}
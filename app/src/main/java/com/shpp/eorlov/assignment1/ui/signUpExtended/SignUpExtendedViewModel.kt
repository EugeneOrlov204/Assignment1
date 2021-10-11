package com.shpp.eorlov.assignment1.ui.signUpExtended

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.eorlov.assignment1.data.storage.SharedPreferencesStorageImpl
import com.shpp.eorlov.assignment1.model.Data
import com.shpp.eorlov.assignment1.model.EditUserModel
import com.shpp.eorlov.assignment1.model.RegisterModel
import com.shpp.eorlov.assignment1.model.ResponseModel
import com.shpp.eorlov.assignment1.repository.MainRepositoryImpl
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Constants.CURRENT_EMAIL
import com.shpp.eorlov.assignment1.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignUpExtendedViewModel @Inject constructor(
    private val storage: SharedPreferencesStorageImpl,
    private val repository: MainRepositoryImpl
) :
    ViewModel() {

    val registerUserLiveData = MutableLiveData<ResponseModel<Data?>>()
    val loadEventLiveData = MutableLiveData<Results>()
    val editUserLiveData = MutableLiveData<ResponseModel<Data?>>()

    fun editUser(
        name: String?,
        phone: String?,
        address: String?,
        career: String?,
        birthday: String?,
        image: String?,
        accessToken: String
    ) {
        viewModelScope.launch {
            loadEventLiveData.value = Results.LOADING
            val response = try {
                repository.editUser(
                    EditUserModel(
                        name,
                        phone,
                        address,
                        career,
                        birthday,
                        image
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
}
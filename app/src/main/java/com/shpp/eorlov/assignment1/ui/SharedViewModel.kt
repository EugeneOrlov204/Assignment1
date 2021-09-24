package com.shpp.eorlov.assignment1.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.eorlov.assignment1.models.*
import com.shpp.eorlov.assignment1.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val newUser = MutableLiveData<UserModel?>(null)
    val updatedUser = MutableLiveData<UserModel?>(null)
    val registerUser = MutableLiveData<RegistrationResponseModel>()
    val authorizeUser = MutableLiveData<AuthorizationResponseModel>()
    val getUser = MutableLiveData<GetUserResponseModel>()

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            registerUser.postValue(
                repository.registerUser(
                    RegisterModel(
                        email,
                        password
                    )
                )
            )
        }
    }

    fun authorizeUser(email: String, password: String) {
        viewModelScope.launch {
            authorizeUser.postValue(
                repository.authorizeUser(
                    AuthorizeModel(
                        email,
                        password
                    )
                )
            )
        }
    }

    fun getUser(accessToken: String) {
        viewModelScope.launch {
            getUser.postValue(
                repository.getUser(accessToken = "Bearer $accessToken")
            )
        }
    }
}
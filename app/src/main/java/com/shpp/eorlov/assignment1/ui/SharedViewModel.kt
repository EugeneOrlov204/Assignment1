package com.shpp.eorlov.assignment1.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.eorlov.assignment1.api.MainService
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(val repository: MainRepository) : ViewModel() {
    val newUser = MutableLiveData<UserModel?>(null)
    val updatedUser = MutableLiveData<UserModel?>(null)
    val registerUser = MutableLiveData<String>()

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            registerUser.postValue(
                repository.registerUser(
                    MainService.RegisterModel(
                        email,
                        password
                    )
                )
            )
        }
    }
}
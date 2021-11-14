package com.shpp.eorlov.assignment1.ui.signUp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.eorlov.assignment1.model.AuthorizeModel
import com.shpp.eorlov.assignment1.repository.MainRepositoryImpl
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.SingleLiveEvent
import com.shpp.eorlov.assignment1.validator.ValidationError
import com.shpp.eorlov.assignment1.validator.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: MainRepositoryImpl,
    private val validator: Validator
) :
    ViewModel() {

    val canRegisterUserLiveData = SingleLiveEvent<Boolean>()
    val loadEventLiveData = MutableLiveData<Results>()

    fun registerUser(email: String, password: String) {
        loadEventLiveData.value = Results.LOADING
        viewModelScope.launch {
            val response = try {
                repository.authorizeUser(
                    AuthorizeModel(
                        email,
                        password
                    )
                )
            } catch (e: IOException) {
                loadEventLiveData.value = Results.INTERNET_ERROR
                return@launch
            } catch (e: HttpException) {
                loadEventLiveData.value = Results.UNEXPECTED_RESPONSE
                return@launch
            }

            if (!response.isSuccessful || response.body() == null) {
                canRegisterUserLiveData.postValue(true)
            }
        }
    }

    fun validatePassword(password: String): ValidationError {
        return validator.validatePassword(password)
    }

    fun validateEmail(email: String): ValidationError {
        return validator.validateEmail(email)
    }
}
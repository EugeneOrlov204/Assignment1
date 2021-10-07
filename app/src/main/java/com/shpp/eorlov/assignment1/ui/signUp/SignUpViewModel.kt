package com.shpp.eorlov.assignment1.ui.signUp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.eorlov.assignment1.model.AuthorizeModel
import com.shpp.eorlov.assignment1.model.Data
import com.shpp.eorlov.assignment1.model.ResponseModel
import com.shpp.eorlov.assignment1.repository.MainRepositoryImpl
import com.shpp.eorlov.assignment1.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: MainRepositoryImpl //fixme move all Impl classes to interfaces
) :
    ViewModel() {

    val authorizeUserLiveData = MutableLiveData<ResponseModel<Data>>()

    fun authorizeUser(email: String, password: String) {
        viewModelScope.launch {
            loadEvent.value = Results.LOADING
            val response = try {
                repository.authorizeUser(
                    AuthorizeModel(
                        email,
                        password
                    )
                )
            } catch (e: IOException) {
                loadEvent.value = Results.INTERNET_ERROR
                return@launch
            } catch (e: HttpException) {
                loadEvent.value = Results.UNEXPECTED_RESPONSE
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                authorizeUserLiveData.postValue(response.body()!!)
            } else {
                loadEvent.value = Results.NOT_SUCCESSFUL_RESPONSE
            }
        }
    }

    val loadEvent = MutableLiveData<Results>()

    fun initializeData() {
        loadEvent.value = Results.OK
    }
}
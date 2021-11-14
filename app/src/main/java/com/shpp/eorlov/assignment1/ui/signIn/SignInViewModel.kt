package com.shpp.eorlov.assignment1.ui.signIn


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.eorlov.assignment1.data.storage.SharedPreferencesStorageImpl
import com.shpp.eorlov.assignment1.model.AuthorizeModel
import com.shpp.eorlov.assignment1.model.Data
import com.shpp.eorlov.assignment1.model.ResponseModel
import com.shpp.eorlov.assignment1.repository.MainRepositoryImpl
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Constants.EMAIL
import com.shpp.eorlov.assignment1.utils.Constants.PASSWORD
import com.shpp.eorlov.assignment1.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val storage: SharedPreferencesStorageImpl,
    private val repository: MainRepositoryImpl
) :
    ViewModel() {
    val authorizeUserLiveData = MutableLiveData<ResponseModel<Data>>()
    val loadEventLiveData = MutableLiveData<Results>()

    fun authorizeUser(email: String, password: String) {
        viewModelScope.launch {
            loadEventLiveData.value = Results.LOADING
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

            if (response.isSuccessful && response.body() != null) {
                authorizeUserLiveData.postValue(response.body()!!)
            } else {
                loadEventLiveData.value = Results.INVALID_CREDENTIALS
            }
        }
    }

    fun initializeData() {
        loadEventLiveData.value = Results.OK

    }

    fun rememberCurrentEmail(email: String) {
        storage.save(Constants.CURRENT_EMAIL, email)
    }

    fun saveToken(accessToken: String) {
        storage.save(Constants.ACCESS_TOKEN, accessToken)
    }

    fun saveLoginData(email: String, password: String) {
        storage.save(EMAIL, email)
        storage.save(PASSWORD, password)
    }

    fun isRememberedUser(): Boolean {
        val knowEmail = storage.getString(EMAIL, "") != ""
        val knowPassword = storage.getString(PASSWORD, "") != ""
        return knowEmail && knowPassword
    }

    fun clearLoginData() {
        storage.save(EMAIL, "")
        storage.save(PASSWORD, "")
    }
}
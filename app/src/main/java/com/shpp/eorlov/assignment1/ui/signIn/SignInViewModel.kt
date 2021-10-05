package com.shpp.eorlov.assignment1.ui.signIn


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.data.storage.SharedPreferencesStorageImpl
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Constants.EMAIL
import com.shpp.eorlov.assignment1.utils.Constants.PASSWORD
import com.shpp.eorlov.assignment1.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val storage: SharedPreferencesStorageImpl) :
    ViewModel() {
    val loadEvent = MutableLiveData<Results>()

    fun initializeData() {
        loadEvent.value = Results.OK
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

    fun isRememberedUser() : Boolean {
        val knowEmail = storage.getString(EMAIL, "") != ""
        val knowPassword = storage.getString(PASSWORD, "") != ""
        return knowEmail && knowPassword
    }

    fun clearLoginData() {
        storage.save(EMAIL, "")
        storage.save(PASSWORD, "")
    }
}
package com.shpp.eorlov.assignment1.ui.signin


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.data.storage.SharedPreferencesStorageImplementation
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val storage: SharedPreferencesStorageImplementation) :
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
}